import java.util.Random;
import java.util.ArrayList;
public class GibbsSampler {
    static int x, y;
    static ArrayList<Integer> randIndex = new ArrayList<>(); // starting index to find motifs for each DNA
    public static void run(String key, ArrayList Dna, int k) {
        long start = System.currentTimeMillis(); // start time to calculate total elapsed time

        x = Dna.size();
        y = k;
        int minScore = -1;
        int counter = 0;
        char[][] motifs = new char[x][k]; // to store Motifs
        double[][] profile = new double[4][k]; // to store profile

        // produce random starting index to find motifs for each DNA
        Random rand = new Random();
        for (int i = 0; i<x; i++) {
            int randomNum = rand.nextInt((Dna.get(i).toString().length() - y + 1) + 1);
            randIndex.add(randomNum);
        }

        while (true){
            motifs = CreateMotifs(Dna);
            int rn = rand.nextInt(x);
            profile = CreateProfile(motifs, rn);
            CalculateProb(rn, String.valueOf(motifs[rn]), profile);
            int currentScore = CalculateScore(motifs);

            counter++;
            if (minScore == -1 || minScore > currentScore){
                minScore = currentScore;
                counter = 0;
            }

            if(counter >= 50){

                // afte
                System.out.println("Score "+minScore);
                System.out.println("---------------------------------");
                System.out.println("Best Motif");
                for (int i = 0; i<motifs.length; i++){
                    for (int j = 0; j<motifs[i].length; j++) {
                        System.out.print(motifs[i][j]+" ");
                    }
                    System.out.println();
                }
                System.out.println("---------------------------------");
                System.out.println("Consensus String: "+FindConsensus(profile, k));

                break;
            }
        }


        // calculate execution time
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("----------");
        System.out.println("Execution Time: "+(timeElapsed)+" milliseconds");

    }

    // code that finds consensusString on profile
    private static String FindConsensus(double[][] profile, int k){

        String consensusString = "";

        for (int i = 0; i<k; i++){
            double numberOfA = profile[0][i]; // counter for A
            double numberOfC = profile[1][i]; // counter for C
            double numberOfG = profile[2][i]; // counter for G
            double numberOfT = profile[3][i]; // counter for T

            // if there is at most A
            if (numberOfA >= numberOfC && numberOfA >= numberOfG && numberOfA >= numberOfT){
                consensusString+= "A";
            }

            // if there is at most C
            else if (numberOfC >= numberOfA && numberOfC >= numberOfG && numberOfC >= numberOfT){
                consensusString+= "C";
            }

            // if there is at most G
            else if (numberOfG >= numberOfC && numberOfG >= numberOfA && numberOfG >= numberOfT){
                consensusString+= "G";
            }

            // if there is at most T
            else if (numberOfT >= numberOfC && numberOfT >= numberOfG && numberOfT >= numberOfA){
                consensusString+= "T";
            }
        }
        return consensusString;
    }

    // the pattern with the highest probability in the deleted row is found and the starting index for the new motif is updated
    private static void CalculateProb(int deletedRowOrder, String deletedRow, double[][] profile){
        double maxProb = -1;
        int maxProbStartingIndex = -1;

        for(int i = 0; i< deletedRow.length() - y + 1; i++){
            double currentProb = 1;
            for (int j = 0; j<y; j++){
                // for each pattern.
                switch (deletedRow.charAt(i + j)){
                    case 'A':
                        currentProb *= profile[0][j]; // couter for A
                        break;
                    case 'C':
                        currentProb *= profile[1][j]; // couter for C
                        break;
                    case 'G':
                        currentProb *= profile[2][j]; // couter for G
                        break;
                    case 'T':
                        currentProb *= profile[3][j]; // couter for T
                        break;
                    default:
                        break;
                }
            }

            // update maxProb if currentProb is bigger than maxProb
            if(currentProb > maxProb){
                maxProb = currentProb;
                maxProbStartingIndex = i;
            }
        }

        // update after find max probability pattern
        randIndex.set(deletedRowOrder, maxProbStartingIndex);
    }

    // function that calculates the score of the motif
    private static int CalculateScore(char[][] motifs) {
        int sumScore = 0;
        for (int i = 0; i< motifs[0].length; i++){
            int numberOfA = 0;
            int numberOfC = 0;
            int numberOfG = 0;
            int numberOfT = 0;
            for (int j = 0; j<motifs.length; j++){
                switch (motifs[j][i]){
                    case 'A':
                        numberOfA++; // Counter of A
                        break;
                    case 'C':
                        numberOfC++; // Counter of C
                        break;
                    case 'G':
                        numberOfG++; // Counter of G
                        break;
                    case 'T':
                        numberOfT++; // Counter of T
                        break;
                    default:
                        break;
                }
            }

            // if there is at most A nucleotides other than A are added.
            if (numberOfA >= numberOfC && numberOfA >= numberOfG && numberOfA >= numberOfT){
                sumScore = sumScore + numberOfC + numberOfG + numberOfT;
            }

            // if there is at most A nucleotides other than C are added.
            else if (numberOfC >= numberOfA && numberOfC >= numberOfG && numberOfC >= numberOfT){
                sumScore = sumScore + numberOfA + numberOfG + numberOfT;
            }

            // if there is at most A nucleotides other than G are added.
            else if (numberOfG >= numberOfC && numberOfG >= numberOfA && numberOfG >= numberOfT){
                sumScore = sumScore + numberOfC + numberOfA + numberOfT;
            }

            // if there is at most A nucleotides other than T are added.
            else if (numberOfT >= numberOfC && numberOfT >= numberOfG && numberOfT >= numberOfA){
                sumScore = sumScore + numberOfC + numberOfA + numberOfG;
            }
        }
        return sumScore;
    }

    // generate motifs from DNA using start indexes.
    private static char[][] CreateMotifs(ArrayList dna) {
        char[][] currentMotifs = new char[x][y];
        for (int i = 0; i<x; i++){
            for (int j = 0; j<y; j++){
                currentMotifs[i][j] = dna.get(i).toString().charAt(randIndex.get(i)+j);
            }
        }
        return currentMotifs;
    }
    private static double[][] CreateProfile(char[][] motifs, int nr) {
        double[][] currentProfile = new double[4][y];

        for (int i = 0; i<motifs[0].length; i++){
            for (int j = 0; j<motifs.length; j++){
                if (i != nr){

                    switch (motifs[j][i]){
                        case 'A':
                            currentProfile[0][i] += 1.0/Double.parseDouble(String.valueOf(y)); // if A add probability
                            break;
                        case 'C':
                            currentProfile[1][i] += 1.0/Double.parseDouble(String.valueOf(y)); // if C add probability
                            break;
                        case 'G':
                            currentProfile[2][i] += 1.0/Double.parseDouble(String.valueOf(y)); // if G add probability
                            break;
                        case 'T':
                            currentProfile[3][i] += 1.0/Double.parseDouble(String.valueOf(y)); // if T add probability
                            break;
                        default:
                            break;
                    }
                }
            }
        }


        // adding 1 to all of them and editing the denominator.
        for (int i = 0; i<currentProfile.length; i++){
            for (int j = 0; j<currentProfile[i].length; j++){
                currentProfile[i][j] += 1.0;
                currentProfile[i][j] *= (1.0/(4+y-1));
            }
        }
        return currentProfile;
    }
}
