import java.util.*;
import java.util.List;
import java.io.*;
import java.util.Map.Entry;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
//          Cory Campbell
// NB) the visualization question that we are to come up with is in the comment below
//          CISC 3130
//          Assignment #4
//          MW 9:05 - 10:45
/* This program scans a csv file and makes a chart showing the genres of different movies and the amount of them per genre, from largest to smallest.
 * There are two classes, a Chart class which holds the code to make the chart and a Info class which parses and sorts the info from the file.
 * The file is sent to 5 methods in the info class to parse the data through the parse and year methods, then counts the data through the count method and finally orders and cleans up empty or null spaces in the descending order method.
 * The info needed is then sent to different mehtods which copy the data and send them to the chart class.
 * Using a line graph and Javafx, the genre and counter method is used to create a table showing the data.
 * Answer: I would like to show the store owner the amount of each genre that was released in order to help him get a good idea for his Catalog sizes, as well as which ones are more
 * commonly made, especially when combining it witht the averages found in this program.
 */

//Assignment4 class to create the chart
public class Assignment4 extends Application{
  
  @Override
  public void start(Stage primaryStage) throws Exception{
    init(primaryStage);
  }
  
  //Receives all the data from the info method and places the information on the stage
  public void init(Stage primaryStage) throws Exception{
    info movie = new info();
    ArrayList<Entry<String,Integer>> entries_copy = new ArrayList<>();
    String [] genres = movie.genre_copy();
    int [] year_copy = movie.years(); 
    entries_copy = info.movies();
    int [] counter = movie.counter_copy();
    
    
    //Creates the borders and size of the chart
    HBox root = new HBox();
    Scene scene = new Scene(root, 600, 600);
    
    //Sets up the Axis for the chart
    CategoryAxis xAxis = new CategoryAxis();
    xAxis.setLabel("Genre");
    
    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Amount per Genre");
    
    LineChart<String,Number> lineChart = new LineChart<>(xAxis,yAxis);
    lineChart.setTitle("My Chart of Movies Genres, Cory Campbell");
    
    XYChart.Series<String,Number> data = new XYChart.Series<>();
    
    //Assigns the data called from info to the chart
    for(int i = 0; i < 10;i++ ){
      data.getData().add(new XYChart.Data<String,Number>(genres[i], counter[i]));
    }
   
    lineChart.getData().add(data);
    root.getChildren().add(lineChart);
    primaryStage.setTitle("Chart of Movie Genres");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  public static void main(String [] args){
    //Calls the chart methods 
    launch(args);
  }
}
//Info class sorts out all the data for the Chart
class info{
  private static final int amount = 21;
  private static String arr[] = new String[amount];
  private static int [] year = new int [amount];
  private static String [] genre = new String [20];
  private static String [] parse = new String [43];
  private static int [] counter_arr = new int[10];
  private static String [] genres = new String[10];
  private static String [] more_average = new String[10];
  private static String [] less_average = new String[10];
  private static HashMap <String,Integer> movies = new HashMap<>();
  private static ArrayList<Entry<String,Integer>> entries;
  private  Scanner sc;
  
  public info()throws Exception{
    sc = new Scanner(new FileReader("D:\\Assignment#3\\movies\\movies.csv"));
    
    int count = 0;
    while (sc.hasNext() && count < amount) {
      arr[count] = sc.nextLine();
      count++;
    }
    //Gets the genres by calling genre method
    genres(arr,genre);
    
        for(int x = 1; x < 20; x++){
          genre[x-1] = genre[x];
        }
        //Gets the separate genres
        parsing(genre,parse);
        //Gets the years from the name
        year(arr,year);
        //Counts the occurence of each genre
        count(parse,counter_arr,genres);
        //Sorts the array
        descendingOrder(genres,counter_arr,year);
        //Finds the average, more than average and less that average
        average(genres,counter_arr,less_average,more_average);
        
        
        for(int i = 0; i < genres.length;i++){
          movies.put(genres[i],counter_arr[i]);
        }
        
        for(int x = 1; x < 20; x++){
          year[x-1] = year[x];
        }     
        
        //Assigns the data in a map
        Map <String, Integer> sorted = new TreeMap<>(movies);
    
        //Map is sorted from the map into an ArrayList to be sent to the Chart class 
        entries = new ArrayList<>(sorted.entrySet());
        Collections.sort(entries, new Comparator<Entry<String,Integer>>(){
          @Override
          public int compare(Entry<String,Integer>o1, Entry<String,Integer>o2)
          {
            return o2.getValue().compareTo(o1.getValue()); 
          }
        });
    
    
  }
  public static void genres(String [] arr, String [] genre){      
      int count = 1;
      while ((!(arr[count] == null)) && count <= 19) {
        String[] x = arr[count].split(",");
        String piece1 = null;
        for (int i = 0; i < x.length; i++) {
          for (int j = 0; j < x[i].length(); j++) {
            if (x[i].charAt(j) == '"' && x[i + 1].charAt(x[i + 1].length() - 1) == '"') {
              piece1 = x[i] + x[i + 1];
              x[2] = x[3];
            }
          }
        }
        genre[count] = x[2];
        count++;
      }
  }
  
