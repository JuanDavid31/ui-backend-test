package models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.ebean.*;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import io.ebean.annotation.NotNull;
import play.data.format.Formats.*;
import play.data.validation.Constraints.*;

/**
 * Clase que modela las categorias
 */
@Entity
@Table(name = "categoria")
public class CategoriaEntity  extends Model implements Validatable<String>{

    /**
     * Id de la categoria
     */
    @Id
    private int id;

    /**
     * Nombre de la categoria
     */
    @NonEmpty @NotNull @Size(min = 3)
    private String nombre;

    /**
     * Lista de productos que contiene la categoria
     */
    @JsonManagedReference
    private List<ProductoEntity> productos;

    /**
     * Atributo utilizado para consultas relacionadas con la entidad
     */
    public static final Finder<Integer, CategoriaEntity> find = new Finder<>(CategoriaEntity.class);

    public CategoriaEntity(){
        productos = new ArrayList();
    }

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

    @OneToMany(mappedBy = "categoria", orphanRemoval = true)
    public List<ProductoEntity> getProductos() {
        return productos;
    }

    @Override
    public String toString() {
        return "CategoriaEntity{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", productos=" + productos +
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
        }
        return null;
    }
}
