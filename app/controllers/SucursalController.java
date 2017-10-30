package controllers;

import Exceptions.ObjetoNoExisteException;
import models.*;
import play.mvc.*;

import java.util.Iterator;
import java.util.List;

/**
 * Clase que controla las acciones relaciones con las sucursales
 */
public class SucursalController extends Controller {

    /**
     * Devuelve todas las sucursales
     * @return  Todas las sucursales disponibles
     */
    public static List<SucursalEntity> darTodos(){
        return SucursalEntity.find.all();
    }

    /**
     * Da una sucursal dado un id
     * @param id de la sucursal
     * @return  Sucursal con el id dado
     * @throws ObjetoNoExisteException Si la sucursal con el id no existe
     */
    public static SucursalEntity darSucursal(int id) throws ObjetoNoExisteException {
        SucursalEntity sucursal = SucursalEntity.find.ref(id);
        if(sucursal.validate() == null){
            return sucursal;
        }else{
            throw new ObjetoNoExisteException("No existe la sucursal");
        }
    }

    /**
     * Guarda una sucursal
     * @param sucursal a guardar
     * @return true si las validaciones son correctas, false en caso contrarior.
     */
    public static boolean guardar(SucursalEntity sucursal){
        if(sucursal.validate() == null){
            sucursal.save();
            return true;
        }
        return false;
    }

    /**
     * Edita una sucursal
     * @param antiguo sucursal que se va a editar
     * @param nuevo sucursal de la cual se van a tomar los datos
     */
    public static void editar(SucursalEntity antiguo, SucursalEntity nuevo){
        antiguo.setdNombre(nuevo.getdNombre());
        antiguo.setaDireccion(nuevo.getaDireccion());
        guardar(antiguo);
    }

    /**
     * Elimina una sucursal
     * @param sucursal a eliminar
     * @return true si todo salio bien, false en caso contrario.
     */
    public static boolean eliminar(SucursalEntity sucursal){
        List<ProductoEntity> productos = sucursal.getProductos();
        Iterator<ProductoEntity> iterador = productos.iterator();
        while(iterador.hasNext()){
            ProductoEntity producto = iterador.next();
            ProductoController.eliminar(producto);
            iterador.remove();
        }
        return sucursal.delete();
    }

    /**
     * Adiciona un producto a una sucursal
     * @param sucursal a la que se le agregara el producto
     * @param producto producto que se va a agregar a la sucursal
     */
    public static void adicionarProducto(SucursalEntity sucursal, ProductoEntity producto){
        sucursal.getProductos().add(producto);
        guardar(sucursal);
    }
}
