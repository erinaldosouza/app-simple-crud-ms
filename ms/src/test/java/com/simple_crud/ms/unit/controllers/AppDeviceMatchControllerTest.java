package com.simple_crud.ms.unit.controllers;

import com.simple_crud.ms.controller.AppDeviceMatchController;
import com.simple_crud.ms.dto.AppDeviceDTO;
import com.simple_crud.ms.model.AppDevice;
import com.simple_crud.ms.service.impl.AppDeviceMatchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AppDeviceMatchController.class)
class AppDeviceMatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private Logger logger;

    @MockitoBean
    private AppDeviceMatchServiceImpl service;

    private AppDevice mockDevice;
    private AppDeviceDTO mockDeviceDTO;

    @BeforeEach
    void setUp() {

        this.mockDevice = AppDevice.builder()
                .browserName("Goolge Chrome")
                .browserVersion("130.3.5")
                .osName("Linux")
                .osVersion("22")
                .hitCount(1)
                .build();
        mockDevice.generateUUID();

        this.mockDeviceDTO = mockDevice.toDTO();

        when(service.save(anyString())).thenReturn(mockDevice);
        when(service.save(any(AppDevice.class))).thenReturn(mockDevice);
        when(service.findById(anyString())).thenReturn(mockDevice);
        when(service.findAllByOsName("Linux")).thenReturn(List.of(mockDevice));
        when(service.findAllByOsName("iOS")).thenReturn(List.of());
        doNothing().when(service).deleteById(mockDevice.getId());
    }

    @Test
    void testDoPost() throws Exception {
        String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 Linux x86_64 Chrome 91.0.4472";
        mockMvc.perform(MockMvcRequestBuilders.post("/devices")
                        .header("User-Agent", userAgent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"));

        verify(service, times(1)).save(anyString());
    }

    @Test
    void testDoGetById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/devices/{id}", mockDevice.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(mockDevice.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.osName").value(mockDeviceDTO.getOsName()));

        verify(service, times(1)).findById(mockDevice.getId());
    }

    @Test
    void testDoGetByIdNotFound() throws Exception {
        doThrow(NoSuchElementException.class).when(service).findById(anyString());

        mockMvc.perform(MockMvcRequestBuilders.get("/devices/{id}", mockDevice.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        verify(service, times(1)).findById(mockDevice.getId());
    }

    @Test
    void testDoGetByOsNameFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/devices")
                        .param("osName", "Linux")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(mockDevice.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].osName").value(mockDeviceDTO.getOsName()));

        verify(service, times(1)).findAllByOsName(mockDeviceDTO.getOsName());
    }

    @Test
    void testDoGetByOsNameNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/devices")
                        .param("osName", "iOS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(service, times(1)).findAllByOsName("iOS");
    }

    @Test
    void testDoDeleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/devices/{id}", mockDevice.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(service, times(1)).deleteById(mockDevice.getId());
    }
}
