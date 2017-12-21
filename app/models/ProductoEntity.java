package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.ebean.*;
import io.ebean.annotation.NotNull;
import play.data.format.Formats.*;
import play.data.validation.Constraints.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Clase que modela los productos
 */
@Entity
@Table(name = "producto")
public class ProductoEntity extends Model implements Validatable<String> {

    /**
     * id del producto
     */
    @Id
    private int id;

    /**
     * Nombre del producto
     */
    @NonEmpty
    @NotNull
    @Size(min = 3)
    private String nombre;

    /**
     * Url donde se encuentra alojada la foto
     */
    private String urlFoto;

    /**
     * Nombre de la categoria a la que pertenece el producto
     */
    private String nombreCategoria;

    /**
     * Fecha limite de disponibilidad del producto
     */
    @NonEmpty
    @NotNull @Size(min = 3)
    private String fechaLimite;

    /**
     * Precio del producto
     */
    @NotNull
    private int precio;

    /**
     * Ingredientes del producto separados por comas (,)
     */
    @NonEmpty
    @NotNull @Size(min = 3)
    private String ingredientes;

    /**
     * Sucursal a la que pertenece el producto
     */
    @JsonBackReference
    private SucursalEntity sucursal;

    /**
     * Categoria a la que pertenece el producto
     */
    @JsonBackReference
    private CategoriaEntity categoria;

    /**
     * Atributo utilizado para consultas relacionadas con la entidad
     */
    public static final Finder<Integer, ProductoEntity> find = new Finder<>(ProductoEntity.class);

    @Column(name = "c_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "d_nombre", unique = true)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Column(name = "d_url_foto")
    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    @Column(name = "d_nombre_categoria")
    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    @Column(name = "f_limite")
    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    @Column(name = "n_precio")
    public double getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    @Column(name = "a_ingredientes")
    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    @ManyToOne(optional = false) @JoinColumn(name = "c_id_sucursal")
    public SucursalEntity getSucursal() {
        return sucursal;
    }

    public void setSucursal(SucursalEntity sucursal) {
        this.sucursal = sucursal;
    }

    @ManyToOne(optional = false) @JoinColumn(name = "c_id_categoria")
    public CategoriaEntity getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEntity categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "ProductoEntity{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", urlFoto='" + urlFoto + '\'' +
                ", nombreCategoria='" + nombreCategoria + '\'' +
                ", fechaLimite='" + fechaLimite + '\'' +
                ", precio=" + precio +
                ", ingredientes='" + ingredientes + '\'' +
                ", sucursal=" + sucursal +
                ", categoria=" + categoria +
                '}';
    }

    /**
     * Valida que cada atributo con etiqueta @NotNull este en un estado optimo antes de ser persistido
     * @return Un mensaje con algun error, null si todo esta bien
     */
    @Override
    public String validate() {
        if(nombre == null || nombre.trim().equalsIgnoreCase("")){
            return "Nombre no puede estar vacio";
        }else if(fechaLimite == null || fechaLimite.trim().equalsIgnoreCase("")){
            return "Fecha no puede estar vacia";
        }else if(precio == 0 || precio < 0){
            return "Precio incorrecto";
        }else if(ingredientes == null || ingredientes.trim().equalsIgnoreCase("")){
            return "Debe haber al menos 1 ingrediente";
        }
        return null;
    }

    public boolean urlValida(){
        if(getUrlFoto() != null && validarFormatos(this)){
            return true;
        }
        return false;
    }

    private boolean validarFormatos(ProductoEntity producto){
        if (producto.getUrlFoto().endsWith(".jpg")) return true;
        if (producto.getUrlFoto().endsWith(".jpeg")) return true;
        if (producto.getUrlFoto().endsWith(".gif")) return true;
        if (producto.getUrlFoto().endsWith(".png")) return true;
        if (producto.getUrlFoto().endsWith(".svg")) return true;
        if (producto.getUrlFoto().endsWith(".bmp")) return true;
        return false;
    }
}
