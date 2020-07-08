package com.xxxx.portal.service.impl;

import com.tencentcloudapi.captcha.v20190722.CaptchaClient;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultRequest;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.xxxx.common.result.BaseResult;
import com.xxxx.portal.service.CaptchaService;
import org.springframework.stereotype.Service;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

	/**
	 * 验证校验码
	 *
	 * @param ticket
	 * @param randStr
	 * @return
	 */
	@Override
	public BaseResult captcha(String ticket, String randStr) {
		try {
			//鉴权
			Credential cred = new Credential("AKIDE9sN5uOw5tb8UrphxGKAmIZfYBakaLlv",
					"IMC5IQSVLSK1gFg2GNErPk394pOp9JHA");
			//生成httpProfile对象，接口地址
			HttpProfile httpProfile = new HttpProfile();
			httpProfile.setEndpoint("captcha.tencentcloudapi.com");
			//生成clientProfile对象
			ClientProfile clientProfile = new ClientProfile();
			clientProfile.setHttpProfile(httpProfile);
			//生成客户端对象，鉴权，机房，接口地址
			CaptchaClient client = new CaptchaClient(cred, "ap-shanghai", clientProfile);
			//参数
			String params =
					"{\"CaptchaType\":9,\"Ticket\":\"" + ticket + "\",\"UserIp\":\"127.0.0.1\",\"Randstr\":\"" + randStr + "\",\"CaptchaAppId\":2086582926,\"AppSecretKey\":\"0k_RaTXmkbL7LE7VbqT7uUw**\"}";
			//封装请求
			DescribeCaptchaResultRequest req = DescribeCaptchaResultRequest.fromJsonString(params,
					DescribeCaptchaResultRequest.class);
			//执行请求
			DescribeCaptchaResultResponse resp = client.DescribeCaptchaResult(req);

			System.out.println(DescribeCaptchaResultRequest.toJsonString(resp));
			if (1L == resp.getCaptchaCode()) {
				return BaseResult.success();
			}
		} catch (TencentCloudSDKException e) {
			System.out.println(e.toString());
		}
		return BaseResult.error();
	}
}