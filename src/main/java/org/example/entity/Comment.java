package org.example.entity;

import lombok.*;
import org.example.dto.BasicUserDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Comment {
    @Min(value = 1, message = "ID cannot be below one")
    int id;

    @NotNull(message = "User cannot be empty")
    BasicUserDto user;

    @NotNull(message = "Item cannot be empty")
    Item item;

    @NotBlank(message = "Description cannot be empty")
    String comment;
}
