package com.xxxx.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Controller
@RequestMapping("search")
public class SearchController {

	@RequestMapping("index")
	public String index(HttpServletRequest request, String searchStr) {
		// 对输入的内容进行编码，防止中文乱码
		try {
			searchStr = URLEncoder.encode(searchStr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "redirect:" + request.getSession().getServletContext().getAttribute("portalUrl") + "search/index" +
				"?searchStr=" + searchStr;
	}

}