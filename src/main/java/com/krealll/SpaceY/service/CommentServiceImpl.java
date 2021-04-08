package com.krealll.SpaceY.service;


import com.krealll.SpaceY.controller.parameters.RequestParameters;
import com.krealll.SpaceY.controller.parameters.ResponseParameters;
import com.krealll.SpaceY.model.Comment;
import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.dto.CommentDTO;
import com.krealll.SpaceY.model.dto.CreateCommentDTO;
import com.krealll.SpaceY.repository.CommentRepository;
import com.krealll.SpaceY.service.impl.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Map<String, Object> createComment(CreateCommentDTO commentDTO, Integer entityId, User user) {
        Map<String, Object> response = new HashMap<>();
        Optional<Comment> commentOptional;
        Comment newComment = commentDTO.toComment();
        newComment.setCreated(LocalDateTime.now());
        newComment.setUpdated(LocalDateTime.now());
        newComment.setUsersId(user.getId());
        newComment.setEntitiesId(entityId);
        if(commentDTO.getParentId() != 0){
            commentOptional = commentRepository.findCommentById(commentDTO.getParentId());
            if(commentOptional.isEmpty()){
                response.put(ResponseParameters.ERROR, HttpStatus.BAD_REQUEST);
                return response;
            }
            newComment.setDepth((short) (commentOptional.get().getDepth() + 1));
        } else {
            newComment.setParentId(null);
            newComment.setDepth((short) 0);
        }
        Comment createdComment = commentRepository.save(newComment);
        response.put(ResponseParameters.COMMENT, createdComment);
        return response;
    }

    @Override
    public Map<String, Object> updateComment(Integer commentId, Map<String, Object> fields) {
        Map<String, Object> response = new HashMap<>();
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            if(fields.containsKey(RequestParameters.CONTENT)){
                String newContent = (String)fields.get(RequestParameters.CONTENT);
                Comment comment = commentOptional.get();
                comment.setContent(newContent);
                comment.setUpdated(LocalDateTime.now());
                comment = commentRepository.save(comment);
                response.put(ResponseParameters.COMMENT, CommentDTO.fromComment(comment));
            } else {
                response.put(ResponseParameters.ERROR, HttpStatus.BAD_REQUEST);
            }
            return response;
        } else {
            response.put(ResponseParameters.ERROR, HttpStatus.BAD_REQUEST);
            return response;
        }
    }


}
