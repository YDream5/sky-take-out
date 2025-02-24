package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wyj
 * @version 1.0
 */

@Mapper
public interface OrderDetailMapper {

    public void insertPatch(List<OrderDetail> orderDetails);
}
