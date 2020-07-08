package com.xxxx.rpc.service.impl;

import com.xxxx.common.result.ShopPageInfo;
import com.xxxx.rpc.pojo.GoodsVo;
import com.xxxx.rpc.service.SearchService;
import org.apache.dubbo.config.annotation.Service;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Service(version = "1.0")
public class SearchServiceImpl implements SearchService {

	@Autowired
	private RestHighLevelClient client;

	/**
	 * 搜索
	 *
	 * @param searchStr
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	public ShopPageInfo<GoodsVo> doSearch(String searchStr, Integer pageNum, Integer pageSize) {
		ShopPageInfo<GoodsVo> shopPageInfo;
		try {
			//指定索引库
			SearchRequest searchRequest = new SearchRequest("shop");
			//构建查询对象
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			//分页
			searchSourceBuilder.from((pageNum - 1) * pageSize).size(pageSize);
			//查询关键词
			searchSourceBuilder.query(QueryBuilders.multiMatchQuery(searchStr, "goodsName"));
			//高亮
			HighlightBuilder highlightBuilder = new HighlightBuilder()
					.field("goodsName")
					.preTags("<span style='color:red'>")
					.postTags("</span>");
			searchSourceBuilder.highlighter(highlightBuilder);
			//执行搜索请求
			searchRequest.source(searchSourceBuilder);
			SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
			//总条数
			Long total = search.getHits().getTotalHits().value;
			if (total > 0) {
				List<GoodsVo> goodsVoList = new ArrayList<>();
				SearchHit[] hits = search.getHits().getHits();
				for (SearchHit hit : hits) {
					//构建项目中所需的数据结果集
					String highlightMessage = String.valueOf(hit.getHighlightFields().get("goodsName").fragments()[0]);
					Integer goodsId = Integer.valueOf((Integer) hit.getSourceAsMap().get("goodsId"));
					String goodsName = String.valueOf(hit.getSourceAsMap().get("goodsName"));
					BigDecimal marketPrice = new BigDecimal(String.valueOf(hit.getSourceAsMap().get("marketPrice")));
					String originalImg = String.valueOf(hit.getSourceAsMap().get("originalImg"));
					GoodsVo goodsVo = new GoodsVo(goodsId, goodsName, highlightMessage, marketPrice, originalImg);
					goodsVoList.add(goodsVo);
				}
				shopPageInfo = new ShopPageInfo<GoodsVo>(pageNum,pageSize,total.intValue());
				shopPageInfo.setResult(goodsVoList);
				return shopPageInfo;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}