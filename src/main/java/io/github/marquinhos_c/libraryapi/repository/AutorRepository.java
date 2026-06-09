package io.github.marquinhos_c.libraryapi.repository;

import io.github.marquinhos_c.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {

    List<Autor> findByNome(String nome);

    List<Autor> findByNacionalidade(String nacionalide);

    List<Autor> findByNomeAndNacionalidade(String nome, String nacionalide);

    Optional<Autor> findByNomeAndDataNascimentoAndNacionalidade(
            String nome, LocalDate dataNascimento, String nacionalidade);

}
