package com.gallo.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gallo.app.entity.Usuario;

// Mecanismo para encapsular la logica para el acceso a la informacion
@Repository
// JpaRepository nos da una gran variedad de metodos ya implementados para la base de datos
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Encontrar usuario por correo
    Optional<Usuario> findByCorreo(String correo);

    // Verificar si existe usuario con ese correo
    boolean existsByCorreo(String correo);

}