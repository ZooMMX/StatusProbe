package com.phesus.statusq.DAL;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.phesus.statusq.BL.DataSourceConfig;
import com.phesus.statusq.BL.Ping;
import com.phesus.statusq.BL.Producto;
import com.phesus.statusq.BL.VentaDia;
import com.phesus.statusq.Config;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Proyecto StatusQ
 * User: octavioruizcastillo
 * Date: 27/11/11
 * Time: 05:52
 */
public class ExtractorOmoikane implements IExtractor {

    private BoneCP pool;
    private DataSourceConfig dataSourceConfig;

    public void init() {
        try {
            DataSourceConfig gralConfig = dataSourceConfig;
            final BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(gralConfig.bdURL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(gralConfig.bdUser);
            config.setPassword(gralConfig.bdPass);
            config.setMinConnectionsPerPartition(1);
            config.setMaxConnectionsPerPartition(3);
            config.setPartitionCount(1);
            config.setDisableJMX(true);
            config.setTransactionRecoveryEnabled(true);
            config.setAcquireRetryAttempts(100000);
            config.setAcquireRetryDelayInMs(5000);
            config.setLogStatementsEnabled(true);
            pool = new BoneCP(config);
        } catch (Exception e) {
            Logger.getLogger(ExtractorOmoikane.class).error("Error iniciando extractor omoikane", e);
        }
    }

    public void shutdown() {
        pool.shutdown();
    }

    public void setDataSourceConfig(DataSourceConfig ds) {
        dataSourceConfig = ds;
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

            Long idSucursal   = dataSourceConfig.idSucursal;
            Date fecha        = rs.getDate("hoy");
            BigDecimal total  = rs.getBigDecimal("total");
            total             = total != null ? total : new BigDecimal("0");

            return new VentaDia(idSucursal, fecha, total);
        } catch (Exception e) {
            Logger.getLogger(ExtractorOmoikane.class).error("Error extrayendo ventas de omoikane", e);
        } finally {
            try {
                if(rs         != null) rs.close();
                if(stmt       != null) stmt.close();
                if(connection != null) connection.close();
            } catch (SQLException e) {
                Logger.getLogger(ExtractorOmoikane.class).error("Error al cerrar conexiones", e);
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
            Logger.getLogger(ExtractorOmoikane.class).error("Error extrayendo productos de omoikane", e);
        } finally {
            try {
                if(rs         != null) rs.close();
                if(stmt       != null) stmt.close();
                if(connection != null) connection.close();
            } catch (SQLException e) {
                Logger.getLogger(ExtractorOmoikane.class).error("Error al cerrar conexiones", e);
            }
        }

        return null;
    }

    public Ping ping() {
        Ping ping = new Ping();

        ping.idSucursal = dataSourceConfig.idSucursal;

        return ping;
    }
}
