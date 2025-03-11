package com.assignment.blogapi.model;

import com.fasterxml.uuid.Generators;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

@Entity
public class BlogUser {
    @Id
    @Column(nullable = false, unique = true, name = "user_id")
    private UUID uuid;

    @Column(nullable = false, unique = true)
    private String email;

    @Column()
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "role_id") })
    @Column(nullable = false)
    private Collection<Role> roles;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .flatMap(role -> role.getPrivileges().stream()).map(p -> new SimpleGrantedAuthority(p.getName()))
                .collect(Collectors.toSet());
    }

    @PrePersist
    public void generateOnCreate() {
        this.uuid = Generators.timeBasedGenerator().generate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlogUser)) return false;
        BlogUser blogUser = (BlogUser) o;
        return Objects.equals(getUuid(), blogUser.getUuid()) &&
                Objects.equals(getEmail(), blogUser.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getEmail());
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
