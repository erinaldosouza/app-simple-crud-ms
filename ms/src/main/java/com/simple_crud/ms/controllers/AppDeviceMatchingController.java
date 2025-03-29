package com.simple_crud.ms.controllers;

import com.simple_crud.ms.controllers.dtos.AppDeviceDTO;
import com.simple_crud.ms.services.IAppAppDeviceMatchServiceCrud;
import com.simple_crud.ms.services.models.AppDevice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("device-match")
public class AppDeviceMatchingController {

    private final IAppAppDeviceMatchServiceCrud service;

    public AppDeviceMatchingController(IAppAppDeviceMatchServiceCrud service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> doPost(
            @RequestHeader(value = "User-Agent") String userAgent) {

        service.create(userAgent);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<AppDeviceDTO> doGetById(@PathVariable("id") String id) {
        var device = service.findById(id);
        return ResponseEntity.ok().body(device.toDTO());
    }

    @GetMapping
    public ResponseEntity<List<AppDeviceDTO>> doGetByOsName(@RequestParam(value = "osName", required = false) String osName) {
        var devices = service.findByOsName(osName).stream().map(AppDevice::toDTO).toList();
        return ResponseEntity.ok().body(devices);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<AppDeviceDTO> doDeleteById(@PathVariable("id") String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
