package com.project.bff_scheduler.business;

import com.project.bff_scheduler.business.dto.EnderecoDTO;
import com.project.bff_scheduler.business.dto.TelefoneDTO;
import com.project.bff_scheduler.business.dto.UsuarioDTO;
import com.project.bff_scheduler.client.UsuarioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UsuarioService {
    private final UsuarioClient client;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        return client.salvaUsuario(usuarioDTO);

    }

    public String loginUsuario(UsuarioDTO usuarioDTO) {
        return client.login(usuarioDTO);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email, String token) {
        return client.buscaUsuarioPorEmail(email, token);
    }

    public void deletaUsuarioPorEmail(String email, String token) {
        client.deletaUsuarioPorEmail(email, token);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {
        return client.atualizaDadoUsuario(dto, token);
    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO, String token) {
        return client.atualizaEndereco(enderecoDTO, idEndereco, token);
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO dto, String token) {
        return client.atualizaTelefone(dto, idTelefone, token);
    }

    public EnderecoDTO cadastraEndereco(String token, EnderecoDTO dto) {
        return client.cadastraEndereco(dto, token);

    }

    public TelefoneDTO cadastraTelefone(String token, TelefoneDTO dto) {
        return client.cadastraTelefone(dto, token);

    }

}