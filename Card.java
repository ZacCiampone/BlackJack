public class Card {
    String name;
    String suit;
    int value;
    public Card(String name, String suit, int value) {
        this.name = name;
        this.suit = suit;
        this.value = value;
    }

    public String getName(){return this.name;}
    public String getSuit(){return this.suit;}
    public int getValue(){return this.value;}
    public void setValue(int val) {this.value = val;}
}
