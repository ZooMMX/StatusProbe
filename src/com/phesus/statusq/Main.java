package com.phesus.statusq;

import com.phesus.statusq.DAL.ExtractorOmoikane;
import com.phesus.statusq.ServiceLayer.PubnubWS;

/**
 * Proyecto Omoikane: SmartPOS 2.0
 * User: octavioruizcastillo
 * Date: 26/11/11
 * Time: 20:11
 */
public class Main {
    public static void main(String[] args) {

        ExtractorOmoikane etl      = new ExtractorOmoikane();
        PubnubWS          ws       = new PubnubWS(etl);
        ws.iniciar(true);

    }
}
