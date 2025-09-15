package com.project.bff_scheduler.controller;

import com.project.bff_scheduler.business.UsuarioService;
import com.project.bff_scheduler.business.dto.EnderecoDTO;
import com.project.bff_scheduler.business.dto.TelefoneDTO;
import com.project.bff_scheduler.business.dto.UsuarioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Tag(name = "Usuário", description = "Cadastro, login e usuários")

public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Salvar Usuários", description = "Cria um usuário.")
    @ApiResponse(responseCode = "200", description = "Usuário salvo com sucesso!")
    @ApiResponse(responseCode = "400", description = "Usuário já cadastrado!")
    @ApiResponse(responseCode = "500", description = "Erro de servidor!")
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }

    @PostMapping("/login")
    @Operation(summary = "Login Usuários", description = "Login do usuário.")
    @ApiResponse(responseCode = "200", description = "Usuário logado com sucesso!")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas!")
    @ApiResponse(responseCode = "500", description = "Erro de servidor!")
    public String login(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.loginUsuario(usuarioDTO);
    }

    @GetMapping
    @Operation(summary = "Busca Dados de Usuários por Email",
            description = "Buscar dados do usário.")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado!")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado!")
    @ApiResponse(responseCode = "500", description = "Erro de servidor!")
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorEmail(@RequestParam("email") String email,
                                                           @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email, token));
    }

    @DeleteMapping("/{email}")
    @Operation(summary = "Deletar Usuários por ID", description = "Deleta usuário.")
    @ApiResponse(responseCode = "200", description = "Usuário excluído com sucesso!")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado!")
    @ApiResponse(responseCode = "500", description = "Erro de servidor!")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email,
                                                      @RequestHeader("Authorization") String token) {
        usuarioService.deletaUsuarioPorEmail(email, token);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Operation(summary = "Atualiza dados de usuário",
            description = "Atualizar dados do usuário.")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso!")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado!")
    @ApiResponse(responseCode = "500", description = "Erro de servidor!")
    public ResponseEntity<UsuarioDTO> atualizaDadoUsuario(@RequestBody UsuarioDTO dto,
                                                          @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.atualizaDadosUsuario(token, dto));
    }

    @PutMapping("/endereco")
    @Operation(summary = "Atualiza Endereço de Usuários",
            description = "Atualizar endereço do usuário.")
    @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso!")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado!")
    @ApiResponse(responseCode = "500", description = "Erro de servidor!")
    public ResponseEntity<EnderecoDTO> atualizaEndereco(@RequestBody EnderecoDTO dto,
                                                        @RequestParam("id") Long id,
                                                        @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, dto, token));
    }

    @PutMapping("/telefone")
    @Operation(summary = "Atualiza Telefone de Usuários",
            description = "Atualizar telefone do usuário.")
    @ApiResponse(responseCode = "200", description = "Telefone atualizado com sucesso!")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado!")
    @ApiResponse(responseCode = "500", description = "Erro de servidor!")
    public ResponseEntity<TelefoneDTO> atualizaTelefone(@RequestBody TelefoneDTO dto,
                                                        @RequestParam("id") Long id,
                                                        @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(usuarioService.atualizaTelefone(id, dto, token));
    }

    @PostMapping("/endereco")
    @Operation(summary = "Salva Endereço de Usuários",
            description = "Salvar endereço do usuário.")
    @ApiResponse(responseCode = "200", description = "Endereço salvo com sucesso!")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado!")
    @ApiResponse(responseCode = "500", description = "Erro de servidor!")
    public ResponseEntity<EnderecoDTO> cadastraEndereco(@RequestBody EnderecoDTO dto,
                                                        @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(usuarioService.cadastraEndereco(token, dto));
    }

    @PostMapping("/telefone")
    @Operation(summary = "Salva Telefone de Usuários",
            description = "Salvar telefone do usuário.")
    @ApiResponse(responseCode = "200", description = "Telefone salvo com sucesso!")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado!")
    @ApiResponse(responseCode = "500", description = "Erro de servidor!")
    public ResponseEntity<TelefoneDTO> cadastraTelefone(@RequestBody TelefoneDTO dto,
                                                        @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(usuarioService.cadastraTelefone(token, dto));
    }
}
