import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Hand {
    int playerValue;
    int playerBet;
    static int playerBalance;
    int dealerValue;
    boolean isSplitHand;
    ArrayList<Card> playerCards = new ArrayList<>();
    ArrayList<Card> dealerCards = new ArrayList<>();
    static Stack<Card> theDeck;
    static Scanner scnr = new Scanner(System.in);

    public Hand(Card player1, Card player2, Card dealer1, Card dealer2, Stack<Card> inputDeck, int inputPlayerBet, int  inputPlayerBalance, boolean isSplit) {
        playerBalance = inputPlayerBalance;
        playerBet = inputPlayerBet;
        this.isSplitHand = isSplit;
        this.playerCards.add(player1);
        this.playerCards.add(player2);
        this.dealerCards.add(dealer1);
        this.dealerCards.add(dealer2);
        theDeck = inputDeck;
        this.playerValue = player1.getValue() + player2.getValue();
        this.dealerValue = dealer1.getValue() + dealer2.getValue();
        this.playerHand();
        System.out.println("-----------------");
        if (!this.isSplitHand) {
            this.dealerHand();
            this.decideWinner();
        }
    }

    public static void startGame() {
        System.out.println("Enter an amount to deposit: ");
        while(true) {
            try {
                playerBalance = Integer.parseInt(scnr.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a correct whole number deposit");
            }
        }
        System.out.println("===========");
    }

    public static int getBet() {
        int theBet;
        while (true) {
            System.out.println("Please enter your bet: ");
            try {
                theBet = Integer.parseInt(scnr.nextLine());
                if (theBet > playerBalance) {System.out.println("You bet more money than you currently have, please try again"); continue;}
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid bet, try again");
            }
        }
        return theBet;
    }

    public void playerHand() {
        boolean playing = true;
        boolean inputValid = false;
        System.out.println("The dealer's cards: ");
        System.out.println(this.dealerCards.get(0).getName() + ", []");
        while (playing) {
            this.playerValue = 0;
            for (Card card : playerCards) {
                playerValue += card.getValue();
            }
            playerChangeAces();
            if (playerValue > 21) {
                System.out.println("You busted!");
                break;
            }
            System.out.println("\nYour cards: ");
            for (Card eachCard : playerCards) {
                System.out.print(eachCard.getName() + ", ");
            }
            System.out.println("\nYour card value is: " + playerValue);
            String input = "";
            while (!inputValid) {
                input = scnr.nextLine();
                if (input.equals("split") && !playerCards.get(0).getName().equals(playerCards.get(1).getName())) {
                    System.out.println("You can't split cards that are not equal, please try again");
                    continue;
                }
                if (!input.equals("hit") && !input.equals("stand") && !input.equals("split") && !input.equals("double")) {
                    System.out.println("Incorrect input please try again");
                    continue;
                }
                if ((input.equals("double") && playerBalance - playerBet < 0) || (input.equals("double") && playerCards.size() > 2)) {
                    System.out.println("You may not double in this situation, try another input");
                    continue;
                }
                if (input.equals("split") && playerBalance < 2*playerBet) {
                    System.out.println("You do not have enough money to split");
                }
                inputValid = true;
            }
            inputValid = false;
            switch (input) {
                case "hit":
                    Card theCard = theDeck.peek();
                    if (theCard.getValue() == 11 && ((playerValue + 11) > 21)) {
                        playerValue += 1;
                    } else {
                        playerValue += theCard.getValue();
                    }
                    playerCards.add(theCard);
                    theDeck.pop();
                    System.out.println("Hit Card: " + theCard.getName());
                    break;
                case "stand":
                    playing = false;
                    break;
                case "split":
                    isSplitHand = true;
                    splitFunction();
                    playing = false;
                    break;
                case "double":
                    theCard = theDeck.peek();
                    if (theCard.getValue() == 11 && ((playerValue + 11) > 21)) {
                        playerValue += 1;
                    } else {
                        playerValue += theCard.getValue();
                    }
                    playerCards.add(theCard);
                    theDeck.pop();
                    playerBet = playerBet * 2;
                    playing = false;
                    System.out.println("\nYour cards: ");
                    for (Card eachCard : playerCards) {
                        System.out.print(eachCard.getName() + ", ");
                    }
                    System.out.println("\nYour card value is: " + playerValue);
                    break;
                default:
                    break;
                }
            }
        }

    public void dealerHand() {
        boolean dealerPlays = true;
        while(dealerPlays) {
            this.dealerValue = 0;
            for (Card card : dealerCards) {
                dealerValue += card.getValue();
            }
            dealerChangeAces();
            System.out.println("Dealer Cards: ");
            for (Card cards : dealerCards) {
                System.out.print(cards.getName() + " ");
            }
            System.out.println("\nDealer card value: " + dealerValue);
            if (dealerValue < 17) {
                Card theCard = theDeck.peek();
                dealerCards.add(theCard);
                if (theCard.getName().equals("Ace") && ((dealerValue + 11) > 21)) {
                    dealerValue += 1;
                } else {dealerValue += theCard.getValue();}
                theDeck.pop();
                continue;
            } else if (dealerValue <= 21) {
                System.out.println("Dealer final value: " + dealerValue);
                break;
            } else {
                System.out.println("Dealer busted");
            }
            dealerPlays = false;
        }
    }

    public void splitFunction() {
        Card card1 = playerCards.get(0);
        Card card2 = playerCards.get(1);
        System.out.println("Split hand #1: ");
        System.out.println("----------------");
        Hand hand1 = new Hand(card1,theDeck.pop(),dealerCards.get(0),dealerCards.get(1),theDeck,playerBet,playerBalance, true);
        System.out.println("Split hand #2: ");
        System.out.println("----------------");
        Hand hand2 = new Hand(card2,theDeck.pop(),dealerCards.get(0),dealerCards.get(1),theDeck,playerBet,playerBalance, true);
        this.dealerHand();
        System.out.println("Hand 1:");
        hand1.decideWinner();
        System.out.println("Hand 2:");
        hand2.decideWinner();
    }

    public void playerChangeAces() {
        // for each card, if an ace, revert to value = 1 until playerValue < 21
        for (Card theCard : playerCards) {
            if (theCard.getName().equals("Ace") && theCard.getValue() == 11 && playerValue > 21) {
                theCard.setValue(1);
                playerValue -= 10;
            }
        }
    }

    public void dealerChangeAces() {
        // for each card, if an ace, revert to value = 1 until playerValue < 21
        for (Card theCard : dealerCards) {
            if (theCard.getName().equals("Ace") && theCard.getValue() == 11 && dealerValue > 21) {
                theCard.setValue(1);
                dealerValue -= 10;
            }
        }
    }
    public void decideWinner() {
        playerBalance -= playerBet;
        if (dealerValue > playerValue && dealerValue <= 21) {
            System.out.println("Dealer wins.");
        } else if (dealerValue == playerValue && playerValue <= 21) {
            System.out.println("It's a push!");
            playerBalance += playerBet;
        } else if (playerValue > 21) {
            System.out.println("Player busts! Dealer wins.");
        } else {
            System.out.println("Player wins");
            playerBalance += playerBet*2;
        }
    }

    public static void main(String [] args) {
        startGame();
        theDeck = Shuffle.shuffleDeck(Shuffle.populateDeck());
        while (playerBalance > 0) {
            int bet = getBet();
            Hand theHand = new Hand(theDeck.pop(), theDeck.pop(), theDeck.pop(), theDeck.pop(),theDeck,bet,playerBalance,false);
            System.out.println("\n----------------------------");
            System.out.println("Player Balance is: " + playerBalance);
            System.out.println("----------------------------");
            theHand.playerCards.clear();
            theHand.playerValue = 0;
            theHand.dealerCards.clear();
            theHand.dealerValue = 0;
        }
    }

}
