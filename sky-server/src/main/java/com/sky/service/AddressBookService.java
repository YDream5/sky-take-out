package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

/**
 * @author wyj
 * @version 1.0
 */
public interface AddressBookService {
    void save(AddressBook addressBook);

    AddressBook getDefault();

    List<AddressBook> getlist();

    AddressBook getById(Long id);

    void updateById(AddressBook addressBook);

    void deleteById(Long id);

    void updatePatch(Long userId);
}
