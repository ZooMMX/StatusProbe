package com.phesus.statusq.ServiceLayer;

import com.google.common.collect.Lists;
import com.phesus.statusq.BL.Producto;
import com.phesus.statusq.BL.VentaDia;
import com.phesus.statusq.DAL.IExtractor;
import org.json.JSONArray;
import org.json.JSONObject;
import pubnub.Callback;
import pubnub.Pubnub;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Proyecto Omoikane: SmartPOS 2.0
 * User: octavioruizcastillo
 * Date: 27/11/11
 * Time: 06:09
 */
public class PubnubWS {

    private Pubnub pubnub;
    private IExtractor extractor;

    public PubnubWS(IExtractor extractor) {
        this.extractor = extractor;
    }
    public void iniciar() {
        pubnub  = new Pubnub( "pub-24eb74f7-b8f9-485a-86bb-a08f05c7cb89", "sub-b3f1fd2e-1897-11e1-8b36-c5b5280f91f0" );
        setCallback(new PubnubCallback());
    }
    public Pubnub getSocket() {
        return pubnub;
    }
    public void setCallback(Callback callback) {
        pubnub.subscribe( "clientes", callback );
    }

    class PubnubCallback implements Callback {

        public boolean execute(JSONObject message) {

            // Print Received Message
            System.out.println("+"+message);
            try {
                if(message.getString("command").equals("getVentas")) {
                    publishVentaDia();

                } else if(message.getString("command").equals("getProductos")) {
                    publishProductos();
                }
            } catch (Exception e) {
                Logger.getLogger("").log(Level.SEVERE, "Excepción en callback", e);
            }

            // Continue Listening?
            return true;
        }

        private void publishProductos() {
            List<Producto>   productos      = extractor.getProductos();
            List<JSONObject> lotesProductos = productos2JSON(productos);

            for(JSONObject loteJSON : lotesProductos) {
                getSocket().publish("servidor", loteJSON);
            }
        }

        private void publishVentaDia() {
            VentaDia   ventaDia     = extractor.getVenta();
            JSONObject ventaDiaJSON = venta2JSON(ventaDia);
            getSocket().publish     ( "servidor", ventaDiaJSON );
        }
    }

    public JSONObject venta2JSON(VentaDia vd) {
        JSONObject resp = new JSONObject();
        try {
            resp.put( "command"   , "setVentas");
            resp.put( "idSucursal", vd.idSucursal);
            resp.put( "fecha"     , vd.fecha.toString()     );
            resp.put( "importe"   , vd.importe.toString()   );
        } catch (Exception j) {
            Logger.getLogger("").log(Level.SEVERE, "Error convirtiendo venta a JSON", j);
        }
        return resp;
    }

    public List<JSONObject> productos2JSON(List<Producto> productos) {

        List<JSONObject>     lotesJSON = new ArrayList<JSONObject>();
        List<List<Producto>> lotesProductos = Lists.partition(productos, 10);

        for(List<Producto> loteProductos : lotesProductos) {
            JSONObject json = new JSONObject();
            try {
                json.put("command", "setProductos");
                JSONArray productosJSON = new JSONArray();
                for( Producto producto : loteProductos ) {
                    final JSONObject productoJSON = new JSONObject();
                    productoJSON.put("codigo"     , producto.getCodigo     ());
                    productoJSON.put("costo"      , producto.getCosto());
                    productoJSON.put("existencias", producto.getExistencias());
                    productoJSON.put("descripcion", producto.getDescripcion());
                    productoJSON.put("id"         , producto.getId         ());
                    productoJSON.put("precio"     , producto.getPrecio());
                    productoJSON.put("utilidad"   , producto.getUtilidad());

                    productosJSON.put(productoJSON);
                }
                json.put("productos", productosJSON);
            } catch (Exception j) {
                Logger.getLogger("").log(Level.SEVERE, "Error convirtiendo venta a JSON", j);
            }
            lotesJSON.add(json);
        }
        return lotesJSON;
    }
}
