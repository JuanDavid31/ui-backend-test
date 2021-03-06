package controllers;

import Exceptions.EntidadNoExisteException;
import org.json.*;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.*;
import views.html.indice;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Clase principal que maneja todas las operaciones de la aplicación
 */
public class ApiController extends Controller {

    /**
     * Indice de la aplicación
     * @return Result con la pagina indice
     */
    public Result indice(){
        return ok(indice.render());
    }

    /**
     * Devuelve todas las sucursales en json
     * @return Result sucursales en formato json
     */
    public Result darSucursales(){
        List<SucursalEntity> sucursales = SucursalController.darTodos();
        response().setHeader(HeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "X-Total-Count");
        response().setHeader(HeaderNames.ACCESS_CONTROL_EXPOSE_HEADERS, "X-Total-Count");
        response().setHeader("X-Total-Count", Integer.toString(sucursales.size()));
        return ok(Json.toJson(sucursales));
    }

    /**
     * Devuelve una sucursal en json
     * @param id de la sucursal
     * @return Result con la sucursal en json
     */
    public Result darSucursal(int id){ //TODO: La sucursal tiene 0 productos pero se muestran como JSON, NO ENTIENDO :s
        SucursalEntity sucursal = null;
        try {
            sucursal = SucursalController.darSucursal(id);
        } catch (EntidadNoExisteException e) {
            e.printStackTrace();
            return badRequest("Sucursal no existe");
        }
        return ok(Json.toJson(sucursal));
    }

    /**
     * Adiciona una sucursal dada un apetición con json
     * @return Result con el estado de la operación
     */
    public Result adicionarSucursal(){
        JsonNode json = request().body().asJson();
        SucursalEntity sucursal = SucursalController.jsonAEntidadSucursal(json, new SucursalEntity());
        String mensaje = sucursal.validate();
        if(mensaje == null) {
            SucursalController.guardar(sucursal);
            return ok("Sucursal adicionado");
        }
        return badRequest(mensaje);
    }

    /**
     * Actualiza una sucursal dado un id
     * @param id de la sucursal a actualizar
     * @return Result con el estado de la operación
     */
    public Result actualizarSucursal(int id){
        JsonNode json = request().body().asJson();
        SucursalEntity sucursal = null;
        String mensaje = null;
        try {
            sucursal = SucursalController.jsonAEntidadSucursal(json, SucursalController.darSucursal(id));
            mensaje = sucursal.validate();
            if(mensaje == null) {
                SucursalController.guardar(sucursal);
                return ok("Sucursal editado");
            }
        } catch (EntidadNoExisteException e) {
            e.printStackTrace();
            return badRequest("La sucursal no existe");
        }
        return badRequest(mensaje);
    }

    /**
     * Elimina una sucursal dado un id
     * @param id de la sucursal a eliminar
     * @return Result con el estado de la operación
     */
    public Result eliminarSucursal(int id){
        SucursalEntity sucursal = null;
        try {
            sucursal = SucursalController.darSucursal(id);
            SucursalController.eliminar(sucursal);
        } catch (EntidadNoExisteException e) {
            e.printStackTrace();
            return badRequest("La sucursal no existe");
        }
        return ok("Sucursal eliminada");
    }

    /**
     * Devuelve todas las categorias en formato json
     * @return Result con las categorias en json
     */
    public Result darCategorias(){
        List<CategoriaEntity> categorias = CategoriaController.darTodas();
        return ok(Json.toJson(categorias));
    }

    /**
     * Adiciona una categoria dada una petición en formato json
     * @return Result con el estado de la operación
     */
    public Result adicionarCategoria(){
        JsonNode json = request().body().asJson();
        CategoriaEntity categoria = CategoriaController.jsonAEntidadCategoria(json, new CategoriaEntity());
        String mensaje = categoria.validate();
        if(mensaje == null) {
            CategoriaController.guardar(categoria);
            return ok("Categoria adicionado");
        }
        return badRequest(mensaje);
    }

    /**
     * Actualiza una categoria dado un id y una petición en json
     * @param id de la categoria a actualizar
     * @return Result con el estado de la operación
     */
    public Result actualizarCategoria(int id){
        JsonNode json = request().body().asJson();
        try {
            CategoriaEntity categoria = CategoriaController.jsonAEntidadCategoria(json, CategoriaController.darCategoria(id));
            String mensaje = categoria.validate();
            if(mensaje == null) {
                CategoriaController.guardar(categoria);
                return ok("Categoria guardada");
            }
            return badRequest(mensaje);
        } catch (EntidadNoExisteException e) {
            e.printStackTrace();
            return badRequest("La categoria no existe");
        }
    }

