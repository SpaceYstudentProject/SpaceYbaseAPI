package com.krealll.SpaceY.model.dto;

import com.krealll.SpaceY.model.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {

    private Integer id;
    private String content;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Integer userId;
    private Integer parentId;
    private Integer entityId;

    public static CommentDTO fromComment(Comment comment){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreated(comment.getCreated());
        commentDTO.setUpdated(comment.getUpdated());
        commentDTO.setUserId(comment.getUsersId());
        commentDTO.setParentId(comment.getParentId());
        commentDTO.setEntityId(comment.getEntitiesId());
        return commentDTO;
    }

}
