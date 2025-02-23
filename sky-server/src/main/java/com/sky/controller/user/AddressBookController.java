package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wyj
 * @version 1.0
 */

@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "地址簿相关接口")
@Slf4j
public class AddressBookController {


    @Autowired
   private AddressBookService addressBookService;

    @PostMapping
    @ApiOperation("新增地址")
    public Result save(@RequestBody AddressBook addressBook){
        log.info("接收到地址信息{}",addressBook);
        addressBookService.save(addressBook);
        return Result.success();

    }

    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefaultAddress(){

        log.info("进入查询默认地址");
       AddressBook addressBook= addressBookService.getDefault();

        return Result.success(addressBook);
    }

    @GetMapping("/list")
    @ApiOperation("查询当前登录用户所有地址")
    public Result<List<AddressBook>> getAddressList(){

        log.info("进入查询当前登录用户所有地址");
        List<AddressBook> addressBooks=addressBookService.getlist();
        return Result.success(addressBooks);
    }


    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public  Result<AddressBook> getAddressById(@PathVariable Long id ){

        log.info("接收到id{}",id);
        AddressBook addressBook= addressBookService.getById(id);
        return Result.success(addressBook);
    }

    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result updateDefaultAddress(@RequestBody AddressBook addressBook){
        log.info("接收到默认修改地址",addressBook);
        // 默认地址不只是要修改自己，还要把其他都设置为0



        //修改其他地址为非默认地址
        Long userId= BaseContext.getCurrentId();
        addressBookService.updatePatch(userId);

        //修改地址为默认地址
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);



        return Result.success();

    }

    @PutMapping
    @ApiOperation("根据id修改地址")
    public Result updateAddressById(@RequestBody AddressBook addressBook){

        log.info("接收到{}",addressBook);
        addressBookService.updateById(addressBook);
        return Result.success();
    }


    @DeleteMapping
    @ApiOperation("根据id删除地址")

    public  Result deleteById( Long id){

        log.info("接收到要删除的id{}",id);

        addressBookService.deleteById(id);
        return Result.success();

    }


}
