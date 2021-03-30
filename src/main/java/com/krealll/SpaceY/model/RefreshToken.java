package com.krealll.SpaceY.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "tokens")
@Entity
public class RefreshToken  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    private String value;
    private Long expiresIn;
    private LocalDateTime createdAt;

    @JoinColumn(name = "users_id", referencedColumnName = "id")
    @JsonIgnore
    @Column(name = "users_id")
    private Integer usersId;


}
