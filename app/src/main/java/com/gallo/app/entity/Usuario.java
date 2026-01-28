package com.gallo.app.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

// Una entidad es una representacion de la tabla
@Entity
@Table(name = "usuarios") // Tabla usuarios en la base de datos
@Data // De lombok para getters y setters
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    private String fotoPerfil;

    @Column(unique = true)
    @NotBlank
    @Email
    // Patron permitido para el correo institucional
    @Pattern(regexp = "^al\\d{6}@edu\\.uaa\\.mx$")
    private String correo;

    @NotBlank
    private String contra;

    private String codigoVerificacion;

    private LocalDateTime fechaExpiracionCodigo;

    private boolean verificado = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaRegistro;

    @Enumerated(EnumType.STRING)
    private Rol rol = Rol.ESTUDIANTE;

    public enum Rol {
        ESTUDIANTE, ADMIN
    }
}