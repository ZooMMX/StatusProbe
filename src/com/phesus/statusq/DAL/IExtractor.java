package com.phesus.statusq.DAL;

import com.phesus.statusq.BL.DataSourceConfig;
import com.phesus.statusq.BL.Ping;
import com.phesus.statusq.BL.Producto;
import com.phesus.statusq.BL.VentaDia;

import java.util.List;

/**
 * Proyecto StatusQ
 * User: octavioruizcastillo
 * Date: 27/11/11
 * Time: 05:36
 */
public interface IExtractor {
    public void setDataSourceConfig(DataSourceConfig ds);
    public VentaDia getVenta();
    public List<Producto> getProductos();
    public Ping ping();
    public void init();
}
