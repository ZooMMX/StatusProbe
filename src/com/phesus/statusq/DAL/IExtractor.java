package com.phesus.statusq.DAL;

import com.phesus.statusq.BL.Producto;
import com.phesus.statusq.BL.VentaDia;

import java.util.List;

/**
 * Proyecto Omoikane: SmartPOS 2.0
 * User: octavioruizcastillo
 * Date: 27/11/11
 * Time: 05:36
 */
public interface IExtractor {
    public VentaDia getVenta();
    public List<Producto> getProductos();
}
