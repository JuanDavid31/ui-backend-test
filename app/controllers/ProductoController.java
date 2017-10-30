package controllers;

import Exceptions.ObjetoNoExisteException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.JsonNode;
import models.CategoriaEntity;
import models.ProductoEntity;
import models.SucursalEntity;
import play.mvc.*;
import play.mvc.Http.*;
import play.mvc.Http.MultipartFormData.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @throws ObjetoNoExisteException Si el producto no existe
     */
    public static ProductoEntity darProducto(int id) throws ObjetoNoExisteException {
        ProductoEntity producto = ProductoEntity.find.ref(id);
        if(producto.validate() == null){
            return producto;
        }else{
            throw new ObjetoNoExisteException("El producto no existe");
        }
    }

    /**
     * Guarda un producto dado
     * @param producto dado
     * @return True si todo salio bien al guarda, false en caso contrarior
     */
    public static boolean guardar(ProductoEntity producto){
        if(producto.validate() == null){
            producto.save();
            return true;
        }
        return false;
    }

    /**
     * Relaciona una sucursal dado un producto
     * @param idSucursal el id de la sucursal a relacionar
     * @param producto producto al que se le relacionara la sucursal
     * @throws ObjetoNoExisteException si la sucursal no existe
     */
    public static void adicionarSucursalAProducto(int idSucursal, ProductoEntity producto) throws ObjetoNoExisteException {
        SucursalEntity sucursal = SucursalController.darSucursal(idSucursal);
        producto.setSucursal(sucursal);
    }

    /**
     * Relaciona una categoria dado un producto
     * @param idCategoria id de la categoria a relacionar
     * @param producto producto al que se le relacionara la categoria
     */
    public static void adicionarCategoriaAProducto(int idCategoria, ProductoEntity producto){
        CategoriaEntity categoria = CategoriaController.darCategoria(idCategoria);
        producto.setCategoria(categoria);
        producto.setdNombreCategoria(categoria.getdNombre());
    }

    /**
     * Elimina un producto
     * @param producto a eliminar
     * @return True si todo salio bien, false en caso contrario
     */
    public static boolean eliminar(ProductoEntity producto){
        producto.getCategoria().getProductos().remove(producto);
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
            producto.setdUrlFoto(url);
            resultado = productoConUrlValido(producto);
        } catch (ObjetoNoExisteException e) {
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
        if(producto.getdUrlFoto() != null && (producto.getdUrlFoto().endsWith(".jpg") || producto.getdUrlFoto().endsWith(".jpeg") || producto.getdUrlFoto().endsWith(".gif") || producto.getdUrlFoto().endsWith(".png") || producto.getdUrlFoto().endsWith(".svg") || producto.getdUrlFoto().endsWith(".bmp"))){
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
     * @throws ObjetoNoExisteException Si el producto no existe
     */
    public static Result subirFoto(MultipartFormData<File> body, int idProducto) throws IOException, ObjetoNoExisteException {
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
     * @throws ObjetoNoExisteException si el producto no existe
     */
    private static void guardar(Map datos) throws ObjetoNoExisteException {
        ProductoEntity producto = ProductoController.darProducto(Integer.parseInt(datos.get("id").toString()));
        producto.setdUrlFoto(datos.get("url").toString());
        ProductoController.guardar(producto);
    }
}
