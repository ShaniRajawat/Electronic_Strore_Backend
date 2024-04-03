package com.sr.electronic.store.Electronic_Store.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDtos {

    private String categoryId;
    @NotBlank(message = "Title is Required !!")
    @Size(min = 4,message = "Title must be of minimum 4 characters")
    private String title;
    @NotBlank(message = "Description required !!")
    private String description;

    private String coverImage;
}
