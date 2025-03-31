package com.simple_crud.ms.service.impl;

import com.simple_crud.ms.exception.AppIllegalUserAgentException;
import com.simple_crud.ms.model.AppDevice;
import com.simple_crud.ms.repository.IAppDeviceMatchRepository;
import com.simple_crud.ms.service.IAppDeviceMatchService;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua_parser.Parser;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppDeviceMatchServiceImpl implements IAppDeviceMatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppDeviceMatchServiceImpl.class);

    public static final String ERROR_TITLE = "An error occurred";
    public static final String EMPTY_USER_AGENT_MSG = "User-Agent is invalid, null or empty";
    private static final int HIT_COUNT_INCREMENT = 1;
private static final List<String> VALID_AGENT = List.of("Safari", "Chrome", "Firefox", "Edg", "Postman", "Insomnia");
    private static final Parser UA_PARSER = new Parser();

    private final IAppDeviceMatchRepository repository;

   /*
   TODO - How to deal with race conditions when incrementing the hit count or event when creating new device profile?
   Not sure about how making it synchronized (using synchronized key word or using ReentrantLook class, for instance) would degrade performance
   Using OracleDB, one alternative to solve this kind of issue is using "Select for update feature"
    */
    @Override
    public AppDevice save(final AppDevice appDevice) {
        LOGGER.atInfo().log("[APP_SERVICE] Start creating device: {}", appDevice);

        var id = appDevice.getId();
        LOGGER.atInfo().log("[APP_SERVICE] Looking for device match id: {}", id);

        var dbDevice = repository.findById(id);

        dbDevice.ifPresentOrElse(deviceFound -> {
            LOGGER.atInfo().log("[APP_SERVICE] Device id: {} found. Incrementing Hit Count from: {} to: {}", id, deviceFound.getHitCount(), deviceFound.getHitCount() + HIT_COUNT_INCREMENT);
            deviceFound.setHitCount(deviceFound.getHitCount() + HIT_COUNT_INCREMENT);
            deviceFound.setLastMatchLdt(LocalDateTime.now());
            repository.save(deviceFound);
        },
        () -> {
            LOGGER.atInfo().log("[APP_SERVICE] No matching found for device id: {}. Creating new device.", id);

            var ldt = LocalDateTime.now();
            appDevice.setId(id);
            appDevice.setHitCount(HIT_COUNT_INCREMENT);
            appDevice.setFirstMatchLdt(ldt);
            appDevice.setLastMatchLdt(ldt);
            repository.save(appDevice);
        });

        LOGGER.atInfo().log("[APP_SERVICE] Successfully executed device operation. Device: {}", appDevice);

        return  dbDevice.orElse(appDevice);
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
    public AppDevice save(String userAgent) {
        LOGGER.atInfo().log("[APP_SERVICE] Start creating device from User-Agent: {}", userAgent);

        var appDevice = this.parseDevice(userAgent);
        appDevice =  this.save(appDevice);

        LOGGER.atInfo().log("[APP_SERVICE] Successfully created device from User-Agent: {}", userAgent);

        return appDevice;
    }

    @Override
    public AppDevice parseDevice(String userAgent) {
        LOGGER.atInfo().log("[APP_SERVICE] Start parsing device from User-Agent: {}", userAgent);

        validateUserAgent(userAgent);

        var client = UA_PARSER.parse(userAgent);

        var appDevice = AppDevice.builder()
                .osName(client.os.family)
                .osVersion(getVersion(client.os.major, client.os.minor,  client.os.patch))
                .browserName(client.userAgent.family)
                .browserVersion(getVersion(client.userAgent.major, client.userAgent.minor, client.userAgent.patch))
                .build();

        appDevice.generateUUID();

        LOGGER.atInfo().log("[APP_SERVICE] Successfully parsed device from User-Agent: {}", userAgent);

        return appDevice;
    }

    @Override
    public List<AppDevice> findAllByOsName(String osName) {
        LOGGER.atInfo().log("[APP_SERVICE] Start retrieving devices by OS Name: {}", osName);

        var devices = repository.findAllByOsName(osName);

        LOGGER.atInfo().log("[APP_SERVICE] Successfully retried devices by OS Name: {}", osName);

        return  devices;
    }

    private void validateUserAgent(String userAgent) throws AppIllegalUserAgentException {
        LOGGER.atError().log("[APP_SERVICE] Start validating User-Agent: {}", userAgent);

        if(StringUtil.isNullOrEmpty(userAgent)) {
            LOGGER.atError().log("[APP_SERVICE] Failed to validate. User-Agent is empty or null");
            throw new AppIllegalUserAgentException(ERROR_TITLE, EMPTY_USER_AGENT_MSG);
        }

        VALID_AGENT.stream()
                .filter(validUa -> userAgent.toLowerCase().contains(validUa.toLowerCase()))
                .findAny().orElseThrow(() -> {
                    LOGGER.atError().log("[APP_SERVICE] Failed to validate. User-Agent: '{}' is invalid", userAgent);
                    return new AppIllegalUserAgentException(ERROR_TITLE, EMPTY_USER_AGENT_MSG);
                });
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
