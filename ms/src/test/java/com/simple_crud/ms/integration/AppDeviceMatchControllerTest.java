package com.simple_crud.ms.integration;

import com.simple_crud.ms.dto.AppDeviceDTO;
import com.simple_crud.ms.model.AppDevice;
import com.simple_crud.ms.repository.IAppDeviceMatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AppDeviceMatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IAppDeviceMatchRepository repository;

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

        when(repository.save(any(AppDevice.class))).thenReturn(mockDevice);
        when(repository.findById(anyString())).thenReturn(Optional.of(mockDevice));
        when(repository.findAllByOsName("Linux")).thenReturn(List.of(mockDevice));
        when(repository.findAllByOsName("iOS")).thenReturn(List.of());
        doNothing().when(repository).deleteById(mockDevice.getId());
    }

    @Test
    void testDoPost() throws Exception {
        String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 Linux x86_64 Chrome 91.0.4472";
        mockMvc.perform(MockMvcRequestBuilders.post("/device-match")
                        .header("User-Agent", userAgent)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"));

        verify(repository, times(1)).save(any(AppDevice.class));
    }

    @Test
    void testDoGetById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/device-match/{id}", mockDevice.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(mockDevice.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.osName").value(mockDeviceDTO.getOsName()));

        verify(repository, times(1)).findById(mockDevice.getId());
    }

    @Test
    void testDoGetByIdNotFound() throws Exception {
        doThrow(NoSuchElementException.class).when(repository).findById(anyString());

        mockMvc.perform(MockMvcRequestBuilders.get("/device-match/{id}", mockDevice.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        verify(repository, times(1)).findById(mockDevice.getId());
    }

    @Test
    void testDoGetByOsNameFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/device-match")
                        .param("osName", "Linux")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(mockDevice.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].osName").value(mockDeviceDTO.getOsName()));

        verify(repository, times(1)).findAllByOsName(mockDeviceDTO.getOsName());
    }

    @Test
    void testDoGetByOsNameNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/device-match")
                        .param("osName", "iOS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

        verify(repository, times(1)).findAllByOsName("iOS");
    }

    @Test
    void testDoDeleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/device-match/{id}", mockDevice.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(repository, times(1)).deleteById(mockDevice.getId());
    }
}
