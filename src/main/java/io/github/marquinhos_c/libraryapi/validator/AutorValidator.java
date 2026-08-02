package io.github.marquinhos_c.libraryapi.validator;

import io.github.marquinhos_c.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.marquinhos_c.libraryapi.model.Autor;
import io.github.marquinhos_c.libraryapi.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Valida regras de negócio relacionadas ao Autor.
 * Impede o cadastro de autores duplicados.
 */
@Component
@RequiredArgsConstructor
public class AutorValidator {

    // Acesso aos dados de autor
    private final AutorRepository repository;

    /**
     * Executa as validações do autor.
     */
    public void validar(Autor autor) {
        if (existeAutorCadastrado(autor)) {
            throw new RegistroDuplicadoException("Autor já cadastrado");
        }
    }

    /**
     * Verifica se já existe um autor com os mesmos dados.
     */
    private boolean existeAutorCadastrado(Autor autor) {
        // Busca autor com nome, data de nascimento e nacionalidade iguais
        Optional<Autor> autorEncontrado =
                repository.findByNomeAndDataNascimentoAndNacionalidade(
                        autor.getNome(),
                        autor.getDataNascimento(),
                        autor.getNacionalidade()
        );

        // Regra para cadastro
        if (autor.getId() == null) {
            return autorEncontrado.isPresent();
        }

        // Regra para atualização
        return autorEncontrado.isPresent() && !autor.getId().equals(autorEncontrado.get().getId());
    }
}
