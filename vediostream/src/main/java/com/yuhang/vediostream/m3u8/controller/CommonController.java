package com.yuhang.vediostream.m3u8.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yuhang.vediostream.base.util.FtpUtils;
import com.yuhang.vediostream.base.util.IDUtils;


/**
 * 统一上传文件接口
 * @author user
 *
 */
@RestController
@RequestMapping(value="/common")
public class CommonController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/upload", method={RequestMethod.POST,RequestMethod.GET})
	public String upload(@RequestParam(value = "file", required = true) MultipartFile multipartFile,
			HttpServletRequest request,HttpServletResponse response) throws IOException {
		String uploadFile = "";
		String originalFilename = multipartFile.getOriginalFilename();
		String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
		logger.info("进入上传文件接口" + originalFilename);
		try {
			FtpUtils ftpUtils = new FtpUtils();
			uploadFile = ftpUtils.uploadFile("/",IDUtils.genItemId() + "." + suffix,multipartFile.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("文件上传成功地址为  ： " + uploadFile);
		return uploadFile;
	}
}
