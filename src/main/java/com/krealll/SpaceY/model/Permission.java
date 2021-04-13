package com.krealll.SpaceY.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "permissions")
public class Permission extends BaseEntity{

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permissions",
            joinColumns = @JoinColumn(name="roles_id_role", referencedColumnName = "id"),

            inverseJoinColumns = @JoinColumn(name = "permissions_id_permission", referencedColumnName = "id"))
    @JsonIgnore
    private Set<Role> roles;

}
