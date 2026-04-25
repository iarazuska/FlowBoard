package com.flowboard.model;

import java.time.LocalDate;

public class Card {

    private String id;
    private String title;
    private String description;
    private String color;
    private LocalDate dueDate;
    private Priority priority;

    public enum Priority {
        HIGH, MEDIUM, LOW
    }

    public Card(String title, String description, String color, LocalDate dueDate, Priority priority) {
        this.id = java.util.UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.color = color;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
}