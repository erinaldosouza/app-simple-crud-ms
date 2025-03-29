package com.simple_crud.ms.services.models;

import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.client.query.IndexType;
import com.simple_crud.ms.controllers.dtos.AppDeviceDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.NonFinal;
import org.springframework.data.aerospike.annotation.Indexed;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.aerospike.mapping.Field;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class AppDevice {

    @Id
    private String id;

    @Field
    @NonFinal
    private Integer hitCount;

    @Indexed(type = IndexType.STRING, collectionType = IndexCollectionType.DEFAULT)
    private String osName;

    @Indexed(type = IndexType.STRING, collectionType = IndexCollectionType.DEFAULT)
    private String osVersion;

    @Indexed( type = IndexType.STRING, collectionType = IndexCollectionType.DEFAULT)
    private String browserName;

    @Indexed(type = IndexType.STRING, collectionType = IndexCollectionType.DEFAULT)
    private String browserVersion;

    @Field
    private LocalDateTime firstMatchDateTime;

    @Field
    @NonFinal
    private LocalDateTime lastMatchDateTime;

    public AppDeviceDTO toDTO() {
        return AppDeviceDTO.builder()
                .id(id)
                .hitCount(hitCount)
                .osName(osName)
                .osVersion(osVersion)
                .browserName(browserName)
                .browserVersion(browserVersion)
                .build();
    }

}
