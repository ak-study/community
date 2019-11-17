package com.ak.community.mapper;

import com.ak.community.dto.NotificationDTO;
import com.ak.community.model.Notification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NotificationMapper {
    @Insert("insert into notification values(null,#{notifier},#{receiver},#{outerId},#{type},#{gmt_create},#{status})")
    void insertNotification(Notification notification);

    @Select("select * from notification where receiver=#{receiver}")
    List<Notification> getNotifyListByID(Long receiver);

    @Update("update notification set status = 0 where id = #{id}")
    void setNotifyStatusByID(Long id);
}
