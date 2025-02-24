package org.example.service;

import org.example.dao.UserDao;
import org.example.dto.UserDto;
import org.example.entity.User;
import org.example.mapper.CreateUserMapper;
import org.example.mapper.ReadUserMapper;
import org.example.mapper.UpdateUserMapper;
import java.util.Optional;


public class UserService {

    private static final UserService INSTANCE = new UserService();
    private static final CreateUserMapper createUserMapper = CreateUserMapper.getInstance();
    private static final UserDao userDao = UserDao.getInstance();
    private static final ReadUserMapper readUserMapper = ReadUserMapper.getInstance();
    private static final UpdateUserMapper updateUserMapper = UpdateUserMapper.getInstance();

    private UserService() {}

    public Optional<User> findByEmail (String email) {

        return userDao.findByEmail(email);
    }

    public Integer create(UserDto userDto) {
        final User userEntity = createUserMapper.mapFrom(userDto);
        userDao.save(userEntity);
        return userEntity.getId();
    }

    public Optional<User> getUserById (String id) {
        return userDao.findById(id)
                .map(readUserMapper::mapFrom);

    }

    public boolean deleteUserById (String id) {
        return userDao.delete(id);
    }


    public static UserService getInstance() {return INSTANCE ;}

    public User editUser(UserDto userDto, int userId) {
        final User userEntity = updateUserMapper.mapFrom(userDto);
        return userDao.edit(userEntity, userId);
    }
}
