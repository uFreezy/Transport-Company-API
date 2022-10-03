package com.jws.transcomp.api.controller;

import com.jws.transcomp.api.controller.base.BaseTestController;
import com.jws.transcomp.api.models.Client;
import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.dto.client.CreateClientDto;
import com.jws.transcomp.api.models.responses.PaginatedResponse;
import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
@WebAppConfiguration
class ClientControllerTest extends BaseTestController {
    @MockBean
    ApplicationContext context;
    private List<Client> clients = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.clients = this.generateClients();

        // Mock logged username
        given(securityService.findLoggedInUsername())
                .willReturn("admin");
        // Mock employee
        given(userService.findByUsername("admin"))
                .willReturn(this.employee);
    }

    @AfterEach
    void tearDown() {
        this.clients = new ArrayList<>();
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getClient_By_Id_Successfully() throws Exception {
        Client client = this.clients.get(0);

        given(clientService.findById(client.getId())).willReturn(client);

        mockMvc.perform(MockMvcRequestBuilders.get("/client?id=" + client.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivan"));
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getClientById_Wrong_Id() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/client?id=-999"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getCompanyClients_Successfully() throws Exception {

        Pageable pageable = PageRequest.of(0, 8);
        PaginatedResponse expectedResponse = new PaginatedResponse(this.clients.subList(0, 1), 1L, 1);
        given(clientService.getClients(pageable)).willReturn(expectedResponse);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/client/all?page=0&size=8"));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item_list[0].name").value("Ivan"))
                .andExpect(jsonPath("$.item_list[8]").doesNotHaveJsonPath());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void addClient_Successfully() throws Exception {
        Client client = this.clients.get(new Random().nextInt(this.clients.size()));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objToJson(client)));

        result.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void addClient_Invalid() throws Exception {
        Client client = new Client();
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objToJson(client)));

        result.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void addClientExisting_Successfully() throws Exception {
        Client client = new Client(1000L, "sample");

        given(this.clientService.idExists(1000L)).willReturn(true);
        given(clientService.findById(client.getId())).willReturn(client);


        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objToJson(client)));

        result.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void addClientExisting_Invalid() throws Exception {
        Client client = new Client(1000L, "sample");

        Set<Company> companySet = new HashSet<>();
        companySet.add(userService.findByUsername("admin").getCompany());
        client.setCompanies(companySet);

        given(this.clientService.idExists(1000L)).willReturn(true);
        given(clientService.findById(client.getId())).willReturn(client);


        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objToJson(new CreateClientDto(1000L, "sample"))));

        result.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void removeFromCompany_Successful() throws Exception {
        Client client = this.clients.get(new Random().nextInt(this.clients.size()));

        Set<Company> companySet = new HashSet<>();
        companySet.add(userService.findByUsername("admin").getCompany());
        client.setCompanies(companySet);

        given(clientService.findById(client.getId())).willReturn(client);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/client/remove?id=" + client.getId()));

        result.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void removeFromCompany_Invalid() throws Exception {
        given(clientService.findById(-1)).willThrow(IllegalArgumentException.class);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/client/remove?id=-1"));

        result.andExpect(status().isBadRequest());

        given(clientService.findById(-2)).willThrow(RuntimeException.class);

        ResultActions result2 = mockMvc.perform(MockMvcRequestBuilders.put("/client/remove?id=-2"));

        result2.andExpect(status().is5xxServerError());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void removeFromCompany_NotAClient() throws Exception {
        Client client = this.clients.get(new Random().nextInt(this.clients.size()));
        client.setCompanies(new HashSet<>());

        given(clientService.findById(client.getId())).willReturn(client);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/client/remove?id=" + client.getId()));

        result.andExpect(status().isBadRequest());
    }
}