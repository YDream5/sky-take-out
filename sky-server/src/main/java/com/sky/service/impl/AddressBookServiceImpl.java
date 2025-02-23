package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wyj
 * @version 1.0
 */

@Service
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public void save(AddressBook addressBook) {

        Long userId= BaseContext.getCurrentId();
        addressBook.setUserId(userId);
        addressBook.setIsDefault(0);//设置为不是默认地址；
        addressBookMapper.insert(addressBook);


    }

    @Override
    public AddressBook getDefault() {

        AddressBook addressBook=new AddressBook();
        //创建一个新对象并设置为默认地址，让后面的查询更有通用性
        addressBook.setIsDefault(1);

        return addressBookMapper.getDefaultAddress(addressBook).get(0);

    }

    @Override
    public List<AddressBook> getlist() {
        AddressBook addressBook=new AddressBook();
        Long currentId = BaseContext.getCurrentId();
        addressBook.setUserId(currentId);
        return addressBookMapper.getDefaultAddress(addressBook);
    }

    @Override
    public AddressBook getById(Long id) {
        AddressBook addressBook =new AddressBook();
        addressBook.setId(id);
        return addressBookMapper.getDefaultAddress(addressBook).get(0);
    }

    @Override
    public void updateById(AddressBook addressBook) {

        addressBookMapper.updateById(addressBook);

    }

    @Override
    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);

    }

    @Override
    public void updatePatch(Long userId) {
        addressBookMapper.updatePatch(userId);

    }
}
