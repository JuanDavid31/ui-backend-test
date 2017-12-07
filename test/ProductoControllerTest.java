import Exceptions.EntidadNoExisteException;
import akka.http.impl.util.JavaMapping;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Source;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import controllers.CategoriaController;
import controllers.ProductoController;
import controllers.SucursalController;
import models.CategoriaEntity;
import models.ProductoEntity;
import models.SucursalEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;

import java.io.File;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import static org.apache.commons.io.FileUtils.getFile;
import static org.junit.Assert.assertEquals;

public class ProductoControllerTest extends WithApplication{

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testCRUD(){

        try {
            int numeroAleatorio = ThreadLocalRandom.current().nextInt(100, 900);
            
            SucursalEntity sucursal = new SucursalEntity();
            sucursal.setcId(numeroAleatorio);
            sucursal.setaDireccion("dir");
            sucursal.setdNombre("nombre");
            SucursalController.guardar(sucursal);

            CategoriaEntity categoria = new CategoriaEntity();
            categoria.setcId(numeroAleatorio);
            categoria.setdNombre("nombre");
            CategoriaController.guardar(categoria);


            ProductoEntity producto = new ProductoEntity();
            producto.setcId(numeroAleatorio);
            producto.setdNombre("nombre");
            producto.setnPrecio(200);
            producto.setaIngredientes("ingrediente");
            producto.setdNombreCategoria("categoria");
            producto.setdUrlFoto("url.jpg");
            producto.setfLimite("fecha");

            ProductoController.adicionarSucursalAProducto(numeroAleatorio,producto);
            ProductoController.adicionarCategoriaAProducto(numeroAleatorio,producto);

            ProductoController.guardar(producto);

            assertEquals("No guardo",producto, ProductoController.darProducto(numeroAleatorio));

            ObjectNode json = Json.newObject();
            json.put("url", "urlTEST.jpg");

            ProductoController.editarProductoConUrl(numeroAleatorio,json);

            assertEquals("No edito la url", "urlTEST.jpg",ProductoController.darProducto(numeroAleatorio).getdUrlFoto());

            assertEquals("No elimino", true, ProductoController.eliminar(producto));

            ProductoController.eliminar(producto);
            SucursalController.eliminar(sucursal);
        } catch (EntidadNoExisteException e) {
            System.out.println("No debería pasar por aquí");
        }
    }
}
