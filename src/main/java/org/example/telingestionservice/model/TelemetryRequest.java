package org.example.telingestionservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TelemetryRequest {
    @NotBlank(message = "Device ID is required")
    private String deviceId;

    @NotNull(message = "Temperature is required")
    private Double temperature;

    private Long timestamp;
}