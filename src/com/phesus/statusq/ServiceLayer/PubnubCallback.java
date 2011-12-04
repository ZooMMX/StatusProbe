package com.phesus.statusq.ServiceLayer;

import com.phesus.statusq.BL.Producto;
import com.phesus.statusq.BL.VentaDia;
import org.json.JSONObject;
import pubnub.Callback;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* Created by IntelliJ IDEA.
* User: usuario1
* Date: 3/12/11
* Time: 03:25 PM
* To change this template use File | Settings | File Templates.
*/
class PubnubCallback implements Callback {

    private PubnubWS pubnubWS;

    public PubnubCallback(PubnubWS pubnubWS) {
        this.pubnubWS = pubnubWS;
    }

    public boolean execute(JSONObject message) {

        // Print Received Message
        System.out.println("+"+message);
        try {
            if(message.getString("command").equals("getVentas")) {
                pubnubWS.publishVentaDia();

            } else if(message.getString("command").equals("getProductos")) {
                pubnubWS.publishProductos();
            }
        } catch (Exception e) {
            Logger.getLogger("").log(Level.SEVERE, "Excepción en callback", e);
        }

        // Continue Listening?
        return true;
    }
}
