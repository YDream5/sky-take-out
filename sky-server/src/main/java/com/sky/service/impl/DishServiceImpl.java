package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wyj
 * @version 1.0
 */
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Override
    @Transactional//由于可能要操作多张表
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.save(dish);

        Long dishId = dish.getId();

        List<DishFlavor> dishList=dishDTO.getFlavors();
        if(dishList!=null&&dishList.size()>0){
            //遍历数据，保存id
            for (DishFlavor dishflavor: dishList
                 ) {
                dishflavor.setDishId(dishId);
            }

            //插入数据
            dishFlavorMapper.save(dishList);

        }

    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {

        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        //如果菜品状态为起售中不能删除 优雅
        ids.forEach(id->{

            Dish dish=dishMapper.getById(id);
            if(dish.getStatus()== StatusConstant.ENABLE)
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);

        });

        //如果菜品在套餐中不能删除
        List<Long> setmealIds=setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIds!=null&&setmealIds.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //正常删除菜品 这样写不好，应该在sql语句中批量删除
        for (Long id : ids) {
            dishMapper.deleteById(id);

            //删除对应的Flavor有就删
            dishFlavorMapper.deleteByDishId(id);

        }

    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {

        DishVO dishVO=new DishVO();
       Dish dish =dishMapper.getById(id);

       //写错了，因为一个菜品对应多种口味，所以要用list集合封装
     //  DishFlavor dishFlavor=flavorMapper.getByDishId(id);

        List<DishFlavor> flavors=dishFlavorMapper.getByDishId(id);

       BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(flavors);

       return dishVO;
    }

    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);



      //删除原来的口味
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

      // 重新插入新口味
        List<DishFlavor> flavors=dishDTO.getFlavors();

        if(flavors!=null&&flavors.size()>0) {
            for (DishFlavor dishflavor : flavors) {
                dishflavor.setDishId(dishDTO.getId());
            }

            //批量插入数据
            dishFlavorMapper.save(flavors);
        }

    }

    /**
     * 条件查询菜品和口味 copy
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        //
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    @Override
    public List<Dish> list(Long categoryId) {

        Dish dish=Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();

        return dishMapper.list(dish);


    }

    /**
     * 起售或禁售菜品
     * @param status
     * @param dishId
     */
    @Override
    public void startOrstop(Integer status,Long id) {
        Dish dish=Dish.builder().status(status)
                .id(id).build();

        dishMapper.update(dish);


        if (status == StatusConstant.DISABLE) {
            // 如果是停售操作，还需要将包含当前菜品的套餐也停售
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            // select setmeal_id from setmeal_dish where dish_id in (?,?,?)
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(dishIds);
            if (setmealIds != null && setmealIds.size() > 0) {
                for (Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    setmealMapper.update(setmeal);
                }
            }
        }

    }


}
