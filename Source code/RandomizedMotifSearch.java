import java.util.Random;
import java.util.ArrayList;
public class RandomizedMotifSearch {
    static int x, y;
    public static void run(String key, ArrayList Dna, int k) {
        long start = System.currentTimeMillis();

        x = Dna.size();
        y = k;
        int t = Dna.size();
        int motifScore = 0;
        int oldScore = 0;
        int minScore = -1;
        int counter = 0;
        char[][] motifs = new char[t][k];
        char[][] bestMotifs = new char[t][k];
        double[][] profile = new double[4][k];
        double[][] bestProfile = new double[4][k];

        motifs = CreateRandomMotifs(Dna);

        while (true){

            profile = CreateProfile(motifs);
            motifs = CreateMotifs(Dna, profile);
            motifScore = CalculateScore(motifs, key);

            // Checks for changes. If it has not changed, the counter increases. If the counter exceeds 50, the algorithm stops.
            if (oldScore == motifScore){
                counter++;
            } else {
                counter = 0;
            }
            oldScore = motifScore;


            // change minScore in new score if lower
            if(minScore == -1 || motifScore < minScore){
                minScore = motifScore;
                bestMotifs = motifs.clone();
                bestProfile = profile.clone();
            }
            if (counter >= 50){

                // print output
                System.out.println("Score: "+CalculateScore(bestMotifs, key));
                System.out.println("----------");
                System.out.println("Best Motif: ");
                for (int i = 0; i<bestMotifs.length; i++){
                    for (int j = 0; j<bestMotifs[i].length; j++){
                        System.out.print(bestMotifs[i][j] + " ");
                    }
                    System.out.println();
                }
                System.out.println("----------");
                System.out.println("Consensus String: "+FindConsensus(bestProfile, k));;
                break;

            }

        }



        // calculate execution time
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("----------");

        System.out.println("Execution Time: "+(timeElapsed)+" milliseconds");


    }

    // calculate score for last motif
    private static int CalculateScore(char[][] motifs, String key) {
        int sumScore = 0;
        for (int i = 0; i< motifs[0].length; i++){
            int numberOfA = 0;
            int numberOfC = 0;
            int numberOfG = 0;
            int numberOfT = 0;
            for (int j = 0; j<motifs.length; j++){
                switch (motifs[j][i]){
                    case 'A':
                        numberOfA++; // A counter
                        break;
                    case 'C':
                        numberOfC++; // C counter
                        break;
                    case 'G':
                        numberOfG++; // G counter
                        break;
                    case 'T':
                        numberOfT++; // T counter
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


    // generate motifs from DNA using profile.
    private static char[][] CreateMotifs(ArrayList dna, double[][] profile) {
        double[] maxProbs = new double[dna.size()];
        char[][] currentMotifs = new char[x][y];
        for (int i = 0; i<dna.size(); i++){
            for (int j = 0; j< dna.get(i).toString().length() - y + 1; j++){
                double currentProb = 1;
                for (int k = 0; k<y; k++){
                    switch (dna.get(i).toString().charAt(j+k)){
                        case 'A':
                            currentProb *= profile[0][k]; // A counter
                            break;
                        case 'C':
                            currentProb *= profile[1][k]; // C counter
                            break;
                        case 'G':
                            currentProb *= profile[2][k]; // G counter
                            break;
                        case 'T':
                            currentProb *= profile[3][k]; // T counter
                            break;
                        default:
                            break;

                    }
                }
                if (maxProbs[i]<currentProb){
                    maxProbs[i] = currentProb;
                    for (int k = 0; k<y; k++) {
                        currentMotifs[i][k] = dna.get(i).toString().charAt(j+k);
                    }
                }

            }
        }
        return currentMotifs;
    }

    // generates random motif to start
    private static char[][] CreateRandomMotifs(ArrayList dna) {
        char[][] currentMotifs = new char[x][y];
        Random rand = new Random();

        for (int i = 0; i<x; i++){
            int randomNum = rand.nextInt(500-y);
            for (int j = 0; j<y; j++){
                currentMotifs[i][j] = dna.get(i).toString().charAt(randomNum+j);
            }
        }

        return currentMotifs;
    }

    // creates a profile from a motif.
    private static double[][] CreateProfile(char[][] motifs) {
        double[][] currentProfile = new double[4][y];
        for (int i = 0; i<y; i++){
            for (int j = 0; j<x; j++){
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
        return currentProfile;
    }
}
