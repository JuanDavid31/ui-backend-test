package models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.ebean.*;
import io.ebean.annotation.NotNull;
import play.data.format.Formats.*;
import play.data.validation.Constraints.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "sucursal")
public class SucursalEntity extends Model implements Validatable<String> {

    @Id
    private int cId;
    @NonEmpty
    @NotNull
    @Size(min = 3)
    private String dNombre;
    @NonEmpty @NotNull @Size(min = 3)
    private String aDireccion;
    @JsonManagedReference
    private List<ProductoEntity> productos;
    public static final Finder<Integer, SucursalEntity> find = new Finder<>(SucursalEntity.class);

    @Column(name = "c_id")
    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    @Column(name = "dNombre")
    public String getdNombre() {
        return dNombre;
    }

    public void setdNombre(String dNombre) {
        this.dNombre = dNombre;
    }

    @Column(name = "a_direccion")
    public String getaDireccion() {
        return aDireccion;
    }

    public void setaDireccion(String aDireccion) {
        this.aDireccion = aDireccion;
    }

    @OneToMany(mappedBy = "sucursal")
    public List<ProductoEntity> getProductos() {
        return productos;
    }

    @Override
    public String toString() {
        return "SucursalEntity{" +
                "cId=" + cId +
                ", dNombre='" + dNombre + '\'' +
                ", aDireccion='" + aDireccion + '\'' +
                ", productos=" + productos +
                '}';
    }

    @Override
    public String validate() {
        if(dNombre == null || dNombre.trim().equalsIgnoreCase("")){
            return "Nombre no puede ser vacio";
        }else if(aDireccion == null || aDireccion.trim().equalsIgnoreCase("")){
            return "Direccion no puede ser vacio";
        }
        return null;
    }
}
