package com.jws.transcomp.api.controller;

import com.jws.transcomp.api.models.dto.RoleInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/utility")
public class UtilityController extends BaseController {

    @GetMapping("/roles")
    public ResponseEntity<Object> getRoles() {
        List<RoleInfoDto> roleInfo = new ArrayList<>();

        this.roleService.getAll().forEach(r -> roleInfo.add(new RoleInfoDto(r.getId(), r.getName())));

        return ResponseEntity.ok(roleInfo);
    }

    @GetMapping("/loggedrole")
    public ResponseEntity<Object> getLoggedUserRole() {
        return ResponseEntity.ok(super.getLoggedUser().getRole());
    }

}
