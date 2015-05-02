package com.juanma.alcoholgames;


import java.io.Serializable;

public class Game implements Serializable {

    private int id;
    private String title, description, instructions, addons;

    // Getters:
    public int getID() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getInstructions() {
        return instructions;
    }
    public String getAddons() {
        return addons;
    }

    public Game(int id, String title, String description, String instructions, String addons) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.addons = addons;
        this.instructions = instructions;
    }

    public String toString() {
        return title;
    }

    public boolean hasAddons() {
        return !addons.equals("");
    }

    public boolean hasDescription() { return !description.equals(""); }
}
