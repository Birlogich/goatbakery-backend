package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.entity.ItemInOrder;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    @Min(value = 1, message = "ID must be above zero")
    private int customerId;

    @NotEmpty(message = "List of items cannot be empty")
    private List<@NotNull ItemInOrder> itemInOrders;

}
