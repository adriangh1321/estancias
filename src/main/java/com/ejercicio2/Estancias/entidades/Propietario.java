package com.ejercicio2.Estancias.entidades;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import javax.persistence.OneToOne;

@Entity
public class Propietario extends Usuario {
//    @Id
//    @GeneratedValue(generator="uuid")
//    @GenericGenerator(name="uuid",strategy="uuid2")
//    private String id;

    private String nombre;
    
    @Lob
    private String descripcion;
    private Integer calificacion;
    private String telefono;


    @OneToMany(mappedBy = "propietario",fetch =FetchType.EAGER )
    private List<Casa> casas=new ArrayList();

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Casa> getCasas() {
        return casas;
    }

    public void setCasas(List<Casa> casas) {
        this.casas = casas;
    }
    

    
    
     public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }
    
    

}
