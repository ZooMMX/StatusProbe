package com.phesus.statusq;

import code.lucamarrocco.hoptoad.HoptoadNotice;
import code.lucamarrocco.hoptoad.HoptoadNoticeBuilder;
import code.lucamarrocco.hoptoad.HoptoadNotifier;
import com.phesus.statusq.DAL.ExtractorOmoikane;
import com.phesus.statusq.ServiceLayer.PubnubWS;
import org.apache.log4j.Logger;

/**
 * Proyecto Omoikane: SmartPOS 2.0
 * User: octavioruizcastillo
 * Date: 26/11/11
 * Time: 20:11
 */
public class Main {
    public static void main(String[] args) throws Exception {

        ExtractorOmoikane etl      = new ExtractorOmoikane();
        PubnubWS          ws       = new PubnubWS(etl);
        ws.iniciar(true);
    }
}
