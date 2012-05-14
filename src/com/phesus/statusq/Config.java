package com.phesus.statusq;

import com.phesus.statusq.BL.DataSourceConfig;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Proyecto StatusQ
 * User: octavioruizcastillo
 * Date: 27/11/11
 * Time: 08:09
 */
public class Config {
    public static Config instance;
    public static Config getInstance() throws IOException {
        if(instance == null) {
            instance = new Config();
            instance.loadConfig();
        }
        return instance;
    }

    public DataSourceConfig dataSourceConfig = new DataSourceConfig();
    public String activeExtractor;

    public void loadConfig() throws IOException {

         StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
         encryptor.setPassword("megn.5603");

         Properties props = new EncryptableProperties(encryptor);
         props.load(new FileInputStream("configuration.properties"));

         DataSourceConfig ds = dataSourceConfig;

         ds.bdURL        = props.getProperty("datasource.URL");
         ds.bdUser       = props.getProperty("datasource.user");
         ds.bdPass       = props.getProperty("datasource.pass");
         ds.idSucursal   = new Long(props.getProperty("datasource.idSucursal"));
         activeExtractor = props.getProperty("activeExtractor");

    }
}
