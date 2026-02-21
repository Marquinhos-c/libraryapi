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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTest {

    @Autowired
    AutorRepository repository;

    @Autowired
    LivroRepository livroRepository;

    @Test
    public void salvarTest() {
        Autor autor = new Autor();
        autor.setNome("Maria");
        autor.setNacionalidade("Brasileira");
        autor.setDataNascimento(LocalDate.of(1951, 1, 31));

        var autorSalvo = repository.save(autor);
        System.out.println("Autor salvo: " + autorSalvo);
    }

    @Test
    public void atualizarTest() {
        var id = UUID.fromString("754960d7-09d4-4a83-ba5e-2bb5cae8a5ab");

        Optional<Autor> possivelAutor = repository.findById(id);

        if (possivelAutor.isPresent()) {

            Autor autorEncontrado = possivelAutor.get();
            System.out.println("Dados do Autor: ");
            System.out.println(autorEncontrado);

            autorEncontrado.setDataNascimento(LocalDate.of(1960, 1, 30));
            repository.save(autorEncontrado);
        }
    }

    @Test
    public void listarTest() {
        List<Autor> lista = repository.findAll();
        lista.forEach(System.out::println);
    }

    @Test
    public void coutTest() {
        System.out.println("Contagem de autores: " + repository.count());
    }

    @Test
    public void deletePorIdTest() {
        var id = UUID.fromString("754960d7-09d4-4a83-ba5e-2bb5cae8a5ab");
        repository.deleteById(id);
    }

    @Test
    public void deletePorObjetoTest() {
        var id = UUID.fromString("bbb523b3-f048-4f70-a3bc-ed10278b4286");
        var autor = repository.findById(id).get();
        repository.delete(autor);
    }

    @Test
    void salvarAutorComLivrosTest() {
        Autor autor = new Autor();
        autor.setNome("Marcos");
        autor.setNacionalidade("Espanhol");
        autor.setDataNascimento(LocalDate.of(2003, 5, 24));

        Livro livro = new Livro();
        livro.setIsbn("20887-84874");
        livro.setPreco(BigDecimal.valueOf(204));
        livro.setGenero(GeneroLivro.ROMANCE);
        livro.setTitulo("5 passos de você");
        livro.setDataPublicacao(LocalDate.of(2015, 1, 2));
        livro.setAutor(autor);

        Livro livro2 = new Livro();
        livro2.setIsbn("50887-87852");
        livro2.setPreco(BigDecimal.valueOf(99));
        livro2.setGenero(GeneroLivro.BIOGRAFIA);
        livro2.setTitulo("O Atleta");
        livro2.setDataPublicacao(LocalDate.of(2017, 5, 2));
        livro2.setAutor(autor);

        autor.setLivros(new ArrayList<>());
        autor.getLivros().add(livro);
        autor.getLivros().add(livro2);

        repository.save(autor);

//        livroRepository.saveAll(autor.getLivros());

    }

    @Test
    void listarLivrosAutor() {
        var id = UUID.fromString("3e97a390-0572-4b6c-bc2d-08e29b519c04");
        var autor = repository.findById(id).get();

        // buscar os livros do autor
        List<Livro> livrosLista = livroRepository.findByAutor(autor);
        autor.setLivros(livrosLista);

        autor.getLivros().forEach(System.out::println);

    }

}
