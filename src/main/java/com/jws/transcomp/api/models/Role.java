package com.jws.transcomp.api.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
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

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
