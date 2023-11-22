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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Main extends Application {

	// please do not leak this key i will kill you
	public final static String map = "273a346184cebe767ac61195101d83ae";
	public static boolean toggle = true;
	
	
    public static void main(String [] args) throws URISyntaxException, IOException {
        //APICall("Los Angeles");
    	launch(args);
    }

    //@Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
    	
    	// logo
    	ImageView view = new ImageView();
    	Image logos = new Image("file:./res/logo2.png");
    	view.setImage(logos);
    	view.setPreserveRatio(true);
    	view.setSmooth(true);
    	view.setFitWidth(57);
    	view.setTranslateX(320);
    	view.setTranslateY(11);
    	
    	Text ibm = new Text("An IBM Business");
    	ibm.setFont(Font.font("Arial", 14));
    	ibm.setFill(Color.WHITE);
    	ibm.setTranslateX(401);
    	ibm.setTranslateY(45);
    	
    	TextField search = new TextField();
    	search.setPromptText("Search City or Zip Code");
    	search.setAlignment(Pos.CENTER);
    	search.setPrefSize(380, 30);
    	search.setTranslateX(705);
    	search.setTranslateY(25);
    	search.setStyle(
    			"-fx-background-radius: 10;" +
    			"-fx-border-radius: 10;" +
    			"-fx-font-size: 14;");

    	// header
    	Rectangle header = new Rectangle();
    	header.setWidth(1920);
    	header.setHeight(80);
    	header.setFill(Color.web("#005986"));
    	
    	ToggleButton fc = new ToggleButton("°C");
    	fc.setPrefWidth(45);
    	fc.setStyle(
    			"-fx-base: #EF9E1C;" +
    			"-fx-text-fill: #FFFFFF;" +
    			"-fx-background-color: #DB4F4A;" +
    			"-fx-background-insets: 0, 1;" +
    			"-fx-background-radius: 5em;" +
    			"-fx-padding: 5px;" +
    			"-fx-font-size: 14px;" +
    			"-fx-content-display: center;"
    			);
    	fc.setOnAction(e -> {
    		if(fc.isSelected()) {
    			fc.setText("°F");
    			toggle = false;
    	    	fc.setStyle(
    	    			"-fx-background-color: #EF9E1C;" +
    	    			"-fx-text-fill: #FFFFFF;" +
    	    			"-fx-background-insets: 0, 1;" +
    	    			"-fx-background-radius: 5em;" +
    	    			"-fx-padding: 5px;" +
    	    			"-fx-font-size: 14px;" +
    	    			"-fx-content-display: center;"
    	    			);
    		}
    		else {
    			fc.setText("°C");
    	    	fc.setStyle(
    	    			"-fx-background-color: #DB4F4A;" +
    	    			"-fx-text-fill: #FFFFFF;" +
    	    			"-fx-background-insets: 0, 1;" +
    	    			"-fx-background-radius: 5em;" +
    	    			"-fx-padding: 5px;" +
    	    			"-fx-font-size: 14px;" +
    	    			"-fx-content-display: center;"
    	    			);
    		}
    	});
    	fc.setTranslateX(1325);
    	fc.setTranslateY(25);
    	
    	Button prem = new Button("GO PREMIUM");
    	prem.setPrefSize(80, 30);
    	prem.setStyle("-fx-background-color: #FFFFFF;" +
    			"-fx-text-fill: #000000;" + 
    			"-fx-background-insets: 0, 1;" +
    			"-fx-background-radius: 5em;" +
    			"-fx-padding: 5px;" +
    			"-fx-font-size: 10px");
    	prem.setTranslateX(1380);
    	prem.setTranslateY(25);
    	
    	// remembering previous searches
    	VBox mem = new VBox();
    	mem.setPrefWidth(1920);
    	mem.setPrefHeight(40);
    	mem.setTranslateY(80);
    	mem.setStyle("-fx-background-color: #337A9E;");
    	
    	ImageView login = new ImageView();
    	Image logins = new Image("file:./res/login.png");
    	login.setImage(logins);
    	login.setPreserveRatio(true);
    	login.setSmooth(true);
    	login.setFitWidth(30);
    	login.setTranslateX(1480);
    	login.setTranslateY(25);
    	
        Line line1 = new Line(0, 0, 30, 0);
        Line line2 = new Line(0, 10, 30, 10);
        Line line3 = new Line(0, 20, 30, 20);
        
        Pane ham = new Pane(line1, line2, line3);

        for (Line line : new Line[]{line1, line2, line3}) {
            line.setStrokeWidth(3);
            line.setStroke(Color.WHITE);
        }
        ham.setTranslateX(1540);
        ham.setTranslateY(30);
        
    	Pane pn = new Pane(header, view, ibm, fc, search, mem, prem, login, ham);
    	
    	Scene scene = new Scene(pn, 1920, 1000);
    	primaryStage.setScene(scene);
    	primaryStage.show();
    }
    
    // method to call API and store the resulting JSON data
    public static void APICall(String city) throws URISyntaxException, IOException {
    	
    	// defaults to metric unless specified
		String apiURL = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + map;
		String forecastURL = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + map;
    	
    	if(toggle) {
    		// imperial data
    		apiURL = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + map + "&units=imperial";
    		forecastURL = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + map + "&units=imperial";
    	}
    	
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
    	
    	// extracting current day data
    	JSONObject data = new JSONObject(jsn.toString());
    	int humidity = data.getJSONObject("main").getInt("humidity");
    	int wind = data.getJSONObject("wind").getInt("speed");
    	String description = data.getJSONArray("weather").getJSONObject(0).getString("description");
    	int temp = data.getJSONObject("main").getInt("temp");
    	int tempMax = data.getJSONObject("main").getInt("temp_max");
    	int tempMin = data.getJSONObject("main").getInt("temp_min");
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
    	
    	// TreeMap to get only one weather per date because it returns data for every 3 hrs
    	for(int i = 0; i < forecastData.length(); i++) {
    		JSONObject day = forecastData.getJSONObject(i);
    		String date = day.getString("dt_txt").split(" ")[0];
    		
    		if(!checkDate.containsKey(date))
    			checkDate.put(date, day);
    	}
    	
    	// checking to validate data
        for (Map.Entry<String, JSONObject> entry : checkDate.entrySet()) {
            System.out.println("Date: " + entry.getKey());
            System.out.println(entry.getValue().toString(2));
        }
        
        // extracting forecast daily data
        ArrayList<Integer> forecastTemp = new ArrayList<>();
        ArrayList<String> forecastDescr = new ArrayList<>();
        ArrayList<Integer> forecastHigh = new ArrayList<>();
        ArrayList<Integer> forecastLow = new ArrayList<>();
        for (Map.Entry<String, JSONObject> entry : checkDate.entrySet()) {
        	int temperature = entry.getValue().getJSONObject("main").getInt("temp");
        	String desc = entry.getValue().getJSONArray("weather").getJSONObject(0).getString("description");
        	int high = entry.getValue().getJSONObject("main").getInt("temp_max");
        	int low = entry.getValue().getJSONObject("main").getInt("temp_min");
        	
        	forecastTemp.add(temperature);
        	forecastDescr.add(desc);
        	forecastHigh.add(high);
        	forecastLow.add(low);
        }
        
        for(int i = 0; i < forecastTemp.size(); i++) {
        	System.out.println("Temp: " + forecastTemp.get(i));
        	System.out.println("Description: " + forecastDescr.get(i));
        	System.out.println("High: " + forecastHigh.get(i));
        	System.out.println("Low: " + forecastLow.get(i));
        }
    }
}
