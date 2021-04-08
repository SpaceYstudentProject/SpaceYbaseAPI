package com.krealll.SpaceY.model.dto;

import com.krealll.SpaceY.model.Comment;
import lombok.Data;

@Data
public class CreateCommentDTO {
    private String content;
    private String username;
    private Integer parentId;


    public Comment toComment(){
        Comment comment = new Comment();
        comment.setContent(this.content);
        comment.setParentId(this.parentId);
        return comment;
    }
}
