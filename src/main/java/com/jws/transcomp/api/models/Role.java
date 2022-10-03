package com.jws.transcomp.api.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @OneToMany
    @JoinColumn(name = "users")
    private Set<Employee> employees;

    public Role() {
    }

    public Role(String name, Set<Employee> employees) {
        this.name = name;
        this.employees = employees;
    }

    public Role(String name) {
        this(name, new HashSet<>());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Employee> getUsers() {
        return employees;
    }

    public void setUsers(Set<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
