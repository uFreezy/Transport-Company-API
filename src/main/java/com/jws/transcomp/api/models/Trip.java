package com.jws.transcomp.api.models;

import com.jws.transcomp.api.models.base.TripType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String startingPoint;
    @NotNull
    private String endingPoint;
    @NotNull
    private Date departure;
    @NotNull
    private Date arrival;
    @NotNull
    private BigDecimal basePrice;
    @NotNull
    private BigDecimal totalPrice;
    @NotNull
    @Enumerated(EnumType.STRING)
    private TripType type;
    private short peopleOnboard;
    private int cargoSize;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, optional = false)
    @JoinColumn(name = "employee_id")
    private Employee driver;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, optional = false)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToMany
    private Set<Client> clients = new HashSet<>();
    @ManyToMany
    private Set<Client> paidClients = new HashSet<>();

    public Trip() {
    }

    public Trip(String startingPoint, String endingPoint, Date departure, Date arrival, BigDecimal basePrice, TripType type, int cargoSize, Employee driver, Company comp, Vehicle vehicle, Set<Client> clients) {
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
        this.departure = departure;
        this.arrival = arrival;
        this.clients = clients;
        if (type == TripType.PASSENGER_TRIP)
            this.peopleOnboard = (short) (this.clients.size() + 1);
        else
            this.peopleOnboard = 1;
        this.cargoSize = cargoSize;
        this.vehicle = vehicle;
        this.driver = driver;
        this.basePrice = basePrice;
        this.type = type;
        this.company = comp;

        if (this.type == TripType.PASSENGER_TRIP)
            this.totalPrice = this.basePrice.multiply(BigDecimal.valueOf(this.clients.size()));
        else
            this.totalPrice = this.basePrice.multiply(BigDecimal.valueOf(cargoSize));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }

    public String getEndingPoint() {
        return endingPoint;
    }

    public void setEndingPoint(String endingPoint) {
        this.endingPoint = endingPoint;
    }

    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    public Date getArrival() {
        return arrival;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public TripType getType() {
        return type;
    }

    public void setType(TripType type) {
        this.type = type;
    }

    public short getPeopleOnboard() {
        return peopleOnboard;
    }

    public void setPeopleOnboard(short peopleOnboard) {
        this.peopleOnboard = peopleOnboard;
    }

    public int getCargoSize() {
        return cargoSize;
    }

    public void setCargoSize(int cargoSize) {
        this.cargoSize = cargoSize;
    }

    public Employee getDriver() {
        return driver;
    }

    public void setDriver(Employee driver) {
        this.driver = driver;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public Set<Client> getPaidClients() {
        return paidClients;
    }

    public void setPaidClients(Set<Client> paidClients) {
        this.paidClients = paidClients;
    }

    public void registerPayment(Client client) {
        this.paidClients.add(client);
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", startingPoint='" + startingPoint + '\'' +
                ", endingPoint='" + endingPoint + '\'' +
                ", departure=" + departure +
                ", arrival=" + arrival +
                ", basePrice=" + basePrice +
                ", totalPrice=" + totalPrice +
                ", type=" + type +
                ", peopleOnboard=" + peopleOnboard +
                ", cargoSize=" + cargoSize +
                ", driver=" + driver +
                ", vehicle=" + vehicle +
                ", company=" + company +
                ", passengers=" + clients +
                '}';
    }


}
