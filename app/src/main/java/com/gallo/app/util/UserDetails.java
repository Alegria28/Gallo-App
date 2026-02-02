package com.gallo.app.util;

import com.gallo.app.entity.Usuario;

import lombok.Data;

@Data
public class UserDetails {

    private Long id;
    private String nombre;
    private String correo;
    private boolean verificado;
    private Usuario.Rol rol;

    // Constructor
    public UserDetails(Long id, String nombre, String correo, boolean verificado, Usuario.Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.verificado = verificado;
        this.rol = rol;
    }

}
