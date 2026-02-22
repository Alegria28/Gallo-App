package com.gallo.app.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "usuario_detalles") // Tabla usuario_detalles en la base de datos
@Data
public class UsuarioDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuarioDetalle;

    @NotBlank
    private String nombre;

    private String fotoPerfil;

    private boolean verificado = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaRegistro;

    // Relacion 1:1 con Usuario, mapeada por el atributo "usuarioDetalle" en Usuario
    @OneToOne(mappedBy = "usuarioDetalle")
    private Usuario usuario;

    // Relacion n:n con Rol
    // Se crea una tabla intermedia con los IDs de las entidades
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "usuariodetalle_rol", joinColumns = @JoinColumn(name = "idUsuarioDetalle"), inverseJoinColumns = @JoinColumn(name = "idRol"))
    // Guardar los roles que puede tener un usuario (sin repetir)
    private Set<Rol> roles = new HashSet<>();

}