  public static void parsing(String [] genre,String [] parse){
    int count = 0;
    int amount = 0;
    while((!(genre[count] == null)) && count <= 18){
      String [] parts = genre[count].split("\\|");
      for(String gen : parts){
        parse[amount] = gen;
        amount++;
      }
      count++;
      }
  }
  
  public static void count(String [] parse, int [] counter_arr, String [] genres){
    String holder;
    int [] points = new int[10];
    String [] arr = {"Adventure","Animation","Children","Comedy","Fantasy","Romance","Drama","Crime","Thriller","Horror"};
    for(int j = 0; j < arr.length; j++){
        int counter = 0;
        for(int i = 0; i < parse.length; i++){
          if(arr[j].equals(parse[i])){
            counter++;
          }
        }
        points[j] = counter;
        counter_arr[j] = points[j];
        genres[j] = arr[j];
        
    }
  }
  public static void descendingOrder(String [] genres, int [] counter_arr,int [] year){
    String [] genres_copy = genres;
      int [] counter_copy = counter_arr;
      int [] year_copy = year;
      int temp;
      int temp3;
      String temp2;
      for (int i = 0; i < counter_arr.length; i++) 
      {
        for (int j = i + 1; j < counter_arr.length; j++) { 
          if (counter_copy[i] < counter_copy[j]) 
          {
            temp = counter_copy[i];
            temp2 = genres_copy[i];
            if(year_copy[i] == 0)
              temp3 = year_copy[i+1];
            else
              temp3 = year_copy[i];
            counter_copy[i] = counter_copy[j];
            genres_copy[i] = genres_copy[j];
            year_copy[i] = year_copy[j]; 
            counter_copy[j] = temp;
            genres_copy[j] = temp2;
            year_copy[j] = temp3;
          }
        }
      }
      for(int i = 0; i < counter_copy.length;i++){
        counter_arr[i] = counter_copy[i];
        genres[i] = genres_copy[i];
        year[i] = year_copy[i];
      }
  }
  public static void year(String [] arr, int [] year){
    int count = 1;
    while ((!(arr[count] == null)) && count <= 19) {
        String[] x = arr[count].split(",");
        String piece1 = null;
        for (int i = 0; i < x.length; i++) {
          for (int j = 0; j < x[i].length(); j++) {
            if (x[i].charAt(j) == '"' && x[i + 1].charAt(x[i + 1].length() - 1) == '"') {
              piece1 = x[i] + x[i + 1];
              x[2] = x[3];
            }
          }
        }
        
        String name = x[1].replaceAll("[^0-9]", "");
        String name2 = null;
        if (!(piece1 == null))
          name2 = piece1.replaceAll("[^0-9]", "");
        int year_temp = 0;
        
        //Years of each movies release
        if (piece1 == null) {
          year_temp = Integer.parseInt(name);
        } else if (!(piece1 == null)) {
          year_temp = Integer.parseInt(name2);
        }
        year[count] = year_temp;
        count++;
    }
  }
  public static void average(String [] genres, int [] counter_arr, String [] less_average, String [] more_average){
    int [] counter_copy = counter_arr;
    String [] genres_copy = genres;
    int total = 0;
    double sum;
    double average = 0;
    double decimal;
    for(int x : counter_copy)
      total += x;
    sum = (double)total;
    average = (sum/counter_copy.length);
    for(int i = 0; i < counter_copy.length; i++){
      decimal = (double)counter_copy[i];
      if(average > decimal){
        less_average[i] = genres_copy[i];
      }
      else if(average < decimal){
        more_average[i] = genres_copy[i];
      }
    }
  }
  public static ArrayList<Entry<String,Integer>> movies(){
    ArrayList<Entry<String,Integer>> numbers = new ArrayList<>();
    numbers = entries;
    return(numbers);
  }
  public static int [] years(){
    int [] year_copies = year;
    return (year_copies);
  }
    public static String [] genre_copy(){
      String [] genre_copies = genres;
      return (genre_copies);
    }
     public static int [] counter_copy(){
       int [] counter_copies = counter_arr;
       return (counter_copies);
     }
}



