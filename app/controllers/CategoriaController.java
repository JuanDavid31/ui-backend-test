package controllers;

import models.ProductoEntity;
import models.CategoriaEntity;
import play.mvc.*;

import java.util.List;

public class CategoriaController extends Controller {

    public static List<CategoriaEntity> darTodas(){
        return CategoriaEntity.find.all();
    }

    public static CategoriaEntity darCategoria(int id){
        return CategoriaEntity.find.ref(id);
    }

    public static boolean guardar(CategoriaEntity categoria){
        if(categoria.validate() == null){
            categoria.save();
            return true;
        }
        return false;
    }

    public static void editar(CategoriaEntity antigua, CategoriaEntity nueva){
        antigua.setdNombre(nueva.getdNombre());
        guardar(antigua);
    }

    public static boolean eliminar(CategoriaEntity categoria){
        List<ProductoEntity> productos = categoria.getProductos();
        for(ProductoEntity producto : productos ){
            ProductoController.eliminar(producto);
        }
        return categoria.delete();
    }

    public static void adicionarProducto(CategoriaEntity categoria, ProductoEntity producto){
        categoria.getProductos().add(producto);
        guardar(categoria);
    }
}
