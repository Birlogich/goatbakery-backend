package org.example.dto;

import lombok.*;
import org.example.entity.Item;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class CommentDto {

    @NotNull(message = "User cannot be empty")
    BasicUserDto user;

    @NotNull(message = "Item cannot be empty")
    Item item;

    @NotBlank(message = "Comment cannot be empty")
    String comment;

}
