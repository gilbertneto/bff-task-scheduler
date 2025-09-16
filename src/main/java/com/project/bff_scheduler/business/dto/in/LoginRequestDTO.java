package com.project.bff_scheduler.business.dto.in;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LoginRequestDTO {

    private String email;
    private String senha;
}
