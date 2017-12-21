import Exceptions.EntidadNoExisteException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.CategoriaController;
import controllers.ProductoController;
import controllers.SucursalController;
import models.CategoriaEntity;
import models.ProductoEntity;
import models.SucursalEntity;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.test.WithApplication;

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
            sucursal.setid(numeroAleatorio);
            sucursal.setDireccion("dir");
            sucursal.setNombre("nombre");
            SucursalController.guardar(sucursal);

            CategoriaEntity categoria = new CategoriaEntity();
            categoria.setId(numeroAleatorio);
            categoria.setNombre("nombre");
            CategoriaController.guardar(categoria);


            ProductoEntity producto = new ProductoEntity();
            producto.setId(numeroAleatorio);
            producto.setNombre("nombre");
            producto.setPrecio(200);
            producto.setIngredientes("ingrediente");
            producto.setNombreCategoria("categoria");
            producto.setUrlFoto("url.jpg");
            producto.setFechaLimite("fecha");

            ProductoController.adicionarSucursalAProducto(numeroAleatorio,producto);
            ProductoController.adicionarCategoriaAProducto(numeroAleatorio,producto);

            ProductoController.guardar(producto);

            assertEquals("No guardo",producto, ProductoController.darProducto(numeroAleatorio));

            ObjectNode json = Json.newObject();
            json.put("url", "urlTEST.jpg");

            ProductoController.editarProductoConUrl(numeroAleatorio,json);

            assertEquals("No edito la url", "urlTEST.jpg",ProductoController.darProducto(numeroAleatorio).getUrlFoto());

            assertEquals("No elimino", true, ProductoController.eliminar(producto));

            ProductoController.eliminar(producto);
            SucursalController.eliminar(sucursal);
        } catch (EntidadNoExisteException e) {
            System.out.println("No debería pasar por aquí");
        }
    }
}
