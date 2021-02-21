package Map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Country implements Iterable<Coordinate>{
    private String name;
    private ArrayList<Coordinate> coordinates= new ArrayList<Coordinate>();
    private static String[] countryNames = {"Indonesia", "New Guinea", "Western Australia", "Eastern Australia", "North Africa", "Congo",
            "Egypt", "East Africa", "South Africa", "Madagascar", "Argentina", "Venezuela", "Peru", "Brazil", "UK & Ireland",
            "Iceland", "Western Europe", "Southern Europe", "Northern Europe", "Ukraine", "Scandinavia", "Greenland", "Quebec",
            "Western USA", "Alberta", "Ontario", "North West Territory", "Eastern USA", "Central America", "Alaska",
            "Middle East", "India", "China", "Siam", "Mongolia", "Japan", "Afghanistan", "Ural", "Siberia", "Irkutsk", "Yakutsk",
            "Kamchatka"};

    //adds a coordinate to the country's coordinate arraylist
    public void addCoordinate(Coordinate c){
        coordinates.add(c);
    }

    //getter for coordinates of a country
    public ArrayList<Coordinate> getCoordinates(){
        return coordinates;
    }

    //setter for country's name
    public void setName(String name) {
        this.name = name;
    }

    //getter for country's name
    public String getName(){
        return name;
    }


    public int getIndex()
    {
        for(int i=0;i<42;i++)
        {
            if(countryNames[i].equals(name))
            {
                return i;
            }
        }
        return -1;
    }

    public static String getCountryNameFromIndex(int index){
        return countryNames[index];
    }


    public static int getIndexFromCountryName(String name)
    {
        int minChange = 6; //min change cutoff value, prevents the user from finding faulty results
        int minChangeIndex = -1;
        for(int i=0;i<42;i++)
        {
            if(countryNames[i].equalsIgnoreCase(name))
            {
                return i;
            }
            int change = calculateLevenshtein(name, countryNames[i]);
            if(change<minChange) {  //this find the most similar index to the inputted data
                                    //we use this to return the index in the case their is no
                                    //exact match
                minChange = change;
                minChangeIndex = i;
            }
        }
        return minChangeIndex;
    }


    //method to determine the amount of changes you have to make to String x to get String y,
    //this is used to help determine what country a user was trying to type if they make
    //a typo or if they write a shortened version.
    public static int calculateLevenshtein(String x, String y) {
        if(x.length()==0){
            return 6; //6 is the cutoff number and will prevent the code from producing invalid results
        }
        //an array we populate when making changes to a word
        int[][] dp = new int[x.length() + 1][y.length() + 1];


        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                }
                else if (j == 0) {
                    dp[i][j] = i;
                }
                else {
                    //this determines if we substitute, insert, remove a letter
                    //or do nothing to String x to make it like String y at the
                    //position
                    dp[i][j] = min(dp[i - 1][j - 1]
                                    + costOfSubstitution(x.charAt(i - 1),
                                    y.charAt(j - 1)),
                                    dp[i - 1][j] + 1,
                                    dp[i][j - 1] + 1);
                }
            }
        }
        return dp[x.length()][y.length()];
    }

    //helper function to levenshtein distance
    //determine if a letter must be substituted
    public static int costOfSubstitution(char a, char b) {
        return Character.toLowerCase(a) == Character.toLowerCase(b) ? 0 : 1;
    }

    //helper function for levenshtein distance
    public static int min(int... numbers) {
        return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }



    //Iterator for iterating through country's coordinates
    private class CountryIterator<Coordinate> implements Iterator<Coordinate>{
        int index = 0;

        @Override
        public boolean hasNext() {
            return index < coordinates.size();
        }

        @Override
        public Coordinate next() {
            if(hasNext()){
                Coordinate ret = (Coordinate) coordinates.get(index);
                index++;
                return ret;
            }else{
                throw new NoSuchElementException();
            }
        }
    }

    //initialise Country iterator
    @Override
    public Iterator<Coordinate> iterator() {
        return new CountryIterator<Coordinate>();
    }
}