    /**
     * Elimina una categoria dado un id
     * @param id de la categoria a eliminar
     * @return Result con el estado de la operación
     */
    public Result eliminarCategoria(int id){
        CategoriaEntity categoria = null;
        try {
            categoria = CategoriaController.darCategoria(id);
            if(categoria.delete()) return ok("Categoria eliminada");
        } catch (EntidadNoExisteException e) {
            e.printStackTrace();
            return badRequest("La categoria no existe");
        }
        return internalServerError("No se pudo eliminar la categoria, intentelo de nuevo.");
    }

    /**
     * Devuelve los productos dado el id de la sucursal
     * @param id de la sucursal
     * @return Result con un json de los productos en caso exitoso,
     * en caso contrario se devolvera algun estado indicando el error
     */
    public Result darProductosPorSucursal(int id){
        SucursalEntity sucursal = null;
        List<ProductoEntity> productos = null;
        try {
            sucursal = SucursalController.darSucursal(id);
            productos = sucursal.getProductos();
        } catch (EntidadNoExisteException e) {
            e.printStackTrace();
            return badRequest("La sucursal no existe");
        }
        return ok(Json.toJson(productos));
    }

    /**
     * Adiciona un producto a una sucursal y a una categoria
     * @param idSucursal id de la sucursal
     * @param idCategoria ud de la categoria
     * @return Result con el estado de la operación
     */
    public Result adicionarProducto(int idSucursal, int idCategoria){
        JsonNode json = request().body().asJson();
        ProductoEntity producto = ProductoController.jsonAEntidadProducto(json, new ProductoEntity());
        String mensaje = producto.validate();
        if(mensaje == null){
            try {
                ProductoController.registrarProducto(idSucursal, idCategoria, producto);
            } catch (EntidadNoExisteException e) {
                e.printStackTrace();
                return badRequest("Alguna entidad no existe");
            }
            return ok("Producto adicionado correctamente");
        }
        return badRequest(mensaje);
    }

    /**
     * Edita la url del producto dado el id y la url en json
     * @param id del producto a editar
     * @return Result con el estado de la operación
     */
    public Result editarProductoConUrl(int id){
        JsonNode json = request().body().asJson();
        return ProductoController.editarProductoConUrl(id, json);
    }

    /**
     * Edita la url del producto dado el id y el archivo enviado en la petición
     * @param id del producto
     * @return Result con el estado de la operación
     */
    public Result editarProductoConArchivo(int id){
        MultipartFormData<File> body = request().body().asMultipartFormData();
        try {
            return ProductoController.subirFoto(body, id);
        } catch (IOException e) {
            e.printStackTrace();
            return badRequest("La cancion no se pudo subir por un error en el servidor");
        } catch (EntidadNoExisteException e) {
            e.printStackTrace();
            return badRequest("El producto no existe");
        }
    }

    //TODO: añadir documentación
    public Result agregarProducto(int idS){
        try {
            MultipartFormData<File> archivo = request().body().asMultipartFormData();
            Map<String, String[]> parteJSON = request().body().asMultipartFormData().asFormUrlEncoded();
            ProductoEntity producto = new ProductoEntity();
            String mensajeValidación = guardarOrgJSONDeEntidadProducto(parteJSON, producto, idS);
            if(mensajeValidación == null){
                ProductoController.subirFoto(archivo, producto.getId());
            }else{
                return badRequest(mensajeValidación);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return internalServerError("Algo salio mal con el JSON");
        } catch (EntidadNoExisteException e) {
            e.printStackTrace();
            return badRequest("Alguna entidad no existe");
        } catch (IOException e) {
            e.printStackTrace();
            return internalServerError("Alguna calamidad ocurrio con el archivo, intentelo de nuevo");
        }
        return ok("El producto fue agregado");
    }

    private String guardarOrgJSONDeEntidadProducto(Map<String, String[]> parteJSON, ProductoEntity producto, int idS) throws JSONException, EntidadNoExisteException {
        String[] strings = parteJSON.get("producto");
        JSONObject productoJSON = null;
        productoJSON = new JSONObject(strings[0]);
        String nombre = productoJSON.getString("nombre");
        int precio = productoJSON.getInt("precio");
        String ingredientes = productoJSON.getString("ingredientes");
        String fecha = productoJSON.getString("fecha");
        int idCategoria = productoJSON.getInt("iDCategoria");
        producto.setSucursal(SucursalController.darSucursal(idS));
        producto.setCategoria(CategoriaController.darCategoria(idCategoria));
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setFechaLimite(fecha);
        producto.setNombreCategoria(CategoriaController.darCategoria(idCategoria).getNombre());
        producto.setIngredientes(ingredientes);
        return ProductoController.guardar(producto);
    }

    /**
     * Elimina un producto dado el id
     * @param id del producto a eliminar
     * @return Result con el estado de la operación
     */
    public Result eliminarProducto(int id){
        ProductoEntity producto = null;
        try {
            producto = ProductoController.darProducto(id);
            ProductoController.eliminar(producto);
        } catch (EntidadNoExisteException e) {
            return internalServerError("El producto no existe");
        }
        return ok("Se elimino el producto");
    }
}
