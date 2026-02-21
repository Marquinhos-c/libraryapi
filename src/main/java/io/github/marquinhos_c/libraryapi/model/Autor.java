package io.github.marquinhos_c.libraryapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Entidade que representa um Autor no sistema.
 * Essa classe é mapeada para a tabela "autor" no banco de dados.
 */
@Entity
@Table(name = "autor", schema = "public")
@Getter
@Setter
@ToString (exclude = "livros")
public class Autor {

    /**
     * Identificador único do autor.
     * Gerado automaticamente no formato UUID.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "nacionalidade", length = 50, nullable = false)
    private String nacionalidade;

    /**
     * Lista de livros associados ao autor.
     *
     * Relacionamento Um-para-Muitos:
     * - Um autor pode possuir vários livros
     * - O mapeamento é feito pelo atributo "autor" na entidade Livro
     * - A tabela "livro" contém a chave estrangeira para o autor
     *
     * mappedBy = "autor":
     * - Indica que a entidade Livro é a dona do relacionamento
     * - A entidade Autor não cria coluna de chave estrangeira
     *
     * cascade = CascadeType.ALL:
     * - As operações realizadas no autor são propagadas para os livros
     * - Exemplo: salvar ou remover um autor afeta seus livros
     *
     * fetch = FetchType.LAZY:
     * - A lista de livros só é carregada quando acessada
     */
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Livro> livros;

}
