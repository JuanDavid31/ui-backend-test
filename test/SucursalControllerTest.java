import Exceptions.EntidadNoExisteException;
import com.google.common.collect.ImmutableMap;
import controllers.SucursalController;
import models.SucursalEntity;
import org.junit.*;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;

import static org.junit.Assert.*;

public class SucursalControllerTest {

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

        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setcId(800);
        sucursal.setaDireccion("dir");
        sucursal.setdNombre("nombre");
        sucursal.save();

        SucursalEntity sucursal1 = new SucursalEntity();
        sucursal1.setcId(900);
        sucursal1.setaDireccion("dir1");
        sucursal1.setdNombre("nombre1");

        try {
            assertEquals("No guardo",sucursal, SucursalController.darSucursal(800));
        } catch (EntidadNoExisteException e) {
            System.out.println("No debería pasar por aquí");
        }

        SucursalController.editar(sucursal, sucursal1);
        assertEquals("No edito nombre",sucursal.getdNombre(),sucursal1.getdNombre());
        assertEquals("No edito direccion",sucursal.getaDireccion(),sucursal1.getaDireccion());

        assertEquals("No borro",sucursal.delete(), false);

    }
}
