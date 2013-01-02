package com.phesus.statusq.ServiceLayer;

import com.phesus.statusq.BL.Ping;
import com.phesus.statusq.BL.Producto;
import com.phesus.statusq.BL.VentaDia;
import com.phesus.statusq.Config;
import com.phesus.statusq.DAL.IExtractor;
import org.java_websocket.client.WebSocketClient;
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

    public WSClient getWsClient() {
        return wsClient;
    }

    private WSClient wsClient;
    private Long idSucursal;
    Boolean ping;

    public WebSocketWS(IExtractor extractor) {
        super(extractor);
    }

    @Override
    public void iniciar() {
        try {
            idSucursal = Config.getInstance().dataSourceConfig.idSucursal;
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
            wsClient.send( loteJSON.toString() );
            Logger.getAnonymousLogger().log(Level.INFO, "Productos publicados");
        }
    }

    @Override
    public void publishVentaDia() {
        VentaDia ventaDia       = getExtractor().getVenta();
        JSONObject ventaDiaJSON = venta2JSON(ventaDia);
        Logger.getAnonymousLogger().log(Level.INFO, "Ventas publicadas: "+ventaDiaJSON.toString());
        wsClient.send(ventaDiaJSON.toString());
    }

    @Override
    /**
     * Nothing to do, ping not used in this implementation, instead, I am using session based online status
     */
    public void publishPing() {
        wsClient.send("{\"command\":\"ping\", \"idSucursal\":"+WebSocketWS.this.idSucursal+"}");
        ping = true;
    }

    public class WSClient extends WebSocketClient {

        public WSClient() throws IOException, URISyntaxException {
            //super(new URI("ws://localhost:9000/hola?sucursalId="+Config.getInstance().dataSourceConfig.idSucursal));
            super(new URI(Config.getInstance().wsURL+"/hola?sucursalId="+idSucursal));
            //super(new URI("ws://damp-anchorage-4847.herokuapp.com/hello?sucursalId="+Config.getInstance().dataSourceConfig.idSucursal));
        }

        @Override
        public void onOpen(ServerHandshake serverHandshake) {
            org.apache.log4j.Logger.getLogger(WebSocketWS.class).info("Open");
            WSClient wsClient1 = this;
            ping = false;

            new Thread() {
                public void run() {
                    do {
                        WebSocketWS.this.publishPing();

                        try {
                            Logger.getLogger("").info("Ping...");
                            Thread.sleep(15000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    } while(ping == false);
                    WebSocketWS.this.getWsClient().close();
                }
            }.start();
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
                if(command.equals("pong")) {
                    ping = false;
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
