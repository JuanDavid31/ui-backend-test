import Exceptions.EntidadNoExisteException;
import com.google.common.collect.ImmutableMap;
import controllers.CategoriaController;
import models.*;
import org.junit.*;
import play.Application;
import play.db.*;
import play.db.evolutions.Evolutions;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

public class CategoriaControllerTest extends WithApplication{

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testCRUD(){

        int numeroAleatorio = ThreadLocalRandom.current().nextInt(100, 900);
        int numeroAleatorio2 = ThreadLocalRandom.current().nextInt(100, 900);

        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setcId(numeroAleatorio);
        categoria.setdNombre("nombre");
        CategoriaController.guardar(categoria);

        CategoriaEntity categoria1 = new CategoriaEntity();
        categoria1.setcId(numeroAleatorio2);
        categoria1.setdNombre("nombre1");

        try {
            assertEquals("No guardo",categoria, CategoriaController.darCategoria(numeroAleatorio));
        } catch (EntidadNoExisteException e) {
            e.printStackTrace();
            System.out.println("No debería pasar por aquí");
        }

        CategoriaController.editar(categoria, categoria1);
        assertEquals("No edito nombre",categoria.getdNombre(),categoria1.getdNombre());

        assertEquals("No borro", CategoriaController.eliminar(categoria), true);

    }
}
