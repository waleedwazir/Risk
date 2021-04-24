import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;


public class ParameterHandler
{
    private String napoleon_1;
    private String napoleon_2;
    private String napoleon_3;
    private String napoleon_4;

    public void read() throws FileNotFoundException
    {
        File file = new File("./src/NapoleonGenes.txt");
        Scanner scan = new Scanner(file);
        napoleon_1 = scan.nextLine();
        napoleon_2 = scan.nextLine();
        napoleon_3 = scan.nextLine();
        napoleon_4 = scan.nextLine();
    }
    public void outputWinners(String winners) throws FileNotFoundException
    {
        PrintWriter out = new PrintWriter("./src/NapoleonGenes.txt");
        out.println(winners);
        out.close();
    }
    public void outPutRandom()
    {

    }

    public String getNapoleon_1()
    {
        return napoleon_1;
    }

    public String getNapoleon_2()
    {
        return napoleon_2;
    }

    public String getNapoleon_3()
    {
        return napoleon_3;
    }

    public String getNapoleon_4()
    {
        return napoleon_4;
    }

}
