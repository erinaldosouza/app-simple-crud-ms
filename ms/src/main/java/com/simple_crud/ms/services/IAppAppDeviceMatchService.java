package com.simple_crud.ms.services;


import com.simple_crud.ms.services.models.AppDevice;

import java.util.List;

public interface IAppAppDeviceMatchService extends IAppGenericInterface<AppDevice, String> {
    AppDevice create(String userAgent);
    AppDevice parseDevice(String userAgent);
    List<AppDevice> findByOsName(String osName);

}
