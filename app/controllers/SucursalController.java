package controllers;

import models.*;
import play.mvc.*;

import java.util.List;

public class SucursalController extends Controller {

    public static List<SucursalEntity> darTodos(){
        return SucursalEntity.find.all();
    }

    public static SucursalEntity darSucursal(int id){
        return SucursalEntity.find.ref(id);
    }

    public static boolean guardar(SucursalEntity sucursal){
        if(sucursal.validate() == null){
            sucursal.save();
            return true;
        }
        return false;
    }

    public static void editar(SucursalEntity antiguo, SucursalEntity nuevo){
        antiguo.setdNombre(nuevo.getdNombre());
        antiguo.setaDireccion(nuevo.getaDireccion());
        guardar(antiguo);
    }

    public static boolean eliminar(SucursalEntity sucursal){
        List<ProductoEntity> productos = sucursal.getProductos();
        for(ProductoEntity producto : productos ){
            ProductoController.eliminar(producto);
        }
        return sucursal.delete();
    }

    public static void adicionarProducto(SucursalEntity sucursal, ProductoEntity producto){
        sucursal.getProductos().add(producto);
        guardar(sucursal);
    }
}
