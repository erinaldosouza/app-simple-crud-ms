package com.simple_crud.ms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "x-aerospike")
public class AerospikeConfigurationProperties {
    private String host;
    private int port;
    private String namespace;

}