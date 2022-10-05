package com.jws.transcomp.api.controller;

import com.jws.transcomp.api.controller.base.BaseTestController;
import com.jws.transcomp.api.models.Client;
import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.Trip;
import com.jws.transcomp.api.models.dto.trip.CreateTripDto;
import com.jws.transcomp.api.models.dto.trip.EditTripDto;
import com.jws.transcomp.api.models.dto.trip.TripDto;
import com.jws.transcomp.api.models.responses.PaginatedResponse;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TripController.class)
@WebAppConfiguration
class TripControllerTest extends BaseTestController {

    private List<Trip> trips = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.trips = this.generateTrips();

        // Mock logged username
        given(securityService.findLoggedInUsername())
                .willReturn("admin");
        // Mock employee
        given(userService.findByUsername("admin"))
                .willReturn(this.employee);
    }


    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getTrip_Successfully() throws Exception {
        Trip trip = this.trips.get(0);
        trip.setCompany(employee.getCompany());
        given(this.tripService.findById(trip.getId()))
                .willReturn(trip);

        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.get("/trip?id=" + trip.getId()));
        res.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(trip.getId()))
                .andExpect(jsonPath("$.starting_point").value(trip.getStartingPoint()));
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getTrip_Invalid() throws Exception {
        given(this.tripService.findById(-1L))
                .willThrow(IllegalArgumentException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/trip?id=-1"))
                .andExpect(status().isBadRequest());

        // doesn't belong to your company
        Trip trip = this.trips.get(0);
        trip.setCompany(this.generateCompanies().get(1));

        given(this.tripService.findById(trip.getId()))
                .willReturn(trip);

        mockMvc.perform(MockMvcRequestBuilders.get("/trip?id=" + trip.getId()))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getTrips_Successfully() throws Exception {
        String destination = this.trips.get(0).getEndingPoint();

        List<TripDto> trips = new ArrayList<>();

        this.trips.stream()
                .filter(t -> t.getEndingPoint().equals(destination)).forEach(t -> {
                    trips.add(modelMapper.map(t, TripDto.class));
                });


        given(this.tripService.filterTrips(employee.getCompany().getId(), destination, null, DEFAULT_PAGE))
                .willReturn(new PaginatedResponse(trips, (long) trips.size(), 1));

        mockMvc.perform(MockMvcRequestBuilders.get("/trip/all?destination=" + destination))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item_list").exists())
                .andExpect(jsonPath("$.item_list[0].ending_point").value(destination))
                .andExpect(jsonPath("$.item_list[1].ending_point").value(destination));

        // sorting
        String sortCriteria = "+base_price";

        List<TripDto> sortedTrips = new ArrayList<>();
        this.trips.forEach(t -> sortedTrips.add(modelMapper.map(t, TripDto.class)));

        sortedTrips.sort(Comparator.comparing(TripDto::getBasePrice));

        given(this.tripService.filterTrips(employee.getCompany().getId(), null, sortCriteria, DEFAULT_PAGE))
                .willReturn(new PaginatedResponse(sortedTrips, (long) sortedTrips.size(), 1));


        mockMvc.perform(MockMvcRequestBuilders.get("/trip/all?sort_by=" + sortCriteria))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item_list").exists())
                .andExpect(jsonPath("$.item_list[0].base_price").value(sortedTrips.get(0).getBasePrice()))
                .andExpect(jsonPath("$.item_list[1].base_price").value(sortedTrips.get(1).getBasePrice()));

    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getTrips_Invalid() throws Exception {
        // invalid sort
        String sortCriteria = "+invalid_attr";

        List<TripDto> sortedTrips = new ArrayList<>();
        this.trips.forEach(t -> sortedTrips.add(modelMapper.map(t, TripDto.class)));

        sortedTrips.sort(Comparator.comparing(TripDto::getBasePrice));

        given(this.tripService.filterTrips(employee.getCompany().getId(), null, sortCriteria, DEFAULT_PAGE))
                .willThrow(PropertyReferenceException.class);

        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.get("/trip/all?sort_by=" + sortCriteria));
        res.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getTripReport_Successfully() throws Exception {
        // hackerman
        given(this.tripService.filterTrips(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .willReturn(new PaginatedResponse(this.trips, (long) this.trips.size(), 1));

        mockMvc.perform(MockMvcRequestBuilders.get("/trip/report"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", "application/pdf"))
                .andExpect(MockMvcResultMatchers.header().exists("Content-Length"));
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void createTrip_Successfully() throws Exception {
        Trip trip = this.trips.get(0);
        CreateTripDto createTrip = new CreateTripDto();

        this.modelMapper.map(trip, createTrip);

        given(this.userService.findById(createTrip.getDriverId()))
                .willReturn(trip.getDriver());
        given(this.vehicleService.findById(createTrip.getVehicleId()))
                .willReturn(trip.getVehicle());

        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.post("/trip")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objToJson(createTrip)));
        res.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void createTrip_Invalid() throws Exception {
        // invalid arguments
        Trip trip = this.trips.get(0);
        CreateTripDto createTrip = new CreateTripDto();

        this.modelMapper.map(trip, createTrip);

        createTrip.setStartingPoint("1");
        createTrip.setEndingPoint("1");

        given(this.userService.findById(createTrip.getDriverId()))
                .willReturn(trip.getDriver());
        given(this.vehicleService.findById(createTrip.getVehicleId()))
                .willReturn(trip.getVehicle());

        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.post("/trip")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objToJson(createTrip)));
        res.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void editTrip_Successfully() throws Exception {
        Trip trip = this.trips.get(0);
        trip.setCompany(this.employee.getCompany());
        EditTripDto editTrip = new EditTripDto();

        this.modelMapper.map(trip, editTrip);

        editTrip.setStartingPoint("new starting point");

        given(this.tripService.findById(trip.getId()))
                .willReturn(trip);

        mockMvc.perform(MockMvcRequestBuilders.put("/trip")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objToJson(editTrip)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void editTrip_Invalid() throws Exception {
        // trip doesn't belong to company
        Trip trip = this.trips.get(0);
        trip.setCompany(new Company("fake company"));
        EditTripDto editTrip = new EditTripDto();

        this.modelMapper.map(trip, editTrip);

        editTrip.setStartingPoint("new starting point");

        given(this.tripService.findById(trip.getId()))
                .willReturn(trip);

        mockMvc.perform(MockMvcRequestBuilders.put("/trip")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objToJson(editTrip)))
                .andExpect(status().isBadRequest());
        // trip already started
        trip.setCompany(this.employee.getCompany());
        trip.setDeparture(DateUtils.addMonths(new Date(), -1));
        trip.setArrival(DateUtils.addMonths(new Date(), -1));

        mockMvc.perform(MockMvcRequestBuilders.put("/trip")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objToJson(editTrip)))
                .andExpect(status().isBadRequest());

        // wrong trip details
        trip.setCompany(this.employee.getCompany());

        EditTripDto glitchTrip = new EditTripDto();

        modelMapper.map(trip, glitchTrip);

        given(this.tripService.findById(glitchTrip.getId()))
                .willThrow(IllegalArgumentException.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/trip")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objToJson(glitchTrip)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void registerPayment_Successfully() throws Exception {
        Trip trip = this.trips.get(0);
        trip.setCompany(this.employee.getCompany());

        List<Client> clients = this.generateClients();
        Client client = clients.get(0);

        trip.setClients(new HashSet<>(clients));

        given(this.tripService.findById(trip.getId()))
                .willReturn(trip);
        given(this.clientService.findById(client.getId()))
                .willReturn(client);

        mockMvc.perform(MockMvcRequestBuilders.put(String.format("/trip/pay?trip_id=%s&user_id=%s", trip.getId(), client.getId())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void registerPayment_Invalid() throws Exception {
        // trip doesn't exist
        given(this.tripService.findById(-1L))
                .willThrow(IllegalArgumentException.class);

        mockMvc.perform(MockMvcRequestBuilders.put(String.format("/trip/pay?trip_id=-1&user_id=%s", this.generateClients().get(0).getId())))
                .andExpect(status().isBadRequest());

        // trip doesn't belong to company
        Trip trip = this.trips.get(0);
        trip.setCompany(new Company());

        List<Client> clients = this.generateClients();
        Client client = clients.get(0);

        trip.setClients(new HashSet<>(clients));

        given(this.tripService.findById(trip.getId()))
                .willReturn(trip);
        given(this.clientService.findById(client.getId()))
                .willReturn(client);

        mockMvc.perform(MockMvcRequestBuilders.put(String.format("/trip/pay?trip_id=%s&user_id=%s", trip.getId(), client.getId())))
                .andExpect(status().isBadRequest());
        // no user in trip
        clients.remove(clients.get(0));
        trip.setClients(new HashSet<>(clients));

        mockMvc.perform(MockMvcRequestBuilders.put(String.format("/trip/pay?trip_id=%s&user_id=%s", trip.getId(), client.getId())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void deleteTrip_Successfully() throws Exception {
        Trip trip = this.trips.get(0);
        trip.setCompany(this.employee.getCompany());

        given(this.tripService.findById(trip.getId()))
                .willReturn(trip);

        mockMvc.perform(MockMvcRequestBuilders.delete("/trip?id=" + trip.getId()))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void deleteTrip_Invalid() throws Exception {
        // invalid id
        Trip trip = this.trips.get(0);

        given(this.tripService.findById(-1L))
                .willThrow(IllegalArgumentException.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/trip?id=-1"))
                .andExpect(status().isBadRequest());

        // other company
        trip.setCompany(new Company());

        given(this.tripService.findById(trip.getId()))
                .willReturn(trip);

        mockMvc.perform(MockMvcRequestBuilders.delete("/trip?id=" + trip.getId()))
                .andExpect(status().isBadRequest());
    }
}
