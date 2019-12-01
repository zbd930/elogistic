package com.duogesi.mapper;

import com.duogesi.entities.address;
import com.duogesi.entities.comments;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface commentsMapper {

        List<comments> get_comments(int user_id);
        int insert_comment(comments comments);
}
