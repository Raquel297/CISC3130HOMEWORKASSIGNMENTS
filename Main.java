//Raquel A. 
//CISC 3130 - TY9 Homework Assignment # 2
import java.io.*;
import java.util.Scanner;
import java.io.BufferedReader; 
import java.util.ArrayList;
import java.io.PrintWriter;
//Please note that I used 4 weeks worth of global song data because I believe a quarter is a month and global song data reveals
//more information than just song data from a particular country

public class Main{
    public static void main(String[] args) throws IOException{
      
      File[] files;
      //The path name below can be changed when directory is created
      File fileHold = new File("C:/Users/Account 1/Documents/SpotifyFiles");
      //Now, we have an array of files containing the spotify files data in the directory. 
      //You can create a directory to hold each file and then you can run the program
      //There is code to create a directory of files, but I do not want to write it for multiple unexplained reasons
      files = fileHold.listFiles(); 
      final int SONGS = 200;
      String song[] = new String[SONGS];
      ArrayList<String> songList = new ArrayList<String>();
      //This array list holds the list of songs from every file without duplicates 
      ArrayList<String> noDuplicates = new ArrayList<String>();
      QueueUsingLinkedList songs = new QueueUsingLinkedList();
      for(int n = 0; n < files.length; n++){
         processFiles(n, files, song);
         insertIntoArrayList(song, songList);
      }
      sortSongs(songList);
      //Since we are working with multiple files from different weeks, they may share songs. 
      //Therefore, our array list songList will have duplicates
      //We remove these duplicates
      removeDuplicates(songList, noDuplicates);
      //It is unnecessary to merge two queues into one since we can just directly insert the elements from the array list 
      //into one queue 
      insertManyIntoQueue(songs, noDuplicates);
      //Now, we output the song list data to a text file 
      outputToFile(noDuplicates);
      //Now, we send songs that were listened to to the stack data structure
      //We prompt the user and ask them if they want to listen to a song
      //If they do, the song will be removed from the queue and placed into the stack 
      StackUsingLinkedList recentlyListenedTo = new StackUsingLinkedList();
      Scanner response = new Scanner(System.in);
      System.out.println("Would you like to listen to a song? Press Y or y if yes and any other key if not.");
      char input = response.next().charAt(0);
      while(input == 'Y' || input == 'y'){
         sendToStack(songs, recentlyListenedTo);
         System.out.println("We added the song to your history play list. Would you like to view your play list?."+
                            " Press Y or y if yes and any other key if not.");
         input = response.next().charAt(0);
         if(input == 'Y' || input == 'y'){
            viewPlayList(recentlyListenedTo);
         }
         System.out.println("Would you like to listen to a song? Press Y or y if yes and any other key if not.");
         input = response.next().charAt(0);
       }
       response.close();
      } //End main() method 
    
    public static void processFiles(int k, File[] files, String[] song) throws IOException{
        FileReader read = new FileReader(files[k]);
        BufferedReader connect = new BufferedReader(read);
        for(int i = 0; i < song.length; i++){
              String[] kipper = connect.readLine().split(","); 
              //Now, the song[] array will soon have every song from every file (after the iterates of the for-loop in main() is done)
              song[i] = kipper[1];
        }
    } //End method
    
    public static void insertIntoArrayList(String[] song, ArrayList<String> songList){
      for(int i = 0; i < song.length; i++){
         songList.add(song[i]);
      }
    } //End method 
    
    //When this method is continuously called, it will automatically create ONE queue with EVERY song from EVERY file!
    //That way, there is no need to merge two queues into one as this method already completes the task
    public static void insertManyIntoQueue(QueueUsingLinkedList songs, ArrayList<String> noDups){
        for(int i = 0; i < noDups.size(); i++){
            songs.insert(noDups.get(i));
        }
    } //End method
    
    public static void sortSongs(ArrayList<String> songList){
        //The choice of sorting algorithms we learned in class does not matter, since the bubble sort, insertion sort, and selection sort each 
        //have a worst-case run-time of N^2 (assuming that there are N data elements). However, I decided to implement the insertion sort because I understand 
        //it the least.
        for(int i = 0; i < songList.size(); i++){
            String temp = songList.get(i);
            int j = i;
            String compare2 = temp.toLowerCase().replaceAll(".;(;);[;];*;$;%;-;+;,;&;x", "");
            while(j > 0 && songList.get(j-1).toLowerCase().replaceAll(".;(;);[;];*;$;%;-;+;,;&;x", "").compareTo(compare2) > 0){
                songList.set(j, songList.get(j-1));
                j--;
            }
            songList.set(j, temp);
        }
    } //End method
    
    public static void removeDuplicates(ArrayList<String> songs, ArrayList<String> noDups){
      //Since the array list songs is already ordered alphabetically, the algorithm we use to check for duplicates is very simple
      //in that it only requires a few lines (if the songs were not ordered alphabetically, we would have to use a nested 
      //for loop
      boolean firstCheck = true;
      for(int i = 0; i < songs.size() - 1; i++){
        if(!(songs.get(i).equals(songs.get(i+1))) || firstCheck == false){
          noDups.add(songs.get(i));
          firstCheck = true;
        }
      }
    } //End method
    
