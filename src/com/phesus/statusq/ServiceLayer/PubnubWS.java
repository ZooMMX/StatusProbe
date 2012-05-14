package com.phesus.statusq.ServiceLayer;

import com.phesus.statusq.BL.Ping;
import com.phesus.statusq.BL.Producto;
import com.phesus.statusq.BL.VentaDia;
import com.phesus.statusq.DAL.IExtractor;
import org.json.JSONObject;
import pubnub.Callback;
import pubnub.Pubnub;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Proyecto StatusQ
 * User: octavioruizcastillo
 * Date: 27/11/11
 * Time: 06:09
 */
public class PubnubWS extends GenericWS {

    private Pubnub pubnub;

    private Boolean bidireccional;

    public PubnubWS(IExtractor extractor) {
        super(extractor);
    }

    @Override
    public void iniciar() {
        pubnub  = new Pubnub( "pub-24eb74f7-b8f9-485a-86bb-a08f05c7cb89", "sub-b3f1fd2e-1897-11e1-8b36-c5b5280f91f0" );
        if(bidireccional) setCallback(new PubnubCallback(this));
    }
    public void setBidireccional(Boolean bidireccional) {
        this.bidireccional = bidireccional;
    }
    public Pubnub getSocket() {
        return pubnub;
    }
    public void setCallback(Callback callback) {
        pubnub.subscribe( "clientes", callback );
    }

    @Override
    public void publishProductos() {
        List<Producto> productos        = getExtractor().getProductos();
        List<JSONObject> lotesProductos = productos2JSON(productos, 4);

        for(JSONObject loteJSON : lotesProductos) {
            getSocket().publish("servidor", loteJSON);
            Logger.getAnonymousLogger().log(Level.INFO, "Productos publicados");
        }
    }

    @Override
    public void publishVentaDia() {
        VentaDia ventaDia       = getExtractor().getVenta();
        JSONObject ventaDiaJSON = venta2JSON(ventaDia);
        getSocket().publish     ( "servidor", ventaDiaJSON );
    }

    @Override
    public void publishPing() {
        Ping       ping     = getExtractor().ping();
        JSONObject pingJSON = ping2JSON(ping);
        getSocket().publish ( "servidor", pingJSON );
    }
}
