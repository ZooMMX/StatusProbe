package com.phesus.statusq.ServiceLayer;

import com.phesus.statusq.BL.Producto;
import com.phesus.statusq.BL.VentaDia;
import com.phesus.statusq.DAL.ExtractorMock;
import org.java_websocket.WebSocket;
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
 * Time: 14:46
 */
public class WebSocketWSTest {
    ExtractorMock extractorMock;

    @Before
    public void setUp() {
        extractorMock = new ExtractorMock();
    }

    @Test
    public void testIniciar() throws Exception {
        WebSocket.DEBUG = true;
        WebSocketWS ws = new WebSocketWS(extractorMock);
        ws.iniciar();
        Thread.sleep(100000);
    }

    @Test
    public void testPublishProductos() {
        WebSocketWS webSocketWS = new WebSocketWS(extractorMock);
        webSocketWS.iniciar();
        webSocketWS.publishProductos();
    }

    @Test
    public void testPublishVentaDia() {
        WebSocketWS webSocketWS = new WebSocketWS(extractorMock);
        webSocketWS.iniciar();
        webSocketWS.publishVentaDia();
    }

    @Test
    public void testPublishPing() {
        WebSocketWS webSocketWS = new WebSocketWS(extractorMock);
        webSocketWS.iniciar();
        webSocketWS.publishPing(); //In this impl. publisPing does nothing at all
    }


}
