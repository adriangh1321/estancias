package com.ejercicio2.Estancias.controladores;

import com.ejercicio2.Estancias.entidades.Casa;
import com.ejercicio2.Estancias.entidades.Propietario;
import com.ejercicio2.Estancias.enumeraciones.Rol;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import com.ejercicio2.Estancias.servicios.CasaServicio;
import com.ejercicio2.Estancias.servicios.PropietarioServicio;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/casa")
public class CasaControlador {

    @Autowired
    private CasaServicio casaServicio;

    @Autowired
    private PropietarioServicio propietarioServicio;

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO')")
    @GetMapping("/guardarCasa/{id}")
    public String guardarCasa(@PathVariable String id, ModelMap modelo) {
        modelo.put("id", id);
        return "guardarCasa.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO','ROLE_ADMIN')")
    @GetMapping("/mostrarCasa/{idPropietario}")
    public String mostrarCasa(@PathVariable String idPropietario, ModelMap modelo) {
        List<Casa> casas = casaServicio.listarCasasPorPropietario(idPropietario);

        modelo.put("casas", casas);
        return "listarCasas.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO')")
    @PostMapping("/guardarCasa/{id}")
    public String guardarCasa(@PathVariable String id,
            @RequestParam String calle,
            @RequestParam Integer numero,
            @RequestParam String codPostal,
            @RequestParam String ciudad,
            @RequestParam String pais,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDesde,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaHasta,
            @RequestParam Integer minDias,
            @RequestParam Integer maxDias,
            @RequestParam Double precio,
            @RequestParam String tipoVivienda,
            @RequestParam String descripcion,
            @RequestParam MultipartFile archivo,
            RedirectAttributes r) {
        try {
            Casa casa = casaServicio.crearCasa(id, calle, numero, codPostal, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda, descripcion, archivo);
            r.addFlashAttribute("exito", "Casa agregada con exito");
            id = casa.getId();
            return "redirect:/casa/mostrarCasa/" + id;
        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());
            return "redirect:/casa/guardarCasa/{id}";

        }

    }

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO','ROLE_ADMIN')")
    @GetMapping("/modificarCasa/{id}")
    public String modificarCasa(@PathVariable String id, ModelMap modelo) {
        Casa casa = casaServicio.buscarPorId(id);
        modelo.put("casa", casa);
        return "modificarCasa.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO','ROLE_ADMIN')")
    @PostMapping("/modificarCasa/{id}")
    public String modificarCasa(@PathVariable String id,
            @RequestParam String calle,
            @RequestParam Integer numero,
            @RequestParam String codPostal,
            @RequestParam String ciudad,
            @RequestParam String pais,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDesde,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaHasta,
            @RequestParam Integer minDias,
            @RequestParam Integer maxDias,
            @RequestParam Double precio,
            @RequestParam String tipoVivienda,
            @RequestParam String descripcion,
            @RequestParam MultipartFile archivo,
            RedirectAttributes r) {
        try {
            casaServicio.modificarCasa(id, calle, numero, codPostal, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda, descripcion, archivo);
            r.addFlashAttribute("exito", "Modificación exitosa ");
            return "redirect:/casa/mostrarCasa/{id}";
        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());
            return "redirect:/casa/modificarCasa/{id}";

        }
    }

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO','ROLE_ADMIN')")
    @GetMapping("/baja/{id}")
    public String baja(@PathVariable String id, RedirectAttributes r) {
        try {
            casaServicio.bajaCasa(id);

        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());

        }
        return "redirect:/casa/mostrarCasa/{id}";

    }

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO','ROLE_ADMIN')")
    @GetMapping("/alta/{id}")
    public String alta(@PathVariable String id, RedirectAttributes r) {
        try {
            casaServicio.altaCasa(id);

        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());

        }
        return "redirect:/casa/mostrarCasa/{id}";

    }

    
    @GetMapping("/filtrarCasas/{idCliente}")
    public String filtrarCasas(@PathVariable String idCliente, ModelMap modelo) {
        modelo.put("idCliente", idCliente);
        return "filtrarCasas.html";
    }

    
    @PostMapping("/filtrarCasas/{idCliente}")
    public String filtrarCasas(@PathVariable String idCliente, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDesde,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaHasta,
            RedirectAttributes r) {

        try {
            List<Casa> casas = casaServicio.buscarCasasPorFechaDisponible(fechaDesde, fechaHasta);
            r.addFlashAttribute("casas", casas);
            r.addFlashAttribute("fechaDesde", fechaDesde);
            r.addFlashAttribute("fechaHasta", fechaHasta);

        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/casa/filtrarCasas/{idCliente}";

    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/listarCasas")
    public String listarCasas(ModelMap modelo){
        List <Casa> casas=casaServicio.listarCasas();
        modelo.put("casas",casas);
        return "listarCasas.html";
    }

}
