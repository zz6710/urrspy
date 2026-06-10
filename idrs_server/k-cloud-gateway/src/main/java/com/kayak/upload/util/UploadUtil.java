package com.kayak.upload.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

import javax.servlet.ServletException;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kayak.core.util.Tools;

/**
 * 通用的文件上传与下载工具类
 *
 * @author liuyg
 */
public class UploadUtil {

    /**
     * 默认的文件上传根目录
     */
    public final static String UPLOAD_PATH = "/upload";

    /**
     * 通用的上传接口，上传根目录为/upload
     *
     * @param temFile
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    public static File upload(MultipartFile temFile) throws Exception {
        return upload(temFile, null);
    }

    /**
     * 通用的上传接口
     *
     * @param temFile
     * @param uploadPath 如果uploadPath为空，这使用默认的上传根目录/upload
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    public static File upload(MultipartFile temFile, String uploadPath) throws Exception {

        if (temFile == null) {
            throw new Exception("上传文件为空");
        }

		// 请限制上传文件的大小
		long size = temFile.getSize();
		if (size > 209715200) {
			throw new Exception("上传文件不能大于100M");
		}
		String fileName = temFile.getOriginalFilename().toLowerCase();

        if (fileName.endsWith(".sh") || fileName.endsWith(".class") || fileName.endsWith(".jsp")
                || fileName.endsWith(".html")) {
            throw new Exception("禁止上传脚本");
        }

        String extension = null;
        if (fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }

		if (!Tools.strIsEmpty(uploadPath)) {// 如果有指定上传的文件夹，这使用上传的文件夹作为上传目录
			if (!uploadPath.startsWith("/")) {
				uploadPath = "/" + uploadPath;
			}
		} else {// 使用默认的上传根目录
			uploadPath = UPLOAD_PATH;
		}
		String path = buildFilePathByExtension(uploadPath, extension);
		if(Pattern.compile("[\u4e00-\u9fa5]").matcher(path).find()) {
			throw new Exception("上传路径包含中文！");
		}

		String system = System.getProperty("os.name");
		if (system.toLowerCase().startsWith("win")) {
			if (!uploadPath.matches("(^[A-Z]:((\\\\|/)([a-zA-Z0-9\\-_]){1,255}){1,255}|([A-Z]:(\\\\|/)))")) {
				throw new Exception("请检查windows文件路径是否正确");
			}
		} else {
			if (!uploadPath.matches("^\\/([^\\|\\\\\\*\\:\\'\\\"\\?\\<\\>]+)+$")) {
				throw new Exception("请检查linux文件路径是否正确");
			}
		}

		File uploadFile = new File(path);

        uploadFile.mkdirs();

        temFile.transferTo(uploadFile);

        return uploadFile;
    }

    public static UploadFile uploadCode(MultipartFile temFile, String uploadPath, String uploadDir) throws Exception {
        if (!Tools.strIsEmpty(uploadDir)) {
            uploadPath += uploadDir;
        }
        File file = UploadUtil.upload(temFile, uploadPath);

        UploadFile uploadFile = new UploadFile();
        uploadFile.setFilePath(file.getPath());

        String fileMd5 = DigestUtils.md5Hex(new FileInputStream(file));

        uploadFile.setFileCode(fileMd5);

        return uploadFile;
    }

    /**
     * 根据当前的时间建立文件夹，时间格式yyyyMMdd
     *
     * @param path
     * @param extension 后缀名
     * @return
     */
    private static String buildFilePathByExtension(String path, String extension) {
        // 创建文件夹
        return getFilePath(path) + generateFileNameByExtension(extension);
    }

    /**
     * 获取文件随机目录
     *
     * @param path
     * @return
     */
    private static String getFilePath(String path) {
        // 获取当前日期
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        String formatDate = format.format(new Date());

        // 创建文件夹
        String filePath = path + '/' + formatDate + '/';

        return filePath;
    }

    /**
     * 根据后缀名生成文件名
     *
     * @param extension
     * @return
     */
    private static String generateFileNameByExtension(String extension) {
        DateFormat format = new SimpleDateFormat("HHmmss");
        String formatDate = format.format(new Date());
        int random = new Random().nextInt(10000);

        if (Tools.strIsEmpty(extension)) {
            return formatDate + random;
        } else {
            return formatDate + random + "." + extension;
        }

    }

}
