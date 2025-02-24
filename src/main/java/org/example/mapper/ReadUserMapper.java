package org.example.mapper;
import org.example.entity.User;

public class ReadUserMapper implements Mapper<User, User> {

    public static final ReadUserMapper INSTANCE = new ReadUserMapper();

    @Override
    public User mapFrom(User object) {
        return User.builder()
                .id(object.getId())
                .firstName(object.getFirstName())
                .lastName(object.getLastName())
                .address(object.getAddress())
                .email(object.getEmail())
                .password(object.getPassword())
                .phoneNumber(object.getPhoneNumber())
                .createDate(object.getCreateDate())
                .updateDate(object.getUpdateDate())
                .build();
    }

    public static ReadUserMapper getInstance() { return INSTANCE; }

}
