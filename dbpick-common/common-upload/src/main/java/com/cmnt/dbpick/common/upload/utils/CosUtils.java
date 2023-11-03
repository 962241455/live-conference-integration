package com.cmnt.dbpick.common.upload.utils;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.config.TencentConfig;
import com.cmnt.dbpick.common.model.FileResponsePoJo;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 腾讯云文件上传配置
 */
@Component
@Slf4j
@DependsOn("tencentConfig")
@Lazy
public class CosUtils {

	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd_HHmmss");

	private COSClient cosClient ;

	/**
	 * 初始化客户端
	 */
	public CosUtils() {
		// 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
		log.info("初始化 COSClient 客户端......桶：{}, 文件夹：{}"
				, TencentConfig.TX_COS_BUCKET_NAME, TencentConfig.TX_COS_FOLDERS_PREFIX);
		this.cosClient = getCosClient();
	}
	/*** 生成客户端 */
	private COSClient getCosClient(){
		// 1 初始化用户身份信息(secretId, secretKey)
		COSCredentials cred = new BasicCOSCredentials(TencentConfig.TX_CLOUD_SECRETID, TencentConfig.TX_CLOUD_SECRETKEY);

		// ClientConfig 中包含了后续请求 COS 的客户端设置：
		ClientConfig clientConfig = new ClientConfig();

		// 设置 bucket 的地域
		// COS_REGION 请参照 https://cloud.tencent.com/document/product/436/6224
		// new ClientConfig(new Region(TencentConfig.REGION));
		clientConfig.setRegion(new Region(TencentConfig.REGION));

		// 设置请求协议, http 或者 https
		// 5.6.53 及更低的版本，建议设置使用 https 协议
		// 5.6.54 及更高版本，默认使用了 https
		clientConfig.setHttpProtocol(HttpProtocol.https);

		// 以下的设置，是可选的：
		// 设置 socket 读取超时，默认 30s
		clientConfig.setSocketTimeout(30*1000);
		// 设置建立连接超时，默认 30s
		clientConfig.setConnectionTimeout(30*1000);
		// 如果需要的话，设置 http 代理，ip 以及 port
//		clientConfig.setHttpProxyIp("httpProxyIp");
//		clientConfig.setHttpProxyPort(80);
		return new COSClient(cred, clientConfig);//生成cos客户端
	}

	/**
	 * 传入threadPool, 若不传入线程池, 默认TransferManager中会生成一个单线程的线程池
	 */
	private TransferManager createTransferManager(COSClient cosClient){

		// 自定义线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
		// 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。

		ExecutorService threadPool = Executors.newFixedThreadPool(32);
		// 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
		TransferManager transferManager = new TransferManager(cosClient, threadPool);
		// 设置高级接口的配置项
		// 分块上传阈值和分块大小分别为 5MB 和 1MB
		TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
		transferManagerConfiguration.setMultipartUploadThreshold(5*1024*1024);
		transferManagerConfiguration.setMinimumUploadPartSize(1*1024*1024);
		transferManager.setConfiguration(transferManagerConfiguration);
		return new TransferManager(cosClient, threadPool);
//		return new TransferManager(cosClient);
	}

	private void shutdownTransferManager(TransferManager transferManager) {
		// 默认 true, 同时关闭 transferManager 内部的 COSClient 实例; 设为 false, 则不会关闭
		if(Objects.nonNull(transferManager)) {transferManager.shutdownNow(false);}
	}

