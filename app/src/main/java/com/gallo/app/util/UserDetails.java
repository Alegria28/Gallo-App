package com.gallo.app.util;

import java.util.Set;

import com.gallo.app.entity.Rol;

import lombok.Data;

@Data
public class UserDetails {

    private Long id;
    private String nombre;
    private String correo;
    private boolean verificado;
    private Set<Rol> roles;

    // Constructor
    public UserDetails(Long id, String nombre, String correo, boolean verificado, Set<Rol> roles) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.verificado = verificado;
        this.roles = roles;
    }

}
