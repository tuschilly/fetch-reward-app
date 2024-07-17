package com.example.fetchrewards;

/**
 * @brief Item class is to encapsulate the data associated with each item in a
 * structured manner. It provides methods to retrieve the attributes (listId,
 * name, id) and a constructor to initialize an Item object with specific
 * values.
 *
 */
public class Item {
    private int listId;
    private String name;
    private int id;

    public Item(int listId, String name, int id) {
        this.listId = listId;
        this.name = name;
        this.id = id;
    }

    public int getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }

    public int getId() { return id; }
}

