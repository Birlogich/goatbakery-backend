package org.example.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.example.enums.BakingItem;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Order {
    private int id;
    private int customerId;
    private List<ItemInOrder> itemInOrders;
    private LocalDateTime createDate;
    private int totalSum;
}
