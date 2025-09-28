package com.project.bff_scheduler.business;

import com.project.bff_scheduler.business.dto.out.TarefasDTOResponse;
import com.project.bff_scheduler.infrastructure.client.EmailClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class EmailService {

    private final EmailClient emailClient;

    public void enviaEmail(TarefasDTOResponse dto) {
        emailClient.enviarEmail(dto);
    }
}