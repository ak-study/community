package com.ak.community.mapper;

import com.ak.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("insert into user(id,name,account_id,token,gmt_create,gmt_modified) values(#{id},#{name},#{accountid},#{token},#{gmtCreate},#{gmtModified})")
    void insertUser(User user);
}
