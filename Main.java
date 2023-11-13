package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.json.*;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	// please do not leak this key i will kill you
	public final static String map = "273a346184cebe767ac61195101d83ae";
	
	
    public static void main(String [] args) throws URISyntaxException, IOException {
        APICall("Los Angeles");
    	//launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
    	
    	
    	
    	//primaryStage.setScene(scene);
    	//primaryStage.show();
    }
    
    // method to call API and store the resulting JSON data
    public static void APICall(String city) throws URISyntaxException, IOException {
    	// imperial data
    	String apiURL = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + map + "&units=imperial";
    	String forecastURL = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + map + "&units=imperial";

    	// metric data
    	//String apiURL = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + map;
    	//String forecastURL = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + map;
    	
    	// establishes connection with daily API
    	URL url = new URL(apiURL);
    	URLConnection connection = url.openConnection();
    	BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    	StringBuilder jsn = new StringBuilder();
    	
    	// reading JSON data
    	String ln;
    	while((ln = rd.readLine()) != null)
    		jsn.append(ln);
    	ln = "";
    	rd.close();
    	
    	JSONObject data = new JSONObject(jsn.toString());
    	int humidity = data.getJSONObject("main").getInt("humidity");
    	int wind = data.getJSONObject("wind").getInt("speed");
    	String description = data.getJSONArray("weather").getJSONObject(0).getString("description");
    	int temp = data.getJSONObject("main").getInt("temp");
    	int feels = data.getJSONObject("main").getInt("feels_like");
    	int sunrise = data.getJSONObject("sys").getInt("sunrise");
    	int sunset = data.getJSONObject("sys").getInt("sunset");
    	
    	// establishes connection with forecast API
    	url = new URL(forecastURL);
    	connection = url.openConnection();
    	rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    	jsn = new StringBuilder();
    	
    	// reading JSON data
    	while((ln = rd.readLine()) != null)
    		jsn.append(ln);
    	ln = "";
    	rd.close();
    	
    	// extracting forecast data
    	JSONArray forecastData = new JSONObject(jsn.toString()).getJSONArray("list");
    	Map<String, JSONObject> checkDate = new TreeMap<>();
    	
    	for(int i = 0; i < forecastData.length(); i++) {
    		JSONObject day = forecastData.getJSONObject(i);
    		String date = day.getString("dt_txt").split(" ")[0];
    		
    		if(!checkDate.containsKey(date))
    			checkDate.put(date, day);
    	}
    	
        for (Map.Entry<String, JSONObject> entry : checkDate.entrySet()) {
            System.out.println("Date: " + entry.getKey());
            System.out.println(entry.getValue());
        }
    	
    	
    	
    }

}
