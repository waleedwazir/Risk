
package sample;

import java.io.File;
import java.util.ArrayList;

public class Countries
{
    File file;
    private ArrayList<Country> countries;

    //reads and stores all all coordinates in the countries array
    public Countries()
    {
        this.file = new File("./resources/countries.txt");
        readFile();
    }
    public void readFile() {

        Reader reader = new Reader(file);
        reader.run();
        countries = reader.getCountries();
    }

    public ArrayList<Country> getCountries()
    {
        return countries;
    }



    /*//returns an ArrayList of all the pixels in a country
    public ArrayList<Coordinate> getIndonesia()
    {
        return countries.get(0);
    }
    public ArrayList<Coordinate> getNewGuinea()
    {
        return countries.get(1);
    }
    public ArrayList<Coordinate> getWesternAustralia()
    {
        return countries.get(2);
    }
    public ArrayList<Coordinate> getEasternAustralia()
    {
        return countries.get(3);
    }
    public ArrayList<Coordinate> getNorthAfrica()
    {
        return countries.get(4);
    }
    public ArrayList<Coordinate> getCongo()
    {
        return countries.get(5);
    }
    public ArrayList<Coordinate> getEygpt()
    {
        return countries.get(6);
    }
    public ArrayList<Coordinate> getEastAfrica()
    {
        return countries.get(7);
    }
    public ArrayList<Coordinate> getSouthAfrica()
    {
        return countries.get(8);
    }
    public ArrayList<Coordinate> getMadagascar()
    {
        return countries.get(9);
    }
    public ArrayList<Coordinate> getArgentina()
    {
        return countries.get(10);
    }
    public ArrayList<Coordinate> getVenezuela()
    {
        return countries.get(11);
    }
    public ArrayList<Coordinate> getPeru()
    {
        return countries.get(12);
    }
    public ArrayList<Coordinate> getBrazil()
    {
        return countries.get(13);
    }
    public ArrayList<Coordinate> getIrelandAndUK()
    {
        return countries.get(14);
    }
    public ArrayList<Coordinate> getIceland()
    {
        return countries.get(15);
    }
    public ArrayList<Coordinate> getWesternEurope()
    {
        return countries.get(16);
    }
    public ArrayList<Coordinate> getSouthernEurope()
    {
        return countries.get(17);
    }
    public ArrayList<Coordinate> getNorthernEurope()
    {
        return countries.get(18);
    }
    public ArrayList<Coordinate> getUkraine()
    {
        return countries.get(19);
    }
    public ArrayList<Coordinate> getScandinavia()
    {
        return countries.get(20);
    }
    public ArrayList<Coordinate> getGreenland()
    {
        return countries.get(21);
    }
    public ArrayList<Coordinate> getQuebec()
    {
        return countries.get(22);
    }
    public ArrayList<Coordinate> getWesternUSA()
    {
        return countries.get(23);
    }
    public ArrayList<Coordinate> getAlberta()
    {
        return countries.get(24);
    }
    public ArrayList<Coordinate> getOntario()
    {
        return countries.get(25);
    }
    public ArrayList<Coordinate> getNorthWestTerritory()
    {
        return countries.get(26);
    }
    public ArrayList<Coordinate> getEasternUSA()
    {
        return countries.get(27);
    }
    public ArrayList<Coordinate> getCentralAmerica()
    {
        return countries.get(28);
    }
    public ArrayList<Coordinate> getAlaska()
    {
        return countries.get(29);
    }
    public ArrayList<Coordinate> getMiddleEast()
    {
        return countries.get(30);
    }
    public ArrayList<Coordinate> getIndia()
    {
        return countries.get(31);
    }
    public ArrayList<Coordinate> getChina()
    {
        return countries.get(32);
    }
    public ArrayList<Coordinate> getSlam()
    {
        return countries.get(33);
    }
    public ArrayList<Coordinate> getMongolia()
    {
        return countries.get(34);
    }
    public ArrayList<Coordinate> getJapan()
    {
        return countries.get(35);
    }
    public ArrayList<Coordinate> getAfghanistan()
    {
        return countries.get(36);
    }
    public ArrayList<Coordinate> getUral()
    {
        return countries.get(37);
    }
    public ArrayList<Coordinate> getSiberia()
    {
        return countries.get(38);
    }
    public ArrayList<Coordinate> getIrkutsk()
    {
        return countries.get(39);
    }
    public ArrayList<Coordinate> getYakutsk()
    {
        return countries.get(40);
    }
    public ArrayList<Coordinate> getKamchatka()
    {
        return countries.get(41);
    }*/
}