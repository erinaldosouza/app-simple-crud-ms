package com.simple_crud.ms.controllers;

import com.simple_crud.ms.controllers.dtos.AppDeviceDTO;
import com.simple_crud.ms.services.IAppAppDeviceMatchingServiceCrud;
import com.simple_crud.ms.services.models.AppDevice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("device-matching")
public class AppDeviceMatchingController {

    private final IAppAppDeviceMatchingServiceCrud service;

    public AppDeviceMatchingController(IAppAppDeviceMatchingServiceCrud service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Void> doPost(
            @RequestHeader(value = "User-Agent") String userAgent) {

        service.crate(userAgent);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<AppDeviceDTO> doGetById(@PathVariable("id") Long id) {
        var device = service.findById(id);
        return ResponseEntity.ok().body(device.toDTO());
    }

    @GetMapping
    public ResponseEntity<List<AppDeviceDTO>> doGetByOsName(@RequestParam(value = "osName", required = false) String osName) {
        var devices = service.findByOsName(osName).stream().map(AppDevice::toDTO).toList();
        return ResponseEntity.ok().body(devices);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<AppDeviceDTO> doDeleteById(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
