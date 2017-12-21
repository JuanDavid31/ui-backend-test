package controllers;

import Exceptions.EntidadNoExisteException;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.mvc.*;

import java.util.*;

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
     * @throws EntidadNoExisteException Si la sucursal con el id no existe
     */
    public static SucursalEntity darSucursal(int id) throws EntidadNoExisteException {
        SucursalEntity sucursal = SucursalEntity.find.ref(id);
        if(sucursal.validate() == null){
            return sucursal;
        }else{
            throw new EntidadNoExisteException("No existe la sucursal");
        }
    }

    /**
     * Guarda una sucursal
     * @param sucursal a guardar
     * @return true si las validaciones son correctas, false en caso contrarior.
     */
    public static String guardar(SucursalEntity sucursal){
        String mensaje = sucursal.validate();
        if(mensaje == null){
            sucursal.save();
            return null;
        }
        return mensaje;
    }

    /**
     * Edita una sucursal
     * @param antiguo sucursal que se va a editar
     * @param nuevo sucursal de la cual se van a tomar los datos
     */
    public static void editar(SucursalEntity antiguo, SucursalEntity nuevo){
        antiguo.setNombre(nuevo.getNombre());
        antiguo.setDireccion(nuevo.getDireccion());
        guardar(antiguo);
    }

    /**
     * Elimina una sucursal
     * @param sucursal a eliminar
     * @return true si todo salio bien, false en caso contrario.
     */
    public static boolean eliminar(SucursalEntity sucursal){
        return sucursal.delete();
    }


    /**
     * Mapea json a una entidad
     * @param json que contiene los datos
     * @param sucursal en la que se va a mapear
     * @return SucursalEntity mapeada
     */
    public static SucursalEntity jsonAEntidadSucursal(JsonNode json, SucursalEntity sucursal){
        String nombre = json.findPath("nombre").textValue();
        String direccion = json.findPath("direccion").textValue();
        sucursal.setNombre(nombre);
        sucursal.setDireccion(direccion);
        return sucursal;
    }
}
