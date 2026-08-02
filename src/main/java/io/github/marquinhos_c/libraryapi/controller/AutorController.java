package io.github.marquinhos_c.libraryapi.controller;

import io.github.marquinhos_c.libraryapi.controller.dto.AutorDTO;
import io.github.marquinhos_c.libraryapi.controller.dto.ErroResposta;
import io.github.marquinhos_c.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.marquinhos_c.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.marquinhos_c.libraryapi.model.Autor;
import io.github.marquinhos_c.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller responsável por expor os endpoints REST relacionados
 * ao gerenciamento de autores.
 *
 * Esta camada é a porta de entrada da API e possui as seguintes
 * responsabilidades:
 *
 * - Receber requisições HTTP.
 * - Converter JSON em objetos Java (DTOs).
 * - Chamar a camada Service.
 * - Retornar respostas HTTP apropriadas.
 * - Traduzir exceções em respostas para o cliente.
 *
 * Fluxo:
 *
 * Cliente
 *    ↓
 * Controller
 *    ↓
 * Service
 *    ↓
 * Repository
 *    ↓
 * Banco de Dados
 *
 * URL Base:
 * /autores
 */
@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController {

    /**
     * Serviço responsável pelas regras de negócio dos autores.
     *
     * O Spring injeta automaticamente esta dependência
     * através do @RequiredArgsConstructor.
     */
    private final AutorService autorService;

    /**
     * Cadastra um novo autor.
     * POST /autores
     */
    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO autor) {

        try {
            // Converte o DTO recebido na requisição para uma entidade Autor.
            // O DTO é usado para transportar dados entre cliente e API.
            Autor autorEntidade = autor.mapearParaAutor();
            autorService.salvar(autorEntidade);

            // Monta a URL do recurso criado
            // http://localhost:8080/autores/76e7c418-ccf9-4e2a-af20-c28b9e50ab55
            URI location = ServletUriComponentsBuilder

                    // Obtém a URL atual da requisição.
                    // Exemplo: http://localhost:8080/autores
                    .fromCurrentRequest()

                    // Adiciona "/{id}" ao final da URL.
                    // Resultado temporário:
                    // http://localhost:8080/autores/{id}
                    .path("/{id}")

                    // Substitui {id} pelo ID gerado após o salvamento.
                    // Exemplo:
                    // http://localhost:8080/autores/76e7c418-ccf9-4e2a-af20-c28b9e50ab55
                    .buildAndExpand(autorEntidade.getId())

                    // Converte para um objeto URI.
                    .toUri();

            // Retorna:
            // HTTP Status 201 (Created)
            // Header Location contendo a URL do recurso criado.
            return ResponseEntity.created(location).build();

        } catch (RegistroDuplicadoException e) {
            var errorDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(errorDTO.status()).body(errorDTO);
        }
    }


    /**
     * Busca um autor pelo ID.
     * GET /autores/{id}
     */
    @GetMapping("{id}")
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorId(idAutor);
        if (autorOptional.isPresent()) {
            Autor autor = autorOptional.get();
            AutorDTO dto = new AutorDTO(
                    autor.getId(),
                    autor.getNome(),
                    autor.getDataNascimento(),
                    autor.getNacionalidade()
            );
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }


    /**
     * Remove um autor.
     * Não permite exclusão se possuir livros.
     * DELETE /autores/{id}
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") String id) {

        try {
            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = autorService.obterPorId(idAutor);

            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            autorService.deletar(autorOptional.get());
            return ResponseEntity.noContent().build();
        }catch (OperacaoNaoPermitidaException e) {
            var erroResposta = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroResposta.status()).body(erroResposta);
        }
    }


    /**
     * Pesquisa autores por nome e/ou nacionalidade.
     * GET /autores
     */
    @GetMapping
    public ResponseEntity<List<AutorDTO>> pesquisar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade){

        List<Autor> resultado = autorService.pesquisaByExample(nome, nacionalidade);
        // Converte entidades em DTOs
        List<AutorDTO> lista = resultado
                .stream()
                .map(autor -> new AutorDTO(
                        autor.getId(),
                        autor.getNome(),
                        autor.getDataNascimento(),
                        autor.getNacionalidade())
                ).collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }


    /**
     * Atualiza um autor existente.
     * PUT /autores/{id}
     */
    @PutMapping("{id}")
    public ResponseEntity<Object> atualizar(
            @PathVariable("id") String id,
            @RequestBody @Valid AutorDTO dto) {

        try {
            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = autorService.obterPorId(idAutor);

            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var autor = autorOptional.get();
            autor.setNome(dto.nome());
            autor.setNacionalidade(dto.nacionalidade());
            autor.setDataNascimento(dto.dataNascimento());

            autorService.atualizar(autor);

            return ResponseEntity.noContent().build();
        }catch (RegistroDuplicadoException e) {
            var errorDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(errorDTO.status()).body(errorDTO);
        }
    }
}
