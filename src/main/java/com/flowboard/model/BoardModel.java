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
        columns.add(new Column("Por hacer"));
        columns.add(new Column("En progreso"));
        columns.add(new Column("Hecho"));
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