    //This method outputs the array list that holds the non-duplicate songs in alphabetical order to a text file 
    //Note: We may instead use the queue songs back in the main() method since it has the same song list as the array list 
    //without duplicates 
    public static void outputToFile(ArrayList<String> songsWithoutDuplicates) throws IOException{
      PrintWriter printTo = new PrintWriter("SpotifyMultipleWeeks.txt");
      for(int i = 0; i < songsWithoutDuplicates.size(); i++){
         printTo.println(songsWithoutDuplicates.get(i));
      }
      printTo.close();
    } //End method
    
    //This method removes an element from the queue (I only implemented removing from the head and not the rear of the queue
    //because I felt it is more logical and practical) to remove the "next runner-up" from the queue and not the song
    //that is the last element of the queue. Infact, many i-Pods and youtube playlists work this way.
    public static void sendToStack(QueueUsingLinkedList songs, StackUsingLinkedList songNoDup){
        songNoDup.push(songs.remove());
    } //End method
    
    //This method calls the display() method in the stack class and it displays the deleted elements from the queue 
    public static void viewPlayList(StackUsingLinkedList history) throws IOException{
        history.display();
    } //End method 
      
    //This method merges two queues into one queue 
    //I did not need to use it at all, however, I will keep it here in the event that I want to use it
    public static QueueUsingLinkedList mergeQueue(QueueUsingLinkedList queue1, QueueUsingLinkedList queue2){
        QueueUsingLinkedList merged = new QueueUsingLinkedList();
        //This removes the first element from queue 1 and places it in merged queue and then removes the first element from queue 2 and places it in merged queue
        while(!(queue1.isEmpty() && queue2.isEmpty())){
            if(!queue1.isEmpty())
              merged.insert(queue1.remove());
            if(!queue2.isEmpty())
              merged.insert(queue2.remove());
        }
        return merged;
    } //End method
    
}

class QueueUsingLinkedList{ 
    class Node{
      String song;
      Node next;
      //Node constructor method
      Node(String data){
        song = data;
        next = null;
      }
    }
    Node rear, head;
    int numberOfItems; //I like to keep track of the number of items in the Queue
    QueueUsingLinkedList(){
        rear = null;
        head = null;
        numberOfItems = 0; 
    }
    

    public boolean isEmpty(){
      return(numberOfItems == 0);
    }
    
    public void insert(String song){
      Node temp = new Node(song);
      if(rear == null){
         head = temp;
         rear = temp;
      }
      rear.next = temp;
      rear = temp;
      numberOfItems++;
    }
    
    public String remove(){
      if(isEmpty()){
        System.out.println("The queue is empty");
        rear = null;
        return "";
      }
      else{
        Node temp = head;
        head = head.next;
        numberOfItems--;
        return temp.song;
      }
    }
    
    //This method returns the head/front of the queue
    public String peakHead(){
      if(isEmpty()) //We check to see if the queue is completely empty
        return "The queue is empty.";
      else 
        return head.song;
    }
    
    //This method returns the rear of the queue 
    public String peakRear(){
      if(isEmpty()){ //We check to see if the queue is completely empty
        rear = null;
        return "The queue is empty";
      }
      else
        return rear.song;
    }
    
} //End of Queue class 


//We implement the Stack data structure so that we can keep track of the music listened to. 
//If a song is removed from a play list, it will go to the stack we created.
//I decided to implement the stack using a linked list. I am aware of its shortcomings. For example, users typically listen to many songs 
//and so the linked list will be accessed frequently because so many songs will be added to the history. However, it is better than arrays 
//since we can add as many songs as we want to. I also wanted to practice using linked lists since we just learned it in class. 
class StackUsingLinkedList{
    class Node{
       String song;
       Node next;
       //Node Constructor method
       Node(){
           next = null;
       }
    }
    Node top; //This reference variable points to the top of the Stack
    int numberOfItems;
    
    //Linked List Constructor method 
    StackUsingLinkedList(){
        top = null;
        numberOfItems = 0;
    }
    
    //This method allows the user/programmer to add songs to their history list if they removed it from the queue 
    public void push(String data){
           Node temp = new Node();
           temp.song = data; 
           temp.next = top; 
           top = temp;
           numberOfItems++;
    }
    
    public boolean isEmpty(){
        return(numberOfItems == 0); 
    }
    
    //This method allows the user/programmer to see what is at the top of the stack
    public String peek(){
        if(!isEmpty()){
            return top.song;
        }
        return "The stack is empty.";
    }
    
    //This method allows the user/programmer to "remove" an element from the stack
    public void pop(){
        if(!isEmpty()){
           top = top.next; //This line of code is very interesting to me, because it means that the top element of the stack isn't actually removed
           //from memory; instead we just move the top Node pointer to the next top-most element in the stack. 
        }
        else{
           System.out.println("The stack is empty.");
            }
        numberOfItems--;
    }
    
    //This method allows the user/programmer to view their current song history on the screen/console
    //I am aware that we are not required to implement this method, however, I decided to because I believe it is helpful 
    public void display() throws IOException{
        Node temp = top;
        if(top == null)
          System.out.println("The stack is empty");
        while(temp != null){
           System.out.println(temp.song);
           temp = temp.next; //Now, the pointer reference variable temp is pointing to the next top-most item in the stack
        }
    }
} //End of stack with linked list implementation
