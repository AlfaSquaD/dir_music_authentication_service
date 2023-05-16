package com.dir.music.authentication_service.controller.auth_controller.input;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthControllerPasswordLoginInput {
    private String username;
    private String password;
}
