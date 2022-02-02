package com.ejercicio2.Estancias.servicios;

import com.ejercicio2.Estancias.entidades.Foto;
import com.ejercicio2.Estancias.repositorios.FotoRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicio {

    @Autowired
    private FotoRepositorio fotoRepositorio;

    @Transactional
    public Foto guardarFoto(MultipartFile archivo) {
        if (!archivo.isEmpty()) {

            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getOriginalFilename());  // en el video usa getName pero no me setea el nombre verdadero del archivo, en cambio getOriginalFileName si lo hace
                foto.setContenido(archivo.getBytes());
                return fotoRepositorio.save(foto);
            } catch (Exception e) {
                System.err.println(e.getMessage());  //imprime en consola el error para analizarlo
            }

        }
        return null;
    }

    @Transactional
    public Foto actualizarFoto(String id, MultipartFile archivo) {
        if (!archivo.isEmpty()) {
            try {
                Foto foto = new Foto();
                if (id != null) {
                    foto = fotoRepositorio.getById(id);

                }
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getOriginalFilename());  // en el video usa getName pero no me setea el nombre verdadero del archivo, en cambio getOriginalFileName si lo hace
                foto.setContenido(archivo.getBytes());
                return fotoRepositorio.save(foto);
            } catch (Exception e) {
                System.err.println(e.getMessage());  //imprime en consola el error para analizarlo
            }
        }
        return null;
    }

}
