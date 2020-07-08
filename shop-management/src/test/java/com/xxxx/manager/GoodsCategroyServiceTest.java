package com.xxxx.manager;

import com.xxxx.common.untils.JsonUtil;
import com.xxxx.manager.pojo.GoodsCategoryVo;
import com.xxxx.manager.service.GoodsCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * 商品分类测试类
 *
 * @author zhoubin
 * @since 1.0.0
 */
@SpringBootTest
public class GoodsCategroyServiceTest {

	@Autowired
	private GoodsCategoryService goodsCategoryService;


	@Test
	public void testListForView(){
		List<GoodsCategoryVo> list = goodsCategoryService.selectListForView();
		System.out.println(JsonUtil.object2JsonStr(list));
	}



	@Test
	public void testHtml(){
		String s = HtmlUtils.htmlEscape("<p><span>测试转义</span></p>", "UTF-8");
		System.out.println(s);
		System.out.println(HtmlUtils.htmlUnescape(s));
	}

}