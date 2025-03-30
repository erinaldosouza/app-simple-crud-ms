package com.simple_crud.ms.units.controllers;

import com.simple_crud.ms.controllers.AppDeviceMatchController;
import com.simple_crud.ms.controllers.dtos.AppDeviceDTO;
import com.simple_crud.ms.services.impl.AppDeviceMatchServiceImpl;
import com.simple_crud.ms.services.models.AppDevice;
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

        when(service.create(anyString())).thenReturn(mockDevice);
        when(service.create(any(AppDevice.class))).thenReturn(mockDevice);
        when(service.findById(anyString())).thenReturn(mockDevice);
        when(service.findByOsName("Linux")).thenReturn(List.of(mockDevice));
        when(service.findByOsName("iOS")).thenReturn(List.of());
        doNothing().when(service).deleteById(mockDevice.getId());
    }

    @Test
    void testDoPost() throws Exception {
        String userAgente = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 Linux x86_64 Chrome 91.0.4472";
        mockMvc.perform(MockMvcRequestBuilders.post("/device-match")
                        .header("User-Agent", userAgente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"));

        verify(service, times(1)).create(anyString());
    }

    @Test
    void testDoGetById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/device-match/{id}", mockDevice.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(mockDevice.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.osName").value(mockDeviceDTO.getOsName()));

        verify(service, times(1)).findById(mockDevice.getId());
    }

    @Test
    void testDoGetByIdNotFound() throws Exception {
        doThrow(NoSuchElementException.class).when(service).findById(anyString());

        mockMvc.perform(MockMvcRequestBuilders.get("/device-match/{id}", mockDevice.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        verify(service, times(1)).findById(mockDevice.getId());
    }

    @Test
    void testDoGetByOsNameFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/device-match")
                        .param("osName", "Linux")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(mockDevice.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].osName").value(mockDeviceDTO.getOsName()));

        verify(service, times(1)).findByOsName(mockDeviceDTO.getOsName());
    }

    @Test
    void testDoGetByOsNameNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/device-match")
                        .param("osName", "iOS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

        verify(service, times(1)).findByOsName("iOS");
    }

    @Test
    void testDoDeleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/device-match/{id}", mockDevice.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(service, times(1)).deleteById(mockDevice.getId());
    }
}
