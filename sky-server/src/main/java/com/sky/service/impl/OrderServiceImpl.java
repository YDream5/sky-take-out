package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wyj
 * @version 1.0
 */

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;


    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
      //1 进行数据的检验 包括地址不能为空 购物车不能为空

//        if(ordersSubmitDTO.getAddressBookId()==null)
//            throw new OrderBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);

        AddressBook addressBook=addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if(addressBook==null)
            throw new OrderBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);

        Long userId= BaseContext.getCurrentId();

        ShoppingCart shoppingCart=new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCarts=shoppingCartMapper.list(shoppingCart);

        if(shoppingCarts==null||shoppingCarts.size()==0)
            throw new OrderBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);


      //2对收到的DTO数据的处理 包括order表插入一条数据 detail表插入多条购物车数据


        Orders orders=new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        //漏了 添加一些别的数据
        orders.setUserId(userId);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setPayStatus(Orders.UN_PAID);

        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());

        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setOrderTime(LocalDateTime.now());



        orderMapper.insertOrder(orders);

        List<OrderDetail> orderDetails=new ArrayList<>();
        for(ShoppingCart cart: shoppingCarts){
            OrderDetail orderDetail=new OrderDetail();
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetail.setOrderId(orders.getId());

            orderDetails.add(orderDetail);
        }
        orderDetailMapper.insertPatch(orderDetails);


    //3忘了  清空购物车中的数据
        shoppingCartMapper.deleteById(shoppingCart);


    //3 封装要返回的VO对象 包括
    OrderSubmitVO orderSubmitVO=OrderSubmitVO.builder()
            .id(orders.getId()).orderNumber(orders.getNumber())
            .orderAmount(orders.getAmount()).build();

    return  orderSubmitVO;

    }
}
