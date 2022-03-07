package com.ejercicio2.Estancias.controladores;

import com.ejercicio2.Estancias.entidades.Cliente;
import com.ejercicio2.Estancias.entidades.Propietario;
import com.ejercicio2.Estancias.entidades.Usuario;
import com.ejercicio2.Estancias.enumeraciones.Rol;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import com.ejercicio2.Estancias.servicios.ClienteServicio;
import com.ejercicio2.Estancias.servicios.PropietarioServicio;
import com.ejercicio2.Estancias.servicios.UsuarioServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ClienteServicio clienteServicio;

    @Autowired
    private PropietarioServicio propietarioServicio;

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
            @RequestParam MultipartFile archivo,
            //DATOS DE PROPIETARIO
            @RequestParam(required = false) String nombrePropietario,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) String telefono,
            
            //DATOS DE CLIENTE
            @RequestParam(required = false) String nombreCliente,
            @RequestParam(required = false) String calle,
            @RequestParam(required = false) Integer numero,
            @RequestParam(required = false) String codPostal,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String pais,
            RedirectAttributes r) {
        try {
            usuarioServicio.crearUsuario(alias, email, clave, rol, archivo, nombrePropietario, descripcion,telefono, nombreCliente, calle, numero, codPostal, ciudad, pais);
            r.addFlashAttribute("exito", "Registro exitoso");

        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());
        }

        switch (rol) {
            case CLIENTE:
                return "redirect:/usuario/guardarCliente";
            case PROPIETARIO:
                return "redirect:/usuario/guardarPropietario";
            default:
                return "index.html";
        }

    }

    @GetMapping("/guardarPropietario")
    public String guardarPropietario(ModelMap modelo) {
        modelo.put("rol", Rol.PROPIETARIO);
        return "guardarPropietario.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN','ROLE_PROPIETARIO')")
    @GetMapping("/alta/{id}/{rol}")
    public String alta(@PathVariable String id, @PathVariable Rol rol, RedirectAttributes r) {

        try {

            switch (rol) {
                case CLIENTE:
                    usuarioServicio.darAltaUsuario(id);
                    return "redirect:/cliente/listarClientes";
                case PROPIETARIO:
                    usuarioServicio.darAltaUsuario(id);
                    return "redirect:/propietario/listarPropietarios";
                default:
                    usuarioServicio.darAltaUsuario(id);
                    return "redirect:/usuario/listarAdmins";
            }

        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());

            switch (rol) {
                case CLIENTE:
                    return "redirect:/cliente/listarClientes";
                case PROPIETARIO:
                    return "redirect:/propietario/listarPropietarios";
                default:
                    return "redirect:/usuario/listarAdmins";
            }
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN','ROLE_PROPIETARIO')")
    @GetMapping("/baja/{id}/{rol}")
    public String baja(@PathVariable String id, @PathVariable Rol rol, RedirectAttributes r) {
        try {
            usuarioServicio.darBajaUsuario(id);

            switch (rol) {
                case CLIENTE:
                    usuarioServicio.darBajaUsuario(id);
                    return "redirect:/cliente/listarClientes";
                case PROPIETARIO:
                    usuarioServicio.darBajaUsuario(id);
                    return "redirect:/propietario/listarPropietarios";
                default:
                    usuarioServicio.darBajaUsuario(id);
                    return "redirect:/usuario/listarAdmins";
            }

        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());

            switch (rol) {
                case CLIENTE:
                    return "redirect:/cliente/listarClientes";
                case PROPIETARIO:
                    return "redirect:/propietario/listarPropietarios";
                default:
                    return "redirect:/usuario/listarAdmins";
            }
        }

    }

    ///////////////////////////////////////////////////////modificar///////////////////
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN')")
    @GetMapping("/modificarCliente/{id}")
    public String modificarCliente(@PathVariable String id, ModelMap modelo) {
        Cliente cliente = clienteServicio.buscarClientePorId(id);
        modelo.put("cliente", cliente);
        return "modificarCliente.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO','ROLE_ADMIN')")
    @GetMapping("/modificarPropietario/{id}")
    public String modificarPropietario(@PathVariable String id, ModelMap modelo) {
        Propietario propietario = propietarioServicio.buscarPropietarioPorId(id);
        modelo.put("propietario", propietario);
        return "modificarPropietario.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/modificarUsuario/{id}")
    public String modificarUsuario(@PathVariable String id, ModelMap modelo) {
        Usuario usuario = usuarioServicio.buscarUsuarioPorId(id);
        modelo.put("usuario", usuario);
        return "modificarUsuario.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO','ROLE_ADMIN','ROLE_CLIENTE')")
    @PostMapping("modificarUsuario/{id}")
    public String modificarUsuario(
            //DATOS USUARIO
            @RequestParam(required = false) String alias,
            @RequestParam(required = false) String email,
            //@RequestParam(required = false) String clave,
            @RequestParam(required = false) Rol rol,
            @PathVariable String id,
            @RequestParam MultipartFile archivo,
            //DATOS CLIENTE
            @RequestParam(required = false) String nombreCliente,
            @RequestParam(required = false) String calle,
            @RequestParam(required = false) Integer numero,
            @RequestParam(required = false) String codPostal,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String pais,
            //DATOS PROPIETARIO
            @RequestParam(required = false) String nombrePropietario,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) String telefono,
            
            RedirectAttributes r,
            HttpSession session) {

        try {
            Usuario usuarioSesion = (Usuario) session.getAttribute("usuariosession");
            switch (rol) {
                case CLIENTE: {
                    Usuario usuario = usuarioServicio.ModificarUsuario(id, alias, email, rol, archivo, nombrePropietario, descripcion,telefono, nombreCliente, calle, numero, codPostal, ciudad, pais);
                    if (usuarioSesion.getId().equals(usuario.getId())) {
                        session.setAttribute("usuariosession", usuario);
                    }

                    r.addFlashAttribute("exito", "Modificación exitosa!");
                    return "redirect:/usuario/modificarCliente/{id}";
                }
                case PROPIETARIO: {
                    Usuario usuario = usuarioServicio.ModificarUsuario(id, alias, email, rol, archivo, nombrePropietario, descripcion,telefono, nombreCliente, calle, numero, codPostal, ciudad, pais);
                    if (usuarioSesion.getId().equals(usuario.getId())) {
                        session.setAttribute("usuariosession", usuario);
                    }
                    r.addFlashAttribute("exito", "Modificación exitosa!");
                    return "redirect:/usuario/modificarPropietario/{id}";
                }
                default: {
                    Usuario usuario = usuarioServicio.modificarAdmin(id, alias, email, archivo);
                    if (usuarioSesion.getId().equals(usuario.getId())) {
                        session.setAttribute("usuariosession", usuario);
                    }
                    
                    r.addFlashAttribute("exito", "Modificación exitosa!");
                    return "redirect:/usuario/modificarUsuario/{id}";
                }
            }
        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());
            switch (rol) {
                case CLIENTE:
                    return "redirect:/usuario/modificarCliente/{id}";
                case PROPIETARIO:
                    return "redirect:/usuario/modificarPropietario/{id}";
                default:
                    return "redirect:/usuario/modificarUsuario/{id}";
            }

        }

    }

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO','ROLE_ADMIN','ROLE_CLIENTE')")
    @GetMapping("/claveUsuario/{id}")
    public String cambiarClave(@PathVariable String id, ModelMap modelo) {
        Usuario usuario = usuarioServicio.buscarUsuarioPorId(id);
        modelo.put("usuario", usuario);
        return "claveUsuario.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO','ROLE_ADMIN','ROLE_CLIENTE')")
    @PostMapping("/claveUsuario/{id}")
    public String cambiarClave(@PathVariable String id,
            @RequestParam(required = false) Rol rol,
            @RequestParam(required = false) String claveActual,
            @RequestParam(required = false) String claveNueva,
            RedirectAttributes r
    ) {

        try {

            usuarioServicio.cambiarClave(id, claveActual, claveNueva);
            r.addFlashAttribute("exito", "La clave fue modificada con exito");

        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());

        }
        return "redirect:/usuario/claveUsuario/{id}";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/guardarAdmin")
    public String guardarAdmin() {
        return "guardarAdmin.html";
    }

    @PostMapping("/guardarAdmin")
    public String guardarAdmin(ModelMap modelo,
            //DATOS DE USUARIO
            @RequestParam(required = false) String alias,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String clave,
            @RequestParam Rol rol,
            @RequestParam MultipartFile archivo,
            RedirectAttributes r) {

        try {
            usuarioServicio.crearAdmin(alias, email, clave, rol, archivo);
            r.addFlashAttribute("exito", "Admin creado exitosamente");

        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuario/guardarAdmin";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/listarAdmins")
    public String listarAdmins(ModelMap modelo) {
        List<Usuario> admins = usuarioServicio.listarAdmins();
        modelo.addAttribute("usuarios", admins);
        return "listarAdmins.html";
    }

}
