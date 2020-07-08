package com.xxxx.manager.service.impl;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.xxxx.common.result.FileResult;
import com.xxxx.manager.service.UploadService;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Service
public class UploadServiceImpl implements UploadService {

	@Override
	public FileResult upload(InputStream inputStream, String fileName) {
		FileResult fileResult = new FileResult();
		//构造一个带指定 Region 对象的配置类
		Configuration cfg = new Configuration(Region.region0());
		//...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		//...生成上传凭证，然后准备上传
		String accessKey = "CWApvs72aLOpYzpOhZ_fM7QSbx1-tsaXXw5nvuQO";
		String secretKey = "yywbB12AvRWjYcpcnM-ehMmW5e-zxZD4U_h1V0WX";
		String bucket = "shop-test-demo";
		//默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = fileName;
		try {
			Auth auth = Auth.create(accessKey, secretKey);
			String upToken = auth.uploadToken(bucket);
			try {
				Response response = uploadManager.put(inputStream, key, upToken, null, null);
				if (200 == response.statusCode) {
					fileResult.setSuccess("success");
					fileResult.setMessage("上传成功");
					fileResult.setFileUrl("http://qcgk3fvwm.bkt.clouddn.com/" + fileName);
					return fileResult;
				}else {
					fileResult.setError("error");
					fileResult.setMessage("上传失败");
				}
			} catch (QiniuException ex) {
				Response r = ex.response;
				System.err.println(r.toString());
				try {
					System.err.println(r.bodyString());
				} catch (QiniuException ex2) {
					//ignore
				}
			}
		} catch (Exception ex) {
			//ignore
		}
		fileResult.setError("error");
		fileResult.setMessage("上传失败");
		return fileResult;
	}
}