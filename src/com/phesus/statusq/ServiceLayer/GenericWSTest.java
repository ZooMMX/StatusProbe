package com.phesus.statusq.ServiceLayer;

import com.phesus.statusq.BL.Producto;
import com.phesus.statusq.BL.VentaDia;
import com.phesus.statusq.DAL.ExtractorMock;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * Proyecto Omoikane: SmartPOS 2.0
 * User: octavioruizcastillo
 * Date: 13/05/12
 * Time: 18:53
 */
public class GenericWSTest {
    ExtractorMock extractorMock;

    @Before
    public void setUp() {
        extractorMock = new ExtractorMock();
    }

    @Test
    public void testVenta2JSON() throws Exception {
        GenericWS ws = genericWSInstance();
        VentaDia ventaDia = extractorMock.getVenta();
        JSONObject jsonObject = ws.venta2JSON(ventaDia);

        Assert.assertTrue(jsonObject.getString("command").equals("setVentas"));
        Assert.assertTrue(jsonObject.getLong  ("idSucursal") == 3l               );
        Assert.assertTrue(jsonObject.getString("fecha")     .equals("2011-12-01"));
        Assert.assertTrue(jsonObject.getString("importe")   .equals("75621.21")  );
    }

    @Test
    public void testProductos2JSON() throws Exception {
        GenericWS ws = genericWSInstance();
        List<Producto> productos = extractorMock.getProductos();
        List<JSONObject> jsonObjects = ws.productos2JSON(productos, 5);
        JSONObject       jsonObject  = jsonObjects.get(0);

        Assert.assertTrue   ( jsonObjects.size() == 20                        );
        Assert.assertTrue   ( jsonObject.get("command").equals("setProductos"));
        Assert.assertNotNull( jsonObject.get("productos")                     );

        for(int i = 0; i < 5; i++) {
            final JSONObject productoJson = jsonObject.getJSONArray("productos").getJSONObject(i);

            final BigDecimal costo       = new BigDecimal(productoJson.getString("costo"      ));
            final BigDecimal existencias = new BigDecimal(productoJson.getString("existencias"));
            final BigDecimal precio      = new BigDecimal(productoJson.getString("precio"     ));
            final BigDecimal utilidad    = new BigDecimal(productoJson.getString("utilidad"   ));

            Assert.assertEquals(productoJson.getString("codigo")     , "abcd" + i * 15        );
            Assert.assertEquals(costo                                , new BigDecimal(i*500)  );
            Assert.assertEquals(productoJson.getString("descripcion"), "pruebaprueba"+i*523   );
            Assert.assertEquals(existencias                          , new BigDecimal(i*777)  );
            Assert.assertEquals(productoJson.getLong("id")           , new Long(i).longValue());
            Assert.assertEquals(precio                               , new BigDecimal(i*213)  );
            Assert.assertEquals(utilidad                             , new BigDecimal(i*121)  );

        }

    }

    private GenericWS genericWSInstance() {
        return new GenericWS(extractorMock) {

            @Override
            public void iniciar() {}

            @Override
            public void publishProductos() {}

            @Override
            public void publishVentaDia() {}

            @Override
            public void publishPing() {}
        };
    }
}
