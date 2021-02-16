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
        int minChange = 6;
        int minChangeIndex = -1;
        for(int i=0;i<42;i++)
        {
            if(countryNames[i].equalsIgnoreCase(name))
            {
                return i;
            }
            int change = calculateLevenshtein(name, countryNames[i]);
            if(change<minChange) {
                minChange = change;
                minChangeIndex = i;
            }
        }
        return minChangeIndex;
    }

    static int calculateLevenshtein(String x, String y) {
        if(x.length()==0){
            return 6;
        }
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
                    dp[i][j] = min(dp[i - 1][j - 1]
                                    + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1);
                }
            }
        }

        return dp[x.length()][y.length()];
    }

    public static int costOfSubstitution(char a, char b) {
        return Character.toLowerCase(a) == Character.toLowerCase(b) ? 0 : 1;
    }

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
