package com.flowboard.model;

import java.util.ArrayList;
import java.util.List;

public class Column {

    private String id;
    private String name;
    private List<Card> cards;

    public Column(String name) {
        this.id = java.util.UUID.randomUUID().toString();
        this.name = name;
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public void moveCard(Card card, int newIndex) {
        cards.remove(card);
        cards.add(newIndex, card);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Card> getCards() { return cards; }
}