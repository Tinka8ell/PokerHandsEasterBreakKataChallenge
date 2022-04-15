# ♥️ ♦️ ♠️ ♣️ Poker Hands - Easter break Kata Challenge

This chalenge is outlined [here](https://codingdojo.org/kata/PokerHands/).

## In summary:

Given two hands in the format:

```
<name1> ':' [ ' '+ <suite><value> ]*5 ' ' <name2> ':' [ ' '+ <suite><value> ]*5
```

Evaluate each hand and declare the "winner" or a "tie" with a description of the winning hand:

```
<name> " wins. - with " <hand description> ': ' <hand value>
```

or:
  
```
"Tie"
```

## Details:
  
* `<suite>` is one of 'H', 'C', 'D' or 'S' (♥️ ♣️ ♦️ ♠️)
* `<value>` is one of '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K' or 'A' in ascending order
* `<names>` are alpha, but we will suport non-blank and non-colon for convenience
* `<hand descriptions>` and `<hand value>` are (ranked lowest to highest):
  * "high card" - ranked by the value of their highest card and next until a difference found - if not then it is a "Tie"
  * "pair" - 2 cards have the same value, ranked by pair value and following cards as above
  * "two pairs" - ranked by the value of their highest pair, then second and finally the spare card as above
  * "three of a kind" - ranked by the value of the 3 cards, then the following cards as above
  * "straight" - 5 cards with consecutive values ranked by their highest card
  * "flush" - 5 cards of the same suit ranked using the rules for high card
  * "full house" - 3 cards of the same value, with 2 cards the same value ranked by the value of the 3 cards (can't be two hand with same 3 cards!)
  * "four of a kind" - 4 cards with the same value ranked by the value of the 4 cards (see above)
  * "straight flush" - 5 cards of the same suit with consecutive values ranked by the highest card
* card value in `<hand value>` will expand the non-numerics to: "Ten", "Jack", "Queen", "King" and "Ace" using plurals if describing multiple cards
* if more than one value in `<hand value>` will be seperated by ", " and finally " and ", except for "full house" which used " over "

  
## Implementation

* as well as a public static String compareHands(String) method, there will be a main method.  
* `public static void main(String[])` method takes parameters as above, or if none then prompts for them and prints the result until a blank line is entered.
* a "simple" regular expression witll be used to validate and break up the input:
  * `"^ *([^ :]+)( +([HCDS][23456789TJQKA])){5, 5} +([^ :]+)( +([HCDS][23456789TJQKA])){5, 5} *$"`
  * `private static Hand[] parseInput(String) throws InvalidInputException` will parse these
* the Hand class consists of:
  * `String name`
  * `Hand.Card[5] cards`
  * and methods to evaluate the hands as a `enum HandDescription` and `List<CardValue>` where CardValues are an enum
* Hand.Card will have methods to extract Suite enum and CardValue enum

## Testing  

* Start with the parseHand method
* Then the Hand and Hand.Card classes

## Design change

Although the regex eventually worked, it was cumbersome:  
``` Java
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
```
Also creating the "PokeHand" objects would be more useful 
if parameters were passed as an array of strings, so parsing is to be changed (refactored).

## What really annoys!

This may seem petty, but it really grates with me when a user brief holds back "hidden" knowledge.
The one bit missing from the spec is a full description of the format of the winning hand's description.
Would it be too much trouble to define exactly what was expected rather than leave incomplete test cases that
leave more questions than answers?

there does not seem to be any clear definition on the internet!  
Really struggling with how the winning hand should be described?
The third example: 
```
Black: 2H 3D 5S 9C KD  White: 2C 3H 4S 8C KH
```
giving: 
```
Black wins. - with high card: 9
```
just does not make sense as the high card is King, not 9, and then the decided is the 9 over the 8.  
So why is the King left out?  If this was because that was the significant card, fine, but why does the second example
```
Black wins. - with full house: 4 over 2 
```
as the full house beats a "high card Ace", whatever the cards involved, so why mention them! 
