package com.simple_crud.ms.repositories;

import com.simple_crud.ms.exceptions.AppIllegalUserAgentException;
import com.simple_crud.ms.services.models.AppDevice;
import org.springframework.data.aerospike.repository.AerospikeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAppDeviceMatchingRepository extends AerospikeRepository<AppDevice, Long> {
    List<AppDevice> findAllByOsName(String osNAme);
    void deleteByUUID(String UUID);
    AppDevice findByUUID(String UUID) throws AppIllegalUserAgentException;

}
