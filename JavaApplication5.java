//Raquel A. 
//CISC 3130 - TY9 Homework Assignment # 4
//Note to professor: Some methods share the same variable names. I could not think of creative and appropriate 
//names, so I decided to re-use them. 
package javaapplication5;
import java.io.*;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.BufferedReader; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.*;
import java.util.stream.Collectors;

public class JavaApplication5 {
   public static void main(String[] args) throws IOException{
      File[] files;
      //Below is the specified file path name in my computer where the files are stored
      //The file path name may be changed as long as the original files are stored in the new file path
      //There is a way to create a directory of files, but I think for this assignment it is uneccessary, since either way
      //would require one to save and store the files in a directory. 
      File fileHold = new File("C:/Users/Account 1/Downloads/movieInformation/ml-latest-small");
      //Now, we have an array of files containing the different files of the movies in the directory. 
      //Note: We only need to access and process files[1], since it is the file that contains the genre list
      files = fileHold.listFiles(); 
      //Instead of re-inventing the wheel, I decided to use the existing HashMap java interface.
      //I learned of the Integer data type, which is different from the primitive data type int, in that 
      //Integer is a class that turns int into an object/non-primitive data type
      HashMap<String, Integer> movieGenres = new HashMap<>();
      HashMap<Integer, Integer> years = new HashMap<>(); 
      ArrayList<String> genre = new ArrayList<>();
      //We create two parallel array lists so that the data will be easier to work with when we figure out how many of each genre appeared each year
      ArrayList<Integer> copyYearData = new ArrayList<>(); //I copied the year data (with duplicates) from the file into an array list
      ArrayList<String> copyGenreData = new ArrayList<>(); //I copied the genre (with duplicates) from the file into an array list
      HashMap<String, MovieGenre[]> secondQuestionAnswer = new HashMap<>();
      processMovieGenres(files, years, movieGenres, copyYearData, copyGenreData); 
      outputResultsToFile(movieGenres); //This method puts the resulting key, value pairs into a text file
      Scanner user = new Scanner(System.in);
      String response;
      //I decided to create a genre 
      System.out.println("We can also display the number of times a movie genre appeared each year. Which movie genre are you looking for? Press N to exit.");
      response = user.nextLine();
      while(!response.equals("N")){
         filterResults(response, copyYearData, copyGenreData, secondQuestionAnswer, years);
         System.out.println("Do you want to search again? Enter N to exit or a movie genre to continue.");
         response =  user.nextLine(); 
      }
      user.close();
      
    } //End main Method
    public static void processMovieGenres(File[] files, HashMap<Integer, Integer> movieYears, HashMap<String, Integer> movieGenres, ArrayList<Integer> copyYearData, ArrayList<String> copyGenreData)throws IOException{
     FileReader movieFile = new FileReader(files[1]);
     BufferedReader connect = new BufferedReader(movieFile);
     final int FILESIZE = 9742;
     for(int i = 0; i < FILESIZE; i++){
       Integer frequency;
       String[] temp1 = connect.readLine().split(",");
       //The string year holds the release year of the movie
       String year = temp1[1].substring(temp1[1].lastIndexOf('(')+1, temp1[1].length()-1).replaceAll("\\)",""); //Extract the yesr from the title
       String years = year.replaceAll("\\(;\\);'\"\';\'\'", "");
       int yearly = Integer.parseInt(years);
       if(movieYears.containsKey(yearly)){
          frequency = movieYears.get(yearly)+1;
          movieYears.put(yearly, frequency);
       }
       else{
           frequency = 1;
           movieYears.put(yearly, frequency);
       }
       String[] temp2 = temp1[2].split("\\|"); //We take into account that there are many genres that have a "|" delimiter.
       int counter = 0; 
       if(temp2[0].isEmpty() == false){
           for(int j = 0; j < temp2.length; j++){
               if(movieGenres.containsKey(temp2[j])){
                 frequency = movieGenres.get(temp2[j])+1;
                 movieGenres.put(temp2[j], frequency);
               } //End if-statement
               else{
                   frequency = 1;
                   movieGenres.put(temp2[j], frequency);
               } //End else-statement
               copyGenreData.add(temp2[j]);
               copyYearData.add(yearly);
           } //End for-loop
       } //End if-statement
       else{
           if(movieGenres.containsKey(temp1[2])){
               frequency = movieGenres.get(temp1[2])+1;
               movieGenres.put(temp1[2], frequency);
           } //End if-statement
           else{
               frequency = 1;
               movieGenres.put(temp1[2], frequency);
           } //End else-statement
            copyGenreData.add(temp1[2]);
            copyYearData.add(yearly);
       } //End else-statement
     } //End for-loop
     //Now, after the for loop has iterated, the HashMap movieGenres will contain each unique movie genre and frequency
     //Meanwhile, the ArrayList movieYears contains the year each movie genre was made 
     //You should turn the movieYears into a parallel HashMap that contains the frequency of each movie Genre
  } //End method processMovieFile
    public static void filterResults(String result, ArrayList<Integer> copyYearData, ArrayList<String> copyGenreData, HashMap<String, MovieGenre[]> sQA, HashMap<Integer, Integer> years){
       final int SIZE = copyGenreData.size();
       String[] copyGenreArray = new String[SIZE]; 
       int[] copyYearArray = new int[SIZE];
       for(int i = 0; i < SIZE; i++){ //This is poor programming practice. There is absolutely no reason to copy the data into an array with finitely many elements
           copyGenreArray[i] = copyGenreData.get(i);
           copyYearArray[i] = copyYearData.get(i);
       }
       MovieGenre[] choiceGenre = new MovieGenre[SIZE]; //Note that this may be a partially filled array
       int counter = 0;
       int[] array = new int[copyYearData.size()];
       for(int i = 0; i < SIZE; i++){
           if(copyGenreArray[i].equals(result)){
               choiceGenre[counter] = new MovieGenre(copyYearData.get(i), result);
               array[counter] = copyYearData.get(i); //I decided to use this simple int array to stream and not choiceGenre  
               counter = counter + 1;
           }
       }
       //The HashMap sQA does not contain any movie title strings. I decided to leave the movie titles out of this program. Of course, I could 
       //easily modify this program to attach movie title strings, but I decided not to.
       sQA.put(result, choiceGenre);
       //The following for-statement enables a search for how many times the genre specified occured for each year
       years.keySet().forEach((key) -> {
           long val = Arrays.stream(array).filter(i -> i == key).count();
           System.out.println(val + "  is the number of times the genre " + result + " appeared in year 1957");
       });
    }
    public static void outputResultsToFile(HashMap<String, Integer> movieGenres)throws IOException{
        PrintWriter results = new PrintWriter("GenreandFrequency.txt");
        String key = "";
        Integer frequency = 0;
        //Please note that the results are printed in the middle of text file. I think it looks more professional. 
        results.printf("%35s %10s\n", "Movie Genres  ", "Frequency"); 
        for(String genre : movieGenres.keySet()){
            key = genre.toString();
            frequency = movieGenres.get(key);
            results.printf("%32s%10d\n", key, frequency);
        }
        results.close();
    } //End method outputResultsToFile
    
    //The following method counts how many times a movie genre appeared each year 
    
} //End class Main

//We create a custom abstract data type called MovieGenre
class MovieGenre{
    //We declare our class variables 
    int year;
    String genre;
    //Here is the class constructor method 
    public MovieGenre(int y, String g){
        year = y;
        genre = g;
    }
}
