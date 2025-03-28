package com.simple_crud.ms.services.models;

import com.aerospike.client.query.IndexType;
import com.simple_crud.ms.controllers.dtos.AppDeviceDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Document
public class AppDevice {

    @Id
    private Long id;
    private Integer hitCount;
    private String osName;
    private String osVersion;
    private String browserName;
    private String browserVersion;

    public AppDeviceDTO toDTO() {
        return null;
    }

}
