package com.project.bff_scheduler.controller;

import com.project.bff_scheduler.business.TarefasService;
import com.project.bff_scheduler.business.dto.in.TarefasDTORequest;
import com.project.bff_scheduler.business.dto.out.TarefasDTOResponse;
import com.project.bff_scheduler.business.enums.StatusNotificationEnum;
import com.project.bff_scheduler.infrastructure.client.security.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
@Tag(name = "Tarefas", description = "Cadastra tarefas de usuários")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)


public class TarefasController {

    private final TarefasService tarefasService;

    @PostMapping
    @Operation(summary = "Salvar Tarefas", description = "Cria uma nova tarefa.")
    @ApiResponse(responseCode = "200", description = "Tarefa salva com sucesso!")
    @ApiResponse(responseCode = "500", description = "Erro de servidor!")
    public ResponseEntity<TarefasDTOResponse> gravarTarefas(@RequestBody TarefasDTORequest dto,
                                                            @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(tarefasService.gravarTarefas(token, dto));
    }

    @GetMapping("/eventos")
    @Operation(summary = "Busca Tarefas por Período", description = "Buscar tarefas cadastradas por período.")
    @ApiResponse(responseCode = "200", description = "Tarefas encontradas")
    @ApiResponse(responseCode = "500", description = "Erro de servidor!")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado")
    public ResponseEntity<List<TarefasDTOResponse>> buscaListaTarefaPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal,
            @RequestHeader(name = "Authorization", required = false) String token) {

        return ResponseEntity.ok(tarefasService.buscaTarefaAgendadaPorPeriodo(dataInicial, dataFinal, token));
    }

    @GetMapping
    @Operation(summary = "Busca Tarefas por Email do Usuário",
            description = "Buscar lista de tarefas cadastradas por usuário.")
    @ApiResponse(responseCode = "200", description = "Tarefas encontradas")
    @ApiResponse(responseCode = "500", description = "Erro de servidor!")
    @ApiResponse(responseCode = "403", description = "Email não encontrado")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado")
    public ResponseEntity<List<TarefasDTOResponse>> buscaTarefasPorEmail(@RequestHeader(name = "Authorization", required = false) String token) {
        List<TarefasDTOResponse> tarefas = tarefasService.buscaTarefasPorEmail(token);

        return ResponseEntity.ok(tarefas);
    }

    @DeleteMapping
    @Operation(summary = "Deleta Tarefa por ID", description = "Deleta tarefas cadastradas pelo ID.")
    @ApiResponse(responseCode = "200", description = "Tarefa deletada")
    @ApiResponse(responseCode = "500", description = "Erro de servidor!")
    @ApiResponse(responseCode = "403", description = "Tarefa ID não encontrado")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado")
    public ResponseEntity<Void> deletaTarefaPorId(@RequestParam("id") String id,
                                                  @RequestHeader(name = "Authorization", required = false) String token) {
        tarefasService.deletaTarefaPorId(id, token);

        return ResponseEntity.ok().build();
    }

    @PatchMapping
    @Operation(summary = "Altera Status da Tarefa", description = "Altera status de tarefa cadastrada.")
    @ApiResponse(responseCode = "200", description = "Status alterado com sucesso!")
    @ApiResponse(responseCode = "500", description = "Erro de servidor!")
    @ApiResponse(responseCode = "403", description = "Tarefa ID não encontrado")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado")
    public ResponseEntity<TarefasDTOResponse> alteraStatusNotificacao(@RequestParam("status") StatusNotificationEnum status,
                                                                      @RequestParam("id") String id,
                                                                      @RequestHeader(name = "Authorization", required = false) String token) {

        return ResponseEntity.ok(tarefasService.alteraStatus(status, id, token));
    }

    @PutMapping
    @Operation(summary = "Alterado dados de Tarefas", description = "Altera dados das tarefas cadastradas.")
    @ApiResponse(responseCode = "200", description = "Tarefas alteradas com sucesso!")
    @ApiResponse(responseCode = "500", description = "Erro de servidor!")
    @ApiResponse(responseCode = "403", description = "Tarefa ID não encontrado")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado")
    public ResponseEntity<TarefasDTOResponse> updateTarefas(@RequestBody TarefasDTORequest dto, @RequestParam("id") String id,
                                                            @RequestHeader(name = "Authorization", required = false) String token) {
        return ResponseEntity.ok(tarefasService.updateTarefas(dto, id, token));
    }
}
