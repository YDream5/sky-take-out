package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;
    //其实感觉在service层写会更好
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        //构造redis中的key，规则为dish_+id
        String key="dish_"+categoryId;

        //查询redis中是否有缓存 放进去的类型就是取出来的类型
        List<DishVO>listVos = (List<DishVO>) redisTemplate.opsForValue().get(key);

        //有，则直接返回
        if(listVos!=null&&listVos.size()>0){
            return Result.success(listVos);
        }

        //没有，就将数据库中的数据载入redis,然后返回

        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品

         listVos = dishService.listWithFlavor(dish);

        //载入redis
        redisTemplate.opsForValue().set(key,listVos);

        return Result.success(listVos);
    }

}
