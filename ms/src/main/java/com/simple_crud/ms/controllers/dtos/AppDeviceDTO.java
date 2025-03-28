package com.simple_crud.ms.controllers.dtos;

import com.simple_crud.ms.services.models.AppDevice;

public record AppDeviceDTO(
        String id,
        Integer hitCount,
        String osName,
        String osVersion,
        String browserName,
        String browserVersion ) {

    public AppDevice toDomain() {
        return null;
    }
};
