package com.project.bff_scheduler.business;

import com.project.bff_scheduler.business.dto.in.TarefasDTORequest;
import com.project.bff_scheduler.business.dto.out.TarefasDTOResponse;
import com.project.bff_scheduler.business.enums.StatusNotificationEnum;
import com.project.bff_scheduler.infrastructure.client.TarefasClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class TarefasService {

    private final TarefasClient tarefasClient;

    public TarefasDTOResponse gravarTarefas(String token, TarefasDTORequest dto) {
        return tarefasClient.gravarTarefas(dto, token);
    }

    public List<TarefasDTOResponse> buscaTarefaAgendadaPorPeriodo(LocalDateTime dataInicial,
                                                                  LocalDateTime dataFinal,
                                                                  String token) {
        return tarefasClient.buscaListaTarefaPorPeriodo(dataInicial, dataFinal, token);
    }

    public List<TarefasDTOResponse> buscaTarefasPorEmail(String token) {
        return tarefasClient.buscaTarefasPorEmail(token);
    }

    public void deletaTarefaPorId(String id, String token) {
        tarefasClient.deletaTarefaPorId(token, id);
    }

    public TarefasDTOResponse alteraStatus(StatusNotificationEnum status, String id,
                                           String token) {
        return tarefasClient.alteraStatusNotificacao(status, id, token);
    }

    public TarefasDTOResponse updateTarefas(TarefasDTORequest dto, String id, String token) {
        return tarefasClient.updateTarefas(dto, id, token);
    }

}