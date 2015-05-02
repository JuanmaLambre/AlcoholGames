package com.juanma.alcoholgames.utils;


import android.content.Context;

import com.juanma.alcoholgames.Game;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/* Class that has miscellaneous functions of universal and generic purpose */
public class Misc {

    // Return the JSON file as string
    public static String loadJSONFromAsset(String filename, Context context) {
        String json = null;

        try {
            // Open the json file as a generic one
            InputStream is = context.getAssets().open(filename);

            // Now I'll read the entire file
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // And I cast it to String
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return json;
    }


    // Searches the game with the passed ID in the JSONArray passed
    public static Game searchGame(JSONArray gamesArray, int searchingGameID) throws Exception {
        for (int i=0; i < gamesArray.length(); i++) {
            int actualGameID;
            JSONObject actualGameObj;
            actualGameObj = gamesArray.getJSONObject(i);
            actualGameID = actualGameObj.getInt(GamesInfoNames.GAME_ID);
            if (actualGameID == searchingGameID) {
                // The game was found, so I return a new instance
                return new Game(
                        actualGameID,
                        actualGameObj.getString(GamesInfoNames.GAME_TITLE),
                        actualGameObj.getString(GamesInfoNames.GAME_DESCRIPTION),
                        actualGameObj.getString(GamesInfoNames.GAME_INSTRUCTIONS),
                        actualGameObj.getString(GamesInfoNames.GAME_ADDONS)
                );
            }
        }

        // If the iteration didnt find the game it will throw an exception
        throw new Exception("ID passed must be in the JSONArray passed");
    }

}
