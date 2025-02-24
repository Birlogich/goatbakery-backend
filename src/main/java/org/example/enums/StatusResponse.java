package org.example.enums;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum StatusResponse {

    SUCCESS ("Success"),
    ERROR ("Error");

    private String status;
}
