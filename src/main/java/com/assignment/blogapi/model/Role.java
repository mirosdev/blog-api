package com.assignment.blogapi.model;

import com.fasterxml.uuid.Generators;

import jakarta.persistence.*;

import java.util.*;

import java.util.stream.Collectors;

import static com.assignment.blogapi.security.BlogAuthorities.Privileges.READ_PRIVILEGE;
import static com.assignment.blogapi.security.BlogAuthorities.Roles.ROLE_ADMIN;
import static com.assignment.blogapi.security.BlogAuthorities.Roles.ROLE_USER;

@Entity
public class Role {
    @Id
    @Column(nullable = false, unique = true, name = "role_id")
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    // TODO compare with privileges
    @JoinTable(joinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "priv_id", referencedColumnName = "priv_id") })
//    @Column(nullable = false)
    private Collection<Privilege> privileges;

    @PrePersist
    public void generateOnCreate() {
        this.uuid = Generators.timeBasedGenerator().generate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Collection<Privilege> privileges) {
        this.privileges = privileges;
    }

    public static Set<Role> singleUserRole(Set<Privilege> privileges) {
        Role role = new Role();
        role.setName(ROLE_USER);
        role.setPrivileges(
                privileges.stream()
                        .filter(privilege -> privilege.getName().equals(READ_PRIVILEGE))
                        .collect(Collectors.toSet())
        );
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        return roles;
    }

    public static Set<Role> singleAuthorRole(Set<Privilege> privileges) {
        Role role = new Role();
        role.setName(ROLE_ADMIN);
        role.setPrivileges(privileges);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        return roles;
    }
}

