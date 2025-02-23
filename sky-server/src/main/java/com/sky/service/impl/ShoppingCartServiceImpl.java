package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wyj
 * @version 1.0
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加到购物车
     * @param shoppingCartDTO
     */
    @Override
    public void save(ShoppingCartDTO shoppingCartDTO) {
        //number问题的解决 通过查询判断购物车是否已经存在
        //user_id的作用 ：不同用户的购物车，要作为查询条件



        ShoppingCart shoppingCart=new ShoppingCart();

        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long userId= BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart>list=shoppingCartMapper.list(shoppingCart);

        //判断逻辑：商品是否已经存在购物车
        // 存在 update
        if(list!=null&&list.size()>0){
            ShoppingCart cart=list.get(0);//这是因为上面的查询最多只可能有一条数据


            cart.setNumber(cart.getNumber()+1);//数量增加1

            //更新数据 update    set number=#{}
            shoppingCartMapper.updateNumberById(cart);
     }
//    不存在就insert
        else {
            Long dishId=shoppingCartDTO.getDishId();
            Long setmealId=shoppingCartDTO.getSetmealId();

            //如果本次添加的是菜品
            if(dishId!=null){
                Dish dish=dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());

                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());


            }
            //如果本次添加的是套餐
            if(setmealId!=null){
                Setmeal setmeal=setmealMapper.getById(setmealId);

                shoppingCart.setName(setmeal.getName());

                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());

            shoppingCartMapper.insert(shoppingCart);
        }

    }

    @Override
    public List<ShoppingCart> list() {
        ShoppingCart shoppingCart=new ShoppingCart();
        Long userId=BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart>shoppingCarts=shoppingCartMapper.list(shoppingCart);
        return shoppingCarts;
    }

    @Override
    public void subCart(ShoppingCartDTO shoppingCartDTO) {

        ShoppingCart shoppingCart=new ShoppingCart();

        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long userId= BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart>list=shoppingCartMapper.list(shoppingCart);
        ShoppingCart cart=list.get(0);
        if(cart.getNumber()==1)shoppingCartMapper.deleteById(shoppingCart);
        else {
            //这是因为上面的查询最多只可能有一条数据
            cart.setNumber(cart.getNumber() - 1);//数量减少一个
            //更新数据 update    set number=#{}
            shoppingCartMapper.updateNumberById(cart);
        }
    }

    @Override
    public void clearCart() {
        Long userId=BaseContext.getCurrentId();
        ShoppingCart shoppingCart=new ShoppingCart();
        shoppingCart.setUserId(userId);
        shoppingCartMapper.deleteById(shoppingCart);

        return;
    }
}
