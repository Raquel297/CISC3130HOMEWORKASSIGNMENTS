//Raquel A. 
//CISC 3130 - TY9 Homework Assignment # 3
//Note to professor: Some methods share the same variable names. I could not think of creative and appropriate 
//names, so I decided to re-use them. 

import java.io.*;
import java.util.Scanner;
import java.io.BufferedReader; 
import java.util.ArrayList;

public class Main{
  public static void main(String[] args)throws IOException{
      File[] files;
      //Below is the specified file path name in my computer where the files are stored
      //The file path name may be changed as long as the original files are stored in the new file path
      //There is a way to create a directory of files, but I think for this assignment it is uneccessary, since either way
      //would require one to save and store the files in a directory. 
      File fileHold = new File("C:/Users/Account 1/Downloads/movieInformation/ml-latest-small");
      //Now, we have an array of files containing the different files of the movies in the directory. 
      //Note: We only need to access and process files[1], since it is the file that contains the movie title list
      files = fileHold.listFiles(); 
      ArrayList<String> movies = new ArrayList<String>();
      //I recently learned that ArrayLists can only be parameterized by non-primitive data types.
      //I learned of the Integer data type, which is different from the primitive data type int, in that 
      //Integer is an class that turns int into an object/non-primitive data type
      ArrayList<Integer> years = new ArrayList<Integer>();
      processMovieFile(files, movies, years);
      //for(int i = 0; i < 9742; i++)
        //System.out.println(movies.get(i));
      //Now, we create an empty binary search tree
      BinarySearchTree movieTree = new BinarySearchTree();
      //Now, we want to input and store MANY movie titles (~10000) into the binary search tree. 
      //Since there are too many movie titles to add, we create a seperate method/function for this particular task
      insertManyIntoTree(movies, movieTree);
      //When we are done inserting, we will want the user to be able to search for a movie title based on the name
      String movieName;
      Scanner scan = new Scanner(System.in);
      System.out.println("What movie title are you searching for?");
      movieName = scan.nextLine();
      while(!movieName.equals("-1")){
         boolean status = movieTree.search(movieName);
         if(status == false){
           System.out.println("We could not find the movie you are looking for.");
         }
         else{
           System.out.println("We found the movie you are looking for!!!");
         }
         System.out.println("Search for another movie or press -1 to exit");
         String fix = scan.nextLine();
         movieName = scan.next();
     }
     scan.close();
     //Below is an example of using the subSet() method to print all movie titles in alphabetical order in a given range
     //Please note I decided to output the results on the console/screen and NOT a text file 
     movieTree.subSet("A Bug's Life", "Harry Potter");
     movieTree.subSet("Bug's life", "Bad Boys II"); //Please notice that I did NOT include "A" in the first title 
     
  }
  public static void processMovieFile(File[] files, ArrayList<String> movieTitles, ArrayList<Integer> movieYears)throws IOException{
     FileReader movieFile = new FileReader(files[1]);
     BufferedReader connect = new BufferedReader(movieFile);
     final int FILESIZE = 9742;
     //After this for-loop has iterated, the array list movieTitles will now contain each movie title from the movies.csv file
     for(int i = 0; i < FILESIZE; i++){
       String[] temp1 = connect.readLine().split(",");
       //The string year holds the release year of the movie
       String year = temp1[1].substring(temp1[1].lastIndexOf('(')+1, temp1[1].length()-1).replaceAll("\\)","");
       String years = year.replaceAll("\\(;\\);'\"\';\'  \'", "");
       //I added the trim() method from the String class because there is trailing white space 
       //after each movie title and I wanted to remove it, because when the user enters the name of the movie,
       //the binary search tree only looks for the exact titles of the movie and not different versions of it.
       //There are ways to implement this such that white space does not matter, but for the purposes of this assignment;
       //That is, to learn how the binary search tree works, I will not handle complex cases such as the user 
       //not entering the exact title as stored in the binary search tree
       String titleOnly = temp1[1].substring(0, temp1[1].indexOf("(")).trim();
       movieTitles.add(titleOnly);
       //I decided to only add the years to the array list and NOT the binary search tree
       //because I believe the user should only have to search the title by name and NOT have to INCLUDE the year
       //However, it is good to have the years of each movie stored somewhere, so I decided to store them in an array list
       int yearly = Integer.parseInt(years);
       movieYears.add(yearly); 
     }
  }
  public static void insertManyIntoTree(ArrayList<String> movies, BinarySearchTree movieTree){
    for(int i = 0; i < movies.size(); i++){ //You may also write this method/function recursively
       movieTree.insert(movies.get(i));
    }
  }
  
} //End class main

