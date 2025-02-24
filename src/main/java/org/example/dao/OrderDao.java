package org.example.dao;

import lombok.SneakyThrows;
import org.example.controller.ServiceMain;
import org.example.entity.Item;
import org.example.entity.ItemInOrder;
import org.example.entity.Order;
import org.example.exception.ApiException;
import org.example.utils.ConnectionPoolManager;
import org.example.utils.LogbackWrapper;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class OrderDao implements EntityDao<Order, Integer, String> {

    private static final DataSource dataSource = ConnectionPoolManager.getDataSource();
    public static final OrderDao INSTANCE = new OrderDao();
    protected static Logger log = LogbackWrapper.getLogger(ServiceMain.class);

    public static final String SAVE_ORDER = "INSERT INTO Orders (customer, createDate) VALUES (?, ?)";
    public static final String SAVE_ITEM_IN_ORDER = "INSERT INTO Order_Items (order_id, item_id, quantity) VALUES (?, ?, ?)";
    public static final String GET_ITEM_PRICE = "SELECT price FROM Items WHERE id = ?";


    @Override
    @SneakyThrows
    public Order save(Order order) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement ps = connection.prepareStatement(SAVE_ORDER, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, order.getCustomerId());
                ps.setObject(2, order.getCreateDate());
                ps.executeUpdate();

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Order was not found, ID was not obtained");
                    }
                }
            }

            order.setTotalSum(saveItemInOrders(connection, order));

            connection.commit();
        } catch (Exception e) {
            log.error("Error during saving the order: {}", e.getMessage());
            throw new RuntimeException("Error during saving the order", e);
        }
        return order;
    }


    private int saveItemInOrders(Connection connection, Order order) throws SQLException {
        int totalSum = 0;
        try (PreparedStatement ps = connection.prepareStatement(SAVE_ITEM_IN_ORDER)) {
            for (ItemInOrder itemInOrder : order.getItemInOrders()) {
                ps.setInt(1, order.getId());
                ps.setInt(2, itemInOrder.getItem().getBakingItem().getId());
                ps.setInt(3, itemInOrder.getQuantity());
                ps.addBatch();

                totalSum += getItemPrice(itemInOrder.getItem()) * itemInOrder.getQuantity();
            }
            ps.executeBatch();
        }
        return totalSum;
    }

    @Override
    public Optional<Order> findById(String s) {
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
    public Order edit(Order order, Integer integer) {
        return null;
    }

    public static OrderDao getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    public static int getItemPrice(Item item) {
        int price = 0;


        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_ITEM_PRICE)) {

            ps.setInt(1, item.getBakingItem().getId());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    price = rs.getInt("price");
                }
            } catch (Exception e) {
                log.error(String.valueOf(e));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return price;
    }

}
