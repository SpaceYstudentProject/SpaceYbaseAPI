package com.krealll.SpaceY.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "permissions")
public class Permission extends BaseEntity{

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permissions",
            joinColumns = @JoinColumn(name="roles_id_role", referencedColumnName = "id"),

            inverseJoinColumns = @JoinColumn(name = "permissions_id_permission", referencedColumnName = "id"))
    private List<Role> roles;

}
