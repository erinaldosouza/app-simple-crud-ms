package com.simple_crud.ms.controller;

import com.simple_crud.ms.dto.AppDeviceDTO;
import com.simple_crud.ms.model.AppDevice;
import com.simple_crud.ms.service.IAppDeviceMatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("device-match")
public class AppDeviceMatchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppDeviceMatchController.class);

    private final IAppDeviceMatchService service;

    public AppDeviceMatchController(IAppDeviceMatchService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> doPost(
            @RequestHeader(value = "User-Agent") String userAgent) {

        LOGGER.atInfo().log("[APP_CONTROLLER] Start matching device. User-Agent: {}", userAgent);

        var device = service.save(userAgent);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(device.getId())
                .toUri();

        LOGGER.atInfo().log("[APP_CONTROLLER] Successfully matched device. User-Agent: {}", userAgent);

        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<AppDeviceDTO> doGetById(@PathVariable("id") String id) {
        LOGGER.atInfo().log("[APP_CONTROLLER] Start retrieving device by Id: {}", id);

        var device = service.findById(id);

        LOGGER.atInfo().log("[APP_CONTROLLER] Successfully retrieved device by Id: {}", id);

        return ResponseEntity.ok().body(device.toDTO());
    }

    @GetMapping
    public ResponseEntity<List<AppDeviceDTO>> doGetByOsName(@RequestParam(value = "osName", required = false) String osName) {
        LOGGER.atInfo().log("[APP_CONTROLLER] Start retrieving devices by OS Name: {}", osName);

        var devices = service.findAllByOsName(osName).stream().map(AppDevice::toDTO).toList();

        if (devices.isEmpty()) {
            LOGGER.atWarn().log("[APP_CONTROLLER] No devices found for OS Name: {}", osName);
        } else {
            LOGGER.atInfo().log("[APP_CONTROLLER] Successfully retrieved devices by OS Name: {}", osName);
        }

        return ResponseEntity.ok().body(devices);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<AppDeviceDTO> doDeleteById(@PathVariable("id") String id) {
        LOGGER.atInfo().log("[APP_CONTROLLER] Start deleting device by Id: {}", id);

        service.deleteById(id);

        LOGGER.atInfo().log("[APP_CONTROLLER] SuccessfulLy deleted device by Id: {}", id);

        return ResponseEntity.noContent().build();
    }

}
