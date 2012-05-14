package com.phesus.statusq.ServiceLayer;

import com.phesus.statusq.BL.JSONAdapter;
import com.phesus.statusq.BL.Ping;
import com.phesus.statusq.BL.Producto;
import com.phesus.statusq.BL.VentaDia;
import com.phesus.statusq.DAL.IExtractor;
import org.json.JSONObject;

import java.util.List;

/**
 * Proyecto Omoikane: SmartPOS 2.0
 * User: octavioruizcastillo
 * Date: 13/05/12
 * Time: 14:15
 */
public abstract class GenericWS {
    private final JSONAdapter JSONAdapter = new JSONAdapter();
    protected IExtractor extractor;

    public GenericWS(IExtractor extractor) {
        this.extractor = extractor;
    }

    public JSONObject venta2JSON(VentaDia vd) {
        return JSONAdapter.venta2JSON(vd);
    }

    public JSONObject ping2JSON(Ping ping) {
        return JSONAdapter.ping2JSON(ping);
    }

    public List<JSONObject> productos2JSON(List<Producto> productos, int partitionSize) {

        return JSONAdapter.productos2JSON(productos, partitionSize);
    }

    public IExtractor getExtractor() {
        return extractor;
    }

    public abstract void iniciar();

    public abstract void publishProductos();

    public abstract void publishVentaDia();

    public abstract void publishPing();
}
