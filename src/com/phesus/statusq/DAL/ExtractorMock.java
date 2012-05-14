package com.phesus.statusq.DAL;

import com.phesus.statusq.BL.DataSourceConfig;
import com.phesus.statusq.BL.Ping;
import com.phesus.statusq.BL.Producto;
import com.phesus.statusq.BL.VentaDia;
import org.apache.commons.lang.NotImplementedException;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Octavio Ruiz
 * Date: 3/12/11
 * Time: 04:09 PM
 */
public class ExtractorMock implements IExtractor {
    public void setDataSourceConfig(DataSourceConfig ds) {
        throw new NotImplementedException("Método no implementado... nada más");
    }

    /**
     * <pre>Genera información de ventas de prueba
     * Específicamente genera la siguiente:
     * ID Sucursal : 3
     * Fecha: 2011-12-01
     * Importe: 75 621.21</pre>
     * @return VentaDia
     */
    public VentaDia getVenta() {
        Long       id       = 3l;
        Date       fecha    = Date.valueOf("2011-12-01");
        BigDecimal importe  = new BigDecimal("75621.21");
        return new VentaDia(id, fecha, importe);
    }

    /**
     * <pre>Genera información de productos de prueba
     * de la siguiente manera:
     * {@code for(int i = 0; i < 100; i++) {
            final Producto producto = new Producto();
            producto.setCodigo     ( "abcd"+i*15          );
            producto.setCosto      ( new BigDecimal(i*500));
            producto.setDescripcion( "pruebaprueba"+i*523 );
            producto.setExistencias( new BigDecimal(i*777));
            producto.setId         ( (long) i             );
            producto.setPrecio     ( new BigDecimal(i*213));
            producto.setUtilidad   ( new BigDecimal(i*121));
            productos.add(producto);
      }}</pre>
     * @return
     */
    public List<Producto> getProductos() {
                ArrayList<Producto> productos = new ArrayList<Producto>();

        for(int i = 0; i < 100; i++) {
            final Producto producto = new Producto();
            producto.setCodigo     ( "abcd"+i*15          );
            producto.setCosto      ( new BigDecimal(i*500));
            producto.setDescripcion( "pruebaprueba"+i*523 );
            producto.setExistencias( new BigDecimal(i*777));
            producto.setId         ( (long) i             );
            producto.setPrecio     ( new BigDecimal(i*213));
            producto.setUtilidad   ( new BigDecimal(i*121));
            productos.add(producto);
        }
        return productos;
    }

    public Ping ping() {
        Ping ping = new Ping();
        ping.idSucursal = 3l;
        return ping;
    }

    public void init() {

    }

}
