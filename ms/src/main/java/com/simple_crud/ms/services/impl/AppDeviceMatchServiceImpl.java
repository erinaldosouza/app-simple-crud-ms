package com.simple_crud.ms.services.impl;

import com.simple_crud.ms.exceptions.AppIllegalUserAgentException;
import com.simple_crud.ms.repositories.IAppDeviceMatchingRepository;
import com.simple_crud.ms.services.IAppAppDeviceMatchServiceCrud;
import com.simple_crud.ms.services.models.AppDevice;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua_parser.Parser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppDeviceMatchServiceImpl implements IAppAppDeviceMatchServiceCrud {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppDeviceMatchServiceImpl.class);

    public static final String ERROR_TITLE = "An error occurred";
    public static final String EMPTY_USER_AGENT_MSG = "User-Agent cannot be null nor empty";
    private static final int HIT_COUNT_INCREMENT = 1;

    private final IAppDeviceMatchingRepository repository;

    public AppDeviceMatchServiceImpl(IAppDeviceMatchingRepository iAppDeviceMatchingRepository) {
        this.repository = iAppDeviceMatchingRepository;
    }

    @Override
    public AppDevice create(AppDevice appDevice) {
        LOGGER.atInfo().log("[APP_SERVICE] Start creating device: {}", appDevice);

        var id = appDevice.generateUUID();
        LOGGER.atInfo().log("[APP_SERVICE] Looking for device match id: {}", id);


        var deviceFound = repository.findById(id);

        deviceFound.ifPresent(device -> {
            LOGGER.atInfo().log("[APP_SERVICE] Device id: {} found. Incrementing Hit Count from: {} to: {}", id, device.getHitCount(), device.getHitCount() + HIT_COUNT_INCREMENT);
            device.setHitCount(device.getHitCount() + HIT_COUNT_INCREMENT);
            device.setLastMatchLdt(LocalDateTime.now());
        });

        if(deviceFound.isEmpty()) {
            LOGGER.atInfo().log("[APP_SERVICE] No matching found for device id: {}. Creating new device.", id);

            var ldt = LocalDateTime.now();
            appDevice.setId(id);
            appDevice.setHitCount(HIT_COUNT_INCREMENT);
            appDevice.setFirstMatchLdt(ldt);
            appDevice.setLastMatchLdt(ldt);
            deviceFound = Optional.of(repository.save(appDevice));
        }

        appDevice = repository.save(deviceFound.get());

        LOGGER.atInfo().log("[APP_SERVICE] Successfully executed device operation. Device: {}", appDevice);

        return appDevice;
    }

    @Override
    public AppDevice findById(String id) {
        LOGGER.atInfo().log("[APP_SERVICE] Start looking for device id: {}.", id);
        var appDevice = repository.findById(id).orElseThrow();
        LOGGER.atInfo().log("[APP_SERVICE] Successfully found device id: {}.", id);

        return appDevice;
    }

    @Override
    public void deleteById(String id) {
        LOGGER.atInfo().log("[APP_SERVICE] Start deleting device id: {}.", id);

        repository.deleteById(id);

        LOGGER.atInfo().log("[APP_SERVICE] Successfully delete device id: {}.", id);

    }

    @Override
    public AppDevice create(String userAgent) {
        LOGGER.atInfo().log("[APP_SERVICE] Start creating device from User-Agent: {}", userAgent);

        var appDevice = this.parseDevice(userAgent);
        appDevice =  this.create(appDevice);

        LOGGER.atInfo().log("[APP_SERVICE] Successfully created device from User-Agent: {}", userAgent);

        return appDevice;
    }

    @Override
    public AppDevice parseDevice(String userAgent) {
        LOGGER.atInfo().log("[APP_SERVICE] Start parsing device from User-Agent: {}", userAgent);

        if(StringUtil.isNullOrEmpty(userAgent)) {
            LOGGER.atError().log("[APP_SERVICE] Failed parsing device. User-Agent is empty or null");
            throw new AppIllegalUserAgentException(ERROR_TITLE, EMPTY_USER_AGENT_MSG);
        }

        var client = new Parser().parse(userAgent);
        var appDevice = new AppDevice();

        appDevice.setOsName(client.os.family);
        appDevice.setOsVersion(getVersion(client.os.major, client.os.minor,  client.os.patch));
        appDevice.setBrowserName(client.userAgent.family);
        appDevice.setBrowserVersion(getVersion(client.userAgent.major, client.userAgent.minor, client.userAgent.patch));

        LOGGER.atInfo().log("[APP_SERVICE] Successfully parsed device from User-Agent: {}", userAgent);

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
