package com.simple_crud.ms.services.models;

import com.aerospike.client.query.IndexType;
import com.simple_crud.ms.controllers.dtos.AppDeviceDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class AppDevice {

    @Id
    private Long id;
    private String UUID;
    private Integer hitCount;
    private String osName;
    private String osVersion;
    private String browserName;
    private String browserVersion;

    public AppDeviceDTO toDTO() {
        return AppDeviceDTO.builder()
                .UUID(UUID)
                .hitCount(hitCount)
                .osName(osName)
                .osVersion(osVersion)
                .browserName(browserName)
                .browserVersion(browserVersion)
                .build();
    }

}
