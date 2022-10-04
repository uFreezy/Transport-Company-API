package com.jws.transcomp.api.models;

import com.jws.transcomp.api.models.base.LiscenceType;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "employees")
public class Employee {
    // TODO: Fix table, there is a column names 'users' for some reason
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String username;
    private String password;
    @Transient
    private String passwordConfirm;
    @NotNull
    private String address;
    @NotNull
    private BigDecimal salary;
    @ElementCollection(targetClass = LiscenceType.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<LiscenceType> licenses = new HashSet<>();
    @OneToOne
    private Role role;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    public Employee() {
    }

    public Employee(String username, String address, BigDecimal salary, Set<LiscenceType> licenses, Role role, Company company) {
        this.username = username;
        this.address = address;
        this.salary = salary;
        this.licenses = licenses;
        this.role = role;
        this.company = company;
        // Generated password
        String pass = this.generatePassword();
        this.password = pass;
        this.passwordConfirm = pass;
    }

    public Employee(String username, String password, String passwordConfirm, String address, BigDecimal salary, Set<LiscenceType> licenses, Role role, Company company) {
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.address = address;
        this.salary = salary;
        this.licenses = licenses;
        this.role = role;
        this.company = company;
    }
    private String generatePassword() {
        return "dummy pass"; // TODO
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + this.getId() +
                ", username='" + username + '\'' +
                ", password='" + "*********" + '\'' +
                ", role=" + role +
                '}';
    }
}