class Node{
   String title; //The binary search tree works with strings only. This is so that the user can search
   Node leftChild; //This is the left-most child 
   Node rightChild;
   //This is the Node constructor method
   Node(String movieTitle){
       title = movieTitle;
       leftChild = null;
       rightChild = null;
   }
} //End class Node

class BinarySearchTree{
    Node root; //This variable holds the root of the tree
    //BinarySearchTree constructor method
    BinarySearchTree(){
        root = null;
    }
    //Now, we implement several methods associated with the binary search tree that are relevant to this program
    //such as isEmpty(), insert(), search(), delete()
    public boolean isEmpty(){ //This method checks to see if the binary search tree is empty
        return (root == null); //This method returns true if the root node is not pointing to any value
    }
    //The recursive insertion method for a binary search tree requires the creation 
    //of two methods (similar to the merge sort)
    public void insert(String key){
      root = insertNode(root, new Node(key));
    }
    public Node insertNode(Node parent, Node current){
      if(parent == null){
         return current;
      }
      else if(parent.title.toLowerCase().compareTo(current.title.toLowerCase()) < 0){
         parent.rightChild = insertNode(parent.rightChild, current);
      }
      else if(parent.title.toLowerCase().compareTo(current.title.toLowerCase()) > 0){
         parent.leftChild = insertNode(parent.leftChild, current);
      }
      //Notice that we did not include a check for if current is equal to the parent. That is because we do not want duplicates
      //in our binary search tree. We only need to work with the unique movie titles. 
      return parent;
    }
    public boolean search(String key){
       Node status = searchTree(root, new Node(key));
       if(status == null){
         return false;
       }
       return true;
    }
    public Node searchTree(Node parent, Node key){
      if(parent == null || parent.title.replaceAll("\\);\\(", "").toLowerCase().equals(key.title)){
         return parent;
      }
      else if(key.title.toLowerCase().compareTo(parent.title.replaceAll("\\);\\(", "").toLowerCase()) < 0){
         return searchTree(parent.leftChild, key);
      }
      else if(key.title.toLowerCase().compareTo(parent.title.replaceAll("\\);\\(", "").toLowerCase()) > 0){
         return searchTree(parent.rightChild, key);
      }
      return parent;
    }
    public void subSet(String title1, String title2){
       Node temp = root;
       treeSubset(temp, title1, title2);
    }
    //This method allows the user to view a subset of titles in alphabetical order from title1 to title 2
    //It is very similar to the regular searchTree() method, except for a few if-else conditions
    //This method also assumes that title1 < title 2 alphabetically. However, it is simple to add a condition in the main() method
    //in the Main class to ensure that title1 is always less than title 2, but I will not add the condition now
    public void treeSubset(Node temp, String title1, String title2){
      if(temp == null){
         return;
      }
      if(temp.title.toLowerCase().compareTo(title1) > 0){
         treeSubset(temp.leftChild, title1, title2);
      }
      if(temp.title.toLowerCase().compareTo(title1) >= 0 && temp.title.compareTo(title2) <= 0){
         System.out.println(temp.title);
      }
      if(temp.title.toLowerCase().compareTo(title2) < 0){
         treeSubset(temp.rightChild, title1, title2);
      }
    }
} //End class BinarySearchTree