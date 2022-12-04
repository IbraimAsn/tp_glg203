package com.yaps.petstore.security.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="yaps_user")
public class YapsUser {
    @Id
    @Size(max = 20)
    @NotBlank
    @Column(name = "username")
    private String username;

    @Size(max = 500)
    @Column(name = "userpassword")
    @NotBlank
    private String userPassword;

    @Column(name = "userrole")
    @NotNull
    @Enumerated(EnumType.STRING)
    private YapsUserRole role;

    YapsUser() {
        super();
    }

    public YapsUser(@Size(max = 20) @NotBlank String username, @NotNull YapsUserRole role) {
        this.username = username;
        this.role = role;
    }


    public YapsUser(@Size(max = 20) @NotBlank String username, @NotNull YapsUserRole role, @NotBlank String password) {
        this.username = username;
        this.role = role;
        this.userPassword = password;
    }


    public String getUsername() {
        return username;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public YapsUserRole getRole() {
        return role;
    }

    public void setRole(@NotNull YapsUserRole role) {
        this.role = role;
    }

}
