package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.json.*;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.BoundingBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
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
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

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
	public static String timezone = "";
	public static String country = "";
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
    	
    	DropShadow drop = new DropShadow();
    	drop.setOffsetX(0);
    	drop.setOffsetY(0);
    	drop.setColor(Color.web("#C9C9C9"));
    	
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
    	ibm.setFont(Font.font("Arial", FontWeight.BOLD, 14));
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
					
					secondStage();
					primaryStage.close();
					
				} catch (URISyntaxException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
    			"-fx-background-color: #82D1DB;" +
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
    	    			"-fx-background-color: #82D1DB;" +
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
        
        
        // --------------------------------------------------------
        
        // home page
    	Text weatherToday = new Text("Weather Today Across the Country");
    	weatherToday.setStyle("-fx-font-size: 20;" +
    			"-fx-font-weight: bold;");
    	weatherToday.setTranslateX(530);
    	weatherToday.setTranslateY(177);
    	
    	LocalDate dated = LocalDate.now();
    	DateTimeFormatter form = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    	String date = dated.format(form);
    	date = date.replace(date.substring(0, 3), date.substring(0, 3).toUpperCase());
    	
    	DropShadow shadow = new DropShadow();
    	shadow.setOffsetX(0);
    	shadow.setOffsetY(0);
    	shadow.setColor(Color.web("#C9C9C9"));
    	
    	Rectangle body = new Rectangle(900, 565);
    	body.setFill(Color.WHITE);
    	body.setTranslateX(510);
    	body.setTranslateY(143);
    	body.setArcWidth(20);
    	body.setArcHeight(20);
    	body.setEffect(shadow);
    	
    	ImageView weatherMap = new ImageView();
    	Image img = new Image("file:./res/weathermap.png");
    	weatherMap.setImage(img);
    	weatherMap.setSmooth(true);
    	weatherMap.setFitWidth(803);
    	weatherMap.setFitHeight(396);
    	weatherMap.setTranslateX(558);
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
    	
    	// ----------------------------------------------------------------
    	
    	// news time
    	
    	Rectangle news = new Rectangle(900, 1057);
    	news.setFill(Color.WHITE);
    	news.setTranslateX(510);
    	news.setTranslateY(735);
    	news.setArcWidth(20);
    	news.setArcHeight(20);
    	news.setEffect(shadow);
    	
    	Text topStory = new Text("TOP STORY");
    	topStory.setStyle("-fx-font-size: 20px;" +
    			"-fx-font-weight: bold;");
    	topStory.setFill(Color.RED);
    	topStory.setTranslateX(530);
    	topStory.setTranslateY(770);
    	
    	Text news1 = new Text("HOLIDAY WEEKEND OUTLOOK");
    	news1.setStyle("-fx-font-size: 40px;");
    	news1.setTranslateX(558);
    	news1.setTranslateY(820);
    	ImageView topNews = new ImageView(new Image("file:./res/news1.png"));
    	topNews.setSmooth(true);
    	topNews.setFitWidth(803);
    	topNews.setFitHeight(396);
    	topNews.setTranslateX(558);
    	topNews.setTranslateY(840);
    	
    	Text news12 = new Text("Here's Where Your Travel Could Be Slick");
    	news12.setStyle("-fx-font-size: 24px;");
    	news12.setTranslateX(558);
    	news12.setTranslateY(1274);
    	
    	ImageView news2 = new ImageView(new Image("file:./res/news2.png"));
    	news2.setSmooth(true);
    	news2.setFitWidth(190);
    	news2.setFitHeight(102);
    	news2.setTranslateX(558);
    	news2.setTranslateY(1305);
    	Text news22 = new Text("Forecase For The Return Home\nThis Weekend");
    	news22.setStyle("-fx-font-size: 14px");
    	news22.setTranslateX(558);
    	news22.setTranslateY(1428);
    	
    	ImageView news3 = new ImageView(new Image("file:./res/news3.png"));
    	news3.setSmooth(true);
    	news3.setFitWidth(190);
    	news3.setFitHeight(102);
    	news3.setTranslateX(762);
    	news3.setTranslateY(1305);
    	Text news33 = new Text("Live Colorada Weather Blog,\nDec. 5-7: Updates from\nDenver's Storm");
    	news33.setStyle("-fx-font-size:14px;");
    	news33.setTranslateX(762);
    	news33.setTranslateY(1428);
    	
    	ImageView news4 = new ImageView(new Image("file:./res/news4.png"));
    	news4.setSmooth(true);
    	news4.setFitWidth(190);
    	news4.setFitHeight(102);
    	news4.setTranslateX(966);
    	news4.setTranslateY(1305);
    	Text news44 = new Text("Chicago First Alert Weather:\nCloudy Skies, Colder Temps");
    	news44.setStyle("-fx-font-size:14px");
    	news44.setTranslateX(966);
    	news44.setTranslateY(1428);
    	
    	ImageView news5 = new ImageView(new Image("file:./res/news5.png"));
    	news5.setSmooth(true);
    	news5.setFitWidth(190);
    	news5.setFitHeight(102);
    	news5.setTranslateX(1171);
    	news5.setTranslateY(1305);
    	Text news55 = new Text("I'm going to kill myself");
    	news55.setStyle("-fx-font-size:14px;");
    	news55.setTranslateX(1171);
    	news55.setTranslateY(1428);
    	
    	Image play = new Image("file:./res/play.png");
    	
    	ImageView play1 = new ImageView(play);
    	play1.setSmooth(true);
    	play1.setPreserveRatio(true);
    	play1.setFitWidth(20);
    	play1.setTranslateX(576);
    	play1.setTranslateY(1500);
    	Text play11 = new Text("Surprise Snowfall Inside A City Bus");
    	play11.setStyle("-fx-font-size: 14;");
    	play11.setTranslateX(608);
    	play11.setTranslateY(1514);

    	
    	ImageView play2 = new ImageView(play);
    	play2.setSmooth(true);
    	play2.setPreserveRatio(true);
    	play2.setFitWidth(20);
    	play2.setTranslateX(576);
    	play2.setTranslateY(1544);
    	Text play22 = new Text("Southern California: Potential Snowfall");
    	play22.setStyle("-fx-font-size: 14;");
    	play22.setTranslateX(608);
    	play22.setTranslateY(1560);
    	Line ln2 = new Line(558, 1532, 1360, 1532);
    	ln2.setOpacity(0.5);
    	
    	ImageView play3 = new ImageView(play);
    	play3.setSmooth(true);
    	play3.setPreserveRatio(true);
    	play3.setFitWidth(20);
    	play3.setTranslateX(576);
    	play3.setTranslateY(1589);
    	Text play33 = new Text("More Weather News");
    	play33.setStyle("-fx-font-size: 14;");
    	play33.setTranslateX(608);
    	play33.setTranslateY(1606);
    	Line ln3 = new Line(558, 1578, 1360, 1578);
    	ln3.setOpacity(0.5);
    	
    	ImageView play4 = new ImageView(play);
    	play4.setSmooth(true);
    	play4.setPreserveRatio(true);
    	play4.setFitWidth(20);
    	play4.setTranslateX(576);
    	play4.setTranslateY(1634);
    	Text play44 = new Text("Lots of Snow Over There");
    	play44.setStyle("-fx-font-size: 14;");
    	play44.setTranslateX(608);
    	play44.setTranslateY(1651);
    	Line ln4 = new Line(558, 1624, 1360, 1624);
    	ln4.setOpacity(0.5);
    	
    	ImageView play5 = new ImageView(play);
    	play5.setSmooth(true);
    	play5.setPreserveRatio(true);
    	play5.setFitWidth(20);
    	play5.setTranslateX(576);
    	play5.setTranslateY(1679);
    	Text play55 = new Text("Yup Still Kinda Warm In SoCal For Some Reason");
    	play55.setStyle("-fx-font-size: 14;");
    	play55.setTranslateX(608);
    	play55.setTranslateY(1693);
    	Line ln5 = new Line(558, 1669, 1360, 1669);
    	ln5.setOpacity(0.5);
    	
    	Button seemore = new Button("See More");
    	seemore.setPrefSize(96, 32);
    	seemore.setTranslateX(533);
    	seemore.setTranslateY(1728);
    	seemore.setStyle(
    			"-fx-text-fill: #FFFFFF;" +
    			"-fx-background-color: #1B4DE4;" +
    			"-fx-background-insets: 0, 1;" +
    			"-fx-background-radius: 5em;" +
    			"-fx-padding: 5px;" +
    			"-fx-font-size: 14px;" +
    			"-fx-font-weight: bold;" +
    			"-fx-content-display: center;" );
    	
    	Pane newsBody = new Pane(news, topStory, news1, topNews, news12, news2, news22, news3, news33, news4, news44, news5, news55, 
    			play1, play11, play2, play22, ln2, play3, play33, ln3, play4, play44, ln4, play5, play55, ln5, seemore);
    	
    	// ----------------------------------------------------------------
    	
    	Rectangle temp = new Rectangle(1920, 2000);
    	temp.setFill(Color.web("#E8EEEE"));
    	
    	Pane frontBody = new Pane(body, weatherMap, weatherToday, todayscast, today, readmore);
    	
    	Pane box = new Pane(temp, frontBody, newsBody, top);
        box.setBackground(new javafx.scene.layout.Background(new javafx.scene.layout.BackgroundFill(Color.web("#E8EEEE"), null, null)));
        box.setPadding(new Insets(20));
        
        ScrollPane scrollPane = new ScrollPane(box);
        scrollPane.setFitToWidth(true);
        
    	Scene scene = new Scene(scrollPane, 1920, 1000);
    	primaryStage.setScene(scene);
        primaryStage.setX(-10);
        primaryStage.setY(0);
    	primaryStage.show();
    }
    
    private void secondStage() {
    	Stage stage = new Stage();
    	
    	String iconURL = "https://openweathermap.org/img/w/" + icon + ".png";
    	ImageView icons = new ImageView(new Image(iconURL));
    	
    	DropShadow drop = new DropShadow();
    	drop.setOffsetX(0);
    	drop.setOffsetY(0);
    	drop.setColor(Color.web("#C9C9C9"));
    	
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
    	ibm.setFont(Font.font("Arial", FontWeight.BOLD, 14));
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
					secondStage();
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
    			"-fx-background-color: #82D1DB;" +
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
    	    			"-fx-background-color: #82D1DB;" +
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
        
        
        // ---------------------------------
        
        Rectangle timeBox = new Rectangle(900, 51);
        timeBox.setTranslateX(510);
        timeBox.setTranslateY(135);
        timeBox.setArcHeight(20);
        timeBox.setArcWidth(20);
        timeBox.setFill(Color.web("#337A9E"));
        Rectangle tempTimeBox = new Rectangle(900, 27);
        tempTimeBox.setTranslateX(510);
        tempTimeBox.setTranslateY(160);
        tempTimeBox.setFill(Color.web("#337A9E"));
        
    	String currentCity = city + ", " + country + " As of " + timezone;
    	Text location = new Text(currentCity);
    	location.setStyle(
    			"-fx-font-size: 20;" +
    			"-fx-font-weight: bold;" 
    			);
    	location.setFill(Color.WHITE);
    	location.setTranslateX(525);
    	location.setTranslateY(168);
        
        Rectangle currentW = new Rectangle(900, 235);
        currentW.setTranslateX(510);
        currentW.setTranslateY(135);
    	currentW.setArcWidth(20);
    	currentW.setArcHeight(20);
    	
    	Pane timePane = new Pane(currentW, timeBox, tempTimeBox, location);
        
    	Pane all = new Pane(top, timePane);
    	
    	Scene scene = new Scene(all, 1920, 1000);
    	stage.setScene(scene);
    	stage.show();
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
    	country = data.getJSONObject("sys").getString("country");
    	int timeTemp = data.getInt("timezone");
    	
    	long currentTime = System.currentTimeMillis();
    	Instant inst = Instant.ofEpochMilli(currentTime);
    	ZoneId zone = ZoneId.ofOffset("UTC" , ZoneOffset.ofTotalSeconds(timeTemp));
    	ZonedDateTime zoned = ZonedDateTime.ofInstant(inst, zone);
    	DateTimeFormatter timez = DateTimeFormatter.ofPattern("HH:mm");
    	timezone = zoned.format(timez);
    	
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
    	
    	// could use a zero constructor but... whatever...
    	forecastTemp.clear();
    	forecastDescr.clear();
    	forecastHigh.clear();
    	forecastLow.clear();
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
        
        /*
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
        */
    }
}
