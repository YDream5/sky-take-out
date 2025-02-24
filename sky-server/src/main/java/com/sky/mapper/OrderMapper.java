package com.sky.mapper;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wyj
 * @version 1.0
 */

@Mapper
public interface OrderMapper {

    void insertOrder(Orders orders);
}
