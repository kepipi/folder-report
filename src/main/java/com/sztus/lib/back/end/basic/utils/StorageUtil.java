package com.sztus.lib.back.end.basic.utils;

import com.sztus.lib.back.end.basic.type.constant.CommonConst;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author Max
 */
public class StorageUtil {
    private StorageUtil() {
    }

    public static void downloadNet(String fileUrl, String fileName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        URL url = new URL(fileUrl);
        URLConnection conn = url.openConnection();
        InputStream inStream = conn.getInputStream();
        //对文件名进行编码防止中文乱码
        String filename = encodeDownloadFilename(fileName, httpServletRequest);
        httpServletResponse.setContentType("application/x-msdownload");
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + filename);

        writeFile(httpServletResponse, inStream);
    }

    public static String encodeDownloadFilename(String filename, HttpServletRequest request)
            throws IOException {
        //获得游览器
        String agent = request.getHeader("user-agent");
        if (agent.contains(CommonConst.FIREFOX)) {
            // 火狐浏览器
            filename = "=?UTF-8?B?"
                    + Arrays.toString(Base64.getEncoder().encode(filename.getBytes(StandardCharsets.UTF_8)))
                    + "?=";
            filename = filename.replace("\r\n", "");
        } else {
            // IE及其他浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }
        return filename;
    }

    public static void previewNet(String fileUrl, HttpServletResponse httpServletResponse) throws IOException {
        URL url = new URL(fileUrl);
        URLConnection conn = url.openConnection();
        InputStream inStream = conn.getInputStream();

        httpServletResponse.setContentType("text/html; charset=UTF-8");
        httpServletResponse.setContentType("image/jpeg");

        writeFile(httpServletResponse, inStream);
    }

    private static void writeFile(HttpServletResponse httpServletResponse, InputStream inStream) throws IOException {
        OutputStream out = httpServletResponse.getOutputStream();

        byte[] buffer = new byte[8192];
        int length;
        while ((length = inStream.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
        inStream.close();
        out.close();
    }
}
