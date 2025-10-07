package com.project.bff_scheduler.business;

import com.project.bff_scheduler.business.dto.in.LoginRequestDTO;
import com.project.bff_scheduler.business.dto.out.TarefasDTOResponse;
import com.project.bff_scheduler.business.enums.StatusNotificationEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class CronService {

    private final TarefasService tarefasService;
    private final EmailService emailService;
    private final UsuarioService usuarioService;

    @Value("${usuario.email}")
    private String email;

    @Value("${usuario.senha}")
    private String senha;

//    pendente para revisão futura
    @Scheduled(cron = "${cron.horario}")
    public void buscaTarefasDaProximaHora() {
        String token = login(converterParaRequestDTO());
        LocalDateTime horaAtual = LocalDateTime.now();
        LocalDateTime horaFutura = LocalDateTime.now().plusHours(1);
        // Qq tarefa que fique entre hora atual - e a hora futura + 1
        // Se agr é 22h - qq tarefa que esteja cadastrada entre 22h e 23h

        List<TarefasDTOResponse> listaTarefas = tarefasService.buscaTarefaAgendadaPorPeriodo(horaAtual, horaFutura, token);
        listaTarefas.forEach(tarefa -> {
            emailService.enviaEmail(tarefa);
            tarefasService.alteraStatus(StatusNotificationEnum.NOTIFICADO, tarefa.getId(),
                    token);
        });
    }

    public String login(LoginRequestDTO dto) {
        return usuarioService.loginUsuario(dto);
    }

    public LoginRequestDTO converterParaRequestDTO() {
        return LoginRequestDTO.builder()
                .email(email)
                .senha(senha)
                .build();
    }
}




