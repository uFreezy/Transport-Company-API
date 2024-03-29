package com.jws.transcomp.api.controller;

import com.jws.transcomp.api.models.Client;
import com.jws.transcomp.api.models.Employee;
import com.jws.transcomp.api.models.Trip;
import com.jws.transcomp.api.models.dto.trip.CreateTripDto;
import com.jws.transcomp.api.models.dto.trip.EditTripDto;
import com.jws.transcomp.api.models.dto.trip.TripDto;
import com.jws.transcomp.api.models.responses.PaginatedResponse;
import com.jws.transcomp.api.util.PdfUtil;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/trips")
public class TripController extends BaseController {

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTrip(@PathVariable Long id) {
        Trip trip = this.tripService.findById(id);
        TripDto tripDto = modelMapper.map(trip, TripDto.class);

        if (!trip.getCompany().getId().equals(getLoggedCompany().getId())) {
            return ResponseEntity.badRequest().body("Trip with id " + id + " doesn't belong to your company.");
        }

        return ResponseEntity.ok(tripDto);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getTrips(
            @RequestParam(value = "destination", required = false) String destination,
            @RequestParam(value = "sort_by", required = false) String sortBy,
            Pageable pageable) {

        Employee loggedUser = getLoggedUser();

        if (loggedUser.getRole().getName().equals("Admin") && loggedUser.getCompany() != null) {
            // Workaround
            PaginatedResponse response = this.tripService.filterTrips(loggedUser.getCompany().getId(), destination, sortBy, pageable);
            response.setItemList(modelMapper.map(response.getItemList(), new TypeToken<List<TripDto>>() {
            }.getType()));

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong with the trip search.");
    }

    @GetMapping("/report")
    public ResponseEntity<Object> getTripsPdf() {
        try {
            List<Trip> companyTrips = new ArrayList<>();

            int page = 0;
            int maxPage;
            do {
                PaginatedResponse trips = this.tripService
                        .filterTrips(getLoggedCompany().getId(), null, null, PageRequest.of(page, 100));
                companyTrips.addAll((List<Trip>) trips.getItemList());

                maxPage = trips.getNumberOfPages();
                page += 1;
            } while (page <= maxPage);

            byte[] nom = PdfUtil.generatePdf(companyTrips);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = "report.pdf";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(nom, headers, HttpStatus.OK);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> createTrip(@Validated @RequestBody CreateTripDto tripDto) {
        tripDto.setDriver(this.userService.findById(tripDto.getDriverId()));
        tripDto.setVehicle(this.vehicleService.findById(tripDto.getVehicleId()));
        tripDto.setCompany(getLoggedCompany());

        Trip trip = this.tripService.save(tripDto.mapToEntity());

        return ResponseEntity.created(getLocation(trip.getId())).body("Trip registered successfully!");
    }

    @PutMapping
    public ResponseEntity<Object> editTrip(@Validated @RequestBody EditTripDto tripDto) {
        Trip trip = this.tripService.findById(tripDto.getId());
        if (!trip.getCompany().equals(getLoggedCompany())) {
            return ResponseEntity.badRequest().body("This trip doesn't belong to your company.");
        }
        if (trip.getDeparture().before(new Date())) {
            return ResponseEntity.badRequest().body("Cannot edit a trip that has already begun.");
        }

        tripDto.mapToEntity(trip);
        this.tripService.save(trip);

        return ResponseEntity.ok("Successfully edited trip.");
    }

    @PutMapping("/pay")
    public ResponseEntity<Object> registerPayment(@RequestParam(name = "trip_id") Long tripId, @RequestParam(name = "user_id") Long userId) {
        Trip trip;

        trip = this.tripService.findById(tripId);

        if (!trip.getCompany().equals(getLoggedCompany())) {
            return ResponseEntity.badRequest().body("This trip doesn't belong to your company.");
        }
        if (trip.getClients().stream().noneMatch(t -> Objects.equals(t.getId(), userId))) {
            return ResponseEntity.badRequest().body(String.format("There is no user with id: %s in this trip", userId));
        }

        Client client = this.clientService.findById(userId);

        // Check if trip is already paid
        if (trip.getPaidClients().stream().anyMatch(client1 -> client1.getId().equals(client.getId()))) {
            return ResponseEntity.badRequest().body("This user has already paid for the trip");
        }

        trip.registerPayment(client);

        this.tripService.save(trip);

        return ResponseEntity.ok("Payment for user registered successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTrip(@PathVariable Long id) {
        Trip trip = this.tripService.findById(id);
        if (!trip.getCompany().equals(getLoggedCompany())) {
            return ResponseEntity.badRequest().body("This trip doesn't belong to your company.");
        }
        this.tripService.delete(trip);

        return ResponseEntity.ok("Trip deleted successfully!");
    }
}
