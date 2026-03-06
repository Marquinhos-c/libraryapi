package io.github.marquinhos_c.libraryapi.service;

import io.github.marquinhos_c.libraryapi.model.Autor;
import io.github.marquinhos_c.libraryapi.model.GeneroLivro;
import io.github.marquinhos_c.libraryapi.model.Livro;
import io.github.marquinhos_c.libraryapi.repository.AutorRepository;
import io.github.marquinhos_c.libraryapi.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TransacaoService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Transactional
    public void executar() {
        // salva o autor
        Autor autor = new Autor();
        autor.setNome("Test Francisco");
        autor.setNacionalidade("Brasileira");
        autor.setDataNascimento(LocalDate.of(1951, 1, 31));

        autorRepository.saveAndFlush(autor);

        // salva o livro
        Livro livro = new Livro();
        livro.setIsbn("90887-84874");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("Test Pequeno Principe");
        livro.setDataPublicacao(LocalDate.of(1989, 2, 28));


        livro.setAutor(autor);

        livroRepository.saveAndFlush(livro);

        if (autor.getNome().equals("Test Francisco")) {
            throw new IllegalArgumentException("Rollback! ");
        }
    }

}
