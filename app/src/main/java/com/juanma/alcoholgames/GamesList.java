package com.juanma.alcoholgames;

import com.juanma.alcoholgames.utils.GamesInfoNames;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/*
Perceptron class
 */
public class GamesList extends ArrayList<Game> {

    static GamesList instance = null;


    private GamesList() {
        super();
    }

    public static GamesList getInstance() {
        if (instance == null)
            instance = new GamesList();
        return instance;
    }


    // Loads the list with all the games in the given jsonFile
    public void loadFromJSON(String jsonFile) {
        clear();
        JSONObject gamesInfoObject;

        try {
            gamesInfoObject = new JSONObject(jsonFile);
            JSONArray gamesArray = gamesInfoObject.getJSONArray(GamesInfoNames.GAMES_ARRAY);

            // Creating the games to add them to the list
            for (int i=0; i < gamesArray.length(); i++) {
                Game actualGame = Game.hydrate(gamesArray.getJSONObject(i));
                add(actualGame);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // Searches the game with the given ID
    public Game getByID(int id) {
        for (Game current : this) {
            if (current.getID() == id)
                return current;
        }
        return null;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonGames = new JSONObject();
        JSONArray gamesArray = new JSONArray();

        for(Game game : this) {
            gamesArray.put(game.toJSONObject());
        }

        try {
            jsonGames.put(GamesInfoNames.GAMES_ARRAY, gamesArray);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } finally {
            return jsonGames;
        }
    }

    public ArrayList<Game> getFavs() {
        ArrayList<Game> favsList = new ArrayList<>();
        for (Game game : this) {
            if (game.isFaved())
                favsList.add(game);
        }
        return favsList;
    }

    public ArrayList<Game> getArrayCopy() {
        return (ArrayList<Game>) clone();
    }
}
