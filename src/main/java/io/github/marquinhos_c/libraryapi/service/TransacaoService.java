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
import java.util.UUID;

@Service
public class TransacaoService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    /**
     * Exemplo de atualização de entidade dentro de uma transação.
     *
     * Neste caso o JPA utiliza o mecanismo chamado Dirty Checking,
     * onde ele detecta automaticamente alterações feitas em entidades
     * gerenciadas pelo contexto de persistência.
     *
     * Mesmo sem chamar save(), o Hibernate irá gerar um UPDATE
     * no banco ao final da transação.
     */
    @Transactional
    public void atualizacaoSemAtualizar() {
        // Busca um livro pelo ID
        var livro = livroRepository
                .findById(UUID.fromString("7c9010f5-ff02-4d97-a518-7ba5209b4cae"))
                .orElse(null);

        // Altera a data de publicação
        // O Hibernate detectará essa mudança automaticamente
        livro.setDataPublicacao(LocalDate.of(2024, 3, 5));
    }


    /**
     * Exemplo de transação envolvendo múltiplas operações no banco.
     *
     * Caso ocorra qualquer RuntimeException dentro do método,
     * toda a transação será revertida (rollback).
     */
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

        // Lança uma exceção proposital para demonstrar rollback
        if (autor.getNome().equals("Test Francisco")) {
            throw new IllegalArgumentException("Rollback! ");
        }
    }

}
