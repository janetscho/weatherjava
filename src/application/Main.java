package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
	
	public static String city = "";
	
	public static boolean toggle = true;
	public static String icon = "";
	public static int humidity = 0;
	public static int wind = 0;
	public static String description = "";
	public static int temp = 0;
	public static int tempMax = 0;
	public static int tempMin = 0;
	public static int feels = 0;
	public static int sunrise = 0;
	public static int sunset = 0;
    public static ArrayList<Integer> forecastTemp = new ArrayList<>();
    public static ArrayList<String> forecastDescr = new ArrayList<>();
    public static ArrayList<Integer> forecastHigh = new ArrayList<>();
    public static ArrayList<Integer> forecastLow = new ArrayList<>();

    public static void main(String [] args) throws URISyntaxException, IOException {
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
    			"-fx-font-size: 14;" +
    			"-fx-background-color: #337A9E;" +
    			"-fx-text-fill: white;");
    	
    	search.setOnKeyPressed(e -> {
    		if(e.getCode() == KeyCode.ENTER) {
    			city = search.getText();
    			try {
					APICall(city);
				} catch (URISyntaxException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    			//primaryStage.setScene(null);
    			//primaryStage.close();
    			//secondStage();
    		}
    	});
    	
    	// search icon
    	ImageView searcher = new ImageView();
    	Image searched = new Image("file:./res/search.png");
    	searcher.setImage(searched);
    	searcher.setPreserveRatio(true);
    	searcher.setSmooth(true);
    	searcher.setFitWidth(20);
    	searcher.setTranslateX(1052);
    	searcher.setTranslateY(30);

    	// header
    	Rectangle header = new Rectangle();
    	header.setWidth(1920);
    	header.setHeight(80);
    	header.setFill(Color.web("#005986"));
    	
    	// F / C
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
    	
    	// premium
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
    	
    	// login
    	ImageView login = new ImageView();
    	Image logins = new Image("file:./res/login.png");
    	login.setImage(logins);
    	login.setPreserveRatio(true);
    	login.setSmooth(true);
    	login.setFitWidth(30);
    	login.setTranslateX(1480);
    	login.setTranslateY(25);
    	
    	// hamburger icon
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
        
        // keep this part since this basically holds the entire top portion of the site
    	Pane top = new Pane();
        top.getChildren().addAll(header, view, ibm, fc, search, searcher, mem, prem, login, ham);
        
        // home page
    	Text weatherToday = new Text("Weather Today Across the Country");
    	weatherToday.setStyle("-fx-font-size: 20;" +
    			"-fx-font-weight: bold;");
    	weatherToday.setTranslateX(520);
    	weatherToday.setTranslateY(177);
    	
    	LocalDate dated = LocalDate.now();
    	DateTimeFormatter form = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    	String date = dated.format(form);
    	date = date.replace(date.substring(0, 3), date.substring(0, 3).toUpperCase());
    	
    	Rectangle body = new Rectangle(900, 565);
    	body.setFill(Color.WHITE);
    	body.setTranslateX(510);
    	body.setTranslateY(143);
    	body.setArcWidth(20);
    	body.setArcHeight(20);
    	
    	ImageView weatherMap = new ImageView();
    	Image img = new Image("file:./res/weathermap.png");
    	weatherMap.setImage(img);
    	weatherMap.setPreserveRatio(true);
    	weatherMap.setSmooth(true);
    	weatherMap.setFitWidth(754);
    	weatherMap.setFitHeight(396);
    	weatherMap.setTranslateX(583);
    	weatherMap.setTranslateY(237);
    	
    	Text todayscast = new Text("TODAY'S FORECAST");
    	todayscast.setStyle("-fx-font-size: 14;" +
    			"-fx-font-weight: bold;");
    	todayscast.setTranslateX(530);
    	todayscast.setTranslateY(211);
    	Text today = new Text(date);
    	today.setStyle("-fx-font-size: 14;" +
    			"-fx-font-weight: bold;");
    	today.setTranslateX(1300);
    	today.setTranslateY(211);
    	
    	Button readmore = new Button("Read More");
    	readmore.setPrefSize(96, 32);
    	readmore.setTranslateX(533);
    	readmore.setTranslateY(653);
    	readmore.setStyle(
    			"-fx-text-fill: #FFFFFF;" +
    			"-fx-background-color: #1B4DE4;" +
    			"-fx-background-insets: 0, 1;" +
    			"-fx-background-radius: 5em;" +
    			"-fx-padding: 5px;" +
    			"-fx-font-size: 14px;" +
    			"-fx-font-weight: bold;" +
    			"-fx-content-display: center;" );
    	
    	Pane frontBody = new Pane(body, weatherMap, weatherToday, todayscast, today, readmore);
    	
    	Pane box = new Pane(frontBody, top);
        box.setBackground(new javafx.scene.layout.Background(new javafx.scene.layout.BackgroundFill(Color.web("#E8EEEE"), null, null)));
    	
    	Scene scene = new Scene(box, 1920, 1000);
    	primaryStage.setScene(scene);
    	primaryStage.show();
    }
    
    /*
    private void secondStage() {
    	Stage stage = new Stage();
    	
    	String iconURL = "https://openweathermap.org/img/w/" + icon + ".png";
    	ImageView icons = new ImageView(new Image(iconURL));

    	Scene scene = new Scene(top2, 1920, 1000);
    	stage.setScene(scene);
    	stage.show();
    }
    */
    
    private void newsStage() {
    	
    }
    
    // method to call API and store the resulting JSON data
    public static void APICall(String city) throws URISyntaxException, IOException {
    	
    	// defaults to metric unless specified
		String apiURL = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + map  + "&units=metric";
		String forecastURL = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + map + "&units=metric";
    	
    	if(!toggle) {
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
    	icon = data.getJSONArray("weather").getJSONObject(0).getString("icon");
    	humidity = data.getJSONObject("main").getInt("humidity");
    	wind = data.getJSONObject("wind").getInt("speed");
    	description = data.getJSONArray("weather").getJSONObject(0).getString("description");
    	temp = data.getJSONObject("main").getInt("temp");
    	tempMax = data.getJSONObject("main").getInt("temp_max");
    	tempMin = data.getJSONObject("main").getInt("temp_min");
    	feels = data.getJSONObject("main").getInt("feels_like");
    	sunrise = data.getJSONObject("sys").getInt("sunrise");
    	sunset = data.getJSONObject("sys").getInt("sunset");
    	
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
    	
        // extracting forecast daily data
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
        
    	// checking to validate data
        for (Map.Entry<String, JSONObject> entry : checkDate.entrySet()) {
            System.out.println("Date: " + entry.getKey());
            System.out.println(entry.getValue().toString(2));
        }
        for(int i = 0; i < forecastTemp.size(); i++) {
        	System.out.println("Temp: " + forecastTemp.get(i));
        	System.out.println("Description: " + forecastDescr.get(i));
        	System.out.println("High: " + forecastHigh.get(i));
        	System.out.println("Low: " + forecastLow.get(i));
        }
    }
}
