package controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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

public class ProductoController extends Controller {

    public static List darTodos(){
        return ProductoEntity.find.all();
    }

    public static ProductoEntity darProducto(int id){
        return ProductoEntity.find.ref(id);
    }

    public static boolean guardar(ProductoEntity producto){
        if(producto.validate() == null){
            producto.save();
            return true;
        }
        return false;
    }

    public static void adicionarSucursalAProducto(int idSucursal, ProductoEntity producto){
        SucursalEntity sucursal = SucursalController.darSucursal(idSucursal);
        producto.setSucursal(sucursal);
    }

    public static void adicionarCategoriaAProducto(int idCategoria, ProductoEntity producto){
        CategoriaEntity categoria = CategoriaController.darCategoria(idCategoria);
        producto.setCategoria(categoria);
    }

    public static boolean eliminar(ProductoEntity producto){
        producto.getCategoria().getProductos().remove(producto);
        producto.getSucursal().getProductos().remove(producto);
        return producto.delete();
    }

    public static Result subirFoto(MultipartFormData<File> body, int idProducto) throws IOException {
        File archivo = subir(body);
        if(archivo != null){
            Map resultados = alojarEnCloudDinary(archivo);
            resultados.put("id", idProducto);
            guardar(resultados);
            return ok("Foto subida");
        }
        return badRequest("El archivo para foto no es valido");
    }

    private static File subir(Http.MultipartFormData<File> body){
        FilePart<File> foto = body.getFiles().get(0);
        if (formatoValido(foto)){
            return  foto.getFile();
        }else {
            return null;
        }
    }

    private static boolean formatoValido(FilePart<File> archivo){
        String formato = archivo.getFilename().toLowerCase();
        return archivo != null && (formato.endsWith(".jpg") || formato.endsWith(".jpeg") || formato.endsWith(".gif") || formato.endsWith(".png") || formato.endsWith(".svg") || formato.endsWith(".bmp"));
    }

    private static Map alojarEnCloudDinary(File cancion) throws IOException {
        Map configuracion = new HashMap();
        configuracion.put("cloud_name","juandavid");
        configuracion.put("api_key","846846898798748");
        configuracion.put("api_secret","07y51L8pSLMbFq0IENcKarmZ1c4");
        Cloudinary cd = new Cloudinary(configuracion);
        return cd.uploader().upload(cancion, ObjectUtils.asMap("resource_type", "auto"));
    }

    private static void guardar(Map datos){
        ProductoEntity producto = ProductoController.darProducto(Integer.parseInt(datos.get("id").toString()));
        producto.setdUrlFoto(datos.get("url").toString());
        ProductoController.guardar(producto);
    }
}
