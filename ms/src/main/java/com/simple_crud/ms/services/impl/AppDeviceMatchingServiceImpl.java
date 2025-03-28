package com.simple_crud.ms.services.impl;

import com.simple_crud.ms.exceptions.AppIllegalUserAgentException;
import com.simple_crud.ms.repositories.IAppDeviceMatchingRepository;
import com.simple_crud.ms.services.IAppAppDeviceMatchingServiceCrud;
import com.simple_crud.ms.services.models.AppDevice;
import org.springframework.stereotype.Service;
import ua_parser.Parser;

import java.util.List;

@Service
public class AppDeviceMatchingServiceImpl implements IAppAppDeviceMatchingServiceCrud {

    private final IAppDeviceMatchingRepository repository;

    public AppDeviceMatchingServiceImpl(IAppDeviceMatchingRepository iAppDeviceMatchingRepository) {
        this.repository = iAppDeviceMatchingRepository;
    }

    @Override
    public void crate(AppDevice appDevice) {
        repository.save(appDevice);
    }

    @Override
    public AppDevice findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void crate(String userAgent) {
        var appDevice = this.parseDevice(userAgent);
        this.crate(appDevice);
    }

    @Override
    public AppDevice parseDevice(String userAgent) throws AppIllegalUserAgentException {

        var client = new Parser().parse(userAgent);
        var appDevice = new AppDevice();

        appDevice.setOsName(client.os.family);
        appDevice.setOsVersion(client.os.major + "." + client.os.minor + "." + client.os.patch);
        appDevice.setBrowserName(client.userAgent.family);
        appDevice.setBrowserVersion(client.userAgent.major + "." + client.userAgent.minor + "." + client.userAgent.patch);

        return appDevice;
    }

    @Override
    public List<AppDevice> findByOsName(String osName) {
        return  repository.findAllByOsName(osName);
    }

}
