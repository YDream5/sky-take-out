package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.ShoppingCart;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author wyj
 * @version 1.0
 */
 @Mapper
public interface ShoppingCartMapper {

     List<ShoppingCart> list(ShoppingCart shoppingCart);

     @Update("update shopping_cart set number=#{number} where id=#{id}")
    void updateNumberById(ShoppingCart cart);
    @Insert("INSERT INTO shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) " +
            "VALUES (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
    void insert(ShoppingCart shoppingCart);



    void deleteById(ShoppingCart shoppingCart);
}
