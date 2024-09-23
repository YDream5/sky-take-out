package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author wyj
 * @version 1.0
 */
@Slf4j
@RequestMapping("/admin/dish")
@Api(tags="菜品相关接口")
@RestController
public class DishController {

    @Autowired//只有实现了对应的类才能注入成功
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 清除redis的缓存数据
     * @param pattern
     */
    private void clearCache(String pattern){
        Set keys=redisTemplate.keys(pattern);
        redisTemplate.delete(keys);

    }

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @ApiOperation("新增菜品")
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("{}",dishDTO);

        dishService.saveWithFlavor(dishDTO);

        //精确清理缓存
        String key="dish_"+dishDTO.getCategoryId();
        clearCache(key);


        return Result.success();

    }


    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */

    //由于是get，所以不用加requestbody
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page( DishPageQueryDTO dishPageQueryDTO){

        log.info("接收到{}",dishPageQueryDTO);
        PageResult pageResult= dishService.pageQuery(dishPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 菜品批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public  Result delete(@RequestParam List<Long> ids){


        log.info("要删除的ids{}",ids);
        dishService.deleteBatch(ids);

        //删除完成后清空缓存
        clearCache("dish_*");



        return Result.success();
    }

    /**
     * 通过id查询菜品和口味回显
     * @return
     */
    @ApiOperation("通过id查询菜品信息回显")
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("id{}",id);

        DishVO dishVO=dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);

    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result  update(@RequestBody DishDTO dishDTO){
        log.info("菜品DTO{}",dishDTO);
        dishService.updateWithFlavor(dishDTO);

        //删除完成后清空缓存
        clearCache("dish_*");
        return Result.success();
    }

    /**
     * 根据分类id查询菜品列表（其实是套餐中的菜品）
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list( Long categoryId){
        log.info("接收到{}",categoryId);
       List<Dish>dishlists= dishService.list(categoryId);

       return Result.success(dishlists);


    }


    @PostMapping("/status/{status}")
    @ApiOperation("起售或禁售菜品")
    public Result startOrStop(@PathVariable Integer status,Long id )
    {
        log.info("接收到修改状态{},菜品id{}",status,id);

        dishService.startOrstop(status,id);

        return Result.success();


    }

}
