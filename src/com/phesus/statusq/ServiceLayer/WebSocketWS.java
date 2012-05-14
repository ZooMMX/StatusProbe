package com.phesus.statusq.ServiceLayer;

import com.phesus.statusq.BL.Ping;
import com.phesus.statusq.BL.Producto;
import com.phesus.statusq.BL.VentaDia;
import com.phesus.statusq.Config;
import com.phesus.statusq.DAL.IExtractor;
import org.java_websocket.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Proyecto Omoikane: SmartPOS 2.0
 * User: octavioruizcastillo
 * Date: 13/05/12
 * Time: 14:27
 */
public class WebSocketWS extends GenericWS {

    private WSClient wsClient;

    public WebSocketWS(IExtractor extractor) {
        super(extractor);
    }

    @Override
    public void iniciar() {
        try {
            wsClient = new WSClient();
            wsClient.connect();
        } catch (IOException e) {
            Logger.getLogger("").throwing("WebSocketWS", "iniciar", e);
        } catch (URISyntaxException e) {
            Logger.getLogger("").throwing("WebSocketWS", "iniciar", e);
        }

    }

    @Override
    public void publishProductos() {
        List<Producto> productos        = getExtractor().getProductos();
        List<JSONObject> lotesProductos = productos2JSON(productos, 400);

        for(JSONObject loteJSON : lotesProductos) {
            try {
                wsClient.send( loteJSON.toString() );
            } catch (InterruptedException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Error publicando productos");
            }
            Logger.getAnonymousLogger().log(Level.INFO, "Productos publicados");
        }
    }

    @Override
    public void publishVentaDia() {
        VentaDia ventaDia       = getExtractor().getVenta();
        JSONObject ventaDiaJSON = venta2JSON(ventaDia);
        try {
            Logger.getAnonymousLogger().log(Level.INFO, "Ventas publicadas: "+ventaDiaJSON.toString());
            wsClient.send(ventaDiaJSON.toString());
        } catch (InterruptedException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Error publicando ventas del día");
        }
    }

    @Override
    /**
     * Nothing to do, ping not used in this implementation, instead, I am using session based online status
     */
    public void publishPing() {

    }

    public class WSClient extends WebSocketClient {

        public WSClient() throws IOException, URISyntaxException {
            super(new URI("ws://localhost:9000/hola?sucursalId="+Config.getInstance().dataSourceConfig.idSucursal));
        }

        @Override
        public void onOpen(ServerHandshake serverHandshake) {
            Logger.getLogger("").info("Open");

        }

        @Override
        public void onMessage(String s) {
            Logger.getLogger("").info("Mensaje: " + s);
            try {
                JSONObject json = new JSONObject(s);
                String command = json.getString("command");
                Logger.getLogger("").info("Comando recibido: '"+command+"'");

                if(command.equals("getVentas")) {
                    publishVentaDia();
                }
            } catch (JSONException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Error convirtiendo JSON entrante");
            }
        }

        @Override
        public void onClose(int i, String s, boolean b) {
            Logger.getLogger("").info("Close");
        }

        @Override
        public void onError(Exception e) {
            Logger.getLogger("").info("Error: "+e.getMessage());
        }
    }
}
