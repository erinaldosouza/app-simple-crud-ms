package com.simple_crud.ms.model;

import com.simple_crud.ms.dto.AppDeviceDTO;
import lombok.*;
import lombok.experimental.NonFinal;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.aerospike.mapping.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class AppDevice {

    @Version
    private Long version;

    @Id
    private String id;

    @Field
    @NonFinal
    private Integer hitCount;

    @Field
    private String osName;

    @Field
    private String osVersion;

    @Field
    private String browserName;

    @Field
    private String browserVersion;

    @Field
    private LocalDateTime firstMatchLdt;

    @Field
    @NonFinal
    private LocalDateTime lastMatchLdt;

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

    public void generateUUID() {
        String input = String.join(":", new String[] {osName, osVersion, browserName, browserVersion});
        this.id = UUID.nameUUIDFromBytes(input.getBytes()).toString();
    }

}
