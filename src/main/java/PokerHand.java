import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PokerHand {

    @SuppressWarnings("SpellCheckingInspection")
    public static final String CARD_VALUES = "23456789TJQKA";
    @SuppressWarnings("SpellCheckingInspection")
    public static final String SUIT_VALUES = "HCDS";
    public static final String[] CARD_NAMES = new String[]{
            "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"
    };
    public static final String[] handDescriptions = new String[]{
            "high card",
            "pair",
            "two pairs",
            "three of a kind",
            "straight",
            "flush",
            "full house",
            "four of a kind",
            "straight flush"
    };


    private final String name;
    private int handLevel = -1;
    private List<String> handValue = new ArrayList<>();
    private String handMainValue = "";
    private List<Integer> handScore = new ArrayList<>();

    /**
     * Create a "poker Hand" from 6 sequential strings from the array
     * starting at the given index.
     *
     * This code is very long!  It should be broekn up, but it works!
     *
     * @param parts of the original line
     * @param index from where to start processing
     * @throws Exception if there is a format error
     */
    public PokerHand(String[] parts, int index) throws Exception {
        // check name is "<name>:"
        int pos = parts[index].indexOf(':');
        if (pos != parts[index].length() - 1)
            throw new Exception("name must end in a ':'");
        name = parts[index].substring(0, pos);
        // check the 5 cards
        int[] suits = new int[SUIT_VALUES.length()];
        int[] values = new int[CARD_VALUES.length()];
        for (int i = 1; i <= 5; i++) {
            String card = parts[index + i].toUpperCase();
            if (card.length() != 2)
                throw new Exception("card must two characters long");
            int value = CARD_VALUES.indexOf(card.charAt(0));
            if (value < 0)
                throw new Exception("first character of card must be a valid value character");
            values[value]++;
            int suit = SUIT_VALUES.indexOf(card.charAt(1));
            if (suit < 0)
                throw new Exception("second character of card must be a valid suit character");
            suits[suit] ++;
        }
        // check for flush
        boolean flush = false;
        for (int suit: suits) {
            if (suit == 5) {
                flush = true;
                break;
            }
        }
        // check for pairs, triples and fours
        int pairs = 0;
        boolean triple = false;
        boolean four = false;
        boolean house = false;
        int start = values.length;
        int last = 0;
        for (int i = 0; i < values.length; i++) {
            int count = values[i];
            switch (count){
                case 1 -> { // check for sequence
                    if (i < start)
                        last = start = i;
                    if (last + 1 == i)
                        last = i;
                }
                case 2 -> { // count pairs
                    pairs++;
                    values[i] = 0;
                    if (triple){
                        handMainValue = "%s over %s".formatted(handMainValue, CARD_NAMES[i]);
                        handScore.add(1, i);
                        house = true;
                    }
                    else {
                        handMainValue = CARD_NAMES[i];
                        handScore.add(0, i);
                    }
                }
                case 3 -> { // count triples
                    triple = true;
                    values[i] = 0;
                    if (pairs > 0){
                        handMainValue = "%s over %s".formatted(CARD_NAMES[i], handMainValue);
                        house = true;
                    }
                    else
                        handMainValue =  CARD_NAMES[i];
                    handScore.add(0, i);
                }
                case 4 -> { // count fours
                    four = true;
                    values[i] = 0;
                    handMainValue = CARD_NAMES[i];
                    handScore.add(0, i);
                }
            }
        }
        for (int i = values.length - 1; i >= 0 ; i--) {
            if (values[i] > 0) {
                handValue.add(CARD_NAMES[i]);
                handScore.add(i);
            }
        }
        // check for sequence
        boolean sequence = (last == start + 4);
        if (sequence){
            handValue = new ArrayList<>();
            handScore = new ArrayList<>();
            handMainValue = CARD_NAMES[last];
        }
        if (handMainValue.isEmpty())
            handMainValue = handValue.remove(0);
        // evaluate hand, testing for highest first
        int level = handDescriptions.length;
        while (handLevel < 0) {
            level--;
            switch (level){
                case 0 -> // lastly high card:
                        handLevel = level;
                case 1 -> { // pair:
                    if (pairs > 0)
                        handLevel = level;
                }
                case 2 -> { // two pairs:
                    if (pairs > 1)
                        handLevel = level;
                }
                case 3 -> { // three of a kind:
                    if (triple)
                        handLevel = level;
                }
                case 4 -> { // straight:
                    if (sequence)
                        handLevel = level;
                }
                case 5 -> { // flush:
                    if (flush)
                        handLevel = level;
                }
                case 6 -> { // full house:
                    if (house)
                        handLevel = level;
                }
                case 7 -> { // four of a kind:
                    if (four)
                        handLevel = level;
                }
                case 8 -> { // straight flush:
                    if (sequence && flush)
                        handLevel = level;
                }
            }
        }
    }

    /**
     * Compare two hands described by the input line
     *
     * @param line representing teo players and they 5 cards respectively
     * @return description of the winning hand, a tie or error message if input is invalid
     */
    public static String compareHands(String line){
        StringBuilder response = new StringBuilder("Bad input format");
        String[] parts = line.trim().split(" +"); // split on any single or multiple whitespace
        if (parts.length == 12){ // 2 hands of (1 name and 5 cards)
            try {
                PokerHand hand1 = new PokerHand(parts, 0); // start at 0 for the first hand
                PokerHand hand2 = new PokerHand(parts, 6); // start at 6 for the first hand
                response = new StringBuilder();
                PokerHand winner = null;
                if (hand1.handLevel > hand2.handLevel){
                    winner = hand1;
                } else if (hand1.handLevel < hand2.handLevel){
                    winner = hand2;
                }
                if (winner != null){
                    response.append(winner.name)
                            .append(" wins. - with ")
                            .append(handDescriptions[winner.handLevel])
                            .append(": ")
                            .append(winner.handMainValue);
                } else { // a tie
                    int maxDiff = 0;
                    for (int i = 0; i < hand1.handScore.size(); i ++) {
                        if (hand1.handScore.get(i) > hand2.handScore.get(i)){
                            winner = hand1;
                            maxDiff = i;
                            break;
                        } else if (hand1.handScore.get(i) < hand2.handScore.get(i)){
                            winner = hand2;
                            maxDiff = i;
                            break;
                        }
                    }
                    if (winner != null){
                        response.append(winner.name)
                                .append(" wins. - with ")
                                .append(handDescriptions[winner.handLevel])
                                .append(": ")
                                .append(winner.handMainValue);
                        for (int i = 0; i < maxDiff; i++) {
                            response.append(" ")
                                    .append(winner.handValue.get(i));
                        }
                    } else { // a real tie
                        response.append("Tie.");
                    }
                }
            } catch (Exception e) {
                response.append(": ")
                        .append(e.getMessage());
            }
        }
        return response.toString();
    }

    /**
     * Accept various poker hands for two players, and return the description of the winner
     * hands are either taken from the command line, or in response to prompts to the screen.
     *
     * @param argv from command line
     * @throws IOException is unlikely as stdin should not fail!
     */
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
