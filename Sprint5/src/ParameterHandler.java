import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;


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

    public void write(String winner1, String winner2) throws FileNotFoundException {
        String[] winner1params = winner1.split(",");
        String[] winner2params = winner2.split(",");
        PrintWriter pw = new PrintWriter(new FileOutputStream("./src/NapoleonGenes.txt", false));
        Random rand = new Random();
        for(int i = 0;i < 8;i++){
            swap(winner1params, winner2params, getRandomNumber(0, 17));
        }
        mutate(winner2params, 2);
        String[] newNapoleon3params = winner1params;
        String[] newNapoleon4params = winner2params;
        mutateRandomise(newNapoleon3params);
        mutateRandomise(newNapoleon4params);

        String toWrite=representDna(winner1params)+"\n"+representDna(winner2params)+"\n"+representDna(newNapoleon3params)+"\n"+representDna(newNapoleon4params);
        pw.println(toWrite);
        pw.close();
    }

    public String representDna(String[] input){
        String dna = "";
        for(int i = 0;i < input.length;i++){
            if(i< input.length-1)
                dna+=input[i]+",";
            else
                dna+=input[i];
        }
        return dna;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public double getRandomDouble(double min, double max) {
        return ((Math.random() * (max - min)) + min);
    }

    public void mutateRandomise(String[] mutationParams){
        for(int toMutate = 0;toMutate < 17;toMutate++){
            if(toMutate < 4) {
                int mutation = Integer.parseInt(mutationParams[toMutate]);
                mutation += getRandomNumber(-1, 2);
                mutationParams[toMutate] = String.valueOf(mutation);
            }else{
                double mutation = Double.parseDouble(mutationParams[toMutate]);
                mutation *= ThreadLocalRandom.current().nextDouble(0.9, 1.1);
                mutationParams[toMutate] = String.valueOf(mutation);
            }
        }
    }


    public void mutate(String[] toMutateParams, int numMutations){
        for(int i = 0;i < numMutations;i++){
            int toMutate = getRandomNumber(0, 17);
            if(toMutate < 4) {
                int mutation = Integer.parseInt(toMutateParams[toMutate]);
                mutation += getRandomNumber(-1, 2);
                toMutateParams[toMutate] = String.valueOf(mutation);
            }else{
                double mutation = Double.parseDouble(toMutateParams[toMutate]);
                mutation *= ThreadLocalRandom.current().nextDouble(0.9, 1.1);
                toMutateParams[toMutate] = String.valueOf(mutation);
            }
        }
    }

    public void swap(String[] winner1params, String[] winner2params, int toSwap){
        String temp = winner1params[toSwap];
        winner1params[toSwap] = winner2params[toSwap];
        winner2params[toSwap] = temp;
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
