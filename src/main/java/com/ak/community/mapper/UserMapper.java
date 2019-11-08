package com.ak.community.mapper;

import com.ak.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    @Insert("insert into user(id,name,account_id,token,gmt_create,gmt_modified,avatar_url) values(#{id},#{name},#{account_id},#{token},#{gmt_create},#{gmt_modified},#{avatarUrl})")
    void insertUser(User user);

    @Select("select * from user where token=#{token}")
    User findByToken(String token);

    @Select("select * from user where id =#{id}")
    User findNameByID(Long id);
}
