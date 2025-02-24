package org.example.entity;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemInOrder {
    private int id;

    @NotNull(message = "Item cannot be null")
    private Item item;

    private Order order;

    @Min(value = 1, message = "Quantity must be above zero")
    private int quantity;
}
