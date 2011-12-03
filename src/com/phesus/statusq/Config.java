package com.phesus.statusq;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Proyecto Omoikane: SmartPOS 2.0
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

    public String  bdURL     ; //  = "jdbc:mysql://sfmvaldez.poweredbyclear.com:3308/Omoikane";
    public String  bdUser    ; //  = "ZooMMX";
    public String  bdPass    ; //  = "GatoGato00";
    public Long    idSucursal; //  = new Long("1");

    public void loadConfig() throws IOException {

         StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
         encryptor.setPassword("megn.5603");

         Properties props = new EncryptableProperties(encryptor);
         props.load(new FileInputStream("configuration.properties"));

         bdURL      = props.getProperty("datasource.URL");
         bdUser     = props.getProperty("datasource.user");
           bdPass     = props.getProperty("datasource.pass");
         idSucursal = new Long(props.getProperty("datasource.idSucursal"));

    }
}
