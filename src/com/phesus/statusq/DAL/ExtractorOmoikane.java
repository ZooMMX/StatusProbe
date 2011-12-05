package com.phesus.statusq.DAL;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.phesus.statusq.BL.Ping;
import com.phesus.statusq.BL.Producto;
import com.phesus.statusq.BL.VentaDia;
import com.phesus.statusq.Config;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Proyecto Omoikane: SmartPOS 2.0
 * User: octavioruizcastillo
 * Date: 27/11/11
 * Time: 05:52
 */
public class ExtractorOmoikane implements IExtractor {

    private BoneCP pool;

    public ExtractorOmoikane() {
        try {
            Config gralConfig = Config.getInstance();
            final BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(gralConfig.bdURL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(gralConfig.bdUser);
            config.setPassword(gralConfig.bdPass);
            config.setMinConnectionsPerPartition(2);
            config.setMaxConnectionsPerPartition(4);
            config.setPartitionCount(1);
            config.setDisableJMX(true);
            config.setTransactionRecoveryEnabled(true);
            config.setAcquireRetryAttempts(2);
            config.setLogStatementsEnabled(true);
            pool = new BoneCP(config);
        } catch (Exception e) {
            Logger.getLogger("").log(Level.SEVERE, "Error iniciando extractor omoikane", e);
        }
    }

    public VentaDia getVenta() {

        Connection connection = null;
        Statement  stmt       = null;
        ResultSet  rs         = null;

        try {
            connection = pool.getConnection();
            stmt       = connection.createStatement();
            rs         = stmt.executeQuery("select curdate() as hoy, sum(total) as total from omoikane.ventas where fecha_hora between curdate() and DATE_ADD(curdate(), INTERVAL 1 DAY)");

            rs.next();

            Long idSucursal   = Config.getInstance().idSucursal;
            Date fecha        = rs.getDate("hoy");
            BigDecimal total  = rs.getBigDecimal("total");
            total             = total != null ? total : new BigDecimal("0");

            return new VentaDia(idSucursal, fecha, total);
        } catch (Exception e) {
            Logger.getLogger("").log(Level.SEVERE, "Error extrayendo ventas de omoikane", e);
        } finally {
            try {
                if(rs         != null) rs.close();
                if(stmt       != null) stmt.close();
                if(connection != null) connection.close();
            } catch (SQLException e) {
                Logger.getLogger("").log(Level.SEVERE, "Error al cerrar conexiones", e);
            }
        }

        return null;
    }

    public List<Producto> getProductos() {
        Connection connection = null;
        Statement  stmt       = null;
        ResultSet  rs         = null;

        try {
            connection = pool.getConnection();
            stmt       = connection.createStatement();
            rs         = stmt.executeQuery("select id,descripcion,costo,existencias,precio,utilidad,codigo from omoikane.ramcachearticulos");

            List<Producto> productos = new ArrayList<Producto>();
            while(rs.next()) {

                try {
                    final Producto p = new Producto();

                    p.setId(rs.getLong("id"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setCosto(rs.getBigDecimal("costo"));
                    p.setExistencias(rs.getBigDecimal("existencias"));
                    p.setPrecio(rs.getBigDecimal("precio"));
                    p.setUtilidad(rs.getBigDecimal("utilidad"));
                    p.setCodigo(rs.getString("codigo"));
                    productos.add(p);

                }  catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return productos;
        } catch (Exception e) {
            Logger.getLogger("").log(Level.SEVERE, "Error extrayendo productos de omoikane", e);
        } finally {
            try {
                if(rs         != null) rs.close();
                if(stmt       != null) stmt.close();
                if(connection != null) connection.close();
            } catch (SQLException e) {
                Logger.getLogger("").log(Level.SEVERE, "Error al cerrar conexiones", e);
            }
        }

        return null;
    }

    public Ping ping() {
        Ping ping = new Ping();
        try {
            ping.idSucursal = Config.getInstance().idSucursal;
        } catch (IOException e) {
            Logger.getLogger("").log(Level.SEVERE, "Error en ping al leer configuración", e);
        }
        return ping;
    }
}
