package com.simple_crud.ms.service;


import com.simple_crud.ms.model.AppDevice;

import java.util.List;

public interface IAppAppDeviceMatchService extends IAppGenericInterface<AppDevice, String> {
    AppDevice create(String userAgent);
    AppDevice parseDevice(String userAgent);
    List<AppDevice> findByOsName(String osName);

}
