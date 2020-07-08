package com.xxxx.portal;

import com.xxxx.common.untils.JsonUtil;
import com.xxxx.rpc.pojo.GoodsCategoryVo;
import com.xxxx.rpc.service.GoodsCategoryService;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@SpringBootTest
public class GoodsCategoryServiceTest {

	@Reference(version = "1.0")
	private GoodsCategoryService goodsCategoryService;

	@Test
	public void testList(){
		List<GoodsCategoryVo> list = goodsCategoryService.selectListForView();
		System.out.println(JsonUtil.object2JsonStr(list));
	}

}