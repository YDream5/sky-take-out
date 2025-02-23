package com.sky.mapper;

import com.sky.entity.AddressBook;
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
public interface AddressBookMapper {

    @Insert("insert into sky_take_out.address_book(user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label, is_default) " +
            "values(#{userId},#{consignee},#{sex},#{phone},#{provinceCode},#{provinceName},#{cityCode},#{cityName},#{districtCode},#{districtName},#{detail},#{label},#{isDefault})")
    void insert(AddressBook addressBook);

    List<AddressBook> getDefaultAddress(AddressBook addressBook);

    void updateById(AddressBook addressBook);
    @Delete("delete  from  sky_take_out.address_book where id =#{id}")
    void deleteById(Long id);

    @Update("update sky_take_out.address_book set is_default=0 where user_id=#{userId}")
    void updatePatch(Long userId);
}
