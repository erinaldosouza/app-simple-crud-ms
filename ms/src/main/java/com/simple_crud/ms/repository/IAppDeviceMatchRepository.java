package com.simple_crud.ms.repository;

import com.simple_crud.ms.model.AppDevice;
import org.springframework.data.aerospike.repository.AerospikeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAppDeviceMatchRepository extends AerospikeRepository<AppDevice, String> {

    List<AppDevice> findAllByOsName(String osName);

}
