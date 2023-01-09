import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.print("Enter k value (Integer): ");
        int k = myObj.nextInt(); // gets k value from user

        ArrayList dna = new ArrayList(); // The DNA chains in the input file are added to this list.

        String key = null;
        try {
            File myObj2 = new File("input.txt");
            Scanner myReader = new Scanner(myObj2);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data.length() >= 1 && data.charAt(0) == '.'){
                    dna.add(data.substring(2)); // get DNAs
                } else if(data.startsWith("key: ")){
                    key = (data.substring(5)); // get Key
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        assert key != null;

        // Run Randomized Motif Search and print outputs
        System.out.println("\n\n---- Randomized Motif Search\n");
        for (int i = 0; i< 5; i++){
            System.out.println("###############");
            System.out.println((i+1)+". Iteration");
            RandomizedMotifSearch.run(key, dna, k);
            System.out.println();
        }

        // Run Gibbs Sampler and print outputs
        System.out.println("\n\n---- Gibbs Sampler\n");
        for (int i = 0; i< 5; i++){
            System.out.println("###############");
            System.out.println((i+1)+". Iteration");
            GibbsSampler.run(key, dna, k);
            System.out.println();
        }

        // Run Median String and print outputs
        MedianString.run(k, dna); // We run this algorithm with k = 9, 10, 11

    }
}