package controllers;

import Exceptions.EntidadNoExisteException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.mvc.*;
import play.mvc.Http.*;
import play.mvc.Http.MultipartFormData.*;

import java.io.*;
import java.util.*;

/**
 * Clase que ealiza todas las acciones relacionadas con los productos
 */
public class ProductoController extends Controller {

    /**
     * Devuelve todos los productos
     * @return Todos los productos disponibles
     */
    public static List darTodos(){
        return ProductoEntity.find.all();
    }

    /**
     * Devuelve un producto
     * @param id del producto
     * @return  El producto existente
     * @throws EntidadNoExisteException Si el producto no existe
     */
    public static ProductoEntity darProducto(int id) throws EntidadNoExisteException {
        ProductoEntity producto = ProductoEntity.find.ref(id);
        if(producto.validate() == null){
            return producto;
        }else{
            throw new EntidadNoExisteException("El producto no existe");
        }
    }

    /**
     * Guarda un producto dado
     * @param producto dado
     * @return mensaje null si no hay error de validación, un mensaje
     * con el error en caso contrario
     */
    public static String guardar(ProductoEntity producto){
        String mensaje = producto.validate();
        if(mensaje == null){
            producto.save();
            return null;
        }
        return mensaje;
    }

    /**
     * Relaciona una sucursal dado un producto
     * @param idSucursal el id de la sucursal a relacionar
     * @param producto producto al que se le relacionara la sucursal
     * @throws EntidadNoExisteException si la sucursal no existe
     */
    public static void adicionarSucursalAProducto(int idSucursal, ProductoEntity producto) throws EntidadNoExisteException {
        SucursalEntity sucursal = SucursalController.darSucursal(idSucursal);
        producto.setSucursal(sucursal);
    }

    /**
     * Relaciona una categoria dado un producto
     * @param idCategoria id de la categoria a relacionar
     * @param producto producto al que se le relacionara la categoria
     */
    public static void adicionarCategoriaAProducto(int idCategoria, ProductoEntity producto) throws EntidadNoExisteException {
        CategoriaEntity categoria = CategoriaController.darCategoria(idCategoria);
        producto.setCategoria(categoria);
        producto.setNombreCategoria(categoria.getNombre());
    }

    /**
     * Elimina un producto
     * @param producto a eliminar
     * @return True si todo salio bien, false en caso contrario
     */
    public static boolean eliminar(ProductoEntity producto){
        return producto.delete();
    }

    /**
     * Editar la url del producto
     * @param id del producto
     * @param json que contiene la url nueva
     * @return Result con el estado de la operación
     */
    public static Result editarProductoConUrl(int id, JsonNode json){
        ProductoEntity producto = null;
        Result resultado = null;
        try {
            producto = ProductoController.darProducto(id);
            String url = json.findPath("url").textValue();
            producto.setUrlFoto(url);
            resultado = productoConUrlValido(producto);
        } catch (EntidadNoExisteException e) {
            e.printStackTrace();
            return badRequest("El producto no existe");
        }
        return resultado;
    }

    /**
     * Verifica si la url es valida para el producto
     * @param producto al que se le verificara la url
     * @return Result con el estado de la operación
     */
    private static Result productoConUrlValido(ProductoEntity producto){
        if(producto.urlValida()){
            ProductoController.guardar(producto);
            return ok("Producto editado");
        }
        return badRequest("Formato incorrecto");
    }

    /**
     * Sube una foto y la añade a un producto
     * @param body Archivo de la canción
     * @param idProducto id del producto
     * @return Result con el estado de la operación
     * @throws IOException Si hay un error en la lectura del archivo
     * @throws EntidadNoExisteException Si el producto no existe
     */
    public static Result subirFoto(MultipartFormData<File> body, int idProducto) throws IOException, EntidadNoExisteException {
        File archivo = subir(body);
        if(archivo != null){
            Map resultados = alojarEnCloudDinary(archivo);
            resultados.put("id", idProducto);
            guardar(resultados);
            return ok("Foto subida");
        }
        return badRequest("El archivo para foto no es valido");
    }

    /**
     * Sube una canción
     * @param body archivo de la canción
     * @return File de la canción
     */
    private static File subir(Http.MultipartFormData<File> body){
        FilePart<File> foto = body.getFiles().get(0);
        if (formatoValido(foto)){
            return  foto.getFile();
        }else {
            return null;
        }
    }

    /**
     * Verifica si el formato del archivo es valido
     * @param archivo al que se le verificara el formato
     * @return True si el formato es valido, false en caso contrario
     */
    private static boolean formatoValido(FilePart<File> archivo){
        String formato = archivo.getFilename().toLowerCase();
        return archivo != null && (formato.endsWith(".jpg") || formato.endsWith(".jpeg") || formato.endsWith(".gif") || formato.endsWith(".png") || formato.endsWith(".svg") || formato.endsWith(".bmp"));
    }

    /**
     * Aloja la canción en cloudDinary
     * @param cancion a alojar
     * @return HashMap con los datos de la subida
     * @throws IOException Si ocurre un error de lectura
     */
    private static Map alojarEnCloudDinary(File cancion) throws IOException {
        Map configuracion = new HashMap();
        configuracion.put("cloud_name","juandavid");
        configuracion.put("api_key","846846898798748");
        configuracion.put("api_secret","07y51L8pSLMbFq0IENcKarmZ1c4");
        Cloudinary cd = new Cloudinary(configuracion);
        return cd.uploader().upload(cancion, ObjectUtils.asMap("resource_type", "auto"));
    }

    /**
     * Guarda el producto con los resultados de la subida
     * @param datos de la subida
     * @throws EntidadNoExisteException si el producto no existe
     */
    private static void guardar(Map datos) throws EntidadNoExisteException {
        ProductoEntity producto = ProductoController.darProducto(Integer.parseInt(datos.get("id").toString()));
        producto.setUrlFoto(datos.get("url").toString());
        ProductoController.guardar(producto);
    }

    /**
     * Registra el producto en la sucursal y categoria dada
     * @param idSucursal id de la sucursal
     * @param idCategoria id de la categoria
     * @param producto que se va a registrar
     * @throws EntidadNoExisteException Si alguna de las entidades no existe
     */
    public static void registrarProducto(int idSucursal, int idCategoria, ProductoEntity producto) throws EntidadNoExisteException {
        adicionarSucursalAProducto(idSucursal, producto);
        adicionarCategoriaAProducto(idCategoria, producto);
        guardar(producto);
    }

    /**
     * Mapea una entidad con json dado
     * @param json con los datos a mapear
     * @param producto en el que se mapearan los datos
     * @return ProductoEntity mapeado
     */
    public static ProductoEntity jsonAEntidadProducto(JsonNode json, ProductoEntity producto){
        String nombre = json.findPath("nombre").textValue();
        String fecha = json.findPath("fecha").textValue();
        int precio = json.findPath("precio").asInt();
        String ingredientes = json.findPath("ingredientes").textValue();
        producto.setNombre(nombre);
        producto.setFechaLimite(fecha);
        producto.setPrecio(precio);
        producto.setIngredientes(ingredientes);
        return producto;
    }
}