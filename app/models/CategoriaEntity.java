package models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.ebean.*;
import javax.persistence.*;
import javax.validation.constraints.Size;
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

    @Id
    private int cId;
    @NonEmpty @NotNull @Size(min = 3)
    private String dNombre;
    @JsonManagedReference
    private List<ProductoEntity> productos;
    public static final Finder<Integer, CategoriaEntity> find = new Finder<>(CategoriaEntity.class);

    @Column(name = "c_id")
    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    @Column(name = "d_nombre", unique = true)
    public String getdNombre() {
        return dNombre;
    }

    public void setdNombre(String dNombre) {
        this.dNombre = dNombre;
    }

    @OneToMany(mappedBy = "categoria")
    public List<ProductoEntity> getProductos() {
        return productos;
    }

    @Override
    public String toString() {
        return "CategoriaEntity{" +
                "cId=" + cId +
                ", dNombre='" + dNombre + '\'' +
                ", productos=" + productos +
                '}';
    }

    @Override
    public String validate() {
        if(dNombre == null || dNombre.trim().equalsIgnoreCase("")){
            return "Nombre no puede estar vacio";
        }
        return null;
    }
}
