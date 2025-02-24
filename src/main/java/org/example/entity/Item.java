package org.example.entity;

import lombok.*;
import org.example.enums.BakingItem;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Item {

    int id;

    @NotNull(message = "BakingItem cannot be null")
    private BakingItem bakingItem;


    private int price;


    private String description;

}
