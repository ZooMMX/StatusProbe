package com.phesus.statusq.BL;

import com.google.common.collect.Lists;
import com.phesus.statusq.Config;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.*;

public class JSONAdapter {
    public JSONAdapter() {
    }

    public JSONObject venta2JSON(VentaDia vd) {
        JSONObject resp = new JSONObject();
        try {
            resp.put("command", "setVentas");
            resp.put("idSucursal", vd.idSucursal);
            resp.put("fecha", vd.fecha.toString());
            resp.put("importe", vd.importe.toString());
        } catch (Exception j) {
            Logger.getLogger("").log(SEVERE, "Error convirtiendo venta a JSON", j);
        }
        return resp;
    }

    /**
     * Convierte listas de productos a objetos JSON. Debido a que puede existir un
     * tamaño máximo de mensaje enviado por un servicio de mensajes, la lista de productos
     * se puede particionar
     * @param productos Lista de productos
     * @param partitionSize Máximo número de productos en cada objeto JSON
     * @return Una lista con un lote de objetos json cada uno conteniendo paritionSize productos
     */
    public List<JSONObject> productos2JSON(List<Producto> productos, int partitionSize) {

        List<JSONObject> lotesJSON = new ArrayList<JSONObject>();
        List<List<Producto>> lotesProductos = Lists.partition(productos, partitionSize);

        for (List<Producto> loteProductos : lotesProductos) {
            JSONObject json = new JSONObject();
            try {
                json.put("command", "setProductos");
                json.put("idSucursal", Config.getInstance().idSucursal);
                JSONArray productosJSON = new JSONArray();
                for (Producto producto : loteProductos) {
                    final JSONObject productoJSON = new JSONObject();
                    productoJSON.put("codigo", producto.getCodigo());
                    productoJSON.put("costo", producto.getCosto());
                    productoJSON.put("existencias", producto.getExistencias());
                    productoJSON.put("descripcion", producto.getDescripcion());
                    productoJSON.put("id", producto.getId());
                    productoJSON.put("precio", producto.getPrecio());
                    productoJSON.put("utilidad", producto.getUtilidad());

                    productosJSON.put(productoJSON);
                }
                json.put("productos", productosJSON);
            } catch (Exception j) {
                Logger.getLogger("").log(SEVERE, "Error convirtiendo venta a JSON", j);
            }
            lotesJSON.add(json);
        }
        return lotesJSON;
    }
}