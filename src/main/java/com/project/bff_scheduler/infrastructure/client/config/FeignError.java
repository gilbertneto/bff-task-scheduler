package com.project.bff_scheduler.infrastructure.client.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bff_scheduler.infrastructure.exception.BusinessException;
import com.project.bff_scheduler.infrastructure.exception.ConflictException;
import com.project.bff_scheduler.infrastructure.exception.IllegalArgumentException;
import com.project.bff_scheduler.infrastructure.exception.ResourceNotFoundException;
import com.project.bff_scheduler.infrastructure.exception.UnauthorizedException;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class FeignError implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        String mensagemErro = extrairMensagem(response);

        return switch (response.status()) {
            case 409 -> new ConflictException(mensagemErro);
            case 404 -> new ResourceNotFoundException(mensagemErro);
            case 401, 403 -> new UnauthorizedException(mensagemErro);
            case 400 -> new BusinessException(mensagemErro);
            default -> new BusinessException("Erro na comunicação entre serviços (" + response.status() + "): " + mensagemErro);
        };
    }

    private String extrairMensagem(Response response) {
        if (response.body() == null) {
            return "Erro sem detalhes retornado pelo serviço remoto.";
        }
        try (InputStream bodyIs = response.body().asInputStream()) {
            String rawBody = new String(bodyIs.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode jsonNode = objectMapper.readTree(rawBody);
            if (jsonNode.has("message") && !jsonNode.get("message").asText().isBlank()) {
                return jsonNode.get("message").asText();
            }
            return rawBody;
        } catch (IOException e) {
            return "Erro ao ler a resposta do serviço remoto.";
        }
    }
}
