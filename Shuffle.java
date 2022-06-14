import java.util.ArrayList;
import java.util.Stack;

public class Shuffle {
    public static ArrayList<Card> populateDeck() {
        ArrayList<Card> deck = new ArrayList<Card>();
        String [] suits = {"Hearts", "Diamonds","Clubs","Spades"};
        for (int i = 0; i < 3; i++) {
            for (String suit : suits) {
                deck.add(new Card("Ace", suit, 11));
                deck.add(new Card("Two", suit, 2));
                deck.add(new Card("Three", suit, 3));
                deck.add(new Card("Four", suit, 4));
                deck.add(new Card("Five", suit, 5));
                deck.add(new Card("Six", suit, 6));
                deck.add(new Card("Seven", suit, 7));
                deck.add(new Card("Eight", suit, 8));
                deck.add(new Card("Nine", suit, 9));
                deck.add(new Card("Ten", suit, 10));
                deck.add(new Card("Jack", suit, 10));
                deck.add(new Card("Queen", suit, 10));
                deck.add(new Card("King", suit, 10));
            }
        }
        return deck;
    }

    public static Stack<Card> shuffleDeck(ArrayList<Card> deck) {
        Stack<Card> nextCards = new Stack<Card>();
        for (int i = 0; i < 154; i++) {
            int max = 155-i;
            int min = 0;
            int num = ((int)(Math.random() * (max - min) + 1) + min);
            nextCards.add(deck.get(num));
            deck.remove(num);
        }
        return nextCards;
    }

    public static void main(String args[]) {
        ArrayList<Card> deck = populateDeck();
        Stack<Card> nextCards = shuffleDeck(deck);
        for (Card theCard : nextCards) {
            System.out.println(theCard.getName() + " of " + theCard.getSuit());
        }
    }
}
