package com.xxxx.order.mapper;

import com.xxxx.order.pojo.OrderGoods;
import com.xxxx.order.pojo.OrderGoodsExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderGoodsMapper {
    long countByExample(OrderGoodsExample example);

    int deleteByExample(OrderGoodsExample example);

    int deleteByPrimaryKey(Integer recId);

    int insert(OrderGoods record);

    int insertSelective(OrderGoods record);

    List<OrderGoods> selectByExample(OrderGoodsExample example);

    OrderGoods selectByPrimaryKey(Integer recId);

    int updateByExampleSelective(@Param("record") OrderGoods record, @Param("example") OrderGoodsExample example);

    int updateByExample(@Param("record") OrderGoods record, @Param("example") OrderGoodsExample example);

    int updateByPrimaryKeySelective(OrderGoods record);

    int updateByPrimaryKey(OrderGoods record);

    /**
     * 批量插入订单商品对象
     * @param orderGoodsList
     * @return
     */
    int insertOrderGoodsBatch(@Param("orderGoodsList") List<OrderGoods> orderGoodsList);
}