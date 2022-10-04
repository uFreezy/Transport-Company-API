package com.jws.transcomp.api;

import com.jws.transcomp.api.models.*;
import com.jws.transcomp.api.models.base.FuelType;
import com.jws.transcomp.api.models.base.LiscenceType;
import com.jws.transcomp.api.models.base.TripType;
import com.jws.transcomp.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
public class Seeder implements ApplicationRunner {
    private final UserService userService;
    private final RoleService roleService;
    private final CompanyService companyService;
    private final VehicleService vehicleService;
    private final TripService tripService;
    private final ClientService clientService;

    private final Random rGenerator = new Random();

    @Autowired
    public Seeder(RoleService roleService, UserService userService, CompanyService companyService, VehicleService vehicleService, TripService tripService, ClientService clientService) {
        this.roleService = roleService;
        this.userService = userService;
        this.companyService = companyService;
        this.vehicleService = vehicleService;
        this.tripService = tripService;
        this.clientService = clientService;
    }

    private void companySeed() {
        if (companyService.any()) return;

        List<Company> companies = new ArrayList<>();
        companies.add(new Company("Ekont", new HashSet<>()));
        companies.add(new Company("Speedy", new HashSet<>()));
        companies.add(new Company("Union Ivkoni", new HashSet<>()));

        companies.forEach(companyService::save);

    }

    private void vehicleSeed() {
        if (vehicleService.any()) return;
        List<String> vehicleMakes = new ArrayList<>();
        vehicleMakes.add("Ford");
        vehicleMakes.add("Toyota");
        vehicleMakes.add("Mercedes-Benz");
        vehicleMakes.add("Renault");
        vehicleMakes.add("Fiat");


        for (int i = 0; i < 100; i++) {
            String make = vehicleMakes.get(rGenerator.nextInt(vehicleMakes.size()));
            // Pick random model
            String model = String.valueOf(rGenerator.nextInt(Integer.MAX_VALUE));
            // Pick random fueltype
            FuelType fuelType = FuelType.values()[rGenerator.nextInt(FuelType.values().length)];
            short peopleCapacity = (short) rGenerator.nextInt(1000);
            int cargoCapacity = rGenerator.nextInt(1000000000);

            Set<LiscenceType> lType = new HashSet<>();
            if (cargoCapacity > 2000)
                lType.add(LiscenceType.C);
            else if (peopleCapacity > 10)
                lType.add(LiscenceType.D);
            else
                lType.add(LiscenceType.B);

            Company comp = companyService.findAll().get(rGenerator.nextInt(companyService.findAll().size()));

            vehicleService.save(new Vehicle(make, model, fuelType, peopleCapacity, cargoCapacity, lType, comp));
        }


    }

    private void clientSeed() {
        if (clientService.any()) return;
        List<String> names = Arrays.asList("Ivan", "Petkan", "Georgi");
        List<String> lastNames = Arrays.asList("Ivanov", "Dimitrov", "Balkandzhiev");
        for (int i = 0; i < 10; i++) {
            // Magic
            long egn = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;

            // Make sure that ids won't repeat
            while (clientService.idExists(egn)) {
                egn = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
            }

            String name = names.get(rGenerator.nextInt(names.size())) + " "
                    + lastNames.get(rGenerator.nextInt(lastNames.size()));
            Client client = new Client(egn, name);

            Set<Company> comps = new HashSet<>();
            comps.add(companyService.findAll().get(rGenerator.nextInt((int) companyService.count())));
            comps.add(companyService.findAll().get(rGenerator.nextInt((int) companyService.count())));
            client.setCompanies(comps);

            clientService.save(client);
        }
    }

    private void tripSeed() {
        if (tripService.any()) return;
        List<String> startingPoints = Arrays.asList("Varna", "Sofia", "Burgas", "Stara Zagora");
        List<String> arrivalPoints = Arrays.asList("Shumen", "Nesebar", "Gorna Oryahovica");


        for (int i = 0; i < 100; i++) {
            String starting = startingPoints.get(rGenerator.nextInt(startingPoints.size()));
            String ending = arrivalPoints.get(rGenerator.nextInt(arrivalPoints.size()));
            Date departure = Date.from(Instant.ofEpochSecond(ThreadLocalRandom.current().nextInt()));
            Date arrival = Date.from(departure.toInstant().plusSeconds((long) Short.MAX_VALUE * 5));

            List<Client> clients = new ArrayList<>();
            TripType trpType = TripType.values()[rGenerator.nextInt(TripType.values().length)];


            int maxPeople = (trpType == TripType.PASSENGER_TRIP) ? 100 : 2;
            for (int j = 0; j < maxPeople; j++) {
                Client cl = clientService.getAll().get(rGenerator.nextInt((int) clientService.count()));
                if (clients.stream().noneMatch(c -> Objects.equals(c.getId(), cl.getId()))) {
                    clients.add(cl);
                }

            }
            int cargSize = (trpType == TripType.CARGO_TRIP) ? 10000 : 500;


            List<Vehicle> vehicles = vehicleService.getAll()
                    .stream()
                    .filter(v -> v.getPeopleCapacity() >= maxPeople && v.getCargoCapacityKg() >= cargSize)
                    .collect(Collectors.toUnmodifiableList());

            Vehicle vehicle = vehicles.get(rGenerator.nextInt(vehicles.size()));

            List<Employee> employees = userService.getAll()
                    .stream()
                    .filter(e -> e.getLicenses().containsAll(vehicle.getRequiredLicenses()))
                    .collect(Collectors.toUnmodifiableList());
            Employee dr = employees.get(rGenerator.nextInt(employees.size()));

            Company comp = dr.getCompany();

            Trip tr = new Trip(starting, ending, departure, arrival, BigDecimal.valueOf(22), trpType, cargSize, dr, comp, vehicle, new HashSet<>(clients));
            tripService.save(tr);
        }
    }

    private void roleSeed() {
        if (roleService.any()) return;

        List<String> roleNames = Arrays.asList("User", "Admin");

        roleNames.forEach(name -> roleService.save(new Role(name)));
    }

    private void userSeed() {
        if (userService.any()) return;

        List<String> drivers = Arrays.asList("Ivan", "Petar", "Gosho", "Vanya", "Maria");
        List<String> companyAdmins = Arrays.asList("Petya", "Ivaylo");
        List<Company> companies = companyService.findAll();
        String pass = "123456";

        for (String name : drivers) {
            Company comp = companies.get(rGenerator.nextInt(companies.size()));
            Employee user = new Employee(name, pass, pass, "new address", BigDecimal.valueOf(123), Set.of(LiscenceType.D, LiscenceType.C, LiscenceType.B),
                    roleService.findByName("User"), comp);
            user.setLicenses(Set.of(LiscenceType.D, LiscenceType.C, LiscenceType.B));
            userService.save(user);
        }

        for (String name : companyAdmins) {
            Company comp = companies.get(rGenerator.nextInt(companies.size()));
            Employee user = new Employee(name, pass, pass, "new address", BigDecimal.valueOf(123), Set.of(LiscenceType.D, LiscenceType.C, LiscenceType.B),
                    roleService.findByName("Admin"), comp);
            userService.save(user);
        }
    }


    public void runTest() {
        this.companySeed();
        this.roleSeed();
        this.userSeed();
        this.vehicleSeed();
        this.clientSeed();
        this.tripSeed();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        companySeed();
        roleSeed();
        userSeed();
        vehicleSeed();
        clientSeed();
        tripSeed();
    }
}