package com.simple_crud.ms.services.impl;

import com.simple_crud.ms.exceptions.AppIllegalUserAgentException;
import com.simple_crud.ms.repositories.IAppDeviceMatchingRepository;
import com.simple_crud.ms.services.IAppAppDeviceMatchServiceCrud;
import com.simple_crud.ms.services.models.AppDevice;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;
import ua_parser.Parser;

import java.util.List;
import java.util.Optional;

@Service
public class AppDeviceMatchServiceImpl implements IAppAppDeviceMatchServiceCrud {

    public static final String ERROR_TITLE = "An error occurred";
    public static final String EMPTY_USER_AGENT_MSG = "User-Agent cannot be null nor empty";
    private static final int HITCOUNT_INCREMENT = 1;

    private final IAppDeviceMatchingRepository repository;

    public AppDeviceMatchServiceImpl(IAppDeviceMatchingRepository iAppDeviceMatchingRepository) {
        this.repository = iAppDeviceMatchingRepository;
    }

    @Override
    public AppDevice create(AppDevice appDevice) {

        var deviceFound = repository.findByOsNameAndOsVersionAndBrowserNameAndBrowserVersion(
                appDevice.getOsName(), appDevice.getOsVersion(), appDevice.getBrowserName(), appDevice.getBrowserVersion()
        );

        deviceFound.ifPresent(device -> {
            device.setHitCount(appDevice.getHitCount() + HITCOUNT_INCREMENT);
        });

        if(deviceFound.isEmpty()) {
            deviceFound = Optional.of(repository.save(appDevice));
        }

        return deviceFound.orElseThrow();
    }

    @Override
    public AppDevice findById(String id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public AppDevice create(String userAgent) {
        var appDevice = this.parseDevice(userAgent);
        return this.create(appDevice);
    }

    @Override
    public AppDevice parseDevice(String userAgent) {

        if(StringUtil.isNullOrEmpty(userAgent)) {
            throw new AppIllegalUserAgentException(ERROR_TITLE, EMPTY_USER_AGENT_MSG);
        }

        var client = new Parser().parse(userAgent);
        var appDevice = new AppDevice();

        appDevice.setOsName(client.os.family);
        appDevice.setOsVersion(getVersion(client.os.major, client.os.minor,  client.os.patch));
        appDevice.setBrowserName(client.userAgent.family);
        appDevice.setBrowserVersion(getVersion(client.userAgent.major, client.userAgent.minor, client.userAgent.patch));

        return appDevice;
    }

    @Override
    public List<AppDevice> findByOsName(String osName) {
        return  repository.findAllByOsName(osName);
    }

    private String getVersion(String major, String minor, String patch) {
        StringBuilder version = new StringBuilder();
        if (!StringUtil.isNullOrEmpty(major)) {
            version.append(major);
            if(!StringUtil.isNullOrEmpty(minor)) {
                version.append(".").append(minor);
                if (!StringUtil.isNullOrEmpty(patch)) {
                    version.append(".").append(patch);
                }
            }
        }

        return version.toString();
    }

}
