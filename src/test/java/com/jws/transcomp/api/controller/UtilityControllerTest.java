package com.jws.transcomp.api.controller;

import com.jws.transcomp.api.controller.base.BaseTestController;
import com.jws.transcomp.api.models.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UtilityController.class)
@WebAppConfiguration
class UtilityControllerTest extends BaseTestController {

    private List<Role> roles = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.roles = this.generateRoles();

        // Mock logged username
        given(securityService.findLoggedInUsername())
                .willReturn("admin");
        // Mock employee
        given(userService.findByUsername("admin"))
                .willReturn(this.employee);
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getRoles_Successfully() throws Exception {
        given(this.roleService.getAll())
                .willReturn(this.roles);

        mockMvc.perform(MockMvcRequestBuilders.get("/utility/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].name").value(this.roles.get(0).getName()));
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getLoggedRoleSuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/utility/loggedrole"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(this.roles.get(1).getName()));
    }

}
