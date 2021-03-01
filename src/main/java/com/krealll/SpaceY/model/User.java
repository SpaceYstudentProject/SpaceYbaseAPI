package com.krealll.SpaceY.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User  extends BaseEntity{

    private String login;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn(name="users_id_user", referencedColumnName = "id"),

            inverseJoinColumns = @JoinColumn(name = "roles_id_role", referencedColumnName = "id"))
    private List<Role> roles;
}
