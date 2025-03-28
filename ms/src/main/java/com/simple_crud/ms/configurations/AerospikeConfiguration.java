package com.simple_crud.ms.configurations;

import com.aerospike.client.Host;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.config.AbstractAerospikeDataConfiguration;
import org.springframework.data.aerospike.repository.config.EnableAerospikeRepositories;

import java.util.Collection;
import java.util.Collections;

@Configuration
@EnableAerospikeRepositories(basePackages = "com.simple_crud.ms.services.models")
public class AerospikeConfiguration extends AbstractAerospikeDataConfiguration {

    @Value("${x_aerospike.host}")
    private String dbHost;

    @Value("${x_aerospike.port}")
    private Integer dbPort;

    @Value("${x_aerospike.namespace}")
    private String namespace;

    @Override
    protected Collection<Host> getHosts() {
        return Collections.singleton(new Host(dbHost, dbPort));
    }

    @Override
    protected String nameSpace() {
        return namespace;
    }
}