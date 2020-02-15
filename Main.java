import java.util.Scanner;
import java.io.*;
//I decided to use only one Spotify chart (The United States). In my story, the CEO wants to know the top-200 artists in the USA!!!!
class Main{
    public static void main(String[] args) throws IOException{
        //We create an empty linked list
        LinkedList linkData = new LinkedList();
        File spotifyData = new File("Spotify.csv");
        //Connect scanner object scan to the file with the music data
        Scanner scan = new Scanner(spotifyData);
        //This value ROWS holds the number of artists we are tracking (with duplicates for now)
        final int ROWS = 200;
        //The first five columns are to hold the data from the CSV file
        final int COLUMNS = 5; //this number should NOT be changed
        String[][] artistsWithDuplicates = new String[ROWS][COLUMNS];
        readInFile(spotifyData, scan, artistsWithDuplicates, ROWS, COLUMNS);
        //Please note that the 1-dimensional arrays artistNameCheck[] and howMany[] may be partially filled arrays
        //We check to see how many times an artist's name appears
        //We make use of parallel arrays (NOT 2-dimensional arrays this time)
        //The 1-dimensional array artistNameCheck[] will contain each non-duplicated artist's name 
        //The 1-dimensional array howMany[] will contain the number of times an artist appeared on the Spotify top-200 chart
        String[] artistNameChecked = new String[ROWS];
        int[] howMany = new int[ROWS];
        boolean isDuplicate = false;
        checkDupsAndCount(artistNameChecked, artistsWithDuplicates, howMany, isDuplicate, ROWS);
        //We now call the method that will alphabetize the array of artist's names
        alphabetize(artistNameChecked, artistsWithDuplicates, ROWS, howMany);
        //I am aware that the array howMany[] can contain extraneous data. We will not print 
        //the extraneous data to the output file. That is why I wrote the if() condition below
        for(int i = 0; i < ROWS; i++){
          if(artistsWithDuplicates[i][2].equals("") == false){
               linkData.insertToLinkedList(artistNameChecked[i], howMany[i]);
          }
        }
        linkData.displayLinkedList();
        scan.close();
    }
    
