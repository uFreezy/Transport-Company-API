package com.jws.transcomp.api.controller;

import com.jws.transcomp.api.controller.base.BaseTestController;
import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.Vehicle;
import com.jws.transcomp.api.models.base.FuelType;
import com.jws.transcomp.api.models.dto.vehicle.CreateVehicleDto;
import com.jws.transcomp.api.models.dto.vehicle.EditVehicleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleController.class)
@WebAppConfiguration
class VehicleControllerTest extends BaseTestController {

    private List<Vehicle> vehicles = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.vehicles = this.generateVehicles();

        // Mock logged username
        given(securityService.findLoggedInUsername())
                .willReturn("admin");
        // Mock employee
        given(userService.findByUsername("admin"))
                .willReturn(this.employee);
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getVehicle_Successfully() throws Exception {
        Vehicle vh = this.vehicles.get(new Random().nextInt(this.vehicles.size()));

        given(vehicleService.findById(vh.getId()))
                .willReturn(vh);

        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.get("/vehicle?id=" + vh.getId()));
        res.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vh.getId()));
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getVehicle_Invalid() throws Exception {
        // invalid id
        given(vehicleService.findById(1000L))
                .willThrow(IllegalArgumentException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle?id=1000"))
                .andExpect(status().isBadRequest());
        // not your company
        Vehicle vh = this.vehicles.get(0);
        vh.setCompany(new Company());

        given(vehicleService.findById(vh.getId()))
                .willReturn(vh);

        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.get("/vehicle?id=" + vh.getId()));
        res.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getCompanyVehicles_Successfully() throws Exception {
        given(vehicleService.getByCompany(this.vehicles.get(0).getCompany()))
                .willReturn(this.vehicles.stream().filter(v -> v.getCompany().getName().equals(this.vehicles.get(0).getCompany().getName())).
                        collect(Collectors.toList()));

        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].make").value("Ford"))
                .andExpect(jsonPath("$[1].make").value("BMW"))
                .andExpect(jsonPath("$[2].make").value("Mazda"))
                .andExpect(jsonPath("$[3]").doesNotExist());

    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void createVehicle_Successfully() throws Exception {
        CreateVehicleDto createDto = new CreateVehicleDto("BMW", "X3", FuelType.DIESEL, (short) 6, (short) 1000, new HashSet<>());

        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.post("/vehicle")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objToJson(createDto)));
        res.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void createVehicle_Invalid() throws Exception {
        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.post("/vehicle")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objToJson(new CreateVehicleDto())));
        res.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void editVehicle_Successfully() throws Exception {
        Vehicle vh = this.vehicles.get(0);
        given(this.vehicleService.findById(vh.getId()))
                .willReturn(vh);

        EditVehicleDto editVh = new EditVehicleDto(vh.getId(), "make", "model", "electric", (short) 12, 300, new HashSet<>(), this.employee.getCompany());

        mockMvc.perform(MockMvcRequestBuilders.put("/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objToJson(editVh)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void editVehicle_Invalid() throws Exception {
        // invalid id
        given(this.vehicleService.findById(-1L))
                .willThrow(IllegalArgumentException.class);

        EditVehicleDto editVh = new EditVehicleDto(-1L, "make", "model", "electric", (short) 12, 300, new HashSet<>(), this.employee.getCompany());

        mockMvc.perform(MockMvcRequestBuilders.put("/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objToJson(editVh)))
                .andExpect(status().isBadRequest());

        // not owned by company
        Vehicle vh = this.vehicles.get(0);
        editVh.setId(vh.getId());
        vh.setCompany(new Company());

        given(this.vehicleService.findById(vh.getId()))
                .willReturn(vh);

        mockMvc.perform(MockMvcRequestBuilders.put("/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objToJson(editVh)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void deleteVehicle_Successfully() throws Exception {
        Vehicle vh = this.vehicles.get(0);
        given(this.vehicleService.findById(vh.getId()))
                .willReturn(vh);

        mockMvc.perform(MockMvcRequestBuilders.delete("/vehicle?id=" + vh.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void deleteVehicle_Invalid() throws Exception {
        // invalid id
        given(this.vehicleService.findById(-1L))
                .willThrow(IllegalArgumentException.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/vehicle?id=-1"))
                .andExpect(status().isBadRequest());

        // not owned by company
        Vehicle vh = this.vehicles.get(0);
        vh.setCompany(new Company());
        given(this.vehicleService.findById(vh.getId()))
                .willReturn(vh);

        mockMvc.perform(MockMvcRequestBuilders.delete("/vehicle?id=" + vh.getId()))
                .andExpect(status().isBadRequest());
    }
}
