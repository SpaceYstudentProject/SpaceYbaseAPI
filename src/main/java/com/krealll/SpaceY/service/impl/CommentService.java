package com.krealll.SpaceY.service.impl;

import com.krealll.SpaceY.model.Comment;
import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.dto.CreateCommentDTO;

import java.util.Map;

public interface CommentService {

    Map<String,Object> createComment(CreateCommentDTO commentDTO, Integer entityId, User user);

    Map<String,Object> updateComment(Integer commentId,Map<String,Object> fields);

}
