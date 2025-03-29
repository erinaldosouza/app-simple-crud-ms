package com.simple_crud.ms.services;


import com.simple_crud.ms.services.models.AppDevice;

import java.util.List;

public interface IAppAppDeviceMatchingServiceCrud extends IAppGenericCrudInterface<AppDevice, Long> {
    AppDevice crate(String userAgent);
    AppDevice parseDevice(String userAgent);
    List<AppDevice> findByOsName(String osName);
    void deleteByUUID(String UUID);
    AppDevice findByUUID(String UUID);



}
