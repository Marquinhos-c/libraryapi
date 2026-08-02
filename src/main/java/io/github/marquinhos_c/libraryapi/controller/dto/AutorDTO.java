package io.github.marquinhos_c.libraryapi.controller.dto;

import io.github.marquinhos_c.libraryapi.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) responsável por transportar
 * os dados de um Autor entre o cliente e a API.
 *
 * <p>Esta classe utiliza o recurso Record do Java para
 * armazenar apenas os dados necessários para a requisição
 * e resposta da API, evitando expor diretamente a entidade
 * Autor.</p>
 *
 * Fluxo:
 * Cliente -> JSON -> AutorDTO -> Autor -> Banco de Dados
 */
public record AutorDTO(
        UUID id,

        @NotBlank(message = "campo obrigatorio")
        @Size(max = 100, min = 2, message = "campo fora do tamanho padrão")
        String nome,

        @NotNull(message = "campo obrigatorio")
        @Past(message = "não pode ser data futura")
        LocalDate dataNascimento,

        @NotBlank(message = "campo obrigatorio")
        @Size(max = 50, min = 2, message = "campo fora do tamanho padrão")
        String nacionalidade) {

    /**
     * Converte o DTO para uma entidade Autor.
     *
     * <p>Esse método é utilizado para transformar os dados
     * recebidos pela API em um objeto da camada de domínio
     * (Entidade), que poderá ser manipulado pela camada
     * Service e posteriormente persistido no banco de dados.</p>
     *
     * <p>O campo ID não é mapeado neste método, pois normalmente
     * ele é gerado automaticamente pela aplicação ou pelo banco
     * de dados durante o processo de cadastro.</p>
     *
     * @return objeto Autor preenchido com os dados do DTO.
     */
    public Autor mapearParaAutor() {

        // Cria uma nova instância da entidade Autor
        Autor autor = new Autor();
        // Copia o nome informado no DTO para a entidade
        autor.setNome(this.nome);
        // Copia a nacionalidade para a entidade
        autor.setDataNascimento(this.dataNascimento);
        // Copia a nacionalidade para a entidade
        autor.setNacionalidade(this.nacionalidade);

        // Retorna a entidade pronta para ser utilizada
        // pela camada Service ou Repository
        return autor;
    }
}