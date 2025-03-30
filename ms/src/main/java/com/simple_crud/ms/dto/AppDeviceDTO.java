package com.simple_crud.ms.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppDeviceDTO {
    private String id;
    private Integer hitCount;
    private String osName;
    private String osVersion;
    private String browserName;
    private String browserVersion;

}
