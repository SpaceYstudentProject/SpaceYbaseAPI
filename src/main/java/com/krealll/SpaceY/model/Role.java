package com.krealll.SpaceY.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
public class Role extends BaseEntity{

    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<User> users;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<Permission> permissions;

}
