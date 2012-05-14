package com.phesus.statusq.BL;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Proyecto StatusQ
 * User: octavioruizcastillo
 * Date: 27/11/11
 * Time: 05:40
 */
public class VentaDia {
    public Date       fecha;
    public BigDecimal importe;
    public Long       idSucursal;

    public VentaDia(Long idSucursal, Date fecha, BigDecimal importe) {
        this.idSucursal = idSucursal;
        this.fecha      = fecha;
        this.importe    = importe;
    }

    public String toString() {
        return "[IDSucursal:"+idSucursal+", fecha:"+fecha+", importe:"+importe+"]";
    }

}
