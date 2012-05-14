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
 * Proyecto StatusQ
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
        pubnubWS.setBidireccional(false);
        pubnubWS.iniciar();
        pubnubWS.publishProductos();
    }

    @Test
    public void testPublishVentaDia() {
        PubnubWS pubnubWS = new PubnubWS(extractorMock);
        pubnubWS.setBidireccional(false);
        pubnubWS.iniciar();
        pubnubWS.publishVentaDia();
    }

    @Test
    public void testPublishPing() {
        PubnubWS pubnubWS = new PubnubWS(extractorMock);
        pubnubWS.setBidireccional(false);
        pubnubWS.iniciar();
        pubnubWS.publishPing();
    }

}
