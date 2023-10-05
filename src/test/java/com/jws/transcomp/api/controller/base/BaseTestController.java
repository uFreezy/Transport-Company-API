package com.jws.transcomp.api.controller.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jws.transcomp.api.models.*;
import com.jws.transcomp.api.models.base.FuelType;
import com.jws.transcomp.api.models.base.LiscenceType;
import com.jws.transcomp.api.models.base.TripType;
import com.jws.transcomp.api.repository.ClientRepository;
import com.jws.transcomp.api.repository.CompanyRepository;
import com.jws.transcomp.api.service.base.*;
import org.apache.commons.lang3.time.DateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.*;

@AutoConfigureMockMvc

public abstract class BaseTestController {
    protected final Pageable DEFAULT_PAGE = PageRequest.of(0, 20);
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper mapper;
    protected ModelMapper modelMapper = new ModelMapper();

    @MockBean
    protected ClientRepository clientRepository;
    @MockBean
    protected CompanyRepository companyRepository;
    @MockBean
    protected UserDetailsService userDetailsService;
    @MockBean
    protected SecurityService securityService;
    @MockBean
    protected UserService userService;
    @MockBean
    protected EmployeeService employeeService;
    @MockBean
    protected ClientService clientService;
    @MockBean
    protected RoleService roleService;
    @MockBean
    protected CompanyService companyService;
    @MockBean
    protected VehicleService vehicleService;
    @MockBean
    protected TripService tripService;

    protected Employee employee = new Employee(
            "admin",
            "1234",
            "1234",
            "admin's address",
            BigDecimal.valueOf(100),
            null,
            this.generateRoles().get(1), this.generateCompanies().get(0));

    protected String objToJson(Object dtoObject) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(dtoObject);
    }

    protected List<Employee> generateEmployees() {
        List<Employee> employees = new ArrayList<>();

        String[] usernames = {"user1", "user2", "user3", "user4"};
        BigDecimal[] salaries = {
                new BigDecimal(100),
                new BigDecimal(200),
                new BigDecimal(300),
                new BigDecimal(400)};

        LiscenceType[] liscenceTypes = {
                LiscenceType.B,
                LiscenceType.C,
                LiscenceType.D,
                LiscenceType.A2
        };

        Role role = new Role();
        role.setId(1L);

        Company cmp = new Company("Kadabra");

        for (int i = 0; i < usernames.length; i++) {
            Set<LiscenceType> liscenses = new HashSet<>();
            liscenses.add(liscenceTypes[i]);

            cmp.setId(1L);
            Employee emp = new Employee(usernames[i], "adress", salaries[i], liscenses, role, cmp);
            emp.setId((long) i);
            employees.add(emp);
        }

        return employees;
    }

    protected List<Company> generateCompanies() {
        List<Company> companies = new ArrayList<>();

        String[] companyNames = {"Abra", "Kadabra", "Zebra"};

        long i = 0L;
        for (String companyName : companyNames) {
            Company comp = new Company(companyName);
            comp.setId(i);

            companies.add(comp);
            i++;
        }

        return companies;
    }

    protected List<Vehicle> generateVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();

        String[] makes = {"Ford", "BMW", "Mazda"};
        FuelType[] fuelTypes = {FuelType.DIESEL, FuelType.PETROL, FuelType.HYBRID};
        Set<LiscenceType> liscenceTypes = new HashSet<>();
        liscenceTypes.add(LiscenceType.B);

        for (int i = 0; i < 3; i++) {
            Vehicle vh = new Vehicle(makes[i], "sample", fuelTypes[i], (short) 10, 10, liscenceTypes, employee.getCompany());
            vh.setId((long) i);
            vehicles.add(vh);
        }

        vehicles.add(new Vehicle("invalid", "invalid", FuelType.DIESEL, (short) 1, 1, new HashSet<>(), new Company("invalid")));

        return vehicles;
    }


    protected List<Role> generateRoles() {
        List<Role> roles = new ArrayList<>();

        Set<Employee> employees = new HashSet<>(this.generateEmployees());

        roles.add(new Role("User", employees));
        roles.add(new Role("Admin", employees));


        return roles;
    }

    protected List<Client> generateClients() {
        List<Client> clients = new ArrayList<>();

        Client client1 = new Client(123L, "Ivan");
        client1.addCompany(employee.getCompany());
        clients.add(client1);
        clients.add(new Client(321L, "Pesho"));
        clients.add(new Client(999L, "Dragan"));


        return clients;
    }

    protected List<Trip> generateTrips() {
        List<Trip> trips = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Employee emp = this.generateEmployees().get(0);
            Company cmp = emp.getCompany();
            Vehicle vh = this.generateVehicles().get(0);
            Set<Client> clients = new HashSet<>(this.generateClients());
            Trip trip = new Trip("departure", "destination", DateUtils.addMonths(new Date(), 1), DateUtils.addMonths(new Date(), 2), BigDecimal.valueOf(100 * i), TripType.PASSENGER_TRIP, 100, emp, cmp, vh, clients);

            trip.setId((long) i);
            trips.add(trip);
        }

        return trips;
    }
}
