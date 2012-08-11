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

import static java.lang.Thread.sleep;

/**
 * Proyecto StatusQ
 * User: octavioruizcastillo
 * Date: 26/11/11
 * Time: 20:11
 */
public class Main {
    public static void main(String[] args) throws Exception {

        IExtractor        et       = MultiExtractor.getActiveExtractor();
        WebSocketWS ws       = new WebSocketWS(et);
        //ws.setBidireccional(true);
        ws.iniciar();
        while(true) {
            Thread.sleep(100000);
        }
    }
}
