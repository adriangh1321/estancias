package com.ejercicio2.Estancias.servicios;

import com.ejercicio2.Estancias.entidades.Casa;
import com.ejercicio2.Estancias.entidades.Cliente;
import com.ejercicio2.Estancias.entidades.Reserva;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import com.ejercicio2.Estancias.repositorios.ReservaRepositorio;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaServicio {

    @Autowired
    private ReservaRepositorio reservaRepositorio;
    @Autowired
    private ClienteServicio clienteServicio;
    @Autowired
    private CasaServicio casaServicio;

    @Transactional
    public Reserva crearReserva(String idCliente, String idCasa, Date fechaDesdeReserva, Date fechaHastaReserva) throws ErrorServicio {
        validarReserva(idCliente, idCasa, fechaDesdeReserva, fechaHastaReserva);
        Cliente cliente = clienteServicio.buscarClientePorId(idCliente);
        if (cliente == null) {
            throw new ErrorServicio("El cliente no existe");

        }
        Casa casa = casaServicio.buscarPorId(idCasa);
        if (casa == null) {
            throw new ErrorServicio("La casa no existe");
        }
        Date fechaDesdeCasa = casa.getFechaDesde();
        Date fechaHastaCasa = casa.getFechaHasta();

        if (fechaDesdeReserva.before(fechaDesdeCasa)) {
            throw new ErrorServicio("La fecha de ingreso debe ser igual o posterior a la fecha inicial de disponibilidad de la casa(" + fechaDesdeCasa.toString() + ")");
        }
        if (fechaHastaReserva.after(fechaHastaCasa)) {
            throw new ErrorServicio("La fecha de salida debe ser igual o anterior a la fecha final de disponiblidad de la casa(" + fechaHastaCasa.toString() + ")");
        }
        LocalDate fechaDReserva = fechaDesdeReserva.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaHReserva = fechaHastaReserva.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period p = Period.between(fechaDReserva, fechaHReserva);
        int diasReserva = (int) ChronoUnit.DAYS.between(fechaDReserva, fechaHReserva);
        Integer minDias = casa.getMinDias();
        Integer maxDias = casa.getMaxDias();
        if (diasReserva < minDias) {
            throw new ErrorServicio("La cantidad minima de dias de la reserva no puede ser menor al minimo de dias disponible(" + minDias + ")");
        }
        if (diasReserva > maxDias) {
            throw new ErrorServicio("La cantidad máxima de días de la reserva no puede ser mayor al máximo de dias disponibles(" + maxDias + ")");
        }
        List<Reserva> reservas = reservaRepositorio.listarReservasOcupadas(idCasa, fechaDesdeReserva, fechaHastaReserva);
        if (!reservas.isEmpty()) {
            throw new ErrorServicio("La casa se encuentra ocupada en las fechas indicadas");
        }
        Reserva reserva = new Reserva();
        reserva.setFechaDesde(fechaDesdeReserva);
        reserva.setFechaHasta(fechaHastaReserva);
        reserva.setCliente(cliente);
        reserva.setCasa(casa);
        reserva.setAlta(true);
        return reservaRepositorio.save(reserva);

    }

    public void validarReserva(String idCliente, String idCasa, Date fechaDesdeReserva, Date fechaHastaReserva) throws ErrorServicio {
        if (fechaDesdeReserva == null) {
            throw new ErrorServicio("La fecha de ingreso no puede ser nula");
        }
        if (fechaHastaReserva == null) {
            throw new ErrorServicio("La fecha de salida no puede ser nula");
        }
        if (fechaHastaReserva.before(fechaDesdeReserva)) {
            throw new ErrorServicio("La fecha de salida debe estar despues de la fecha de ingreso");
        }
        if (idCliente == null || idCliente.trim().isEmpty()) {
            throw new ErrorServicio("El id del cliente es nulo o vacío");
        }
        if (idCasa == null || idCasa.trim().isEmpty()) {
            throw new ErrorServicio("El id de la casa es nulo o vacío");
        }

    }

    @Transactional
    public void darBajaReserva(String id) throws ErrorServicio {
        Reserva reserva = reservaRepositorio.getById(id);
        if (reserva != null) {

            reserva.setAlta(false);
            reservaRepositorio.save(reserva);

        } else {
            throw new ErrorServicio("Esa reserva no existe");
        }

    }

    @Transactional(readOnly = true)
    public List<Reserva> listarReservasPorCliente(String idCliente) {
        List<Reserva> reservas = reservaRepositorio.listarReservasPorCliente(idCliente);
        return reservas;
    }

    @Transactional(readOnly = true)
    public List<Reserva> listarReservas() {
        List<Reserva> reservas = reservaRepositorio.findAll();
        return reservas;
    }
    
    @Transactional(readOnly = true)
    public List<Reserva> listarReservasPorPropietario(String idPropietario) {
        List<Reserva> reservas = reservaRepositorio.listarReservasPorPropietario(idPropietario);
        return reservas;
    }

}
