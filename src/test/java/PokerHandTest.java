import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PokerHandTest {

    @ParameterizedTest
    @CsvSource({
            "Black: 2H 3D 5S 9C KD  White: 2C 3H 4S 8C AH, White wins. - with high card: Ace",
            "Black: 2H 4S 4C 2D 4H  White: 2S 8S AS QS 3S, Black wins. - with full house: 4 over 2",
            "Black: 2H 3D 5S 9C KD  White: 2C 3H 4S 8C KH, Black wins. - with high card: 9",
            "Black: 2H 3D 5S 9C KD  White: 2D 3H 5C 9S KH, Tie."
    })
    void checkCompareHands(String input, String expected) {
        assertEquals(expected, PokerHand.compareHands(input));
    }
}