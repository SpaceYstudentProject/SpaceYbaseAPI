package com.krealll.SpaceY.model.dto;

import com.krealll.SpaceY.model.Comment;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentDTO that = (CommentDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(content, that.content) && Objects.equals(created, that.created) && Objects.equals(userId, that.userId) && Objects.equals(parentId, that.parentId) && Objects.equals(entityId, that.entityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, created, userId, parentId, entityId);
    }
}
