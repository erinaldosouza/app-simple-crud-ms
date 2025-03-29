package com.simple_crud.ms.repositories;

import com.simple_crud.ms.services.models.AppDevice;
import org.springframework.data.aerospike.repository.AerospikeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAppDeviceMatchingRepository extends AerospikeRepository<AppDevice, String> {

    List<AppDevice> findAllByOsName(String osName);

    Optional<AppDevice> findByOsNameAndOsVersionAndBrowserNameAndBrowserVersion(
            String osName, String osVersion, String browserName, String browserVersion
    );
}
