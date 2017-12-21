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

    /**
     * Crea una applicación ficitica con la configuración existente
     * @return
     */
    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    /**
     * Prueba el indice de la api
     */
    @Test
    public void testIndice(){
        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(GET)
                .uri("/");

        Result resultado = route(app, peticion);
        assertEquals(OK, resultado.status());
    }

    /**
     * Prueba la ruta para el metodo darSucursales
     */
    @Test
    public void testDarSucursales(){
        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(GET)
                .uri("/sucursales.json");

        Result resultado = route(app, peticion);
        assertEquals(OK, resultado.status());
    }

    /**
     * Prueba la ruta para el metodo darSucursal
     */
    @Test
    public void testDarSucursal(){
        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setNombre("Test");
        sucursal.setDireccion("Desde los test");

        SucursalController.guardar(sucursal);

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(GET)
                .uri("/sucursales.json/"+sucursal.getid());

        Result resultado = route(app, peticion);
        SucursalController.eliminar(sucursal);
        assertEquals(OK, resultado.status());
    }

    /**
     * Prueba la ruta para el metodo adicionarSucursal
     */
    @Test
    public void testAdicionarSucursal(){

        ObjectNode json = Json.newObject();
        json.put("nombre", "test");
        json.put("direccion", "test");

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(json)
                .uri("/sucursales.json");


        Result resultado = route(app, peticion);
        SucursalController.eliminar(SucursalController.darTodos().get(SucursalController.darTodos().size() - 1));
        assertEquals(OK, resultado.status());
    }

    /**
     * Prueba la ruta para el metodo actualizarSucursal
     */
    @Test
    public void actualizarSucursalTest(){
        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setNombre("Test");
        sucursal.setDireccion("Desde los test");

        SucursalController.guardar(sucursal);

        ObjectNode json = Json.newObject();
        json.put("nombre", "test");
        json.put("direccion", "test");

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(PUT)
                .bodyJson(json)
                .uri("/sucursales.json/" + sucursal.getid());

        Result resultado = route(app, peticion);
        SucursalController.eliminar(sucursal);
        assertEquals(OK, resultado.status());
    }

    /**
     * Prueba la ruta para el metodo eliminarSucursal
     */
    @Test
    public void eliminarSucursalTest(){
        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setNombre("Test");
        sucursal.setDireccion("Desde los test");

        SucursalController.guardar(sucursal);

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(DELETE)
                .uri("/sucursales.json/" + sucursal.getid());

        Result resultado = route(app, peticion);
        assertEquals(OK, resultado.status());
    }

    /**
     * Prueba la ruta para el metodo darCategorias
     */
    @Test
    public void darCategoriasTest(){
        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(GET)
                .uri("/categorias.json");

        Result resultado = route(app, peticion);
        assertEquals(OK, resultado.status());
    }

    /**
     * Prueba la ruta para el metodo adicionarCategorias
     */
    @Test
    public void adicionarCategoriaTest(){
        ObjectNode json = Json.newObject();
        json.put("nombre", "test");

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(json)
                .uri("/categorias.json");

        Result resultado = route(app, peticion);
        CategoriaController.eliminar(CategoriaController.darTodas().get(CategoriaController.darTodas().size() - 1));
        assertEquals(OK, resultado.status());
    }

    /**
     * Prueba la ruta para el metodo acualiazar categoria
     */
    @Test
    public void actualizarCategoriaTest(){
        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setNombre("Test");

        CategoriaController.guardar(categoria);

        ObjectNode json = Json.newObject();
        json.put("nombre", "test");

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(PUT)
                .bodyJson(json)
                .uri("/categorias.json/" + categoria.getId());

        Result resultado = route(app, peticion);
        CategoriaController.eliminar(categoria);
        assertEquals(OK, resultado.status());
    }

    /**
     * Prueba la ruta para el metodo eliminarCategoria
     */
    @Test
    public void eliminarCategoriaTest(){
        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setNombre("Test");

        CategoriaController.guardar(categoria);

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(DELETE)
                .uri("/categorias.json/" + categoria.getId());

        Result resultado = route(app, peticion);
        assertEquals(OK, resultado.status());
    }

    /**
     * Prueba la ruta para el metodo darProductoPorSucursal
     */
    @Test
    public void darProductosPorSucursalTest(){
        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setNombre("Test");
        sucursal.setDireccion("Desde los test");

        SucursalController.guardar(sucursal);

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(GET)
                .uri("/productos.json/"+sucursal.getid());

        Result resultado = route(app, peticion);
        SucursalController.eliminar(sucursal);
        assertEquals(OK, resultado.status());
    }

    /**
     * Prueba la ruta para el metodo adicionarProducto
     */
    @Test
    public void adicionarProductoTest(){
        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setNombre("SucursalTest");
        sucursal.setDireccion("Desde los test");

        SucursalController.guardar(sucursal);

        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setNombre("Categoriatest");

        CategoriaController.guardar(categoria);

        ObjectNode json = Json.newObject();
        json.put("nombre", "Nombre test");
        json.put("fecha", "Fecha test");
        json.put("precio", 234);
        json.put("ingredientes", "Ingredientes test");

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(json)
                .uri("/productos.json/"+sucursal.getid() + "/" + categoria.getId());

        Result resultado = route(app, peticion);
        SucursalController.eliminar(sucursal);
        CategoriaController.eliminar(categoria);
        assertEquals(OK, resultado.status());
    }

    /**
     * Prueba la ruta para el metodo editarProductoConUrl
     */
    @Test
    public void editarProductoConUrlTest() throws EntidadNoExisteException {
        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setNombre("Test");
        sucursal.setDireccion("Desde los test");

        SucursalController.guardar(sucursal);

        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setNombre("test");

        CategoriaController.guardar(categoria);

        ProductoEntity producto = new ProductoEntity();
        producto.setNombre("test");
        producto.setIngredientes("test");
        producto.setFechaLimite("test");
        producto.setPrecio(12345);

        ProductoController.adicionarSucursalAProducto(sucursal.getid(), producto);
        ProductoController.adicionarCategoriaAProducto(categoria.getId(), producto);
        ProductoController.guardar(producto);

        ObjectNode json = Json.newObject();
        json.put("url", "test.jpg");

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(PUT)
                .bodyJson(json)
                .uri("/productos.json/" + producto.getId());

        Result resultado = route(app, peticion);
        ProductoController.eliminar(producto);
        SucursalController.eliminar(sucursal);
        CategoriaController.eliminar(categoria);
        assertEquals(OK, resultado.status());
    }

    //TODO: Editar producto con archivo
/*    public void editarProductoConArchivoTest(){

    }*/

    /**
     * Prueba la ruta para el metodo eliminarProducto
     */
    public void eliminarProductoTest() throws EntidadNoExisteException {
        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setNombre("Test");
        sucursal.setDireccion("Desde los test");

        SucursalController.guardar(sucursal);

        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setNombre("test");

        CategoriaController.guardar(categoria);

        ProductoEntity producto = new ProductoEntity();
        producto.setNombre("test");
        producto.setIngredientes("test");
        producto.setFechaLimite("test");
        producto.setPrecio(12345);

        ProductoController.adicionarSucursalAProducto(sucursal.getid(), producto);
        ProductoController.adicionarCategoriaAProducto(categoria.getId(), producto);
        ProductoController.guardar(producto);

        Http.RequestBuilder peticion = new Http.RequestBuilder()
                .method(DELETE)
                .uri("/productos.json/" + producto.getId());

        Result resultado = route(app, peticion);
        SucursalController.eliminar(sucursal);
        CategoriaController.eliminar(categoria);
        assertEquals(OK, resultado.status());
    }

}
