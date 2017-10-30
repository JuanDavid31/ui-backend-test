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

    @Id
    private int cId;
    @NonEmpty
    @NotNull
    @Size(min = 3)
    private String dNombre;
    private String dUrlFoto;
    private String dNombreCategoria;
    @NonEmpty
    @NotNull @Size(min = 3)
    private String fLimite;
    @NotNull
    private int nPrecio;
    @NonEmpty
    @NotNull @Size(min = 3)
    private String aIngredientes;
    @JsonBackReference
    private SucursalEntity sucursal;
    @JsonBackReference
    private CategoriaEntity categoria;
    public static final Finder<Integer, ProductoEntity> find = new Finder<>(ProductoEntity.class);

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

    @Column(name = "d_url_foto")
    public String getdUrlFoto() {
        return dUrlFoto;
    }

    public void setdUrlFoto(String dUrlFoto) {
        this.dUrlFoto = dUrlFoto;
    }

    @Column(name = "dNombreCategoria")
    public String getdNombreCategoria() {
        return dNombreCategoria;
    }

    public void setdNombreCategoria(String dNombreCategoria) {
        this.dNombreCategoria = dNombreCategoria;
    }

    @Column(name = "f_limite")
    public String getfLimite() {
        return fLimite;
    }

    public void setfLimite(String fLimite) {
        this.fLimite = fLimite;
    }

    @Column(name = "n_precio")
    public double getnPrecio() {
        return nPrecio;
    }

    public void setnPrecio(int precio) {
        this.nPrecio = precio;
    }

    @Column(name = "a_ingredientes")
    public String getaIngredientes() {
        return aIngredientes;
    }

    public void setaIngredientes(String ingredientes) {
        this.aIngredientes = ingredientes;
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
                "cId=" + cId +
                ", dNombre='" + dNombre + '\'' +
                ", dUrlFoto='" + dUrlFoto + '\'' +
                ", dNombreCategoria='" + dNombreCategoria + '\'' +
                ", fLimite='" + fLimite + '\'' +
                ", nPrecio=" + nPrecio +
                ", aIngredientes='" + aIngredientes + '\'' +
                ", sucursal=" + sucursal +
                ", categoria=" + categoria +
                '}';
    }

    @Override
    public String validate() {
        if(dNombre == null || dNombre.trim().equalsIgnoreCase("")){
            return "Nombre no puede estar vacio";
        }else if(fLimite == null || fLimite.trim().equalsIgnoreCase("")){
            return "Fecha no puede estar vacia";
        }else if(nPrecio == 0 || nPrecio < 0){
            return "Precio incorrecto";
        }else if(aIngredientes == null || aIngredientes.trim().equalsIgnoreCase("")){
            return "Debe haber al menos 1 ingrediente";
        }
        return null;
    }
}
