package controllers;

import Exceptions.EntidadNoExisteException;
import com.fasterxml.jackson.databind.JsonNode;
import models.CategoriaEntity;
import models.ProductoEntity;
import models.SucursalEntity;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.*;
import views.html.indice;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
        return ok(Json.toJson(sucursales));
    }

    /**
     * Devuelve una sucursal en json
     * @param id de la sucursal
     * @return Result con la sucursal en json
     */
    public Result darSucursal(int id){
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
        SucursalEntity sucursal = jsonAEntidadSucursal(json, new SucursalEntity());
        String mensaje = sucursal.validate();
        if(mensaje == null) {
            SucursalController.guardar(sucursal);
            return ok("Sucursal adicionado");
        }
        return badRequest(mensaje);
    }

    /**
     * Mapea json a una entidad
     * @param json que contiene los datos
     * @param sucursal en la que se va a mapear
     * @return SucursalEntity mapeada
     */
    private SucursalEntity jsonAEntidadSucursal(JsonNode json, SucursalEntity sucursal){
        String nombre = json.findPath("nombre").textValue();
        String direccion = json.findPath("direccion").textValue();
        sucursal.setdNombre(nombre);
        sucursal.setaDireccion(direccion);
        return sucursal;
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
            sucursal = jsonAEntidadSucursal(json, SucursalController.darSucursal(id));
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
        return ok("Producto eliminado");
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
        String nombre = json.findPath("nombre").textValue();
        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setdNombre(nombre);
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
        String nombre = json.findPath("nombre").textValue();
        CategoriaEntity categoria = CategoriaController.darCategoria(id);
        categoria.setdNombre(nombre);
        String mensaje = categoria.validate();
        if(mensaje == null) {
            CategoriaController.guardar(categoria); 
            return ok("Categoria guardada");
        }
        return badRequest(mensaje);
    }

    /**
     * Elimina una categoria dado un id
     * @param id de la categoria a eliminar
     * @return Result con el estado de la operación
     */
    public Result eliminarCategoria(int id){
        CategoriaEntity categoria = CategoriaController.darCategoria(id);
        if(categoria.delete()){
            return ok("Categoria eliminada");
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
        ProductoEntity producto = jsonAEntidadProducto(json, new ProductoEntity());
        String mensaje = producto.validate();
        if(mensaje == null){
            try {
                ProductoController.adicionarSucursalAProducto(idSucursal, producto);
                ProductoController.adicionarCategoriaAProducto(idCategoria, producto);
                ProductoController.guardar(producto);
            } catch (EntidadNoExisteException e) {
                e.printStackTrace();
                return badRequest("La sucursal no existe");
            }
            return ok("Producto adicionado correctamente");
        }
        return badRequest(mensaje);
    }

    /**
     * Mapea una entidad con json dado
     * @param json con los datos a mapear
     * @param producto en el que se mapearan los datos
     * @return ProductoEntity mapeado
     */
    private ProductoEntity jsonAEntidadProducto(JsonNode json, ProductoEntity producto){
        String nombre = json.findPath("nombre").textValue();
        String fecha = json.findPath("fecha").textValue();
        int precio = json.findPath("precio").asInt();
        String ingredientes = json.findPath("ingredientes").textValue();
        producto.setdNombre(nombre);
        producto.setfLimite(fecha);
        producto.setnPrecio(precio);
        producto.setaIngredientes(ingredientes);
        return producto;
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
