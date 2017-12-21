import Exceptions.EntidadNoExisteException;
import controllers.CategoriaController;
import models.*;
import org.junit.*;
import play.Application;
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
        categoria.setId(numeroAleatorio);
        categoria.setNombre("nombre");
        CategoriaController.guardar(categoria);

        CategoriaEntity categoria1 = new CategoriaEntity();
        categoria1.setId(numeroAleatorio2);
        categoria1.setNombre("nombre1");

        try {
            assertEquals("No guardo",categoria, CategoriaController.darCategoria(numeroAleatorio));
        } catch (EntidadNoExisteException e) {
            e.printStackTrace();
            System.out.println("No debería pasar por aquí");
        }

        CategoriaController.editar(categoria, categoria1);
        assertEquals("No edito nombre",categoria.getNombre(),categoria1.getNombre());

        assertEquals("No borro", CategoriaController.eliminar(categoria), true);

    }
}
