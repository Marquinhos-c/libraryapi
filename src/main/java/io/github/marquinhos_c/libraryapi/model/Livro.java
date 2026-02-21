package io.github.marquinhos_c.libraryapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entidade que representa um Livro no sistema.
 * Mapeada para a tabela "livro" no banco de dados.
 */
@Entity
@Table(name = "livro")
@Data
@ToString(exclude = "autor")
public class Livro {

    /**
     * Identificador único do livro.
     * Gerado automaticamente no formato UUID.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "isbn", length = 20, nullable = false)
    private String isbn;

    @Column(name = "titulo", length = 150, nullable = false)
    private String titulo;

    @Column(name = "data_publicacao")
    private LocalDate dataPublicacao;

    /**
     * Gênero do livro.
     * Enum armazenado como String no banco de dados.
     * evita problemas se a ordem do enum mudar
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "genero", length = 30, nullable = false)
    private GeneroLivro genero;

    /**
     * Preço do livro.
     * Utiliza BigDecimal para garantir precisão em valores monetários.
     * Precision 18 e scale 2 permitem valores grandes com duas casas decimais.
     * padrão correto para dinheiro
     */
    @Column(name = "preco", precision = 18, scale = 2)
    private BigDecimal preco;

    /**
     * Autor associado ao livro.
     *
     * Relacionamento Muitos-para-Um:
     * - Muitos livros podem estar associados a um único autor
     * - A tabela "livro" possui a chave estrangeira "id_autor"
     * - Esta entidade (Livro) é a dona do relacionamento
     *
     * Configurações importantes:
     * - fetch = FetchType.LAZY:
     *   O autor só será carregado do banco quando for acessado explicitamente
     *
     * - cascade (comentado):
     *   Demonstra o efeito de propagação de operações entre entidades
     *   Deve ser usado com cuidado neste tipo de relacionamento
     */
    @ManyToOne (
            //cascade = CascadeType.ALL //muito cuidado ao usar cascade se deletar um livro delata o autor junto
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "id_autor")
    private Autor autor;

}
