package com.example.trpp_project.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * DTO class for authentication (login) request.
 *
 * @author Eugene Suleimanov
 * @version 1.0
 */

@Data
public class AuthenticationRequestDto {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
