package com.owu.geekhub.jwtmessage.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class VerifyForm {
    @NotBlank
    @Size(min=3, max = 60)
    private String username;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    
}
