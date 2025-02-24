package org.example.dao;

import lombok.SneakyThrows;
import org.example.controller.ServiceMain;
import org.example.dto.BasicUserDto;
import org.example.entity.User;
import org.example.exception.ApiException;
import org.example.utils.ConnectionManager;
import org.example.utils.ConnectionPoolManager;
import org.example.utils.LogbackWrapper;
import org.example.utils.PasswordUtil;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.sql.Statement.*;

public class UserDao implements EntityDao <User, Integer, String>{

    private static final DataSource dataSource = ConnectionPoolManager.getDataSource();

    public static final UserDao INSTANCE = new UserDao();
    protected static Logger log = LogbackWrapper.getLogger(ServiceMain.class);


    public static final String SAVE = "insert into Users " +
            "(firstName, lastName, address, email, password, phoneNumber, createDate, updateDate) " +
            "VALUES " +"(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_EMAIL_AND_PASSWORD = "SELECT * FROM Users WHERE email = ? AND password = ?";
    private static final String FIND_BY_EMAIL = "SELECT * FROM Users WHERE email = ?";
    private static final String CHECK_EMAIL = "SELECT email from Users where email = ?";
    private static final String FIND_BY_ID = "SELECT * from Users where id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM Users WHERE id = ?";
    private static final String UPDATE_USER = "UPDATE Users SET firstName = ?, lastName = ?," +
            "address = ?, email = ?, password = ?, phoneNumber = ?, updateDate = ? WHERE id = ?";

    @SneakyThrows
    public User save(User user) {

       try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(SAVE, RETURN_GENERATED_KEYS)) {
           connection.setAutoCommit(false);
            ps.setObject(1, user.getFirstName());
            ps.setObject(2, user.getLastName());
            ps.setObject(3, user.getAddress());
            ps.setObject(4, user.getEmail());
            ps.setObject(5, user.getPassword());
            ps.setObject(6, user.getPhoneNumber());
            ps.setObject(7, user.getCreateDate());
            ps.setObject(8, null);
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    user.setId(generatedKeys.getInt("id"));
                    connection.commit();
                } else {
                    if(connection != null) {
                        connection.rollback();
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                if(connection != null) {
                    connection.rollback();
                    if(e.getSQLState().equals("2627") || e.getSQLState().equals("2601")) {
                        connection.rollback();
                        throw new ApiException(409, "Email already exists:" + user.getEmail());
                    }
                }
            }
        }
        return user;
    }

    @Override
    public Optional<User> findById(String s) {
        return Optional.empty();
    }


    public Optional<BasicUserDto> findBasicById(String id) {
        int idToInt = Integer.parseInt(id);

        try (Connection connection = ConnectionManager.get();
             PreparedStatement ps = connection.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, idToInt);

            return buildBasicUser(ps);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Optional<User> findByEmailAndPassword(String email, String password) throws SQLException {

        Connection connection = dataSource.getConnection();

        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_EMAIL_AND_PASSWORD)) {
            ps.setString(1, email);
            ps.setString(2, password);

            return buildUser(ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(String id) {
        int idToInt = Integer.parseInt(id);
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement ps = connection.prepareStatement(DELETE_BY_ID)) {
            ps.setInt(1, idToInt);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isPresent(String email) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement ps = connection.prepareStatement(CHECK_EMAIL)) {
            ps.setString(1, email);

            ResultSet resultSet = ps.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public User edit(User user, Integer userId) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_USER)) {
            ps.setObject(1, user.getFirstName());
            ps.setObject(2, user.getLastName());
            ps.setObject(3, user.getAddress());
            ps.setObject(4, user.getEmail());
            ps.setObject(5, PasswordUtil.hashPassword(user.getPassword()));
            ps.setObject(6, user.getPhoneNumber());
            ps.setObject(7, user.getUpdateDate());
            ps.setInt(8, userId);


            int rowsAffected = ps.executeUpdate();
            log.info("Rows affected: " + rowsAffected);

            if (rowsAffected > 0) {
                return user; // Возвращаем обновленного пользователя
            } else {
                return null; // Возвращаем null, если обновление не удалось
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<User> buildUser(PreparedStatement ps) throws SQLException {
        ResultSet resultSet = ps.executeQuery();

        if (!resultSet.next()) {
            log.warn("No user found for the given email.");
            return Optional.empty();
        }
            User user = User.builder()
                    .id(resultSet.getInt("id"))
                    .firstName(resultSet.getString("firstName"))
                    .lastName(resultSet.getString("lastName"))
                    .address(resultSet.getString("address"))
                    .email(resultSet.getString("email"))
                    .password(resultSet.getString("password"))
                    .phoneNumber(resultSet.getString("phoneNumber"))
                    .createDate(resultSet.getObject("createDate", LocalDateTime.class))
                    .updateDate(null)
                    .build();
        log.debug("Found user: {}", user);
        return Optional.ofNullable(user);
    }

    private Optional<BasicUserDto> buildBasicUser(PreparedStatement ps) throws SQLException {
        ResultSet resultSet = ps.executeQuery();

        if (!resultSet.next()) {
            log.warn("No user found for the given email.");
            return Optional.empty();
        }
        BasicUserDto user = BasicUserDto.builder()
                .id(resultSet.getInt("id"))
                .firstName(resultSet.getString("firstName"))
                .lastName(resultSet.getString("lastName"))
                .email(resultSet.getString("email"))
                .phoneNumber(resultSet.getString("phoneNumber"))
                .build();
        log.debug("Found user: {}", user);
        return Optional.ofNullable(user);
    }


    public static UserDao getInstance() {
        return INSTANCE;
    }

    public Optional<User> findByEmail(String email) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_EMAIL)) {
            ps.setString(1, email);

            return buildUser(ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
