package com.xxxx.portal;

import com.xxxx.common.result.ShopPageInfo;
import com.xxxx.common.untils.JsonUtil;
import com.xxxx.rpc.pojo.GoodsVo;
import com.xxxx.rpc.service.SearchService;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@SpringBootTest
public class SearchServiceTest {

	@Reference(version = "1.0")
	private SearchService searchService;

	@Test
	public void testSearch(){
		ShopPageInfo<GoodsVo> shopPageInfo = searchService.doSearch("中国移动联通电信", 1, 10);
		System.out.println(JsonUtil.object2JsonStr(shopPageInfo));
	}

}