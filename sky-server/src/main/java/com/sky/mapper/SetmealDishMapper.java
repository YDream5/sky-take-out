package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wyj
 * @version 1.0
 */
@Mapper
public interface SetmealDishMapper {


    //select * from setmeal_dish where dish_id in(1,3,4)
    List<Long> getSetmeanIdsByDishIds(List<Long> ids);
}
