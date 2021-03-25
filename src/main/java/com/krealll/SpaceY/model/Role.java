package com.krealll.SpaceY.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
public class Role extends BaseEntity{

    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private List<Permission> permissions;

}
