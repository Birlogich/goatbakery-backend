package org.example.mapper;

import org.example.dto.UserDto;
import org.example.entity.User;

import java.time.LocalDateTime;

public class CreateUserMapper implements Mapper <UserDto, User> {

    private static final CreateUserMapper INSTANCE = new CreateUserMapper();

    @Override
    public User mapFrom(UserDto object) {
        return User.builder()
                .firstName(object.getFirstName())
                .lastName(object.getLastName())
                .address(object.getAddress())
                .email(object.getEmail())
                .password(object.getPassword())
                .phoneNumber(object.getPhoneNumber())
                .createDate(LocalDateTime.now())
                .build();
    }

    public static CreateUserMapper getInstance() {
        return INSTANCE;
    }

}
