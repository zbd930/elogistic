package com.duogesi.mapper;

import com.duogesi.beans.comments;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface commentsMapper {

    List<comments> get_comments(int user_id);

    int insert_comment(comments comments);
}
