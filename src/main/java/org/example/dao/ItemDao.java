package org.example.dao;

import org.example.controller.ServiceMain;
import org.example.entity.Item;
import org.example.entity.Order;
import org.example.enums.BakingItem;
import org.example.utils.ConnectionManager;
import org.example.utils.ConnectionPoolManager;
import org.example.utils.LogbackWrapper;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ItemDao  implements EntityDao<Item, Integer, String>{

    private static final DataSource dataSource = ConnectionPoolManager.getDataSource();
    public static final ItemDao INSTANCE = new ItemDao();
    protected static Logger log = LogbackWrapper.getLogger(ServiceMain.class);
    public static final String GET_ALL_ITEMS = "SELECT id, name, price, description FROM Items FOR JSON PATH";
    public static final String FIND_BY_ID = "SELECT id, name, price, description from Items WHERE id = ?";

    @Override
    public Item save(Item item) {
        return null;
    }

    @Override
    public Optional<Item> findById(String s) {
        int id = Integer.parseInt(s);

        try (Connection connection = ConnectionManager.get();
             PreparedStatement ps = connection.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();

            if(!resultSet.next()) {
                log.warn("No item found for the given id.");
                return Optional.empty();
            }

            Item item = Item.builder()
                    .id(resultSet.getInt("id"))
                    .bakingItem(BakingItem.valueOf(resultSet.getString("name")))
                    .description(resultSet.getString("description"))
                    .price(resultSet.getInt("price"))
                    .build();
            return Optional.ofNullable(item);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
    public Item edit(Item item, Integer integer) {
        return null;
    }

    public String getAllItems () {

        String jsonResult = "";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_ALL_ITEMS)) {

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {

                jsonResult = resultSet.getString(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return jsonResult;
    }

    public static ItemDao getInstance() {
        return INSTANCE;
    }
}
