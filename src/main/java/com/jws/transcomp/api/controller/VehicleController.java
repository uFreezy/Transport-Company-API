package com.jws.transcomp.api.controller;

import com.jws.transcomp.api.models.Vehicle;
import com.jws.transcomp.api.models.dto.vehicle.CreateVehicleDto;
import com.jws.transcomp.api.models.dto.vehicle.EditVehicleDto;
import com.jws.transcomp.api.models.dto.vehicle.VehicleDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController extends BaseController {
    @GetMapping("/{id}")
    public ResponseEntity<Object> getVehicle(@PathVariable Long id) {
        Vehicle vh = vehicleService.findById(id);
        VehicleDto vehicleDto = this.modelMapper.map(vh, VehicleDto.class);

        if (vh.getCompany().equals(getLoggedCompany())) {
            return ResponseEntity.ok(vehicleDto);
        } else {
            return ResponseEntity.badRequest().body("This vehicle is not owned by your company! ");
        }
    }

    @GetMapping(value = "all")
    public ResponseEntity<Object> getCompanyVehicles() {
        List<Vehicle> vehicles = vehicleService.getByCompany(getLoggedCompany());
        List<VehicleDto> vehicleDtos = new ArrayList<>();

        vehicles.forEach(vehicle -> vehicleDtos.add(modelMapper.map(vehicle, VehicleDto.class)));

        return ResponseEntity.ok(vehicleDtos);
    }

    @PostMapping
    public ResponseEntity<Object> createVehicle(@Valid @RequestBody CreateVehicleDto vehicleDto) {
        Vehicle vehicle;
        vehicle = this.modelMapper.map(vehicleDto, Vehicle.class);
        vehicle.setCompany(getLoggedCompany());

        Vehicle vh = this.vehicleService.save(vehicle);

        return ResponseEntity.created(getLocation(vh.getId())).body("Vehicle created successfully!");
    }

    @PutMapping
    public ResponseEntity<Object> editVehicle(@RequestBody EditVehicleDto vehicleDto) {
        Vehicle vehicle = this.vehicleService.findById(vehicleDto.getId());
        if (vehicle.getCompany().equals(getLoggedCompany())) {
            vehicleDto.mapToEntity(vehicle);

            this.vehicleService.save(vehicle);
            return ResponseEntity.ok("Vehicle edited successfully!");
        } else {
            return ResponseEntity.badRequest().body("This vehicle is not owned by your company.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteVehicle(@PathVariable Long id) {
        Vehicle vh = this.vehicleService.findById(id);

        if (vh.getCompany().equals(getLoggedCompany())) {
            this.vehicleService.delete(vh.getId());
            return ResponseEntity.ok("Vehicle deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body("This vehicle isn't owned by your company!");
        }
    }
}
