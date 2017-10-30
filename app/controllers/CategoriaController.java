package controllers;

import models.ProductoEntity;
import models.CategoriaEntity;
import play.mvc.*;

import java.util.List;

/**
 * Clase que realiza todas las operaciones relacionadas con categorias
 */
public class CategoriaController extends Controller {

    /**
     * Devuelve todas las categorias
     * @return categorias desponibles
     */
    public static List<CategoriaEntity> darTodas(){
        return CategoriaEntity.find.all();
    }

    /**
     * Devueve una categoria
     * @param id de la categoria a devolver
     * @return categoria
     */
    public static CategoriaEntity darCategoria(int id){
        return CategoriaEntity.find.ref(id);
    }

    /**
     * Guarda una categoria
     * @param categoria a guardar
     * @return true si las validaciones son correctas, false en caso contrario
     */
    public static boolean guardar(CategoriaEntity categoria){
        if(categoria.validate() == null){
            categoria.save();
            return true;
        }
        return false;
    }

    /**
     * Edita una categoria
     * @param antigua categoria a editar
     * @param nueva categoria de la que se tomaran los nuevos datos
     */
    public static void editar(CategoriaEntity antigua, CategoriaEntity nueva){
        antigua.setdNombre(nueva.getdNombre());
        guardar(antigua);
    }

    /**
     * Elimina una categoria
     * @param categoria a eliminar
     * @return True, si todo salio bien, false en caso contrario
     */
    public static boolean eliminar(CategoriaEntity categoria){
        List<ProductoEntity> productos = categoria.getProductos();
        for(ProductoEntity producto : productos ){
            ProductoController.eliminar(producto);
        }
        return categoria.delete();
    }

    /**
     * Adicionar un producto a una categoria
     * @param categoria a la que se adicionara el producto
     * @param producto Que se va a adicioanr
     */
    public static void adicionarProducto(CategoriaEntity categoria, ProductoEntity producto){
        categoria.getProductos().add(producto);
        guardar(categoria);
    }
}
