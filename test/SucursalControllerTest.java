import Exceptions.EntidadNoExisteException;
import controllers.SucursalController;
import models.SucursalEntity;
import org.junit.*;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

public class SucursalControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testCRUD(){ //TODO: número aleatorio

        int numeroAleatorio = ThreadLocalRandom.current().nextInt(100, 900);
        int numeroAleatorio2 = ThreadLocalRandom.current().nextInt(100, 900);

        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setcId(numeroAleatorio);
        sucursal.setaDireccion("dir");
        sucursal.setdNombre("nombre");
        SucursalController.guardar(sucursal);

        SucursalEntity sucursal1 = new SucursalEntity();
        sucursal1.setcId(numeroAleatorio2);
        sucursal1.setaDireccion("dir1");
        sucursal1.setdNombre("nombre1");

        try {
            assertEquals("No guardo",sucursal, SucursalController.darSucursal(numeroAleatorio));
        } catch (EntidadNoExisteException e) {
            System.out.println("No debería pasar por aquí");
        }

        SucursalController.editar(sucursal, sucursal1);
        assertEquals("No edito nombre", sucursal.getdNombre(),sucursal1.getdNombre());
        assertEquals("No edito direccion", sucursal.getaDireccion(),sucursal1.getaDireccion());
        assertEquals("No borro", SucursalController.eliminar( sucursal), true);

    }
}
