package com.gallo.app.entity;

import java.util.List;

import com.gallo.app.util.NombreCarrera;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "carrera")
@Data
public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarrera;

    @NotBlank
    private NombreCarrera nombreCarrera;

    @Min(1)
    private int semestre;

    @NotNull
    private Character grupo;

    // Relacion 1:n con UsuarioDetalle
    @OneToMany(mappedBy = "carrera")
    private List<UsuarioDetalle> usuarios;

}