	/**
	 * 上传文件
	 * @param file 文件
	 * @param account 上传用户
	 */
	public FileResponsePoJo uploadFileRename(MultipartFile file, String account) {
		return uploadFile(file, account, Boolean.TRUE);
	}
	public FileResponsePoJo uploadFile(MultipartFile file, String account) {
		return uploadFile(file, account, Boolean.FALSE);
	}
	/**
	 * 上传文件
	 * @param file 文件
	 * @param account 上传用户
	 * @param rename 是否重命名
	 */
	public FileResponsePoJo uploadFile(MultipartFile file, String account, Boolean rename){
		try {
			String localFileName = file.getName();
			if(rename){//修改文件名字
				String oldFileName = file.getOriginalFilename();
				String fileSuffix = oldFileName.substring(oldFileName.lastIndexOf("."));
				//新名字 (日期戳+随机字符串+文件后缀)
				localFileName = file.getName().replace(fileSuffix,"")+"_"+ SDF.format(new Date())+"-"
						+ (new Random().nextInt(10000)+ DateUtil.getTimeStrampSeconds()) + fileSuffix;
			}
			File localFile = File.createTempFile("temp_", null);
			file.transferTo(localFile);
			return uploadFile(localFile, account, localFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public FileResponsePoJo uploadFile(File localFile, String account, String localFileName){
		TransferManager transferManager = null;
		try {
			/*File localFile = File.createTempFile("temp_", null);
			file.transferTo(localFile);*/
			String cosPath = TencentConfig.TX_COS_FOLDERS_PREFIX +account+"/"+ localFileName;// 指定要上传到 COS 上的路径
			PutObjectRequest objectRequest = new PutObjectRequest(TencentConfig.TX_COS_BUCKET_NAME, cosPath, localFile);
			log.info("cos文件上传传递的参数是：{}", JSON.toJSONString(objectRequest));
			transferManager = createTransferManager(cosClient);//多线程
			// 返回一个异步结果Upload, 可同步的调用waitForUploadResult等待upload结束, 成功返回UploadResult, 失败抛出异常
			Upload upload = transferManager.upload(objectRequest);
			showTransferProgress(upload);
			UploadResult result = upload.waitForUploadResult();
			/*//不使用线程，直接上传
			this.cosClient.putObject(objectRequest);
			COSObject object = this.cosClient.getObject(TencentConfig.TX_COS_BUCKET_NAME, cosPath);//线上地址URL*/
			log.info("cos文件上传返回结果：{}", JSON.toJSONString(result));
			return FileResponsePoJo.builder().fileUrl(TencentConfig.TX_COS_ACCESS_URL+result.getKey())
					.content(TencentConfig.TX_COS_ACCESS_URL+result.getKey()).objectKey(result.getKey())
					.requestId(result.getRequestId())
					.build();
		} catch (CosServiceException e) {
			log.error("cos -> CosServiceException 上传文件报错：{}", e);
			e.printStackTrace();
		} catch (CosClientException e) {
			log.error("cos -> CosClientException 上传文件报错：{}", e);
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("cos -> InterruptedException 上传文件报错：{}", e);
			e.printStackTrace();
		}finally {
			shutdownTransferManager(transferManager);
		}
		return null;
	}


	/**
	 * @param filePath 文件全路径
	 * @param targetPath 下载到文件夹
	 * @return
	 */
	public Boolean downloadFile(String filePath, String targetPath) {
		/*// 生成cos客户端
		COSClient cosClient = getCosClient();*/
		if(StringUtils.isBlank(filePath) || !filePath.contains(TencentConfig.TX_COS_ACCESS_URL)){
			log.error("cos文件路径错误：{}", filePath);
			return Boolean.FALSE;
		}
		String cosPath = filePath.replace(TencentConfig.TX_COS_ACCESS_URL,"");
		if(!this.cosClient.doesObjectExist(TencentConfig.TX_COS_BUCKET_NAME, cosPath)){
			log.error("cos未查询到该文件：{}", cosPath);
			return Boolean.FALSE;
		}
		TransferManager transferManager = null;
		try {
			//下载文件
			String fileSuffix = filePath.substring(filePath.lastIndexOf("/"));
			File downloadFile = new File(targetPath+fileSuffix);
			GetObjectRequest getObjectRequest = new GetObjectRequest(TencentConfig.TX_COS_BUCKET_NAME, cosPath);
			transferManager = createTransferManager(cosClient);
			//返回一个异步结果 Donload, 可同步的调用 waitForCompletion 等待下载结束, 成功返回 void, 失败抛出异常
			Download download = transferManager.download(getObjectRequest, downloadFile);
			download.waitForCompletion();
			log.info("cos下载完成。文件路径：{}", downloadFile.getPath());
			return Boolean.TRUE;
		} catch (InterruptedException e) {
			log.error("cos下载文件报错：{}", e);
			return Boolean.FALSE;
		} finally {
			shutdownTransferManager(transferManager);
		}
	}


	/**
	 * 删除文件
	 * @param filePath 文件全路径
	 */
	public Boolean deleteFile(String filePath){
		/*// 生成cos客户端
		COSClient cosClient = getCosClient();*/
		log.info("删除文件： filePath={}",filePath);
		if(StringUtils.isBlank(filePath) || !filePath.contains(TencentConfig.TX_COS_ACCESS_URL)){
			log.error("cos文件路径错误：{}", filePath);
			return Boolean.TRUE;
		}
		String cosPath = filePath.replace(TencentConfig.TX_COS_ACCESS_URL,"");
		try {
			this.cosClient.deleteObject(TencentConfig.TX_COS_BUCKET_NAME,cosPath);
			log.info("cos删除完成。文件路径：{}", cosPath);
			return Boolean.TRUE;
		} catch (CosClientException e) {
			log.error("cos删除文件报错：{}", e);
			return Boolean.FALSE;
		} finally {
			this.cosClient.shutdown();
		}

	}

	// 可以参考下面的例子，结合实际情况做调整
	void showTransferProgress(Transfer transfer) {
		// 这里的 Transfer 是异步上传结果 Upload 的父类
		log.info("上传开始：{}",transfer.getDescription());

		// transfer.isDone() 查询上传是否已经完成
		while (transfer.isDone() == false) {
			try {
				// 每 2 秒获取一次进度
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return;
			}

			TransferProgress progress = transfer.getProgress();
			long sofar = progress.getBytesTransferred();
			long total = progress.getTotalBytesToTransfer();
			double pct = progress.getPercentTransferred();
			log.info("upload progress: [%d / %d] = %.02f%%\n", sofar, total, pct);
		}

		// 完成了 Completed，或者失败了 Failed
		log.info("上传结束：{}",JSON.toJSONString(transfer.getState()));
	}

	//删除文件和目录
	public static void clearFiles(String workspaceRootPath){
		File file = new File(workspaceRootPath);
		if(file.exists()){
			deleteFile(file);
		}
	}

	public static void deleteFile(File file){
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(int i=0; i<files.length; i++){
				deleteFile(files[i]);
			}
		}
		file.delete();
	}


//	public static void main(String[] args) throws Exception {
///*
//		File pdfFile = new File("F://迅雷下载/一步之遥.mp4");
//		FileInputStream fileInputStream = new FileInputStream(pdfFile);
//		MultipartFile multipartFile = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(),
//				ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
////			<dependency>
////          	<groupId>org.springframework</groupId>
////          	<artifactId>spring-test</artifactId>
////          	<version>4.3.7.RELEASE</version>
////      	</dependency>
//
//		TencentCOSUtils util = new TencentCOSUtils();
//		FileResponsePoJo fileResponsePoJo = util.uploadFile(multipartFile);
//		System.err.println(fileResponsePoJo);
//*/
//
///*
//		TencentCOSUtils util = new TencentCOSUtils();
//		util.downloadFile("https://cnmt-live-video-1303153810.cos.ap-guangzhou.myqcloud.com/test/video/20220926162853-1664180934006.mp4",
//				"F://迅雷下载");
//*/
///*
//		TencentCOSUtils util = new TencentCOSUtils();
//		util.deleteFile("https://cnmt-live-video-1303153810.cos.ap-guangzhou.myqcloud.com/test/video/一步之遥_20220926_173449-1664184890583.mp4");
//*/
//
//	}

}
