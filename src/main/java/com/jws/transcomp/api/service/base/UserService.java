package com.jws.transcomp.api.service.base;

import com.jws.transcomp.api.models.Employee;

import java.util.List;

public interface UserService {
    void save(Employee employee);

    boolean delete(Long id);

    Employee findByUsername(String username);

    Employee findById(Long id);

    Employee getByIndex(int indx);

    List<Employee> findByRole(String roleName);

    List<Employee> getAll();

    boolean any();

    long count();
}
