package com.krealll.SpaceY.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
public class Role extends BaseEntity{

    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<User> users;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)

    private Set<Permission> permissions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name) && Objects.equals(users, role.users) && Objects.equals(permissions, role.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
