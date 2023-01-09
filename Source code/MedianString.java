import java.util.ArrayList;

public class MedianString {
    static int minSum = -1;
    static String bestPattern = "";
    public static void run(int k, ArrayList dna){
        long start = System.currentTimeMillis();

        char[] set = {'A', 'C', 'T', 'G'};


        int n = set.length;
        findAllPatternsAndRunAlgorithm(dna, set, "", n, k);


        // calculate execution time.
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;


        System.out.println("-------------");
        System.out.println("Results for k = 9");
        System.out.println("Score: "+minSum);
        System.out.println("Pattern: "+bestPattern + " (" + k + "-mer)");
        System.out.println("Total Time: "+timeElapsed/1000 + " seconds");


    }

    private static int MedianStringAlgorithm(String key, ArrayList dna) {
        int[][] distances = new int[dna.size()][dna.get(0).toString().length() - key.length() + 1]; // Find distance for all DNAs

        for (int i = 0; i < dna.size(); i++){
            for (int j = 0; j < dna.get(i).toString().length() - key.length() + 1; j++){
                for (int a = 0; a<key.length(); a++){
                    if (key.toUpperCase().charAt(a) == dna.get(i).toString().toUpperCase().charAt(j+a)){
                    }else {
                        distances[i][j] += 1;
                    }
                }
            }
        }

        int sum = 0;
        int min = -1;

        // find minimum distance
        for (int i = 0; i<distances.length; i++){
            min = -1;
            for (int j = 0 ; j< distances[i].length; j++){
                if (distances[i][j] < min || min == -1){
                    min = distances[i][j];
                }
            }
            sum += min;
        }

        // save best score and pattern
        if(minSum == -1 || sum < minSum){
            minSum = sum;
            bestPattern = key;
        }

        return sum;
    }




    static void findAllPatternsAndRunAlgorithm(ArrayList dna, char[] set, String prefix, int n, int k){
        if (k == 0)
        {
            // Send current pattern to algorithm.
            MedianStringAlgorithm(prefix, dna);
            return;
        }

        for (int i = 0; i < n; ++i)
        {
            String newPrefix = prefix + set[i];
            findAllPatternsAndRunAlgorithm(dna, set, newPrefix, n, k - 1);
        }
    }
}
