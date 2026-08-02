package io.github.marquinhos_c.libraryapi.controller.dto;

/**
 * DTO utilizado para representar um erro de validação
 * associado a um campo específico da requisição.
 *
 * <p>Normalmente é utilizado quando a API recebe dados
 * inválidos enviados pelo cliente.</p>
 *
 * Exemplo de retorno:
 *
 * {
 *   "campo": "nome",
 *   "erro": "O nome é obrigatório"
 * }
 *
 * @param campo Nome do campo que apresentou erro.
 * @param erro Mensagem descrevendo o problema encontrado.
 */
public record ErroCampo(String campo, String erro) {

}
