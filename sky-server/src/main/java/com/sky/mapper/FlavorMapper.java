package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wyj
 * @version 1.0
 */

@Mapper
public interface FlavorMapper {


  

    public void save(List<DishFlavor> dishList);

    /**
     * 根据dishId删除口味
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id=#{dishId}")
    void deleteByDishId(Long dishId);




}
