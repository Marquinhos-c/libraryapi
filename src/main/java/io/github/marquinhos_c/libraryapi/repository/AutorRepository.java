package io.github.marquinhos_c.libraryapi.repository;

import io.github.marquinhos_c.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {

}
