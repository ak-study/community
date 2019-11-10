package com.ak.community.dto;

import com.ak.community.model.Question;
import com.ak.community.model.User;
import lombok.Data;

import java.util.List;

@Data
public class PersonQuestionDTO {
    private List<Question> questionList;
    private User user;
}
