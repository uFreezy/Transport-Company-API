package com.jws.transcomp.api.service.impl;

import com.jws.transcomp.api.models.Employee;
import com.jws.transcomp.api.repository.EmployeeRepository;
import com.jws.transcomp.api.repository.RoleRepository;
import com.jws.transcomp.api.service.base.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(EmployeeRepository employeeRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Employee save(Employee employee) {
        employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
        return employeeRepository.save(employee);
    }

    @Override
    public boolean delete(Long id) {
        if (this.employeeRepository.existsById(id)) {
            this.employeeRepository.deleteById(id);

            return this.employeeRepository.findById(id).isEmpty();
        } else {
            throw new EntityNotFoundException("User with provided id doesn't exist!");
        }

    }

    @Override
    public Employee findByUsername(String username) {
        return employeeRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username " + username + " doesn't exist."));
    }

    @Override
    public Employee findById(Long id) {
        return this.employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User id is not valid."));
    }

    @Override
    public Employee getByIndex(int indx) {
        return this.employeeRepository.findAll().get(indx);
    }

    @Override
    public List<Employee> findByRole(String roleName) {
        return employeeRepository.findByRole(roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("No role exists with name: " + roleName)));
    }

    @Override
    public List<Employee> getAll() {
        return this.employeeRepository.findAll();
    }

    @Override
    public boolean any() {
        return employeeRepository.count() > 0;
    }

    @Override
    public long count() {
        return employeeRepository.count();
    }
}
