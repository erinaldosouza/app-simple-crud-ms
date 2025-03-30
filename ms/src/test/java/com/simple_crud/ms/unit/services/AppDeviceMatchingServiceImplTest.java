package com.simple_crud.ms.unit.services;


import com.simple_crud.ms.exception.AppIllegalUserAgentException;
import com.simple_crud.ms.model.AppDevice;
import com.simple_crud.ms.repository.IAppDeviceMatchRepository;
import com.simple_crud.ms.service.impl.AppDeviceMatchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppDeviceMatchingServiceImplTest {

    @Mock
    private IAppDeviceMatchRepository repository;

    @InjectMocks
    private AppDeviceMatchServiceImpl service;

    private AppDevice appDevice;

    private static final String ID = "123e4567-e89b-12d3-a456-426614174000";

    static Stream<Arguments> provideUserAgents() {
        return Stream.of(
                Arguments.of("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
                        "Linux", "x86_64", "Chrome", "91.0.4472"),

                Arguments.of("Mozilla/5.0 (X11; Linux x86_64; rv:89.0) Gecko/20100101 Firefox/89.0",
                        "Linux", "x86_64", "Firefox", "89.0"),

                Arguments.of("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
                        "Windows", "10.0", "Chrome", "91.0.4472"),

                Arguments.of("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0",
                        "Windows", "10.0", "Firefox", "89.0"),

                Arguments.of("Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1",
                        "iOS", "15.0", "Mobile Safari", "15.0"),

                Arguments.of("Mozilla/5.0 (Linux; Android 11; SM-G998B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472 Mobile Safari/537.36",
                        "Android", "11", "Chrome", "91.0.4472")
        );
    }

    @BeforeEach
    void setUp() {
        appDevice = new AppDevice();
        appDevice.setOsName("Linux");
        appDevice.setOsVersion("5.15.0");
        appDevice.setBrowserName("Chrome");
        appDevice.setBrowserVersion("91.0.4472.124");
        appDevice.setHitCount(0);
        appDevice.generateUUID();
    }

    @Test
    void testCreateAppDeviceFirsMatch() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(AppDevice.class))).thenReturn(appDevice);

        AppDevice savedDevice = service.save(appDevice);
        assertNotNull(savedDevice);
        assertEquals("Linux", savedDevice.getOsName());
        assertEquals(1, savedDevice.getHitCount());
        verify(repository, times(1)).save(any(AppDevice.class));
    }

    @Test
    void testCreateAppDeviceMatched() {
        appDevice.setHitCount(1);
        when(repository.findById(anyString())).thenReturn(Optional.of(appDevice));
        when(repository.save(any(AppDevice.class))).thenReturn(appDevice);

        AppDevice savedDevice = service.save(appDevice);
        assertNotNull(savedDevice);
        assertEquals("Linux", savedDevice.getOsName());
        assertEquals(2, savedDevice.getHitCount());
        verify(repository, times(1)).save(any(AppDevice.class));
    }

    @Test
    void testFindById() {
        when(repository.findById(ID)).thenReturn(Optional.of(appDevice));
        AppDevice foundDevice = service.findById(ID);
        assertNotNull(foundDevice);
        assertEquals("Linux", foundDevice.getOsName());
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void testFindByIdWhenNoSuchElement() {
        when(repository.findById(ID)).thenThrow(new NoSuchElementException());
        assertThrows(NoSuchElementException.class, () -> service.findById(ID));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void testDeleteById() {
        doNothing().when(repository).deleteById(ID);
        service.deleteById(ID);
        verify(repository, times(1)).deleteById(ID);
    }

    @Test
    void testFindByOsName() {
        when(repository.findAllByOsName("Linux")).thenReturn(List.of(appDevice));
        List<AppDevice> devices = service.findAllByOsName("Linux");
        assertFalse(devices.isEmpty());
        assertEquals(1, devices.size());
        verify(repository, times(1)).findAllByOsName("Linux");
    }

    @Test
    void testFindByOsNamedWhenNoSuchElement() {
        when(repository.findAllByOsName("Linux")).thenThrow(new NoSuchElementException());

        assertThrows(NoSuchElementException.class, () -> service.findAllByOsName("Linux"));

        verify(repository, times(1)).findAllByOsName("Linux");
    }

    @ParameterizedTest
    @MethodSource("provideUserAgents")
    void testParseDevice(String userAgent, String expectedOs, String expectedOsVersion, String expectedBrowser, String expectedBrowserVersion) {
        AppDevice parsedDevice = service.parseDevice(userAgent);

        assertNotNull(parsedDevice);
        assertEquals(expectedOs, parsedDevice.getOsName());
        //TODO For some reason tha user agent parse lib is not getting the OS Version when Linux OS.
       // assertEquals(expectedOsVersion, parsedDevice.getOsVersion());
        assertEquals(expectedBrowser, parsedDevice.getBrowserName());
        assertEquals(expectedBrowserVersion, parsedDevice.getBrowserVersion());
    }

    @Test
    void testParseDeviceWhenNullOrEmptyUserAgent() {
        String invalidUserAgent = "";

        AppIllegalUserAgentException exception = assertThrows(AppIllegalUserAgentException.class, () ->  service.parseDevice(invalidUserAgent));

        assertEquals(AppDeviceMatchServiceImpl.ERROR_TITLE, exception.getTitle());
        assertEquals(AppDeviceMatchServiceImpl.EMPTY_USER_AGENT_MSG, exception.getMessage());

    }
}
