package com.gallo.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gallo.app.entity.UsuarioDetalle;

@Repository
public interface UsuarioDetalleRepository extends JpaRepository<UsuarioDetalle, Long> {

}
