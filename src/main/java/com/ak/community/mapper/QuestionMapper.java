package com.ak.community.mapper;

import com.ak.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description,gmt_modified,gmt_Create,creator,tag) values(#{title},#{description},#{gmt_modified},#{gmt_create},#{creator},#{tag})")
    void insertQuestion(Question question);

    @Select("select * from question")
    List<Question> getQuestionList();

    @Select("select * from question where creator = #{creator}")
    List<Question> getQuestionByPerson(Long creator);

    @Select("select count(1) from question where creator = #{creator}")
    Integer getQuestionCount(Long creator);

    @Select("select * from question where id =#{id}")
    Question getQuestionByID(Integer id);

    @Update("update question set title=#{title},description=#{description},tag=#{tag} where id=#{id}")
    void updateQuestionByID(Question question);
}
