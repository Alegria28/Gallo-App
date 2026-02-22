package com.gallo.app.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

// Una entidad es una representacion de la tabla
@Entity
@Table(name = "usuario") // Tabla usuario en la base de datos
@Data // De lombok para getters y setters
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(unique = true)
    @NotBlank
    @Email
    // Patron permitido para el correo institucional
    @Pattern(regexp = "^al\\d{6}@edu\\.uaa\\.mx$")
    private String correo;

    @NotBlank
    private String contra;

    // Relacion 1:1 con UsuarioDetalle
    // Las operaciones se propagan desde la entidad principal hacia las entidades asociadas (asi evitamos que tengamos que persitir toda la relacion)
    // Para relaciones 1:1 o 1:n orphanRemoval ayuda a asegurarse que las entidades secundarias asociadas a una entidad principal sean eliminadas cuando la principal tambien lo sea
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    // Entidad propietaria de la relacion, se crea una columna usuario_detalles_id en la tabla usuario
    @JoinColumn(name = "usuario_detalles_id", referencedColumnName = "idUsuarioDetalle")
    private UsuarioDetalle usuarioDetalle;

    @Enumerated(EnumType.STRING)
    private Rol rol = Rol.ESTUDIANTE;

    public enum Rol {
        ESTUDIANTE, ADMIN
    }
}
