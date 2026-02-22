package com.gallo.app.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "rol")
@Data
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;

    // Los usuarios inician siendo estudiantes
    @Enumerated(EnumType.STRING)
    private Nombre nombre = Nombre.ESTUDIANTE;

    public enum Nombre {
        ESTUDIANTE, ADMIN
    }

    // Relacion n:n con UsuarioDetalle, mapeada al atributo "roles"
    @ManyToMany(mappedBy = "roles")
    private Set<UsuarioDetalle> usuarioDetalle = new HashSet<>();

}
