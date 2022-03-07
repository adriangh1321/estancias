package com.ejercicio2.Estancias.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

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

    @OneToOne
    private Casa casa;

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

    

    public Casa getCasa() {
        return casa;
    }

    public void setCasa(Casa casa) {
        this.casa = casa;
    }

}
