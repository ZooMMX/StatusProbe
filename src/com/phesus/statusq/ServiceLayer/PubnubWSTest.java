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
 * Date: 01/12/11
 * Time: 20:45
 */
public class PubnubWSTest {

    ExtractorMock extractorMock;

    @Before
    public void setUp() {
        extractorMock = new ExtractorMock();
    }

    @Test
    public void testPublishProductos() {
        PubnubWS pubnubWS = new PubnubWS(extractorMock);
        pubnubWS.iniciar(false);
        pubnubWS.publishProductos();
    }

    @Test
    public void testPublishVentaDia() {
        PubnubWS pubnubWS = new PubnubWS(extractorMock);
        pubnubWS.iniciar(false);
        pubnubWS.publishVentaDia();
    }

    @Test
    public void testVenta2JSON() throws Exception {
        PubnubWS pubnubWS = new PubnubWS(extractorMock);
        VentaDia ventaDia = extractorMock.getVenta();
        JSONObject jsonObject = pubnubWS.venta2JSON(ventaDia);

        Assert.assertTrue(jsonObject.getString("command")   .equals("setVentas") );
        Assert.assertTrue(jsonObject.getLong  ("idSucursal") == 3l               );
        Assert.assertTrue(jsonObject.getString("fecha")     .equals("2011-12-01"));
        Assert.assertTrue(jsonObject.getString("importe")   .equals("75621.21")  );
    }

    @Test
    public void testProductos2JSON() throws Exception {
        PubnubWS pubnubWS = new PubnubWS(null);
        List<Producto> productos = extractorMock.getProductos();
        List<JSONObject> jsonObjects = pubnubWS.productos2JSON(productos, 8);
        JSONObject       jsonObject  = jsonObjects.get(0);

        Assert.assertTrue   ( jsonObjects.size() == 10                        );
        Assert.assertTrue   ( jsonObject.get("command").equals("setProductos"));
        Assert.assertNotNull( jsonObject.get("productos")                     );

        for(int i = 0; i < 10; i++) {
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

}
