package com.gideontong.sighduk.API;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.gideontong.sighduk.SearchActivity;
import com.gideontong.sighduk.animeInfoActivity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class myAnimeListAPI {

    private Context context;

    // pulledData returnData;

    public void searchCallback(pulledData data) {

        if (context != null) {
            Intent intent = new Intent(context, animeInfoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("pulledData", data);
            context.startActivity(intent);
        }
    }

    public void tableCallback(pulledData data){
        new SearchActivity().animeCallBack(data);
    }

    public myAnimeListAPI(Context context) {
        this.context = context;
    }
    public myAnimeListAPI() {
        this.context = null;
    }

    public void backgroundSearchAnime(String animeName) {
        try {
            new singleAnimeInfo().execute(animeName);
        } catch (Exception e) {
            System.out.println("kind of ran into an exception...");
        }
    }


    static JSONParser parser = new JSONParser();

    private static HashMap<String, Integer> AnimeGenres = new HashMap<>();
    private static HashMap<String, Integer> MangaGenres = new HashMap<>();


    private class searchAnime extends AsyncTask<String, Void, pulledData> {

        @Override
        protected pulledData doInBackground(String... queries) {

            String animeName = queries[0];
            if (animeName == null || animeName == "") return null;
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.jikan.moe/v3/search/anime?q=" + animeName + "&limit=30")
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                //System.out.println(response.body().string());
                JSONObject json = (JSONObject) parser.parse(response.body().string());
                JSONArray results = ((JSONArray) json.get("results"));
                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> image_url = new ArrayList<>();
                ArrayList<String> url = new ArrayList<>();
                ArrayList<String> synopsis = new ArrayList<>();
                ArrayList<Long> episodes = new ArrayList<>();
                ArrayList<String> score = new ArrayList<>();
                ArrayList<String> rank = new ArrayList<>();
                ArrayList<String> id = new ArrayList<>();

                for (int i = 0; i < results.size(); i++) {
                    title.add(i, ((JSONObject) results.toArray()[i]).get("title").toString());
                    image_url.add(i, ((JSONObject) results.toArray()[i]).get("image_url").toString());
                    url.add(i, ((JSONObject) results.toArray()[i]).get("url").toString());
                    synopsis.add(i, ((JSONObject) results.toArray()[i]).get("synopsis").toString());
                    episodes.add(i, ((Long) ((JSONObject) results.toArray()[i]).get("episodes")));
                    id.add(i, ((JSONObject)results.toArray()[i]).get("mal_id").toString());
                    if ((((JSONObject) results.toArray()[i]).get("rank")) != null) {
                        rank.add(i, (((JSONObject) results.toArray()[i]).get("rank")).toString());
                    } else {
                        rank.add(i, null);
                    }
                    if ((((JSONObject) results.toArray()[i]).get("score")) != null) {
                        score.add(i, (((JSONObject) results.toArray()[i]).get("score")).toString());
                    } else {
                        score.add(i, null);
                    }
                }
                System.out.println(title);
                System.out.println(image_url);
                System.out.println(url);
                System.out.println(synopsis);
                System.out.println(episodes);
                System.out.println(score);
                System.out.println("-----------------------------------------------------------------------------------------");
                System.out.println(json);

                return new pulledData(title, image_url, url, synopsis, episodes, score, rank, null, id);
            } catch (IOException e) {
                return null;
            } catch (ParseException e) {
                return null;
            }
        }

        // This is called when doInBackground() is finished
        @Override
        protected void onPostExecute(pulledData result) {
            tableCallback(result);

            // new SearchActivity().searchCallback(result);
        }
    }

    private class singleAnimeInfo extends AsyncTask<String, Void, pulledData> {

        @Override
        protected pulledData doInBackground(String... queries) {
            String animeID = null;
            String animeName = queries[0];
            if (animeName == null || animeName == "") return null;
            OkHttpClient client = new OkHttpClient();

            Request request2 = new Request.Builder()
                    .url("https://api.jikan.moe/v3/search/anime?q=" + animeName + "&limit=30")
                    .get()
                    .build();
            try (Response response2 = client.newCall(request2).execute()) {
                JSONObject res = (JSONObject)parser.parse(response2.body().string());
                JSONArray results = ((JSONArray)res.get("results"));
                animeID = ((JSONObject)results.toArray()[0]).get("mal_id").toString();
            }
            catch (IOException e){

            }
            catch (ParseException e){

            }
            if (animeID == null || animeID == "") return null;

            Request request = new Request.Builder()
                    .url("https://api.jikan.moe/v3/anime/" + animeID + "")
                    .get()
                    .build();

            //System.out.println(response.body().string());


            /*String title;
            String image_url;
            String url;
            String synopsis;
            Long episodes;
            String score;
            String rank;
            String id;*/

                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> image_url = new ArrayList<>();
                ArrayList<String> url = new ArrayList<>();
                ArrayList<String> synopsis = new ArrayList<>();
                ArrayList<Long> episodes = new ArrayList<>();
                ArrayList<String> score = new ArrayList<>();
                ArrayList<String> rank = new ArrayList<>();
                ArrayList<String> id = new ArrayList<>();
            try (Response response2 = client.newCall(request).execute()) {
                JSONObject importantResponse = (JSONObject) parser.parse(response2.body().string());
                title.add((importantResponse).get("title").toString());
                image_url.add((importantResponse).get("image_url").toString());
                url.add((importantResponse).get("url").toString());
                synopsis.add((importantResponse).get("synopsis").toString());
                episodes.add(((Long) (importantResponse).get("episodes")));
                id.add((importantResponse).get("mal_id").toString());
                if (((importantResponse).get("rank")) != null) {
                    rank.add(((importantResponse).get("rank")).toString());
                } else {
                    rank.add(null);
                }
                if (((importantResponse).get("score")) != null) {
                    score.add(((importantResponse).get("score")).toString());
                } else {
                    score.add(null);
                }
                System.out.println(title);
                System.out.println(image_url);
                System.out.println(url);
                System.out.println(synopsis);
                System.out.println(episodes);
                System.out.println(score);
                System.out.println("-----------------------------------------------------------------------------------------");

                return new pulledData(title, image_url, url, synopsis, episodes, score, rank, null, id);

            } catch (IOException e) {
                return null;
            } catch (ParseException e) {
                return null;
            }
        }


        // This is called when doInBackground() is finished
        @Override
        protected void onPostExecute(pulledData result) {
            searchCallback(result);

            // new SearchActivity().searchCallback(result);
        }
    }

    private class searchGenre extends AsyncTask<String, Void, pulledData> {

        @Override
        protected pulledData doInBackground(String... queries) {

            String genre = queries[0];
            String type = queries[1];
            if (type != "anime" && type != "manga") return null;
            if (!AnimeGenres.containsKey(genre) && !MangaGenres.containsKey(genre)) return null;

            int id = (type.toLowerCase() == "anime") ? AnimeGenres.get(genre) : MangaGenres.get(genre);
            System.out.println(id);
            OkHttpClient client = new OkHttpClient();

            System.out.println("https://api.jikan.moe/v3/genre/"+type+"/"+id+"");
            Request request = new Request.Builder()
                    .url("https://api.jikan.moe/v3/genre/"+type+"/"+id+"")
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                JSONObject json = (JSONObject)parser.parse(response.body().string());
                JSONArray results = ((JSONArray)json.get(type));
                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> image_url = new ArrayList<>();
                ArrayList<String> url = new ArrayList<>();
                ArrayList<String> rank = new ArrayList<>();
                ArrayList<String> synopsis = new ArrayList<>();
                ArrayList<Long> episodes = new ArrayList<>();
                ArrayList<String> score = new ArrayList<>();

                for(int i = 0; i < results.size(); i++){
                    title.add(i, ((JSONObject)results.toArray()[i]).get("title").toString());
                    image_url.add(i, ((JSONObject)results.toArray()[i]).get("image_url").toString());
                    url.add(i, ((JSONObject)results.toArray()[i]).get("url").toString());
                    synopsis.add(i, ((JSONObject)results.toArray()[i]).get("synopsis").toString());
                    episodes.add(i, ((Long)((JSONObject)results.toArray()[i]).get("episodes")));
                    if ((((JSONObject)results.toArray()[i]).get("rank")) != null) {
                        rank.add(i, (((JSONObject)results.toArray()[i]).get("rank")).toString());
                    }
                    else {
                        rank.add(i, null);
                    }
                    if ((((JSONObject)results.toArray()[i]).get("score")) != null) {
                        score.add(i, (((JSONObject)results.toArray()[i]).get("score")).toString());
                    }
                    else {
                        score.add(i, null);
                    }
                }
                System.out.println(results);
                if (type == "anime"){
                    return new pulledData(title, image_url, url, synopsis, episodes, score, rank, null, null);
                }
                else if (type == "manga"){
                    return new pulledData(title, image_url, url, synopsis, null, score, null, null, null);
                }
                return null;
            }
            catch (IOException e){
                System.out.println("IOException");
                return null;
            }
            catch (ParseException e) {
                return null;
            }
        }

        // This is called when doInBackground() is finished
        @Override
        protected void onPostExecute(pulledData result) {
            //searchCallback(result);
            /*Intent intent = new Intent(getApplicationContext(), animeInfoActivity.class);
            intent.putExtra("pulledData", result);
            startActivity(intent);*/
            // new SearchActivity().searchCallback(result);
        }
    }


    private class topItems extends AsyncTask<String, Void, pulledData> {

        @Override
        protected pulledData doInBackground(String... queries) {

            OkHttpClient client = new OkHttpClient();
            String type = queries[0];
            Request request = new Request.Builder()
                    .url("https://api.jikan.moe/v3/top/"+type+"/"+1)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (type == "anime" || type == "manga") {
                    JSONObject json = (JSONObject) parser.parse(response.body().string());
                    JSONArray results = ((JSONArray) json.get("top"));
                    ArrayList<String> title = new ArrayList<>();
                    ArrayList<String> image_url = new ArrayList<>();
                    ArrayList<String> url = new ArrayList<>();
                    ArrayList<String> synopsis = new ArrayList<>();
                    ArrayList<String> rank = new ArrayList<>();
                    ArrayList<Long> episodes = new ArrayList<>();
                    ArrayList<String> score = new ArrayList<>();
                    for (int i = 0; i < results.size(); i++) {
                        title.add(i, ((JSONObject) results.toArray()[i]).get("title").toString());
                        image_url.add(i, ((JSONObject) results.toArray()[i]).get("image_url").toString());
                        url.add(i, ((JSONObject) results.toArray()[i]).get("url").toString());
                        rank.add(i, ((JSONObject) results.toArray()[i]).get("rank").toString());
                        if ((((JSONObject) results.toArray()[i])).get("synopsis") != null){
                            synopsis.add(i, ((JSONObject) results.toArray()[i]).get("synopsis").toString());
                        } else {
                            synopsis.add(i, null);
                        }
                        episodes.add(i, ((Long) ((JSONObject) results.toArray()[i]).get("episodes")));
                        if ((((JSONObject) results.toArray()[i]).get("score")) != null) {
                            score.add(i, (((JSONObject) results.toArray()[i]).get("score")).toString());
                        } else {
                            score.add(i, null);
                        }
                    }
                    System.out.println(results);
                    if (type == "anime"){
                        return new pulledData(title, image_url, url, synopsis, episodes, score, rank, null, null);
                    }
                    else if (type == "manga"){
                        return new pulledData(title, image_url, url, synopsis, null, score, null, null, null);
                    }
                    else return null;
                }
                else if (type == "people") {
                    JSONObject json = (JSONObject)parser.parse(response.body().string());
                    JSONArray results = ((JSONArray)json.get("top"));
                    ArrayList<String> title = new ArrayList<>();
                    ArrayList<String> image_url = new ArrayList<>();
                    ArrayList<String> url = new ArrayList<>();

                    for(int i = 0; i < results.size(); i++){
                        title.add(i, ((JSONObject)results.toArray()[i]).get("title").toString());
                        image_url.add(i, ((JSONObject)results.toArray()[i]).get("image_url").toString());
                        url.add(i, ((JSONObject)results.toArray()[i]).get("url").toString());
                    }
                    System.out.println(title);
                    System.out.println(image_url);
                    System.out.println(url);
                    System.out.println("-----------------------------------------------------------------------------------------");
                    System.out.println(json);
                    return new pulledData(title, image_url, url, null, null, null, null, null, null);

                }
                else if (type == "characters"){
                    JSONObject json = (JSONObject)parser.parse(response.body().string());
                    System.out.println(json);
                    JSONArray results = ((JSONArray)json.get("top"));
                    ArrayList<String> title = new ArrayList<>();
                    ArrayList<String> image_url = new ArrayList<>();
                    ArrayList<String> url = new ArrayList<>();

                    for(int i = 0; i < results.size(); i++){
                        title.add(i, ((JSONObject)results.toArray()[i]).get("title").toString());
                        image_url.add(i, ((JSONObject)results.toArray()[i]).get("image_url").toString());
                        url.add(i, ((JSONObject)results.toArray()[i]).get("url").toString());
                    }
                    System.out.println(title);
                    System.out.println(image_url);
                    System.out.println(url);
                    System.out.println("-----------------------------------------------------------------------------------------");
                    System.out.println(json);
                    return new pulledData(title, image_url, url, null, null, null, null, null, null);

                }
                else {
                    System.out.println("Something went wrong... Sorry!");
                    return null;
                }
            }
            catch (IOException e){
                return null;
            }
            catch (ParseException e){
                return null;
            }
        }

        // This is called when doInBackground() is finished
        @Override
        protected void onPostExecute(pulledData result) {
            searchCallback(result);
            // new SearchActivity().searchCallback(result);
        }
    }

    /*public static pulledData searchGenre(String genre, String type, int limit, int page) throws IOException, ParseException{

        if (type != "anime" && type != "manga") return null;
        if (!AnimeGenres.containsKey(genre) && !MangaGenres.containsKey(genre)) return null;

        int id = (type.toLowerCase() == "anime") ? AnimeGenres.get(genre) : MangaGenres.get(genre);
        System.out.println(id);
        OkHttpClient client = new OkHttpClient();

        System.out.println("https://api.jikan.moe/v3/genre/"+type+"/"+id+"");
        Request request = new Request.Builder()
                .url("https://api.jikan.moe/v3/genre/"+type+"/"+id+"")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            JSONObject json = (JSONObject)parser.parse(response.body().string());
            JSONArray results = ((JSONArray)json.get(type));
            ArrayList<String> title = new ArrayList<>();
            ArrayList<String> image_url = new ArrayList<>();
            ArrayList<String> url = new ArrayList<>();
            ArrayList<String> rank = new ArrayList<>();
            ArrayList<String> synopsis = new ArrayList<>();
            ArrayList<Long> episodes = new ArrayList<>();
            ArrayList<String> score = new ArrayList<>();

            for(int i = 0; i < results.size(); i++){
                title.add(i, ((JSONObject)results.toArray()[i]).get("title").toString());
                image_url.add(i, ((JSONObject)results.toArray()[i]).get("image_url").toString());
                url.add(i, ((JSONObject)results.toArray()[i]).get("url").toString());
                synopsis.add(i, ((JSONObject)results.toArray()[i]).get("synopsis").toString());
                episodes.add(i, ((Long)((JSONObject)results.toArray()[i]).get("episodes")));
                if ((((JSONObject)results.toArray()[i]).get("rank")) != null) {
                    rank.add(i, (((JSONObject)results.toArray()[i]).get("rank")).toString());
                }
                else {
                    rank.add(i, null);
                }
                if ((((JSONObject)results.toArray()[i]).get("score")) != null) {
                    score.add(i, (((JSONObject)results.toArray()[i]).get("score")).toString());
                }
                else {
                    score.add(i, null);
                }
            }
            System.out.println(results);
            if (type == "anime"){
                return new pulledData(title, image_url, url, synopsis, episodes, score, rank, null);
            }
            else if (type == "manga"){
                return new pulledData(title, image_url, url, synopsis, null, score, null, null);
            }
            return null;
        }
    }*/

    public static JSONObject searchPeople(String person, int limit, int page) throws IOException, ParseException{
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.jikan.moe/v3/search/people?q="+person+"&limit=10")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            JSONObject json = (JSONObject)parser.parse(response.body().string());
            JSONArray results = ((JSONArray)json.get("results"));
            ArrayList<String> name = new ArrayList<>();
            ArrayList<String> image_url = new ArrayList<>();
            ArrayList<String> url = new ArrayList<>();

            for(int i = 0; i < results.size(); i++){
                name.add(i, ((JSONObject)results.toArray()[i]).get("name").toString());
                image_url.add(i, ((JSONObject)results.toArray()[i]).get("image_url").toString());
                url.add(i, ((JSONObject)results.toArray()[i]).get("url").toString());
            }
            System.out.println(name);
            System.out.println(image_url);
            System.out.println(url);
            System.out.println("-----------------------------------------------------------------------------------------");
            System.out.println(json);
            return json;
        }
    }

    public static pulledData topItems(String type, int page) throws IOException, ParseException{
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.jikan.moe/v3/top/"+type+"/"+1)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (type == "anime" || type == "manga") {
                JSONObject json = (JSONObject) parser.parse(response.body().string());
                JSONArray results = ((JSONArray) json.get("top"));
                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> image_url = new ArrayList<>();
                ArrayList<String> url = new ArrayList<>();
                ArrayList<String> synopsis = new ArrayList<>();
                ArrayList<String> rank = new ArrayList<>();
                ArrayList<Long> episodes = new ArrayList<>();
                ArrayList<String> score = new ArrayList<>();
                for (int i = 0; i < results.size(); i++) {
                    title.add(i, ((JSONObject) results.toArray()[i]).get("title").toString());
                    image_url.add(i, ((JSONObject) results.toArray()[i]).get("image_url").toString());
                    url.add(i, ((JSONObject) results.toArray()[i]).get("url").toString());
                    rank.add(i, ((JSONObject) results.toArray()[i]).get("rank").toString());
                    if ((((JSONObject) results.toArray()[i])).get("synopsis") != null){
                        synopsis.add(i, ((JSONObject) results.toArray()[i]).get("synopsis").toString());
                    } else {
                        synopsis.add(i, null);
                    }
                    episodes.add(i, ((Long) ((JSONObject) results.toArray()[i]).get("episodes")));
                    if ((((JSONObject) results.toArray()[i]).get("score")) != null) {
                        score.add(i, (((JSONObject) results.toArray()[i]).get("score")).toString());
                    } else {
                        score.add(i, null);
                    }
                }
                System.out.println(results);
                if (type == "anime"){
                    return new pulledData(title, image_url, url, synopsis, episodes, score, rank, null, null);
                }
                else if (type == "manga"){
                    return new pulledData(title, image_url, url, synopsis, null, score, null, null, null);
                }
                else return null;
            }
            else if (type == "people") {
                JSONObject json = (JSONObject)parser.parse(response.body().string());
                JSONArray results = ((JSONArray)json.get("top"));
                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> image_url = new ArrayList<>();
                ArrayList<String> url = new ArrayList<>();

                for(int i = 0; i < results.size(); i++){
                    title.add(i, ((JSONObject)results.toArray()[i]).get("title").toString());
                    image_url.add(i, ((JSONObject)results.toArray()[i]).get("image_url").toString());
                    url.add(i, ((JSONObject)results.toArray()[i]).get("url").toString());
                }
                System.out.println(title);
                System.out.println(image_url);
                System.out.println(url);
                System.out.println("-----------------------------------------------------------------------------------------");
                System.out.println(json);
                return new pulledData(title, image_url, url, null, null, null, null, null, null);

            }
            else if (type == "characters"){
                JSONObject json = (JSONObject)parser.parse(response.body().string());
                System.out.println(json);
                JSONArray results = ((JSONArray)json.get("top"));
                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> image_url = new ArrayList<>();
                ArrayList<String> url = new ArrayList<>();

                for(int i = 0; i < results.size(); i++){
                    title.add(i, ((JSONObject)results.toArray()[i]).get("title").toString());
                    image_url.add(i, ((JSONObject)results.toArray()[i]).get("image_url").toString());
                    url.add(i, ((JSONObject)results.toArray()[i]).get("url").toString());
                }
                System.out.println(title);
                System.out.println(image_url);
                System.out.println(url);
                System.out.println("-----------------------------------------------------------------------------------------");
                System.out.println(json);
                return new pulledData(title, image_url, url, null, null, null, null, null, null);

            }
            else {
                System.out.println("Something went wrong... Sorry!");
                return null;
            }
        }
    }
    public static void main(String args[]) throws IOException, ParseException {

        AnimeGenres.put("Action", 1); AnimeGenres.put("Martial Arts", 17); AnimeGenres.put("Yaoi", 33);
        AnimeGenres.put("Adventure", 2); AnimeGenres.put("Mecha", 18); AnimeGenres.put("Yuri", 34);
        AnimeGenres.put("Cars", 3); AnimeGenres.put("Music", 19); AnimeGenres.put("Harem", 35);
        AnimeGenres.put("Comedy", 4); AnimeGenres.put("Parody", 20); AnimeGenres.put("Slice of Life", 36);
        AnimeGenres.put("Dementia", 5); AnimeGenres.put("Samurai", 21); AnimeGenres.put("Supernatural", 37);
        AnimeGenres.put("Demons", 6); AnimeGenres.put("Romance", 22); AnimeGenres.put("Military", 38);
        AnimeGenres.put("Mystery", 7); AnimeGenres.put("School", 23); AnimeGenres.put("Police", 39);
        AnimeGenres.put("Drama", 8); AnimeGenres.put("Sci Fi", 24); AnimeGenres.put("Psychological", 40);
        AnimeGenres.put("Ecchi", 9); AnimeGenres.put("Shoujo", 25); AnimeGenres.put("Thriller", 41);
        AnimeGenres.put("Fantasy", 10); AnimeGenres.put("Shoujo Ai", 26); AnimeGenres.put("Seinen", 42);
        AnimeGenres.put("Game", 11); AnimeGenres.put("Shounen", 27); AnimeGenres.put("Josei", 43);
        AnimeGenres.put("Hentai", 12); AnimeGenres.put("Shounen Ai", 28);
        AnimeGenres.put("Historical", 13); AnimeGenres.put("Space", 29);
        AnimeGenres.put("Horror", 14); AnimeGenres.put("Sports", 30);
        AnimeGenres.put("Kids", 15); AnimeGenres.put("Super Power", 31);
        AnimeGenres.put("Magic", 16); AnimeGenres.put("Vampire", 32);

        MangaGenres.put("Action", 1); MangaGenres.put("Martial Arts", 17); MangaGenres.put("Yaoi", 33);
        MangaGenres.put("Adventure", 2); MangaGenres.put("Mecha", 18); MangaGenres.put("Yuri", 34);
        MangaGenres.put("Cars", 3); MangaGenres.put("Music", 19); MangaGenres.put("Harem", 35);
        MangaGenres.put("Comedy", 4); MangaGenres.put("Parody", 20); MangaGenres.put("Slice of Life", 36);
        MangaGenres.put("Dementia", 5); MangaGenres.put("Samurai", 21); MangaGenres.put("Supernatural", 37);
        MangaGenres.put("Demons", 6); MangaGenres.put("Romance", 22); MangaGenres.put("Military", 38);
        MangaGenres.put("Mystery", 7); MangaGenres.put("School", 23); MangaGenres.put("Police", 39);
        MangaGenres.put("Drama", 8); MangaGenres.put("Sci Fi", 24); MangaGenres.put("Psychological", 40);
        MangaGenres.put("Ecchi", 9); MangaGenres.put("Shoujo", 25); MangaGenres.put("Seinen", 41);
        MangaGenres.put("Fantasy", 10); MangaGenres.put("Shoujo Ai", 26); MangaGenres.put("Josei", 42);
        MangaGenres.put("Game", 11); MangaGenres.put("Shounen", 27); MangaGenres.put("Doujinshi", 43);
        MangaGenres.put("Hentai", 12); MangaGenres.put("Shounen Ai", 28); MangaGenres.put("Gender Bender", 44);
        MangaGenres.put("Historical", 13); MangaGenres.put("Space", 29); MangaGenres.put("Thriller", 45);
        MangaGenres.put("Horror", 14); MangaGenres.put("Sports", 30);
        MangaGenres.put("Kids", 15); MangaGenres.put("Super Power", 31);
        MangaGenres.put("Magic", 16); MangaGenres.put("Vampire", 32);

        /*OkHttpClient client = new OkHttpClient();
        String type = "anime";
        Request request = new Request.Builder()
                .url("https://api.jikan.moe/v3/top/"+type+"/"+1)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (type == "anime" || type == "manga") {
                JSONObject json = (JSONObject) parser.parse(response.body().string());
                JSONArray results = ((JSONArray) json.get("top"));
                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> image_url = new ArrayList<>();
                ArrayList<String> url = new ArrayList<>();
                ArrayList<String> synopsis = new ArrayList<>();
                ArrayList<Long> episodes = new ArrayList<>();
                ArrayList<String> score = new ArrayList<>();
                for (int i = 0; i < results.size(); i++) {
                    title.add(i, ((JSONObject) results.toArray()[i]).get("title").toString());
                    image_url.add(i, ((JSONObject) results.toArray()[i]).get("image_url").toString());
                    url.add(i, ((JSONObject) results.toArray()[i]).get("url").toString());
                    if ((((JSONObject) results.toArray()[i])).get("synopsis") != null){
                        synopsis.add(i, ((JSONObject) results.toArray()[i]).get("synopsis").toString());
                    } else {
                        synopsis.add(i, null);
                    }
                    episodes.add(i, ((Long) ((JSONObject) results.toArray()[i]).get("episodes")));
                    if ((((JSONObject) results.toArray()[i]).get("score")) != null) {
                        score.add(i, (((JSONObject) results.toArray()[i]).get("score")).toString());
                    } else {
                        score.add(i, null);
                    }
                }
                System.out.println(results);
            }
            else if (type == "people") {
                JSONObject json = (JSONObject)parser.parse(response.body().string());
                JSONArray results = ((JSONArray)json.get("top"));
                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> image_url = new ArrayList<>();
                ArrayList<String> url = new ArrayList<>();

                for(int i = 0; i < results.size(); i++){
                    title.add(i, ((JSONObject)results.toArray()[i]).get("title").toString());
                    image_url.add(i, ((JSONObject)results.toArray()[i]).get("image_url").toString());
                    url.add(i, ((JSONObject)results.toArray()[i]).get("url").toString());
                }
                System.out.println(title);
                System.out.println(image_url);
                System.out.println(url);
                System.out.println("-----------------------------------------------------------------------------------------");
                System.out.println(json);
            }
            else if (type == "characters"){
                JSONObject json = (JSONObject)parser.parse(response.body().string());
                System.out.println(json);
                JSONArray results = ((JSONArray)json.get("top"));
                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> image_url = new ArrayList<>();
                ArrayList<String> url = new ArrayList<>();

                for(int i = 0; i < results.size(); i++){
                    title.add(i, ((JSONObject)results.toArray()[i]).get("title").toString());
                    image_url.add(i, ((JSONObject)results.toArray()[i]).get("image_url").toString());
                    url.add(i, ((JSONObject)results.toArray()[i]).get("url").toString());
                }
                System.out.println(title);
                System.out.println(image_url);
                System.out.println(url);
                System.out.println("-----------------------------------------------------------------------------------------");
                System.out.println(json);
            }
            else {
                System.out.println("Something went wrong... Sorry!");
            }
        }*/


    }
}
