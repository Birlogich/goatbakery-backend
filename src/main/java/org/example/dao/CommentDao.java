package org.example.dao;

import org.example.controller.ServiceMain;
import org.example.entity.Comment;
import org.example.utils.ConnectionPoolManager;
import org.example.utils.LogbackWrapper;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;


public class CommentDao implements EntityDao<Comment, Integer, String>{

    private static final DataSource dataSource = ConnectionPoolManager.getDataSource();
    public static final CommentDao INSTANCE = new CommentDao();
    protected static Logger log = LogbackWrapper.getLogger(ServiceMain.class);

    public static final String SAVE_COMMENT = "INSERT INTO Comments (user_id, item_id, comment) VALUES (?, ?, ?)";
    public static final String GET_ALL_COMMENTS = "SELECT id, user_id, item_id, comment\n" +
            "FROM Comments\n" +
            "FOR JSON PATH";
    public static final String GET_COMMENT_BY_ITEM_ID = "SELECT comment from Comments where item_id = ? FOR JSON PATH";

    @Override
    public Comment save(Comment comment) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SAVE_COMMENT, RETURN_GENERATED_KEYS)) {
            ps.setInt(1, comment.getUser().getId());
            ps.setInt(2, comment.getItem().getId());
            ps.setString(3, comment.getComment());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    comment.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("The comment didn't created, ID was not found");
                }
            }

        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
        return comment;
    }

    public String getAllComments () {

        String jsonResult = "";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(GET_ALL_COMMENTS)) {

                ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {

                jsonResult = resultSet.getString(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return jsonResult;
    }

    @Override
    public Optional<Comment> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public boolean isPresent(String s) {
        return false;
    }

    @Override
    public Comment edit(Comment comment, Integer integer) {
        return null;
    }

    public static CommentDao getInstance() {
        return INSTANCE;
    }
}
