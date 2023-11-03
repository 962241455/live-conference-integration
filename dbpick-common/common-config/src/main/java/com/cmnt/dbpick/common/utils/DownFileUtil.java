package com.cmnt.dbpick.common.utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @description: 从url下载文件
 * @create: 2021-01-12 10:20
 */
public class DownFileUtil {

    public static class HttpDownloadUtility {
        private static final int BUFFER_SIZE = 4096;


        public static void downloadFile(String fileURL, String saveDir,String fileName)
                throws IOException {
            URL url = new URL(fileURL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();

            // always check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Content-Type = " + httpConn.getContentType());
                System.out.println("Content-Disposition = " + httpConn.getHeaderField("Content-Disposition"));
                System.out.println("Content-Length = " + httpConn.getContentLength());
                System.out.println("fileName = " + fileName);

                // opens input stream from the HTTP connection
                InputStream inputStream = httpConn.getInputStream();
                String saveFilePath = saveDir + File.separator + fileName;

                // opens an output stream to save into file
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                int bytesRead = -1;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                System.out.println("File downloaded");
            } else {
                System.out.println("No file to download. Server replied HTTP code: " + responseCode);
            }
            httpConn.disconnect();
        }
    }
//    public static void main(String[] args) {
//
//        String fileURL = "https://1303153810.vod2.myqcloud.com/44289d23vodtranscq1303153810/a0acfe90387702307753464423/v.f1425285.flv";
//        String saveDir = "E:\\";
//        String filename="v.flv";
//        try {
//            HttpDownloadUtility.downloadFile(fileURL, saveDir,filename);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//    }
}
