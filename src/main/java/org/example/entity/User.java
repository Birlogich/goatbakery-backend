package org.example.entity;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
@Builder
@Setter
public class User {
    int id;
    String firstName;
    String lastName;
    String address;
    String email;
    String password;
    String phoneNumber;
    LocalDateTime createDate;
    LocalDateTime updateDate;
}
