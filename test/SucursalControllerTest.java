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
    public void testCRUD(){

        int numeroAleatorio = ThreadLocalRandom.current().nextInt(100, 900);
        int numeroAleatorio2 = ThreadLocalRandom.current().nextInt(100, 900);

        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setid(numeroAleatorio);
        sucursal.setDireccion("dir");
        sucursal.setNombre("nombre");
        SucursalController.guardar(sucursal);

        SucursalEntity sucursal1 = new SucursalEntity();
        sucursal1.setid(numeroAleatorio2);
        sucursal1.setDireccion("dir1");
        sucursal1.setNombre("nombre1");

        try {
            assertEquals("No guardo",sucursal, SucursalController.darSucursal(numeroAleatorio));
        } catch (EntidadNoExisteException e) {
            System.out.println("No debería pasar por aquí");
        }

        SucursalController.editar(sucursal, sucursal1);
        assertEquals("No edito nombre", sucursal.getNombre(),sucursal1.getNombre());
        assertEquals("No edito direccion", sucursal.getDireccion(),sucursal1.getDireccion());
        assertEquals("No borro", SucursalController.eliminar( sucursal), true);

    }
}
