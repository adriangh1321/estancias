package com.ejercicio2.Estancias.servicios;

import com.ejercicio2.Estancias.entidades.Casa;
import com.ejercicio2.Estancias.entidades.Propietario;
import com.ejercicio2.Estancias.entidades.Foto;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import com.ejercicio2.Estancias.repositorios.CasaRepositorio;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CasaServicio {

    @Autowired
    private CasaRepositorio casaRepositorio;
    
    @Autowired
    private PropietarioServicio propietarioServicio;
    
    @Autowired
    private FotoServicio fotoServicio;

    @Transactional
    public Casa crearCasa(String idPropietario,
            String calle,
            Integer numero,
            String codPostal,
            String ciudad,
            String pais,
            Date fechaDesde,
            Date fechaHasta,
            Integer minDias,
            Integer maxDias,
            Double precio,
            String tipoVivienda,
            String descripcion,
            MultipartFile archivo) throws ErrorServicio {
        validarCasa(calle, numero, codPostal, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda);
        validarDescripcion(descripcion);
        Propietario propietario=propietarioServicio.buscarPropietarioPorId(idPropietario);
        if (propietario!=null) {
            Casa casa= new Casa();
            casa.setCalle(calle);
            casa.setNumero(numero);
            casa.setCodPostal(codPostal);
            casa.setCiudad(ciudad);
            casa.setPais(pais);
            casa.setFechaDesde(fechaDesde);
            casa.setFechaHasta(fechaHasta);
            casa.setMinDias(minDias);
            casa.setMaxDias(maxDias);
            casa.setPrecio(precio);
            casa.setTipoVivienda(tipoVivienda);
            casa.setDescripcion(descripcion);
            Foto foto=fotoServicio.guardarFoto(archivo);
            casa.setFoto(foto);
            casa.setAlta(true);
            propietario.setCasa(casa);
            return casaRepositorio.save(casa);
        }else{
            throw new ErrorServicio("El propietario no fue encontrado");
        }
    }

    public void validarCasa(
            String calle,
            Integer numero,
            String codPostal,
            String ciudad,
            String pais,
            Date fechaDesde,
            Date fechaHasta,
            Integer minDias,
            Integer maxDias,
            Double precio,
            String tipoVivienda
            
    ) throws ErrorServicio {
        if (calle == null || calle.trim().isEmpty()) {
            throw new ErrorServicio("La calle es nula o está vacía");
        }
        if (numero == null) {
            throw new ErrorServicio("El numero de la calle es nulo");
        }
        if (numero < 0) {
            throw new ErrorServicio("El numero de la calle es menor que cero");
        }
        if (codPostal == null || codPostal.trim().isEmpty()) {
            throw new ErrorServicio("El código postal es nulo o está vacío");
        }
        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new ErrorServicio("La ciudad es nula o está vacía");
        }
        if (pais == null || pais.trim().isEmpty()) {
            throw new ErrorServicio("El pais es nulo o está vacío");
        }
        validarFechas(fechaDesde,fechaHasta);
//        if (fechaDesde == null) {
//            throw new ErrorServicio("La fecha desde es nula");
//        }
//        if (fechaHasta == null) {
//            throw new ErrorServicio("La fecha hasta es nula");
//        }
//        if (fechaHasta.before(fechaDesde)) {
//            throw new ErrorServicio("La fecha hasta ingresada está antes que la fecha desde");
//        }
//        if (fechaHasta.equals(fechaDesde)) {
//            throw new ErrorServicio("El alquiler no puede ser menor a dos dias");
//        }

        LocalDate fechaD = fechaDesde.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaH = fechaHasta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period p = Period.between(fechaD, fechaH);
        int dias = (int) ChronoUnit.DAYS.between(fechaD, fechaH);

        if (minDias == null) {
            throw new ErrorServicio("Los dias minimos son nulos");
        }
        if (maxDias == null) {
            throw new ErrorServicio("Los dias máximos son nulos");
        }
        if (dias < minDias) {
            throw new ErrorServicio("Los dias mínimo deben ser menor a la cantidad de dias ("+dias+") que hay entre las dos fechas");
        }
        if (dias <= maxDias) {
            throw new ErrorServicio("Los dias máximo deben ser menor a la cantidad de dias ("+dias+") que hay entre las dos fechas");
        }
        if (maxDias < minDias) {
            throw new ErrorServicio("El minimo dias debe ser menor al maximo dias");
        }
        if (precio==null) {
            throw new ErrorServicio("El precio es nulo");
        }
        if (precio<0) {
            throw new ErrorServicio("El precio es negativo");
        }
        if (tipoVivienda==null || tipoVivienda.trim().isEmpty()) {
            throw new ErrorServicio("El tipo de vivienda es nulo o está vacío");
        }
        

    }
    
    
    public Casa modificarCasa(String idCasa,
            String calle,
            Integer numero,
            String codPostal,
            String ciudad,
            String pais,
            Date fechaDesde,
            Date fechaHasta,
            Integer minDias,
            Integer maxDias,
            Double precio,
            String tipoVivienda,
            String descripcion,
            MultipartFile archivo) throws ErrorServicio {
        
        validarCasa(calle, numero, codPostal, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda);
        validarDescripcion(descripcion);
        
        if (idCasa==null || idCasa.trim().isEmpty()) {
            throw new ErrorServicio("El id de la casa es nulo o vacío");
        }
        Casa casa=casaRepositorio.getById(idCasa);
        if (casa!=null) {
            
            casa.setCalle(calle);
            casa.setNumero(numero);
            casa.setCodPostal(codPostal);
            casa.setCiudad(ciudad);
            casa.setPais(pais);
            casa.setFechaDesde(fechaDesde);
            casa.setFechaHasta(fechaHasta);
            casa.setMinDias(minDias);
            casa.setMaxDias(maxDias);
            casa.setPrecio(precio);
            casa.setTipoVivienda(tipoVivienda);
            casa.setDescripcion(descripcion);
            String idFoto=null;
            if (casa.getFoto()!=null) {
                idFoto=casa.getFoto().getId();
                                
            }
            Foto foto = fotoServicio.actualizarFoto(idFoto, archivo);
            if (foto!=null) {
             casa.setFoto(foto);   
            }
            
            return casaRepositorio.save(casa);
        }else{
            throw new ErrorServicio("Esa casa no se encuentra");
        }
    }
    
    @Transactional(readOnly=true)
    public Casa buscarPorId(String id){
        
        return casaRepositorio.getById(id);
    }
    
    @Transactional
    public void altaCasa(String id) throws ErrorServicio{
        Casa casa=casaRepositorio.getById(id);
        if (casa!=null) {
            casa.setAlta(true);
            casaRepositorio.save(casa);
        }else{
            throw new ErrorServicio("Esa casa no se encuentra disponible");
        }
        
    }
    
    @Transactional
    public void bajaCasa(String id) throws ErrorServicio{
        Casa casa=casaRepositorio.getById(id);
        if (casa!=null) {
            casa.setAlta(false);
            casaRepositorio.save(casa);
        }else{
            throw new ErrorServicio("Esa casa no se encuentra disponible");
        }
        
    }
    
    public void validarDescripcion(String descripcion) throws ErrorServicio{
        if (descripcion.length()>1500) {
            throw new ErrorServicio("La descripción excede los 1500 caracteres");
        }
    }
    
   @Transactional(readOnly=true)
   public List<Casa> buscarCasasPorFechaDisponible(Date fechaDesde,Date fechaHasta) throws ErrorServicio{
       validarFechas(fechaDesde,fechaHasta);
       return casaRepositorio.buscarCasasPorFechaDisponible(fechaDesde, fechaHasta);
   }
   
   public void validarFechas(Date fechaDesde,Date fechaHasta) throws ErrorServicio{
        if (fechaDesde == null) {
            throw new ErrorServicio("La fecha desde es nula");
        }
        if (fechaHasta == null) {
            throw new ErrorServicio("La fecha hasta es nula");
        }
        if (fechaHasta.before(fechaDesde)) {
            throw new ErrorServicio("La fecha hasta ingresada está antes que la fecha desde");
        }
        if (fechaHasta.equals(fechaDesde)) {
            throw new ErrorServicio("El alquiler no puede ser menor a dos dias");
        }
   }
   @Transactional(readOnly=true)
   public List<Casa> listarCasas(){
       return casaRepositorio.findAll();
   }

}
