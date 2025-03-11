package com.assignment.blogapi.model;

import com.fasterxml.uuid.Generators;
import jakarta.persistence.*;

import java.util.*;

import static com.assignment.blogapi.security.BlogAuthorities.Privileges.AUTHOR_PRIVILEGE;
import static com.assignment.blogapi.security.BlogAuthorities.Privileges.READ_PRIVILEGE;

@Entity
public class Privilege {
    @Id
    @Column(nullable = false, unique = true, name = "`priv_id`")
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "privileges")
    @Column(nullable = false)
    private Collection<Role> roles;

    @PrePersist
    public void generateOnCreate() {
        this.uuid = Generators.timeBasedGenerator().generate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Privilege)) return false;
        Privilege privilege = (Privilege) o;
        return name.equals(privilege.name);
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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public static Set<Privilege> userPrivilegeSet() {
        Privilege readPrivilege = new Privilege();
        readPrivilege.setName(READ_PRIVILEGE);
        Set<Privilege> privileges = new HashSet<>();
        privileges.add(readPrivilege);

        return privileges;
    }

    public static Set<Privilege> authorPrivilegeSet() {
        Privilege readPrivilege = new Privilege();
        readPrivilege.setName(READ_PRIVILEGE);
        Privilege authorPrivilege = new Privilege();
        authorPrivilege.setName(AUTHOR_PRIVILEGE);
        Set<Privilege> privileges = new HashSet<>();
        privileges.add(readPrivilege);
        privileges.add(authorPrivilege);

        return privileges;
    }
}
