package com.pragma.powerup.trazabilidad.domain.model;

import java.util.Date;

public class HistorialEstado {
    private String id;
    private Long idPedido;
    private Long idCliente;
    private String estado;
    private Date fechaCambio;

    private HistorialEstado(Builder builder) {
        this.id = builder.id;
        this.idPedido = builder.idPedido;
        this.idCliente = builder.idCliente;
        this.estado = builder.estado;
        this.fechaCambio = builder.fechaCambio;
    }

    public HistorialEstado() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private Long idPedido;
        private Long idCliente;
        private String estado;
        private Date fechaCambio;

        public Builder id(String id) {
            this.id = id;
            return this;
        }
        public Builder id(Long idPedido) {
            this.idPedido = idPedido;
            return this;
        }
        public Builder idCliente(Long idCliente) {
            this.idCliente = idCliente;
            return this;
        }
        public Builder estado(String estado) {
            this.estado = estado;
            return this;
        }
        public Builder fechaCambio(Date fechaCambio) {
            this.fechaCambio = fechaCambio;
            return this;
        }
        public HistorialEstado build() {
            return new HistorialEstado(this);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

}