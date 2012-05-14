package com.phesus.statusq.DAL;

import com.phesus.statusq.BL.DataSourceConfig;
import com.phesus.statusq.Config;

import java.io.IOException;

/**
 * Proyecto StatusQ
 * User: octavioruizcastillo
 * Date: 14/01/12
 * Time: 14:33
 */
public class MultiExtractor {
    public static IExtractor getActiveExtractor() throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {

        DataSourceConfig dsc   = Config.getInstance().dataSourceConfig;
        String activeExtractor = Config.getInstance().activeExtractor;
        IExtractor extractor   = (IExtractor) Class.forName(activeExtractor).newInstance();
        extractor.setDataSourceConfig(dsc);
        extractor.init();
        return extractor;
    }
}
