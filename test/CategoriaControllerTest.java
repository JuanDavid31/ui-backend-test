import com.google.common.collect.ImmutableMap;
import controllers.CategoriaController;
import models.*;
import org.junit.*;
import play.db.*;
import play.db.evolutions.Evolutions;

import static org.junit.Assert.assertEquals;

public class CategoriaControllerTest {

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

        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setcId(800);
        categoria.setdNombre("nombre");
        categoria.save();

        CategoriaEntity categoria1 = new CategoriaEntity();
        categoria1.setcId(900);
        categoria1.setdNombre("nombre1");

        assertEquals("No guardo",categoria, CategoriaController.darCategoria(800));

        CategoriaController.editar(categoria, categoria1);
        assertEquals("No edito nombre",categoria.getdNombre(),categoria1.getdNombre());

        assertEquals("No borro",categoria.delete(), false);

    }
}
