import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PokerHand {

    private static final String handInput =
            "^ *" + // optional leading spaces
            "([^ :]+):" + // name field
            " +([23456789TJQKA][HCDS])" + // a card
            " +([23456789TJQKA][HCDS])" + // a card
            " +([23456789TJQKA][HCDS])" + // a card
            " +([23456789TJQKA][HCDS])" + // a card
            " +([23456789TJQKA][HCDS])" + // a card
            " +" + // a space separator
            "([^ :]+):" + // name field
            " +([23456789TJQKA][HCDS])" + // a card
            " +([23456789TJQKA][HCDS])" + // a card
            " +([23456789TJQKA][HCDS])" + // a card
            " +([23456789TJQKA][HCDS])" + // a card
            " +([23456789TJQKA][HCDS])" + // a card
            " *$"; // optional trailing spaces

    private final String name;
    private final List<String> cards = new ArrayList<>();

    public PokerHand(String name, String card1, String card2, String card3, String card4, String card5) {
        this.name = name;
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        cards.add(card4);
        cards.add(card5);
    }

    public static String compareHands(String line){
        Pattern pattern = Pattern.compile(handInput);
        Matcher matcher = pattern.matcher(line);
        StringBuilder response = new StringBuilder("Bad input format");
        if (matcher.find()){
            PokerHand hand1 = new PokerHand(matcher.group(1),
                    matcher.group(2),
                    matcher.group(3),
                    matcher.group(4),
                    matcher.group(5),
                    matcher.group(6));
            PokerHand hand2 = new PokerHand(matcher.group(7),
                    matcher.group(8),
                    matcher.group(9),
                    matcher.group(10),
                    matcher.group(11),
                    matcher.group(12));
            response = new StringBuilder();
            response.append(hand1)
                    .append(" and ")
                    .append(hand2);
        }
        return response.toString();
    }

    @Override
    public String toString() {
        return "PokerHand{" +
                "name='" + name + '\'' +
                ", cards=" + cards +
                '}';
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
