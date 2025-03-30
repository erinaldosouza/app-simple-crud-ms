package com.simple_crud.ms.model;

import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.client.query.IndexType;
import com.simple_crud.ms.dto.AppDeviceDTO;
import lombok.*;
import org.springframework.data.aerospike.annotation.Indexed;
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
@Document(collection = "app_devices")
public class AppDevice {

    @Version
    private Long version;

    @Id
    @Field("device_id")
    private String id;

    @Field("hit_count")
    private Integer hitCount;

    @Field("os_name")
    @Indexed(name = "osName_idx", type = IndexType.STRING, collectionType = IndexCollectionType.DEFAULT)
    private String osName;

    @Field("os_version")
    private String osVersion;

    @Field("browser_name")
    private String browserName;

    @Field("browser_version")
    private String browserVersion;

    @Field("fist_match_ltd")
    private LocalDateTime firstMatchLdt;

    @Field("last_math_ltd")
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
