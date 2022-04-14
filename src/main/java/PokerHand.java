import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PokerHand {



    public static String compareHands(String line){
        return line;
    }

    public static void main(String[] argv) throws IOException {
        if(argv.length > 0){
            String line = String.join(" ", argv);
            System.out.println(compareHands(line));
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Welcome to Poker Hand Comparator");
            System.out.println("Enter two hands in the following format:");
            System.out.println("   <name>: <card>*5 <name>: <card>*5");
            System.out.println("   where <card> is <value><suit>");
            String line = in.readLine();
            while (!line.isBlank()){
                System.out.println(compareHands(line));
                System.out.println("Enter another pair of hands or a blank line:");
                line =in.readLine();
            }
        }

    }
}