    /**Method readInFile() reads in a CSV file and stores the data in a 2-dimensional array
     *@param spotify is an object of type file
     *@param scanner is an object of type Scanner that is connected to the spotify file
     *@param artDup[][] is a 2-dimensional array that holds 5 pieces of data from the spotify file: position, 
     *artist name, track, streams, and url
     *@ROW and @COLUMN hold the dimensions of the 2-d array artDup[][]
     *In this example, they hold 200*5 for the top-200 songs on spotify and the 5 pieces of data associated with each song
     */
     public static void readInFile(File spotify, Scanner scanner, String[][] artDup, int ROW, int COLUMN){
         for(int i = 0; i < ROW; i++){
            String temp1 = scanner.nextLine();
            for(int j = 0; j < COLUMN; j++){
               //Read in each line of the file into string temp1 
               String[] temp2 = temp1.split(",");
               //Store each piece of data from string temp1 (that is: position, track, artist name, streams, url)
               //into each of the five columns. Please note that artistsWithDuplicates[i][2] contains each artist's name
               //I decided store ALL artist names (even if there are duplicates) since there are different pieces
               //of data associated with each name (position, track, streams, url)
               //I will print all artist names (without duplication) by using a 1-dimensional array later on in the program
               artDup[i][j] = temp2[j];
            }
        }
    }
    /**Method checkDupsAndCount() counts the number of times an artist's name 
     *appears and checks if an artist's name appears more than once
     *@param artist[] is the 1-d array that holds the non-duplicated artist names
     *@param artDups[][] is the 2-d array that holds the spotify file data
     *@param counting[] is the 1-d array that holds the number of times each artist appeared on the spotify file
     *@param isDup is a boolean variable to help us decide if an artist's name has duplicates
     *@param ROW is the first dimension of the 2-d array artDups so that we may count through each artist's name on
     *the spotify list
     */
    public static void checkDupsAndCount(String[] artist, String[][] artDups, int[] counting, boolean isDup, int ROW){
        for(int i = 0; i < ROW; i++){
           //We presume that each artist's name appears at least once and if they appear more than once, we will add 1
           counting[i] = 1;
           for(int j = i+1; j < ROW; j++){
              if((artDups[i][2].toLowerCase()).equals((artDups[j][2].toLowerCase())) && artDups[i][2] != ""){
                //We get rid of the duplicate found
                artDups[j][2] = "";
                isDup = true;
                counting[i]++;
              }
           }
           //In the event that there are no duplicates
           if(isDup == false){
              artist[i]  = artDups[i][2];
           }
           //In the event that there are duplicates
           else{ 
              artist[i] = artDups[i][2];
           }
        }
    }
    /**Method alphabetize() rearranges the data in the list to be in alphabetical order
     *@param artNoDup[] is a 1-d array that holds each artist's name without duplicates
     *@artWithDup[][] is the 2-d array that holds the spotify data list. We need this 
     *array because as stated in the main method, some String artist names in artWithDup[][] 
     *are empty. 
     *@param ROW is the first dimension of the 2-d array artWithDup[][]. We need this piece of data
     *since artNoDup[] may be a partially filled array
     *@param howMany[] is the parallel array to artNoDup[]. We need this array because if there are
     *two names that are out of order, when we switch them to be in their proper place, we will need
     *to also switch their parallel values (the number of times they appeared on the spotify list)
     */
    public static void alphabetize(String[] artNoDup, String[][] artWithDup, int ROW, int[] howMany){
        for(int i = 0; i < ROW; i++){
            for(int j = i+1; j < ROW; j++){
               //It is quite difficult (in my opinion) to reverse the elements of a linked list
               //Therefore, when I sorted the names in the 1-dimensional array artNoDup[]
               //I ordered them from z-a (and not a-z). That way, the linked list will print 
               //out the names in alphabetical order (a-z)
               String name1 = artNoDup[i].toLowerCase().replaceAll(".;(;);[;];*;$;%;-;+;,;&;x", "");
               String name2 = artNoDup[j].toLowerCase().replaceAll(".;(;);[;];*;$;%;-;+;,;&;x", "");
               if(name1.compareTo(name2) < 0 && artWithDup[j][2].equals("") == false 
                  && artWithDup[i][2].equals("") == false){
                  String temp = artNoDup[j];
                  artNoDup[j] = artNoDup[i];
                  artNoDup[i] = temp;
                  int tempo = howMany[j];
                  howMany[j] = howMany[i];
                  howMany[i] = tempo;
               }
            }          
        }
    }
}

//We create the Node{} class
class Node{
        String musicArtists;
        int howMany;
        Node next; 
        //Constructor method/function for creating a new node
        Node(String music, int number){
            musicArtists = music;
            howMany = number;
        }
        public void holdStuff() throws IOException{
            PrintWriter print = new PrintWriter("SpotifyTop200ArtistsGloballyList.txt");
        }
        //This method displays the data in each node
        public void displayNodeData(PrintWriter print) throws IOException{
            String format = "%-20s %5d\n";
            print.printf(format, musicArtists, howMany);
        }
}

//We create the actual LinkedList{} class and implementation
class LinkedList{
    //We create the header reference variable that will point to the first node
    private Node head;
    LinkedList(){
        head = null;
    }
       public boolean isEmpty(){
           return (head == null);
       }
    //This method actually connects each node's piece of data 
    public void insertToLinkedList(String musicArtists, int number){
        //We create a new reference variable of type Node 
        Node musicPassOn = new Node(musicArtists, number);
        musicPassOn.next = head;
        head = musicPassOn;
    }
    //HERE IS WHERE YOU ARE SUPPOSED TO IMPLEMENT THE DISPLAY METHOD/FUNCTION THAT WILL PUT THE LIST OF ALPHABETIZED ARTIST NAMES INTO AN OUTPUT TEXT FILE 
    public void displayLinkedList() throws IOException{
        Node current = head;
        PrintWriter print = new PrintWriter("SpotifyTop200ArtistsList.txt");
        while(current != null){
            current.displayNodeData(print);
            current = current.next;
        }
        print.close();
    }
}

