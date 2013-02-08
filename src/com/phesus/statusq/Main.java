package com.phesus.statusq;

import code.lucamarrocco.hoptoad.HoptoadNotice;
import code.lucamarrocco.hoptoad.HoptoadNoticeBuilder;
import code.lucamarrocco.hoptoad.HoptoadNotifier;
import com.phesus.statusq.DAL.ExtractorOmoikane;
import com.phesus.statusq.DAL.IExtractor;
import com.phesus.statusq.DAL.MultiExtractor;
import com.phesus.statusq.ServiceLayer.PubnubWS;
import com.phesus.statusq.ServiceLayer.WebSocketWS;
import org.apache.log4j.Logger;

import java.io.IOException;

import static java.lang.Thread.sleep;

/**
 * Proyecto StatusQ
 * User: octavioruizcastillo
 * Date: 26/11/11
 * Time: 20:11
 * Éste programa se constituye principalmente de 2 procesos problemático, cada uno en su propio hilo:
 * - Websockets: Cuando existe algún error en el websocket este es reiniciado
 * - Extractor: Contiene un pool de conexiones a la base de datos, cuando existe algun error (normalmente con las
 *      conexiones a JDBC) éste proceso es reiniciado
 */
public class Main {
    public static void main(String[] args) throws Exception {

        Logger.getLogger(Main.class).info("Phesus Status Probe '12-'13 V. 1.1");
        IExtractor        et       = MultiExtractor.getActiveExtractor();
        WebSocketWS       ws       = new WebSocketWS(et);
        ws.iniciar();


        while(true) {
            try {
                Thread.sleep(60000);
                if( ws.getWsClient().getReadyState() != 1 ) {
                    et.shutdown();
                    et = MultiExtractor.getActiveExtractor();
                    ws = new WebSocketWS(et);
                    ws.iniciar();
                }
                Logger.getLogger(Main.class).info("Estado: " + ws.getWsClient().getReadyState());
            } catch(Exception e) {
                Logger.getLogger(Main.class).error("Error en main loop", e);
            }
        }
    }

}
