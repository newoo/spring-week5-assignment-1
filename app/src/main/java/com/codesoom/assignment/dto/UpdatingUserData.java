package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Builder
public class UpdatingUserData {
    private Long id;

    @NotBlank
    @Mapping("name")
    private String name;

    @Mapping("email")
    private String email;

    @NotBlank
    @Mapping("password")
    private String password;
}
