package com.pragma.powerup.trazabilidad.application.dto;

import java.util.Date;

public class HistorialEstadoResponseDto {
    private String estado;
    private Date fechaCambio;

    public HistorialEstadoResponseDto(String estado, Date fechaCambio) {
        this.estado = estado;
        this.fechaCambio = fechaCambio;
    }

    public String getEstado() {
        return estado;
    }

    public Date getFechaCambio() {
        return fechaCambio;
    }
}