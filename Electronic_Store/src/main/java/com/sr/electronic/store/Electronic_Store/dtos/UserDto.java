package com.sr.electronic.store.Electronic_Store.dtos;


import com.sr.electronic.store.Electronic_Store.validate.ImageNameValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
        private String userId;

        @Size(min =3, max = 20, message = "Invalid Name !!")
        @Schema(name = "user_name", accessMode = Schema.AccessMode.READ_ONLY, description = "Username of Entity")
        private String name;

//        @Email(message = "Invalid User Email !!")
        @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message = "Invalid User Email !!")
        @NotBlank(message = "Email is required")
        private String email;

        @NotBlank(message = "Password is required !!")
        private String password;

        @Size(min =4,max = 6,message = "Invalid gender !!")
        private  String gender;

        @NotBlank(message = "Write something about yourself")
        private String about;

        @ImageNameValid
        private String imageName;

        private Set<RolesDto> roles = new HashSet<>();
}
