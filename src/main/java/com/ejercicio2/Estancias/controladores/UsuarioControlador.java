package com.ejercicio2.Estancias.controladores;

import com.ejercicio2.Estancias.entidades.Cliente;
import com.ejercicio2.Estancias.entidades.Familia;
import com.ejercicio2.Estancias.entidades.Usuario;
import com.ejercicio2.Estancias.enumeraciones.Rol;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import com.ejercicio2.Estancias.servicios.ClienteServicio;
import com.ejercicio2.Estancias.servicios.FamiliaServicio;
import com.ejercicio2.Estancias.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ClienteServicio clienteServicio;
    
    @Autowired
    private FamiliaServicio familiaServicio;

    @GetMapping("/guardarCliente")
    public String guardarCliente(ModelMap modelo) {
        modelo.put("rol", Rol.CLIENTE);
        return "guardarCliente.html";
    }

    @PostMapping("/guardarUsuario")
    public String guardarUsuario(ModelMap modelo,
            //DATOS DE USUARIO
            @RequestParam(required = false) String alias,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String clave,
            @RequestParam Rol rol,
            //DATOS DE FAMILIA
            @RequestParam(required = false) String nombreFamilia,
            @RequestParam(required = false) Integer edadMin,
            @RequestParam(required = false) Integer edadMax,
            @RequestParam(required = false) Integer numHijos,
            //DATOS DE CLIENTE
            @RequestParam(required = false) String nombreCliente,
            @RequestParam(required = false) String calle,
            @RequestParam(required = false) Integer numero,
            @RequestParam(required = false) String codPostal,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String pais,
            RedirectAttributes r) {
        try {
            usuarioServicio.crearUsuario(alias, email, clave, rol, nombreFamilia, edadMin, edadMax, numHijos, nombreCliente, calle, numero, codPostal, ciudad, pais);
            r.addFlashAttribute("exito", "Registro exitoso");

        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());
        }
        if (rol.equals(Rol.CLIENTE)) {
            return "redirect:/usuario/guardarCliente";
        }
        if (rol.equals(Rol.FAMILIA)) {
            return "redirect:/usuario/guardarFamilia";
        } else {
            return "index.html";
        }

    }

    @GetMapping("/guardarFamilia")
    public String guardarFamilia(ModelMap modelo) {
        modelo.put("rol", Rol.FAMILIA);
        return "guardarFamilia.html";
    }

    @GetMapping("/alta/{id}/{rol}")
    public String alta(@PathVariable String id, @PathVariable Rol rol, RedirectAttributes r) {

        try {

            if (rol == Rol.CLIENTE) {
                usuarioServicio.darAltaUsuario(id);
                return "redirect:/cliente/listarClientes";
            }
            if (rol == Rol.FAMILIA) {
                usuarioServicio.darAltaUsuario(id);
                return "redirect:/familia/listarFamilias";
            } else {
                throw new Exception("Rol invalido");
            }

        } catch (Exception e) {
            r.addFlashAttribute("error", e.getMessage());
            if (rol == Rol.CLIENTE) {

                return "redirect:/cliente/listarClientes";
            }
            if (rol == Rol.FAMILIA) {

                return "redirect:/familia/listarFamilias";
            } else {
                return "index.html";
            }
        }

    }

    @GetMapping("/baja/{id}/{rol}")
    public String baja(@PathVariable String id, @PathVariable Rol rol, RedirectAttributes r) {
        try {
            usuarioServicio.darBajaUsuario(id);

            if (rol == Rol.CLIENTE) {
                usuarioServicio.darBajaUsuario(id);
                return "redirect:/cliente/listarClientes";
            }
            if (rol == Rol.FAMILIA) {
                usuarioServicio.darBajaUsuario(id);
                return "redirect:/familia/listarFamilias";
            } else {
                throw new Exception("Rol invalido");
            }

        } catch (Exception e) {
            r.addFlashAttribute("error", e.getMessage());
            
            if (rol == Rol.CLIENTE) {

                return "redirect:/cliente/listarClientes";
            }
            if (rol == Rol.FAMILIA) {

                return "redirect:/familia/listarFamilias";
            } else {
                return "index.html";
            }
        }

    }

    ///////////////////////////////////////////////////////modificar///////////////////
    @GetMapping("/modificarCliente/{id}")
    public String modificarCliente(@PathVariable String id, ModelMap modelo) {
        Cliente cliente = clienteServicio.buscarClientePorId(id);
        modelo.put("cliente", cliente);
        return "modificarCliente.html";
    }
    
    @GetMapping("/modificarFamilia/{id}")
    public String modificarFamilia(@PathVariable String id, ModelMap modelo){
        Familia familia = familiaServicio.buscarFamiliaPorId(id);
        modelo.put("familia", familia);
        return "modificarFamilia.html";
    }
    
       
    

    @PostMapping("modificarUsuario/{id}")
    public String modificarCliente(
            //DATOS USUARIO
            @RequestParam(required = false) String alias,
            @RequestParam(required = false) String email,
            //@RequestParam(required = false) String clave,
            @RequestParam(required = false) Rol rol,
            @PathVariable String id,
            //DATOS CLIENTE
            @RequestParam(required = false) String nombreCliente,
            @RequestParam(required = false) String calle,
            @RequestParam(required = false) Integer numero,
            @RequestParam(required = false) String codPostal,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String pais,
            //DATOS FAMILIA
            @RequestParam(required = false) String nombreFamilia,
            @RequestParam(required = false) Integer edadMin,
            @RequestParam(required = false) Integer edadMax,
            @RequestParam(required = false) Integer numHijos,
            RedirectAttributes r) {

        try {

            if (rol.equals(Rol.CLIENTE)) {
                usuarioServicio.ModificarUsuario(id, alias, email, rol, nombreFamilia, edadMin, edadMax, numHijos, nombreCliente, calle, numero, codPostal, ciudad, pais);
                r.addFlashAttribute("exito", "Modificación exitosa!");
                return "redirect:/usuario/modificarCliente/{id}";
            }
            if (rol.equals(Rol.FAMILIA)) {
                usuarioServicio.ModificarUsuario(id, alias, email, rol, nombreFamilia, edadMin, edadMax, numHijos, nombreCliente, calle, numero, codPostal, ciudad, pais);
                r.addFlashAttribute("exito", "Modificación exitosa!");
                return "redirect:/usuario/modificarFamilia/{id}";
            } else {
                throw new Exception("Rol invalido");
            }
        } catch (Exception e) {
            r.addFlashAttribute("error", e.getMessage());
            if (rol.equals(Rol.CLIENTE)) {
                return "redirect:/usuario/modificarCliente/{id}";
            }
            if (rol.equals(Rol.FAMILIA)) {
                return "redirect:/usuario/modificarFamilia/{id}";
            } else {
                return "index.html";
            }

        }

    }

    @GetMapping("/claveUsuario/{id}")
    public String cambiarClave(@PathVariable String id, ModelMap modelo) {
        Usuario usuario = usuarioServicio.buscarUsuarioPorId(id);
        modelo.put("usuario", usuario);
        return "claveUsuario.html";
    }

    @PostMapping("/claveUsuario/{id}")
    public String cambiarClave(@PathVariable String id,
            @RequestParam(required = false) Rol rol,
            @RequestParam(required = false) String claveActual,
            @RequestParam(required = false) String claveNueva,
            RedirectAttributes r
    ) {

        try {

            if (rol.equals(Rol.CLIENTE)) {
                usuarioServicio.cambiarClave(id, claveActual, claveNueva);
                r.addFlashAttribute("exito", "La clave fue modificada con exito");
                return "redirect:/cliente/listarClientes";
            }
            if (rol.equals(Rol.FAMILIA)) {
                usuarioServicio.cambiarClave(id, claveActual, claveNueva);
                r.addFlashAttribute("exito", "La clave fue modificada con exito");
                return "redirect:/familias/listarFamilias";
            } else {
                throw new Exception("Rol invalido");
            }
        } catch (Exception e) {
            r.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuario/claveUsuario/{id}";
        }

    }

}
