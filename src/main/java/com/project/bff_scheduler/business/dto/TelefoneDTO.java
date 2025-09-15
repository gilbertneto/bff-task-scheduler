package com.project.bff_scheduler.business.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TelefoneDTO {
    private Long id;
    private String numero;
    private String ddd;
}
