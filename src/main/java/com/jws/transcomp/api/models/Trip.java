package com.jws.transcomp.api.models;

import com.jws.transcomp.api.models.base.TripType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
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
    @Min(1)
    private BigDecimal basePrice;
    @NotNull
    @Min(1)
    private BigDecimal totalPrice;
    @NotNull
    @Enumerated(EnumType.STRING)
    private TripType type;
    @Min(1)
    private short peopleOnboard;
    private int cargoSize;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee driver;
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
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
