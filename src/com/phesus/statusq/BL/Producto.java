package com.phesus.statusq.BL;

import java.math.BigDecimal;

/**
 * Proyecto StatusQ
 * User: octavioruizcastillo
 * Date: 30/11/11
 * Time: 18:55
 */
public class Producto {
    private Long id;
    private String descripcion;
    private String codigo;
    private BigDecimal costo;
    private BigDecimal precio;
    private BigDecimal utilidad;
    private BigDecimal existencias;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(BigDecimal utilidad) {
        this.utilidad = utilidad;
    }

    public BigDecimal getExistencias() {
        return existencias;
    }

    public void setExistencias(BigDecimal existencias) {
        this.existencias = existencias;
    }
}
