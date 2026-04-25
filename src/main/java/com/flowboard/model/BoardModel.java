package com.flowboard.model;

import java.util.ArrayList;
import java.util.List;

public class BoardModel {

    private List<Column> columns;

    public BoardModel() {
        this.columns = new ArrayList<>();
        initDefaultColumns();
    }

    private void initDefaultColumns() {
        columns.add(new Column("To Do"));
        columns.add(new Column("In Progress"));
        columns.add(new Column("Done"));
    }

    public void addColumn(String name) {
        columns.add(new Column(name));
    }

    public void removeColumn(Column column) {
        columns.remove(column);
    }

    public Column findColumnByCardId(String cardId) {
        for (Column col : columns) {
            for (Card card : col.getCards()) {
                if (card.getId().equals(cardId)) {
                    return col;
                }
            }
        }
        return null;
    }

    public Card findCardById(String cardId) {
        for (Column col : columns) {
            for (Card card : col.getCards()) {
                if (card.getId().equals(cardId)) {
                    return card;
                }
            }
        }
        return null;
    }

    public List<Column> getColumns() { return columns; }
}