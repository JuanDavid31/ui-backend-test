import Exceptions.EntidadNoExisteException;
import akka.http.impl.util.JavaMapping;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Source;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import controllers.ProductoController;
import models.CategoriaEntity;
import models.ProductoEntity;
import models.SucursalEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import java.io.File;
import java.util.Collections;

import static org.apache.commons.io.FileUtils.getFile;
import static org.junit.Assert.assertEquals;

public class ProductoControllerTest {

    Database database;

    @Before
    public void createDatabase() {
        database = Databases.createFrom(
                "org.postgresql.Driver",
                "jdbc:postgresql://localhost:5432/playdb",
                ImmutableMap.of(
                        "db.default.username", "juan",
                        "password", "12345"
                )
        );
        Evolutions.applyEvolutions(database);
    }

    @After
    public void shutdownDatabase() {
        Evolutions.cleanupEvolutions(database);
        database.shutdown();
    }


    @Test
    public void testCRUD(){

        try {
            SucursalEntity sucursal = new SucursalEntity();
            sucursal.setcId(800);
            sucursal.setaDireccion("dir");
            sucursal.setdNombre("nombre");
            sucursal.save();

            CategoriaEntity categoria = new CategoriaEntity();
            categoria.setcId(800);
            categoria.setdNombre("nombre");
            categoria.save();


            ProductoEntity producto = new ProductoEntity();
            producto.setcId(800);
            producto.setdNombre("nombre");
            producto.setnPrecio(100);
            producto.setaIngredientes("ingrediente");
            producto.setdNombreCategoria("categoria");
            producto.setdUrlFoto("url");
            producto.setfLimite("fecha");

            ProductoController.adicionarSucursalAProducto(800,producto);
            ProductoController.adicionarCategoriaAProducto(800,producto);

            ProductoController.guardar(producto);

            assertEquals("No guardo",producto, ProductoController.darProducto(800));

            JsonNode json = Json.parse("{\\\"url\\\":\\\"url1}");

            ProductoController.editarProductoConUrl(producto.getcId(),json);

            assertEquals("url1",producto.getdUrlFoto());

            assertEquals("No elimino", true, ProductoController.eliminar(producto));
        } catch (EntidadNoExisteException e) {
            System.out.println("No debería pasar por aquí");
        }
    }
}
