package controllers;

import Exceptions.EntidadNoExisteException;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.mvc.*;

import java.util.Iterator;
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
    public static CategoriaEntity darCategoria(int id) throws EntidadNoExisteException{
        CategoriaEntity categoria = CategoriaEntity.find.ref(id);
        if(categoria.validate() == null){
            return categoria;
        }else{
            throw new EntidadNoExisteException("No existe la categoria");
        }
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
        return categoria.delete();
    }

    /**
     * Mapea JSON a una entidad
     * @param json que va a ser mapeado
     * @param categoria en la que se va a mapear el JSON
     * @return categoria mapeada
     */
    public static CategoriaEntity jsonAEntidadCategoria(JsonNode json, CategoriaEntity categoria){
        String nombre = json.findPath("nombre").textValue();
        categoria.setdNombre(nombre);
        return categoria;
    }
}
