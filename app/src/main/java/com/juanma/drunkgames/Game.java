package com.juanma.drunkgames;


import com.juanma.drunkgames.utils.GamesInfoNames;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Game implements Serializable {

    private int id;
    private String title, description, instructions, addons;
    private boolean isFavorite;

    private static int maxId = 0;


    // Setters:
    public void fav() {
        isFavorite = true;
    }
    public void unfav() {
        isFavorite = false;
    }
    public void setTitle(String newTitle) {
        title = newTitle;
    }
    public void setDescription(String newDescription) {
        description = newDescription;
    }
    public void setInstructions(String newInstructions) {
        instructions = newInstructions;
    }
    public void setAddons(String newAddons) {
        addons = newAddons;
    }

    // Getters:
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
    public boolean isFaved() {
        return isFavorite;
    }
    public int getID() {
        return id;
    }

    // Creators:
    public static Game hydrate(JSONObject jsonGame) {
        Game newGame = new Game();
        try {
            newGame.title = jsonGame.getString(GamesInfoNames.GAME_TITLE);
            newGame.addons = jsonGame.getString(GamesInfoNames.GAME_ADDONS);
            newGame.description = jsonGame.getString(GamesInfoNames.GAME_DESCRIPTION);
            newGame.instructions = jsonGame.getString(GamesInfoNames.GAME_INSTRUCTIONS);
            newGame.isFavorite = jsonGame.getBoolean(GamesInfoNames.GAME_ISFAV);
            newGame.id = jsonGame.getInt(GamesInfoNames.GAME_ID);
            if (maxId < newGame.id)
                maxId = newGame.id;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return newGame;
    }

    public Game() {
        this.id = maxId++;
        this.title = null;
        this.description = null;
        this.addons = null;
        this.instructions = null;
        this.isFavorite = false;
    }

    public String toString() {
        return title;
    }

    public boolean hasAddons() {
        return !addons.equals("");
    }

    public boolean hasDescription() {
        return !description.equals("");
    }

    public void copyFrom(Game otherGame) {
        title = otherGame.title;
        description = otherGame.description;
        instructions = otherGame.instructions;
        addons = otherGame.addons;
        isFavorite = otherGame.isFavorite;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonGame = new JSONObject();
        try {
            jsonGame.put(GamesInfoNames.GAME_ID, id);
            jsonGame.put(GamesInfoNames.GAME_TITLE, title);
            jsonGame.put(GamesInfoNames.GAME_INSTRUCTIONS, instructions);
            jsonGame.put(GamesInfoNames.GAME_DESCRIPTION, description);
            jsonGame.put(GamesInfoNames.GAME_ADDONS, addons);
            jsonGame.put(GamesInfoNames.GAME_ISFAV, isFavorite);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return jsonGame;
    }

    public boolean hasTags(String[] words) {
        for (String word : words) {
            if (title.toLowerCase().contains(word.toLowerCase())
                    || instructions.toLowerCase().contains(word.toLowerCase()))
                return true;
        }
        return false;
    }

    public String getPrettyText() {
        String gameText = "";
        gameText += title + ":\n";
        gameText += instructions;
        return gameText;
    }
}
