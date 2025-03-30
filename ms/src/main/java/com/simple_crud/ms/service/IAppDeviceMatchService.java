package com.simple_crud.ms.service;


import com.simple_crud.ms.model.AppDevice;

import java.util.List;

public interface IAppDeviceMatchService extends IAppGenericInterface<AppDevice, String> {
    AppDevice save(String userAgent);
    AppDevice parseDevice(String userAgent);
    List<AppDevice> findAllByOsName(String osName);

}
