package com.cmnt.dbpick.common.upload.utils;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.model.FileResponsePoJo;
import com.cmnt.dbpick.common.upload.config.ObsConfig;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;
import java.util.Objects;

@Component
@Slf4j
@DependsOn("obsConfig")
@Lazy
public class ObsUtils {

	private ObsClient obsClient;


	public ObsUtils() {
		log.info("初始化 ObsClient 客户端......server_url：{}", ObsConfig.SERVER_URL);
		this.obsClient = new ObsClient(ObsConfig.ACCESS_KEY, ObsConfig.SECRET_KEY, ObsConfig.SERVER_URL);
	}

	/**
	 * 上传小于5G的小文件
	 *
	 * @param file
	 * @throws Exception
	 */
	public FileResponsePoJo uploadFile(String bucketName, String objectKey, String type, MultipartFile file, int acl) throws Exception {

		try {
			PutObjectRequest request = new PutObjectRequest();
//			if (acl == 1){
//				request.setAcl(AccessControlList.REST_CANNED_AUTHENTICATED_READ);
//			}else{
//				request.setAcl(AccessControlList.REST_CANNED_PRIVATE);
//			}
			request.setBucketName(bucketName);
			request.setObjectKey(objectKey);
			request.setInput(file.getInputStream());
			log.info("obs文件上传传递的参数是：{}", JSON.toJSONString(request));
			PutObjectResult putObject = null;
			putObject = this.obsClient.putObject(request);

			log.info("上传obs文件返回的信息是：{}", JSON.toJSONString(putObject));
			if (null != putObject) {
				if (Objects.equals(200,putObject.getStatusCode())) {
					return FileResponsePoJo.builder().content(putObject.getObjectUrl())
							.objectKey(putObject.getObjectKey()).build();
				} else {
					throw new BizException("文件存储服务上传失败,请联系管理员");
				}
			} else {
				throw new BizException("文件存储服务上传失败,请联系管理员");
			}
		} catch (ObsException e) {
			log.error(e.getMessage(), e);
			// 推荐：发生异常后，记录失败的HTTP状态码、服务端错误码、服务端请求ID等
			System.out.println("HTTP Code: " + e.getResponseCode());
			System.out.println("Error Code:" + e.getErrorCode());
			System.out.println("Request ID:" + e.getErrorRequestId());
			// 推荐：发生异常后，记录异常堆栈信息
			e.printStackTrace(System.out);
			throw new BizException("文件存储服务上传失败,请联系管理员");
		}

	}

	/**
	 * 获取文件内容 base64
	 * @param objectname
	 * @param bucketName
	 * @return
	 * @throws Exception
	 */
	public String getObsFileBase64(String objectname, String bucketName) throws Exception {
		//创建不同的文件夹目录
		String image = objectname.substring(0, objectname.lastIndexOf("/") + 1);
		String imageName = objectname.substring(objectname.lastIndexOf("/") + 1);
		String file_puth = "./downfile/";
		File file = new File(file_puth + image);
		if (!file.exists()) {
			//先得到文件的上级目录，并创建上级目录，在创建文件
			file.mkdirs();
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		GetObjectRequest request = new GetObjectRequest(bucketName, objectname);
		ObjectRepleaceMetadata replaceMetadata = new ObjectRepleaceMetadata();
		replaceMetadata.setContentType("image/jpg");
		request.setReplaceMetadata(replaceMetadata);
		ObsObject obsObject = null;
		if (bucketName.indexOf("nft") > -1){
//			ObsClient nftObsClient = new ObsClient(nft_access_key_id, nft_secret_access_key, nft_server_url);
//			obsObject = nftObsClient.getObject(request);
		}else{
			obsObject = obsClient.getObject(request);
		}
		try {
			InputStream content = obsObject.getObjectContent();
			if (content != null) {
				byte[] b = new byte[1024];
				int len;
				while ((len = content.read(b)) != -1) {
					bos.write(b, 0, len);
				}
				bos.close();
				content.close();
			}
		} catch (IOException e) {
			log.error("下载文件错误" + e.getMessage());
			System.out.println(e);
			throw new BizException("下载文件错误" + e.getMessage());
		}
		// 保存文件到本地
		String hashimg = Base64.getEncoder().encodeToString(bos.toByteArray());
		try {
			FileOutputStream fw = new FileOutputStream(file.getPath() + "/" + imageName);
			byte[] bytes = bos.toByteArray();
			fw.write(bytes);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new BizException("错误信息" + e.getMessage());
		}
		return hashimg;
	}

}
