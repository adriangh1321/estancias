package com.ejercicio2.Estancias.servicios;

import com.ejercicio2.Estancias.entidades.Cliente;
import com.ejercicio2.Estancias.entidades.Familia;
import com.ejercicio2.Estancias.entidades.Usuario;
import com.ejercicio2.Estancias.enumeraciones.Rol;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import com.ejercicio2.Estancias.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio ur;

    @Autowired
    private FamiliaServicio familiaServicio;

    @Autowired
    private ClienteServicio clienteServicio;

    @Transactional
    public Usuario crearUsuario(String alias,
            String email,
            String clave,
            Rol rol,
            //PARAMETROS DE FAMILIA
            String nombreFamilia,
            Integer edadMin,
            Integer edadMax,
            Integer numHijos,
            //PARAMETROS DE CLIENTE
            String nombreCliente,
            String calle,
            Integer numero,
            String codPostal,
            String ciudad,
            String pais
    ) throws ErrorServicio {

        validarUsuario(alias, email);
        validarClave(clave);

        Usuario usuario = ur.buscarUsuarioPorEmail(email);
        if (usuario != null) {
            throw new ErrorServicio("El usuario ya existe");
        }

        if (rol == Rol.FAMILIA) {
            Familia familia = familiaServicio.crearFamilia(nombreFamilia, edadMin, edadMax, numHijos);
            familia.setAlias(alias);
            String encriptado = new BCryptPasswordEncoder().encode(clave);
            familia.setClave(encriptado);
            familia.setEmail(email);
            familia.setFechaAlta(new Date());
            familia.setRol(rol);
            familia.setAlta(Boolean.TRUE);

            return ur.save(familia);

        }
        // VER EL ROL!!!! FALTA MAPEO PORQUE LO GUARDA COMO INTEGER!
        if (rol == Rol.CLIENTE) {
            Cliente cliente = clienteServicio.crearCliente(nombreCliente, calle, numero, codPostal, ciudad, pais);
            cliente.setAlias(alias);
            String encriptado = new BCryptPasswordEncoder().encode(clave);
            cliente.setClave(encriptado);
            cliente.setEmail(email);
            cliente.setFechaAlta(new Date());
            cliente.setRol(rol);
            cliente.setAlta(Boolean.TRUE);

            return ur.save(cliente);
        } else {
            throw new ErrorServicio("El usuario no tiene rol, no fue creado");
        }

    }

    @Transactional
    public Usuario ModificarUsuario(String id,
            String alias,
            String email,
            //            String clave,
            Rol rol,
            //PARAMETROS DE FAMILIA
            String nombreFamilia,
            Integer edadMin,
            Integer edadMax,
            Integer numHijos,
            //PARAMETROS DE CLIENTE
            String nombreCliente,
            String calle,
            Integer numero,
            String codPostal,
            String ciudad,
            String pais) throws ErrorServicio {

        validarUsuario(alias, email);

        Usuario usuario = ur.getById(id);
        if (!usuario.getEmail().equals(email)) {
            Usuario usuario1 = ur.buscarUsuarioPorEmail(email);
            if (usuario1 != null) {
                throw new ErrorServicio("El usuario ya existe");
            }
        }

        if (rol == Rol.FAMILIA) {
            Familia familia = familiaServicio.modificarFamilia(id, nombreFamilia, edadMin, edadMax, numHijos);
            familia.setAlias(alias);
            familia.setEmail(email);
            return ur.save(familia);
        }
        if (rol == Rol.CLIENTE) {
            Cliente cliente = clienteServicio.modificarCliente(id, nombreCliente, calle, numero, codPostal, ciudad, pais);
            cliente.setAlias(alias);
            cliente.setEmail(email);
            return ur.save(cliente);
        } else {
            throw new ErrorServicio("El usuario no tiene rol, no fue creado");
        }

    }

    @Transactional
    public void darBajaUsuario(String id) throws ErrorServicio {
        if (id == null) {
            throw new ErrorServicio("El id del usuario es nulo");
        }
        Usuario usuario = ur.getById(id);
        if (usuario != null) {
            usuario.setFechaBaja(new Date());
            usuario.setAlta(Boolean.FALSE);
            ur.save(usuario);
        } else {
            throw new ErrorServicio("El usuario no se encontro");
        }
    }

    @Transactional
    public void darAltaUsuario(String id) throws ErrorServicio {
        if (id == null) {
            throw new ErrorServicio("El id del usuario es nulo");
        }
        Usuario usuario = ur.getById(id);
        if (usuario != null) {
            usuario.setFechaBaja(null);
            usuario.setAlta(Boolean.TRUE);
            ur.save(usuario);
        } else {
            throw new ErrorServicio("El usuario no se encontro");
        }
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        return ur.findAll();
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosActivos() {
        return ur.listarUsuariosActivos();
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosInactivos() {
        return ur.listarUsuariosinactivos();
    }

    public void validarUsuario(String alias, String email) throws ErrorServicio {
        if (alias == null || alias.trim().isEmpty()) {
            throw new ErrorServicio("El alias es nulo");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new ErrorServicio("El email es nulo o vacío");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = ur.buscarUsuarioPorEmail(email);

        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + usuario.getRol());
            permisos.add(p1);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            User user = new User(usuario.getEmail(), usuario.getClave(), permisos);
            return user;
        } else {
            return null;

//            throw new UsernameNotFoundException("Ese usuario no está registrado");
//            return null;
        }

    }

    public void validarClave(String clave) throws ErrorServicio {
        if (clave == null || clave.trim().isEmpty()) {
            throw new ErrorServicio("El clave es nulo o vacio");
        }
        if (clave.length() < 4 || clave.length() > 6) {
            throw new ErrorServicio("La clave debe tener 4 a 6 caracteres");
        }
    }

    @Transactional
    public Usuario cambiarClave(String id, String claveActual, String claveNueva) throws ErrorServicio {
        validarClave(claveActual);
        validarClave(claveNueva);
        Usuario usuario = ur.getById(id);
        if (usuario != null) {
            Boolean matches = new BCryptPasswordEncoder().matches(claveActual, usuario.getClave());
            if (matches) {
                String encriptado = new BCryptPasswordEncoder().encode(claveNueva);
                usuario.setClave(encriptado);
                return ur.save(usuario);
            } else {
                throw new ErrorServicio("Su clave actual es incorrecta");
            }

        } else {
            throw new ErrorServicio("Ese usuario no existe");
        }

    }
    
    @Transactional(readOnly=true)
    public Usuario buscarUsuarioPorId(String id){
        return ur.getById(id);
    }
}
