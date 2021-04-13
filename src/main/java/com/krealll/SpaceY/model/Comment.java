package com.krealll.SpaceY.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity{

    private String content;

    @Column(name= "creation_date")
    private LocalDateTime created;

    @Column(name= "updated_date")
    private LocalDateTime updated;

    private Short depth;

    @Column(name = "parent_id")
    private Integer parentId;

    @JoinColumn(name = "entities_id", referencedColumnName = "id")
    @Column(name = "entities_id")
    private Integer entitiesId;

    @JoinColumn(name = "users_id", referencedColumnName = "id")
    @JsonIgnore
    @Column(name = "users_id")
    private Integer usersId;


}
