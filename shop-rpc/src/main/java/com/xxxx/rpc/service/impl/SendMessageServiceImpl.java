package com.xxxx.rpc.service.impl;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.xxxx.common.result.BaseResult;
import com.xxxx.rpc.service.SendMessageService;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Service(version = "1.0")
public class SendMessageServiceImpl implements SendMessageService {

	/**
	 * 发送短信
	 * @param phone
	 * @return
	 */
	@Override
	public BaseResult sendMessage(String phone) {
		try{
			//鉴权
			Credential cred = new Credential("", "");
			//生成httpProfile对象，调用发送短信的接口地址
			HttpProfile httpProfile = new HttpProfile();
			httpProfile.setEndpoint("sms.tencentcloudapi.com");
			//生成clientProfile对象
			ClientProfile clientProfile = new ClientProfile();
			clientProfile.setHttpProfile(httpProfile);
			//生成SmsClient客户端，鉴权，机房，接口地址
			SmsClient client = new SmsClient(cred, "ap-shanghai", clientProfile);
			//参数
			String params = "{\"PhoneNumberSet\":[\"+8613711112222\"],\"TemplateID\":\"490150\",\"Sign\":\"程序猿学习栈\",\"SmsSdkAppid\":\"1400291704\"}";
			//根据参数生成SendSmsRequest对象
			SendSmsRequest req = SendSmsRequest.fromJsonString(params, SendSmsRequest.class);
			//客户端发送短信
			SendSmsResponse resp = client.SendSms(req);

			System.out.println(SendSmsRequest.toJsonString(resp));
			if ("ok".equalsIgnoreCase(resp.getSendStatusSet()[0].getCode())){
				return BaseResult.success();
			}
		} catch (TencentCloudSDKException e) {
			System.out.println(e.toString());
		}
		return BaseResult.error();
	}
}