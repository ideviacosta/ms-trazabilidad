package com.pragma.powerup.trazabilidad.application.dto;

public class RankingEficienciaEmpleadoDto {
    private Long idEmpleado;
    private Double promedioMinutos;

    // Getters, setters, constructor vacío y completo

    public RankingEficienciaEmpleadoDto() {
    }

    public RankingEficienciaEmpleadoDto(Long idEmpleado, Double promedioMinutos) {
        this.idEmpleado = idEmpleado;
        this.promedioMinutos = promedioMinutos;
    }

    public Long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Double getPromedioMinutos() {
        return promedioMinutos;
    }

    public void setPromedioMinutos(Double promedioMinutos) {
        this.promedioMinutos = promedioMinutos;
    }
}
