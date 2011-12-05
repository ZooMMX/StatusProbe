package com.phesus.statusq.DAL;

import com.phesus.statusq.BL.Ping;
import com.phesus.statusq.BL.Producto;
import com.phesus.statusq.BL.VentaDia;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.Assert.assertTrue;


/**
 * Proyecto Omoikane: SmartPOS 2.0
 * User: octavioruizcastillo
 * Date: 01/12/11
 * Time: 12:56
 */
public class ExtractorOmoikaneTest {
    @Test
    public void testGetVenta() throws Exception {
        ExtractorOmoikane eo = new ExtractorOmoikane();
        VentaDia vd = eo.getVenta();
        Logger.getAnonymousLogger().log(Level.INFO, vd.toString());
        assertTrue( vd.fecha != null );
        assertTrue( vd.idSucursal > 0 );
        assertTrue( vd.importe.compareTo(new BigDecimal("0")) >= 0 );
    }

    @Test
    public void testGetProductos() throws Exception {
        ExtractorOmoikane eo = new ExtractorOmoikane();
        List<Producto> productos = eo.getProductos();
        for(Producto p : productos) {
            assertTrue( p.getCodigo() != null );
            assertTrue( !p.getCodigo() .equals("") );
            assertTrue( p.getCosto()   .compareTo(new BigDecimal("0")) >= 0 );
            assertTrue( p.getPrecio()  .compareTo(new BigDecimal("0")) >= 0 );
            assertTrue( p.getUtilidad() != null );
            assertTrue( p.getExistencias() != null );
            assertTrue( p.getId() > 0 );

        }
    }

    @Test
    public void testPing() throws Exception {
        ExtractorMock eo = new ExtractorMock();
        Ping ping = eo.ping();
        assertTrue(ping.idSucursal == 3l);
    }
}
