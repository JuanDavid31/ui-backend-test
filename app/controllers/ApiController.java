package controllers;

import Exceptions.ObjetoNoExisteException;
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

public class ApiController extends Controller {

    public Result indice(){
        return ok(indice.render());
    }

    public Result darSucursales(){
        List<SucursalEntity> sucursales = SucursalController.darTodos();
        return ok(Json.toJson(sucursales));
    }

    public Result darSucursal(int id){
        SucursalEntity sucursal = null;
        try {
            sucursal = SucursalController.darSucursal(id);
        } catch (ObjetoNoExisteException e) {
            e.printStackTrace();
            return badRequest("Sucursal no existe");
        }
        return ok(Json.toJson(sucursal));
    }

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

    private SucursalEntity jsonAEntidadSucursal(JsonNode json, SucursalEntity sucursal){
        String nombre = json.findPath("nombre").textValue();
        String direccion = json.findPath("direccion").textValue();
        sucursal.setdNombre(nombre);
        sucursal.setaDireccion(direccion);
        return sucursal;
    }

    public Result actualizarSucursal(int id){ // De todas maneras tengo que validarla para asegurarme del mensaje
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
        } catch (ObjetoNoExisteException e) {
            e.printStackTrace();
            return badRequest("La sucursal no existe");
        }
        return badRequest(mensaje);
    }

    public Result eliminarSucursal(int id){
        SucursalEntity sucursal = null;
        try {
            sucursal = SucursalController.darSucursal(id);
            SucursalController.eliminar(sucursal);
        } catch (ObjetoNoExisteException e) {
            e.printStackTrace();
            return badRequest("La sucursal no existe");
        }
        return ok("Producto eliminado");
    }

    public Result darCategorias(){
        List<CategoriaEntity> categorias = CategoriaController.darTodas();
        return ok(Json.toJson(categorias));
    }

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

    public Result eliminarCategoria(int id){
        CategoriaEntity categoria = CategoriaController.darCategoria(id);
        if(categoria.delete()){
            return ok("Categoria eliminada");
        }
        return internalServerError("No se pudo eliminar la categoria, intentelo de nuevo.");
    }

    public Result darProductosPorSucursal(int id){ //Debo enviar el nombre de la categoria
        SucursalEntity sucursal = null;
        List<ProductoEntity> productos = null;
        try {
            sucursal = SucursalController.darSucursal(id);
            productos = sucursal.getProductos();
        } catch (ObjetoNoExisteException e) {
            e.printStackTrace();
            return badRequest("La sucursal no existe");
        }
        return ok(Json.toJson(productos));
    }

    public Result adicionarProducto(int idSucursal, int idCategoria){
        JsonNode json = request().body().asJson();
        ProductoEntity producto = jsonAEntidadProducto(json, new ProductoEntity());
        String mensaje = producto.validate();
        if(mensaje == null){
            try {
                ProductoController.adicionarSucursalAProducto(idSucursal, producto);
                ProductoController.adicionarCategoriaAProducto(idCategoria, producto);
                ProductoController.guardar(producto);
            } catch (ObjetoNoExisteException e) {
                e.printStackTrace();
                return badRequest("La sucursal no existe");
            }
            return ok("Producto adicionado correctamente");
        }
        return badRequest(mensaje);
    }

    private ProductoEntity jsonAEntidadProducto(JsonNode json, ProductoEntity producto){
        String nombre = json.findPath("nombre").textValue();
        String fecha = json.findPath("fecha").textValue();
        int precio = json.findPath("precio").asInt(); //Esto puede traer problemas!!!
        String ingredientes = json.findPath("ingredientes").textValue();
        producto.setdNombre(nombre);
        producto.setfLimite(fecha);
        producto.setnPrecio(precio);
        producto.setaIngredientes(ingredientes);
        return producto;
    }

    public Result editarProductoConUrl(int id){
        JsonNode json = request().body().asJson();
        return ProductoController.editarProductoConUrl(id, json);
    }

    public Result editarProductoConArchivo(int id){
        MultipartFormData<File> body = request().body().asMultipartFormData();
        try {
            return ProductoController.subirFoto(body, id);
        } catch (IOException e) {
            e.printStackTrace();
            return badRequest("La cancion no se pudo subir por un error en el servidor");
        } catch (ObjetoNoExisteException e) {
            e.printStackTrace();
            return badRequest("El producto no existe");
        }
    }

    public Result eliminarProducto(int id){
        ProductoEntity producto = null;
        try {
            producto = ProductoController.darProducto(id);
        } catch (ObjetoNoExisteException e) {
            return internalServerError("El producto no existe");
        }
        return ok("Se elimino el producto");
    }
}
