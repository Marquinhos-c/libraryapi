package io.github.marquinhos_c.libraryapi.service;

import io.github.marquinhos_c.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.marquinhos_c.libraryapi.model.Autor;
import io.github.marquinhos_c.libraryapi.repository.AutorRepository;
import io.github.marquinhos_c.libraryapi.repository.LivroRepository;
import io.github.marquinhos_c.libraryapi.validator.AutorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/* Fluxo da aplicação:
        * Controller -> Service -> Repository -> Banco de Dados
*/
@Service
/**
 * O Lombok gera automaticamente um construtor contendo
 * todos os atributos final da classe.
 * Isso facilita a Injeção de Dependência realizada pelo Spring.
 */
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository repository;
    private final AutorValidator validator;
    private final LivroRepository livroRepository;

    /**
     * Salva um novo autor.
     *
     * Fluxo:
     * 1. Valida os dados do autor.
     * 2. Persiste no banco de dados.
     * 3. Retorna o autor salvo.
     *
     * @param autor Autor a ser cadastrado.
     * @return Autor salvo no banco.
     */
    public Autor salvar(Autor autor) {
        validator.validar(autor);
        return repository.save(autor);
    }

    /**
     * Atualiza um autor já existente.
     *
     * Antes da atualização verifica se o autor possui ID,
     * garantindo que o registro já exista na base.
     *
     * @param autor Autor a ser atualizado.
     */
    public void atualizar(Autor autor) {
        if (autor.getId() == null) {
            throw new IllegalArgumentException("Para atualizar, é necessário que o autor já esteja salvo na base.");
        }
        // Executa validações de negócio
        validator.validar(autor);
        // Atualiza os dados no banco
        repository.save(autor);
    }

    /**
     * Busca um autor pelo ID.
     *
     * O Optional é utilizado para representar que
     * o autor pode ou não existir.
     *
     * @param id Identificador do autor.
     * @return Optional contendo o autor encontrado.
     */
    public Optional<Autor> obterPorId(UUID id) {
        return repository.findById(id);
    }

    /**
     * Exclui um autor da base.
     *
     * Regra de negócio:
     * Um autor que possui livros cadastrados
     * não pode ser removido.
     *
     * @param autor Autor a ser removido.
     */
    public void deletar(Autor autor) {

        // Verifica se o autor possui livros vinculados
        if (possuiLivro(autor)) {
            throw new OperacaoNaoPermitidaException(
                    "Não é permitido excluir um Autor que possui livros cadastrados!");
        }
        repository.delete(autor);
    }

/**
 * Realiza pesquisas de autores.
 *
 * Cenários:
 * - Nome e nacionalidade.
 * - Apenas nome.
 * - Apenas nacionalidade.
 * - Nenhum filtro.
 *
 * @param nome Nome do autor.
 * @param nacionalidade Nacionalidade do autor.
 * @return Lista de autores encontrados.
 */
    public List<Autor> pesquisa(String nome, String nacionalidade) {

        if (nome != null && nacionalidade != null) {
            return repository.findByNomeAndNacionalidade(nome, nacionalidade);
        }

        if (nome != null) {
            return repository.findByNome(nome);
        }

        if (nacionalidade != null) {
            return repository.findByNacionalidade(nacionalidade);
        }

        return repository.findAll();
    }
    public List<Autor> pesquisaByExample(String nome, String nacionalidade) {
        var autor = new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Autor> autorExample = Example.of(autor, matcher);
        return repository.findAll(autorExample);
    }

    /**
     * Verifica se o autor possui livros cadastrados.
     *
     * Utilizado para impedir exclusões indevidas.
     *
     * @param autor Autor a ser verificado.
     * @return true caso possua livros.
     */
    public boolean possuiLivro(Autor autor) {
        return livroRepository.existsByAutor(autor);
    }
}