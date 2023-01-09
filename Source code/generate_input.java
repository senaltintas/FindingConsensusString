import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class generate_input {
    public static void run() {
        try {
            FileWriter myWriter = new FileWriter("input.txt");
            myWriter.write("key: GTAACGCTCC\n");
            myWriter.write("\ninputs:\n");
            for(int i=0;i<10;i++){
                myWriter.write(". "+extracted()+"\n");
            }
            System.out.println("\n'input.txt' has been changed.");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String extracted() {
        Character[] options = {'A','T','C','G'};
        List<Character> charList = Arrays.asList(options);

        StringBuilder key = new StringBuilder("GTAACGCTCC");

        //It takes only 4 out of 10 random locations in the key.
        ArrayList<Integer> random_loc_list = new ArrayList<>();
        for (int i=0; i<10; i++) random_loc_list.add(i);
        Collections.shuffle(random_loc_list);

        for(int i=0;i<4;i++){
            key.setCharAt(random_loc_list.get(i),(charList.get((int) (Math.random()*4))));
        }

        //490 nucleotide of DNA
        char[] result = new char[490];
        String last_result="";
        Random r=new Random();

        for(int i=0;i<result.length;i++){
            result[i]=options[r.nextInt(options.length)];
        }

        String result_str = String.valueOf(result);
        int random_result_loc = (int) (Math.random()*490); // Generates random between 0 and 490.
        last_result = result_str.substring(0,random_result_loc) + key + result_str.substring(random_result_loc);
        return last_result;

    }


}
