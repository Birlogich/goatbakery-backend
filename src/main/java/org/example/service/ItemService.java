package org.example.service;

import org.example.dao.ItemDao;

public class ItemService {


    private static final ItemService INSTANCE = new ItemService();
    private final ItemDao itemDao = ItemDao.getInstance();

    public String getAllComments() {
        return itemDao.getAllItems();
    }


    public static ItemService getInstance() {return INSTANCE ;}
}
