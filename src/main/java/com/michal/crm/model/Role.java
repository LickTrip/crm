package com.michal.crm.model;

import com.michal.crm.model.types.RoleTypes;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "role")
public class Role implements GrantedAuthority{

    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Integer id;

    @NotNull
    @Column(name = "user_id")
    private Integer user_id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private RoleTypes name;

    public Integer getId() {
        return id;
    }

    public RoleTypes getName() {
        return name;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setName(RoleTypes name) {
        this.name = name;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    @Override
    public String getAuthority() {
        return getName().toString();
    }

    public Role() {
    }

    public Role(Integer user_id, RoleTypes name) {
        this.user_id = user_id;
        this.name = name;
    }
}
