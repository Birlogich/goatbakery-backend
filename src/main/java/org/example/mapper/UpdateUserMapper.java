package org.example.mapper;

import org.example.dto.UserDto;
import org.example.entity.User;

import java.time.LocalDateTime;

public class UpdateUserMapper implements Mapper<UserDto, User>   {

    public static final UpdateUserMapper INSTANCE = new UpdateUserMapper();

    @Override
    public User mapFrom(UserDto object) {
        return User.builder()
                .firstName(object.getFirstName())
                .lastName(object.getLastName())
                .address(object.getAddress())
                .email(object.getEmail())
                .password(object.getPassword())
                .phoneNumber(object.getPhoneNumber())
                .updateDate(LocalDateTime.now())
                .build();
    }

    public static UpdateUserMapper getInstance() { return INSTANCE; }

}
