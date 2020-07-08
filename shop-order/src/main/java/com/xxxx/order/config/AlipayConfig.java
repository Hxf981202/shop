package com.xxxx.order.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016092800614468";

	// 商户私钥，您的PKCS8格式RSA2私钥
	public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCASEkUgWA65jo90AfQDbsjf9QBQmacpQWAcHitKxfrj+ugquLNaBcKcwTztrQ8uHuryZaw04j04g1bf5bk0LGDuzu6pdEIqHEXmTboRUXgqkIxz9ErNMmCpkGsX1e6DbqyBZe04BROkFkx5r4RWOBcGTit+NngYEFRgObCiuUf7JgD4ig1E8D74xhnzxWsD/WDwBoDlLMIAptuiZ6kzhtvdzJbPWBMHrlxE4tAAc4sTWZWTbirz0v9tqhp5bfEyXJpM2/3oTKhzeBKXPSclpD8uWSpJtbPFfLrVY1+uduW0z4SU5T0QWxSOW6fdA5XA73dZBDNMF1Ye6NvQ0ezXa1hAgMBAAECggEAJjtk7GOSlns/KGTzryIof+/881Mi2dSkyx66GIIbYu1FwGCtr6IyG78j3q65EM+6q/wsmQ6yZqloabig9BjujuAicQIyRm5NJkdXgeoDZW3YxJe3MzVMSU/bis/viVfGKIBaMmIGK1QCAUlPpEPkug4aQl9EMAb/8JaGL7zNhLHDvYGUSq2PZMgm/XmIW1017FNUaa7FkZCt32LmHeJevDqNfa/ptNvO2iNYZhKVV1Ppab6kIGU1WMXryvcZPUUDhZTWLTWYrJA8xDGN08wezJao9ou8s6XASwxRj8D1St1nuLb2DYYw05JiVNh+2qH58igzcfx+Lo4xJ1IwNqsb8QKBgQDkfUnJ5b3ZJGJcPMrbGeWkhw7P564CljAh0ttg7lRnEhBJMc3A7xAmEwKBWBJ3pjd5O975Kr4/IYWzfg+5pBBmZ36HtIllqycXgSzr64D7A8xNbhNlDeeK+xZA8W0XJg3TknxiA9eyIG/rMcOzz89mfEC2eDtVGS+mvw6lnImsawKBgQCPulK42PvqA1Zt+nZj7IdN+obQacP+FZLw7j4iBWx8IRgkqcImdQxuEQXWqpvh2WAZsl1eyhRh+aoo+Cu1MoS277g2nQIE0ncO+s4t2Di67IjHKMUTf9NJ/gcJf0TWUgOCAt9XXo740kahlX8GIeevjz7bHaXQKb6YiIbzBmAAYwKBgQDetmCeMxmB9j0JuxffyLZWUG7Xp/OtAceYsHa1XJlYffrx3WG4lU0gT5c5jCc0CQg5W2pTZJ5b2sj/0/hv8sMpjed/TzI9rz5DYt3ud3cZNPKG5qxw68Sta2CdnLXAwo7nkvk6cJRmQ8fTcIZZVk3Kj4JkWX07zXjtB323X4WyUQKBgCwW9BlQNrbqnzDNjFPK0xbQq1pzl3PyGkleTCETmNvbKxTUHoK9lTqObkQTwg3eKOS5Gnthci877z7yIjZLcPrNPq1CLMRd67fkqCrYEU8GEE9H6my44Y/pJS98oxk7v2e6idi7YRBjMNeQjI7NB82LG7ULuznIFHemKf+TDRARAoGAOXcAd1WFHl/Lg18O3EPPPKZsBBBAnu1uacPTpZPWCJPaHBaw7B6EEVOYWzapQ//1s+N/F4iaTgieTwrJVuda4dUjXgtg6gT1bOuGcIQNr9Wsz26hlTTJ5Ak5rafKDOHGQAsJUj2deh8BbQg36zIs/TbHDGp0fqkLj14ct5pOD2g=";

	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
	public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxYJbnlZc8GaNZPs96suFWyfcIjZBugJvMOLaPIBlQ/I2t2MD/X26Lye+6emawehyh/axiD0gGsr1Y4y60S5AwVUxKTkg1NRZG8JSt6Wb0/uepZ5mjnoJJBYOCBf7oVXHTl3ZO1Wdk+0nxQEEssokM3eCL7Y/LKssCW8fBk9XCXK2f5yAcSQapGp1XvJi9lUAP1ytXV/AjO4mwRpgdM60aGnbcxB8bWaQkZFTsjMukpXSBi1+lCZqkgWQ7P92EfqRtu+weFadbINfGz8Wqifaj3BUkO8IQ68Afy1YQpICYapQqZ/hq1nDAhegHkfF/1JP3ItE3IPlNCy+SXFyO90FJQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://is3q3c.natappfree.cc/shop-portal/order/callback";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://localhost:9092/shop-order/order/myOrder";

	// 签名方式
	public static String sign_type = "RSA2";

	// 字符编码格式
	public static String charset = "utf-8";

	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	/**
	 * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
	 * @param sWord 要写入日志里的文本内容
	 */
	public static void logResult(String sWord) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
			writer.write(sWord);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

