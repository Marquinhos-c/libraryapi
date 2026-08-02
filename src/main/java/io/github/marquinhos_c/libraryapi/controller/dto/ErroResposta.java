package io.github.marquinhos_c.libraryapi.controller.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * DTO responsável por padronizar as respostas de erro da API.
 *
 * <p>Ao invés de retornar apenas uma String com a mensagem
 * de erro, a API retorna uma estrutura organizada contendo:</p>
 *
 * <ul>
 *     <li>Status HTTP da resposta</li>
 *     <li>Mensagem principal do erro</li>
 *     <li>Lista de erros de validação (quando existirem)</li>
 * </ul>
 *
 * Exemplo:
 *
 * {
 *   "status": 400,
 *   "mensagem": "Erro de validação",
 *   "erros": [
 *      {
 *          "campo": "nome",
 *          "erro": "Campo obrigatório"
 *      }
 *   ]
 * }
 *
 * @param status Código HTTP da resposta.
 * @param mensagem Mensagem principal do erro.
 * @param erros Lista de erros detalhados por campo.
 */
public record ErroResposta(int status, String mensagem, List<ErroCampo> erros) {

    /**
     * Cria uma resposta padrão para erros do tipo
     * BAD_REQUEST (HTTP 400).
     *
     * <p>Utilizada quando a requisição enviada pelo
     * cliente possui dados inválidos.</p>
     *
     * Exemplo:
     * {
     *   "status": 400,
     *   "mensagem": "Dados inválidos",
     *   "erros": []
     * }
     *
     * @param mensagem Mensagem do erro.
     * @return Objeto ErroResposta com status 400.
     */
    public static ErroResposta respostaPadrao(String mensagem) {
        return new ErroResposta(HttpStatus.BAD_REQUEST.value(), mensagem, List.of());
    }

    /**
     * Cria uma resposta para situações de conflito
     * (HTTP 409).
     *
     * <p>Utilizada quando o recurso já existe ou quando
     * ocorre alguma inconsistência de negócio.</p>
     *
     * Exemplo:
     * {
     *   "status": 409,
     *   "mensagem": "Autor já cadastrado",
     *   "erros": []
     * }
     *
     * @param mensagem Mensagem do conflito.
     * @return Objeto ErroResposta com status 409.
     */
    public static ErroResposta conflito(String mensagem) {
        return new ErroResposta(HttpStatus.CONFLICT.value(), mensagem, List.of());
    }
}