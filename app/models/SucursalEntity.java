package models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.ebean.*;
import io.ebean.annotation.NotNull;
import play.data.format.Formats.*;
import play.data.validation.Constraints.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

/**
 * Clase que modelas las sucursales
 */
@Entity
@Table(name = "sucursal")
public class SucursalEntity extends Model implements Validatable<String> {

    /**
     * id de la sucursal
     */
    @Id
    private int id;

    /**
     * nombre de la sucursal
     */
    @NonEmpty
    @NotNull
    @Size(min = 3)
    private String nombre;

    /**
     * Dirección de la sucursal
     */
    @NonEmpty @NotNull @Size(min = 3)
    private String direccion;

    /**
     * productos que contiene la sucursal
     */
    @JsonManagedReference
    private List<ProductoEntity> productos;

    /**
     * Atributo utilizado para consultas relacionadas con la entidad
     */
    public static final Finder<Integer, SucursalEntity> find = new Finder<>(SucursalEntity.class);

    public SucursalEntity(){
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

    public void setNombre(String dNombre) {
        this.nombre = dNombre;
    }

    @Column(name = "a_direccion")
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String aDireccion) {
        this.direccion = aDireccion;
    }

    @OneToMany(mappedBy = "sucursal", orphanRemoval = true)
    public List<ProductoEntity> getProductos() {
        return productos;
    }

    @Override
    public String toString() {
        return "SucursalEntity{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
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
            return "Nombre no puede ser vacio";
        }else if(direccion == null || direccion.trim().equalsIgnoreCase("")){
            return "Direccion no puede ser vacio";
        }
        return null;
    }
}
