package com.simple_crud.ms.services.impl;

import com.simple_crud.ms.exceptions.AppIllegalUserAgentException;
import com.simple_crud.ms.repositories.IAppDeviceMatchingRepository;
import com.simple_crud.ms.services.IAppAppDeviceMatchingServiceCrud;
import com.simple_crud.ms.services.models.AppDevice;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;
import ua_parser.Parser;

import java.util.List;

@Service
public class AppDeviceMatchingServiceImpl implements IAppAppDeviceMatchingServiceCrud {

    public static final String ERROR_TITLE = "An error occurred";
    public static final String EMPTY_USER_AGENT_MSG = "User-Agent cannot be null nor empty";

    private final IAppDeviceMatchingRepository repository;

    public AppDeviceMatchingServiceImpl(IAppDeviceMatchingRepository iAppDeviceMatchingRepository) {
        this.repository = iAppDeviceMatchingRepository;
    }

    @Override
    public AppDevice crate(AppDevice appDevice) {
        return repository.save(appDevice);
    }

    @Override
    public AppDevice findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public AppDevice crate(String userAgent) {
        var appDevice = this.parseDevice(userAgent);
        return this.crate(appDevice);
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

    @Override
    public void deleteByUUID(String UUID) {
        repository.deleteByUUID(UUID);
    }

    @Override
    public AppDevice findByUUID(String UUID) {
        return repository.findByUUID(UUID).orElseThrow();
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
