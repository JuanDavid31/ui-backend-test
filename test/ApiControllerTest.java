import Exceptions.EntidadNoExisteException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.*;
import models.*;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.*;
import play.test.WithApplication;

import static junit.framework.TestCase.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class ApiControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testIndice(){
        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(GET)
                .uri("/");

        Result resultado = route(app, peticion);
        assertEquals(OK, resultado.status());
    }

    @Test
    public void testDarSucursales(){
        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(GET)
                .uri("/sucursales.json");

        Result resultado = route(app, peticion);
        assertEquals(OK, resultado.status());
    }

    @Test
    public void testDarSucursal(){
        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setdNombre("Test");
        sucursal.setaDireccion("Desde los test");

        SucursalController.guardar(sucursal);

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(GET)
                .uri("/sucursales.json/"+sucursal.getcId());

        Result resultado = route(app, peticion);
        SucursalController.eliminar(sucursal);
        assertEquals(OK, resultado.status());
    }

    @Test
    public void testAdicionarSucursal(){

        ObjectNode json = Json.newObject();
        json.put("nombre", "test");
        json.put("direccion", "test");

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(json)
                .uri("/adicionarSucursal");


        Result resultado = route(app, peticion);
        SucursalController.eliminar(SucursalController.darTodos().get(SucursalController.darTodos().size() - 1));
        assertEquals(OK, resultado.status());
    }

    @Test
    public void actualizarSucursalTest(){
        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setdNombre("Test");
        sucursal.setaDireccion("Desde los test");

        SucursalController.guardar(sucursal);

        ObjectNode json = Json.newObject();
        json.put("nombre", "test");
        json.put("direccion", "test");

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(PUT)
                .bodyJson(json)
                .uri("/actualizarSucursal/" + sucursal.getcId());

        Result resultado = route(app, peticion);
        SucursalController.eliminar(sucursal);
        assertEquals(OK, resultado.status());
    }

    @Test
    public void eliminarSucursalTest(){
        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setdNombre("Test");
        sucursal.setaDireccion("Desde los test");

        SucursalController.guardar(sucursal);

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(DELETE)
                .uri("/eliminarSucursal/" + sucursal.getcId());

        Result resultado = route(app, peticion);
        assertEquals(OK, resultado.status());
    }

    @Test
    public void darCategoriasTest(){
        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(GET)
                .uri("/categorias.json");

        Result resultado = route(app, peticion);
        assertEquals(OK, resultado.status());
    }

    @Test
    public void adicionarCategoriaTest(){
        ObjectNode json = Json.newObject();
        json.put("nombre", "test");

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(json)
                .uri("/adicionarCategoria");

        Result resultado = route(app, peticion);
        CategoriaController.eliminar(CategoriaController.darTodas().get(CategoriaController.darTodas().size() - 1));
        assertEquals(OK, resultado.status());
    }

    @Test
    public void actualizarCategoriaTest(){
        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setdNombre("Test");

        CategoriaController.guardar(categoria);

        ObjectNode json = Json.newObject();
        json.put("nombre", "test");

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(PUT)
                .bodyJson(json)
                .uri("/actualizarCategoria/" + categoria.getcId());

        Result resultado = route(app, peticion);
        CategoriaController.eliminar(categoria);
        assertEquals(OK, resultado.status());
    }

    @Test
    public void eliminarCategoriaTest(){
        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setdNombre("Test");

        CategoriaController.guardar(categoria);

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(DELETE)
                .uri("/eliminarCategoria/" + categoria.getcId());

        Result resultado = route(app, peticion);
        assertEquals(OK, resultado.status());
    }

    @Test
    public void darProductosPorSucursalTest(){
        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setdNombre("Test");
        sucursal.setaDireccion("Desde los test");

        SucursalController.guardar(sucursal);

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(GET)
                .uri("/productos.json/"+sucursal.getcId());

        Result resultado = route(app, peticion);
        SucursalController.eliminar(sucursal);
        assertEquals(OK, resultado.status());
    }

    @Test
    public void adicionarProductoTest(){
        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setdNombre("SucursalTest");
        sucursal.setaDireccion("Desde los test");

        SucursalController.guardar(sucursal);

        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setdNombre("Categoriatest");

        CategoriaController.guardar(categoria);

        ObjectNode json = Json.newObject();
        json.put("nombre", "Nombre test");
        json.put("fecha", "Fecha test");
        json.put("precio", 234);
        json.put("ingredientes", "Ingredientes test");

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(json)
                .uri("/adicionarProducto/"+sucursal.getcId() + "/" + categoria.getcId());

        Result resultado = route(app, peticion);
        SucursalController.eliminar(sucursal);
        CategoriaController.eliminar(categoria);
        assertEquals(OK, resultado.status());
    }

    @Test
    public void editarProductoConUrlTest() throws EntidadNoExisteException {
        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setdNombre("Test");
        sucursal.setaDireccion("Desde los test");

        SucursalController.guardar(sucursal);

        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setdNombre("test");

        CategoriaController.guardar(categoria);

        ProductoEntity producto = new ProductoEntity();
        producto.setdNombre("test");
        producto.setaIngredientes("test");
        producto.setfLimite("test");
        producto.setnPrecio(12345);

        ProductoController.adicionarSucursalAProducto(sucursal.getcId(), producto);
        ProductoController.adicionarCategoriaAProducto(categoria.getcId(), producto);
        ProductoController.guardar(producto);

        ObjectNode json = Json.newObject();
        json.put("url", "test.jpg");

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(PUT)
                .bodyJson(json)
                .uri("/editarProductoConUrl/" + producto.getcId());

        Result resultado = route(app, peticion);
        ProductoController.eliminar(producto);
        SucursalController.eliminar(sucursal);
        CategoriaController.eliminar(categoria);
        assertEquals(OK, resultado.status());
    }

    //TODO: Editar producto con archivo
/*    public void editarProductoConArchivoTest(){

    }*/


    public void eliminarProductoTest() throws EntidadNoExisteException {
        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setdNombre("Test");
        sucursal.setaDireccion("Desde los test");

        SucursalController.guardar(sucursal);

        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setdNombre("test");

        CategoriaController.guardar(categoria);

        ProductoEntity producto = new ProductoEntity();
        producto.setdNombre("test");
        producto.setaIngredientes("test");
        producto.setfLimite("test");
        producto.setnPrecio(12345);

        ProductoController.adicionarSucursalAProducto(sucursal.getcId(), producto);
        ProductoController.adicionarCategoriaAProducto(categoria.getcId(), producto);
        ProductoController.guardar(producto);

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(DELETE)
                .uri("/eliminarProducto/" + producto.getcId());

        Result resultado = route(app, peticion);
        SucursalController.eliminar(sucursal);
        CategoriaController.eliminar(categoria);
        assertEquals(OK, resultado.status());
    }

}
