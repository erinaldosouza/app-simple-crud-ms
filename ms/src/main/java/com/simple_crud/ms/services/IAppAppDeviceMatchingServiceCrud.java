package com.simple_crud.ms.services;


import com.simple_crud.ms.exceptions.AppIllegalUserAgentException;
import com.simple_crud.ms.services.models.AppDevice;

import java.util.List;

public interface IAppAppDeviceMatchingServiceCrud extends IAppGenericCrudInterface<AppDevice, Long> {
    void crate(String userAgent);
    AppDevice parseDevice(String userAgent) throws AppIllegalUserAgentException;
    List<AppDevice> findByOsName(String osName);
    void deleteByUUID(String UUID);
    AppDevice findByUUID(String UUID) throws AppIllegalUserAgentException;



}
