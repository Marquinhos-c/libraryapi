package io.github.marquinhos_c.libraryapi.repository;

import io.github.marquinhos_c.libraryapi.model.Autor;
import io.github.marquinhos_c.libraryapi.model.GeneroLivro;
import io.github.marquinhos_c.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Classe de testes de integração do LivroRepository.
 *
 * Utiliza o contexto real do Spring Boot para testar
 * operações de persistência, relacionamento e consultas.
 */
@SpringBootTest
class LivroRepositoryTest {

    /**
     * Repositório de Livro injetado pelo Spring.
     */
    @Autowired
    LivroRepository repository;

    /**
     * Repositório de autor injetado pelo Spring.
     */
    @Autowired
    AutorRepository autorRepository;

    /**
     * Testa o salvamento de um livro associado
     * a um autor já existente no banco de dados.
     */
    @Test
    void salvarTest() {
        Livro livro = new Livro();
        livro.setIsbn("90887-84874");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("UFO");
        livro.setDataPublicacao(LocalDate.of(1980, 1, 2));

        Autor autor = autorRepository
                .findById(UUID.fromString("aa331f89-272f-4def-911d-585e13f0a74d"))
                .orElse(null);
        livro.setAutor(autor);

        repository.save(livro);

    }

    // padrão de se usar
    /**
     * Testa o fluxo mais comum:
     * - Cria e salva um autor
     * - Associa o autor a um livro
     * - Salva o livro
     */
    @Test
    void salvarAutorELivroTest() {
        Livro livro = new Livro();
        livro.setIsbn("90887-84874");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("Terceiro livro");
        livro.setDataPublicacao(LocalDate.of(1980, 1, 2));

        Autor autor = new Autor();
        autor.setNome("José");
        autor.setNacionalidade("Brasileira");
        autor.setDataNascimento(LocalDate.of(1951, 1, 31));

        autorRepository.save(autor);

        livro.setAutor(autor);

        repository.save(livro);
    }

    /**
     * Testa o comportamento de cascade no relacionamento.
     *
     * Caso o cascade esteja configurado,
     * o autor será salvo automaticamente
     * ao salvar o livro.
     */
    @Test
    void salvarCascadeTest() {
        Livro livro = new Livro();
        livro.setIsbn("90887-84874");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("Outro Livro");
        livro.setDataPublicacao(LocalDate.of(1980, 1, 2));

        Autor autor = new Autor();
        autor.setNome("João");
        autor.setNacionalidade("Brasileira");
        autor.setDataNascimento(LocalDate.of(1951, 1, 31));

        livro.setAutor(autor);

        repository.save(livro);
    }

    /**
     * Testa a atualização do autor associado a um livro.
     *
     * - Busca um livro existente
     * - Busca outro autor
     * - Substitui o autor do livro
     * - Salva a alteração
     */
    @Test
    void atualizarAutorDoLivro() {
        UUID id = UUID.fromString("679cb6a1-1d26-4fae-bde0-1914deb69152");
        var livroParaAtualizar = repository.findById(id).orElse(null);

        UUID idAutor = UUID.fromString("1fbcbe89-c9e5-4f3e-b9fe-ba3bbfcaafb6");
        var autorParaSubstituir = autorRepository.findById(idAutor).orElse(null);

        livroParaAtualizar.setAutor(autorParaSubstituir);

        repository.save(livroParaAtualizar);
    }

    /**
     * Testa a exclusão de um livro pelo seu ID.
     * Apenas o livro é removido.
     */
    @Test
    void deletarLivro() {
        UUID id = UUID.fromString("c8d4aa19-c0ca-474b-9930-379d4ba6edd6");
        repository.deleteById(id);
    }

    /**
     * Testa o efeito do cascade na exclusão.
     *
     * Dependendo da configuração,
     * se livro estive usando cascade autor relacionado
     * será removido
     */
    @Test
    void deletarCascade() {
        UUID id = UUID.fromString("cd2aee75-b774-4440-b844-74a5e756e58b");
        repository.deleteById(id);
    }


    /**
     * Testa a busca de um livro e o acesso ao autor.
     *
     * @Transactional é necessário para permitir
     * o carregamento LAZY do autor.
     */
    @Test
    @Transactional
    void buscarLivroTest() {
        UUID id = UUID.fromString("2c9b34b8-31aa-4d1d-a94c-b1c6b7daebf0");
        Livro livro = repository.findById(id).orElse(null);
        System.out.println("Livro:");
        System.out.println(livro.getTitulo());

        System.out.println("Autor:");
        System.out.println(livro.getAutor().getNome());
    }

    /**
     * Testa o Query Method que busca livros pelo título.
     */
    @Test
    void pesquisaPorTituloTest() {
        List<Livro> lista = repository.findByTitulo("5 passos de você");
        lista.forEach(System.out::println);
    }

    /**
     * Testa o Query Method que busca livros pelo ISBN.
     */
    @Test
    void pesquisaPorISBNTest() {
        List<Livro> lista = repository.findByIsbn("90887-84874");
        lista.forEach(System.out::println);
    }

    /**
     * Testa o Query Method que busca livros
     * pelo título e pelo preço.
     */
    @Test
    void pesquisaPorTituloEPrecoTest() {
        var preco = BigDecimal.valueOf(100.00);
        var tituloPesquisa = "Terceiro livro";

        List<Livro> lista = repository.findByTituloAndPreco(tituloPesquisa, preco);
        lista.forEach(System.out::println);
    }

    /**
     * Testa o Query Method que busca livros
     * pelo título e pelo preço.
     */
    @Test
    void pesquisaPorTituloOrIsbnTest() {
        var isbn = "90887-84874"; //90887-84874
        var tituloPesquisa = "Terceiro livro";

        List<Livro> lista = repository.findByTituloOrIsbn(tituloPesquisa, isbn);
        lista.forEach(System.out::println);
    }
}