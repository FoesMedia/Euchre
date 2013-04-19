import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Random;

public class Round {
	public LinkedList<Card> deck = new LinkedList<Card>();
	public ArrayList<Card> allCards;
	public ArrayList<Player> players;
	public Trick currentTrick;
	public Card.SUIT trump;
	public int[] trickCount = new int[2];
	public ArrayList<Trick> trickHistory = new ArrayList<Trick>();
	public int dealer;
	public int callingTeam;
	public boolean isInPreGameState = true;
	public boolean isCardTurnedUp;
	public boolean isStickTheDealer = false;
	public Card turnedUpCard;
	public Card.SUIT[] callableSuits = new Card.SUIT[3];
	
	public Round(ArrayList<Card> allCardsIn, ArrayList<Player> playersIn, int dealerIn){
		allCards = allCardsIn;
		players = playersIn;
		shuffle();
		deal();
		turnedUpCard = deck.pop();
		isCardTurnedUp = true;
		fillCallableSuits();
		
		dealer = dealerIn;
		currentTrick = new Trick(dealer + 1,trump);
		trickCount[0] = 0;
		trickCount[1] = 0;
		
		callingTeam = dealer % 2; // to be figured out in a pre-round
	}
	
	// fills array of suits that aren't the trump of the turned up card
	private void fillCallableSuits()
	{
		int index = 0; 
		if (!(turnedUpCard.suit==Card.SUIT.CLUBS))
		{
			callableSuits[index] = Card.SUIT.CLUBS;
			index++;
		}
		if (!(turnedUpCard.suit==Card.SUIT.DIAMONDS))
		{
			callableSuits[index] = Card.SUIT.DIAMONDS;
			index++;
		}
		if (!(turnedUpCard.suit==Card.SUIT.HEARTS))
		{
			callableSuits[index] = Card.SUIT.HEARTS;
			index++;
		}
		if (!(turnedUpCard.suit==Card.SUIT.SPADES))
		{
			callableSuits[index] = Card.SUIT.SPADES;
		}
	}
	
	public void preRoundPass()
	{
		if (currentTrick.currentPlayer == dealer)
		{
			// currently, just default to spades
			trump = Card.SUIT.SPADES;
			prepareRoundForStart();
			
			// to be used later
//			if (isCardTurnedUp)
//			{
//				isCardTurnedUp = false;
//			}
//			else // stick the dealer
//			{
//				isStickTheDealer = true;
//			}
		}
		else
		{
			currentTrick.incrementTurn();
		}
	}
	
//	// for when the player calls
//	public void preRoundCall()
//	{
//		// get card to discard from AI if dealer 
//		Card c = new Card(Card.SUIT.SPADES, 9);
//		
//		preRoundCall(c);
//	}
//	To be implemented with more complicated pre-rounds
//	// for when a card is turned up
//	public void preRoundCall(Card c)
//	{
//		trump = turnedUpCard.suit;
//		players.get(dealer).removeCardFromHand(c);
//		players.get(dealer).hand.add(turnedUpCard);
//		prepareRoundForStart();
//	}
	
	// for when a card isn't turned up
	
	public void preRoundCall()
	{
		trump = turnedUpCard.suit;
		prepareRoundForStart();
	}
	
	
	private void prepareRoundForStart()
	{
		callingTeam = currentTrick.currentPlayer % 2;
		isInPreGameState = false;
		currentTrick.currentPlayer = currentTrick.leadingPlayer;
		currentTrick.trump = trump;
	}
	
	
	public boolean isOver(){
		return trickHistory.size() == 5;
	}
	
	/*
	 * team that called trump get 3 or 4 tricks, get 1 point
	 * team that called trump get all 5, get 2 points
	 * team that didn't call get 3 or more, get 2 points
	 */
	public int[] scoreRound(){
		int[] ans = new int[2];
		if(trickCount[callingTeam]+trickCount[(callingTeam+1)%2]<5){
			ans[0]=0;
			ans[1]=0;
			return ans;
		}
		if(trickCount[callingTeam] < 3)
			ans[(callingTeam+1)%2] = 2;
		else if(trickCount[callingTeam] < 5 )
			ans[callingTeam] = 1;
		else //trickCount[dealer] == 5)
			ans[callingTeam] = 2;
		
		return ans;
	}
	
	/*Shuffles the deck, refilling it with all 24 cards in a random order*/
	public void shuffle(){
		Random r = new Random();
		boolean[] used = new boolean[24];
		for (int x=0; x<24; x++){
			used[x] = false;
		}
		for (int x=0; x<24; x++){
			int y = r.nextInt(24);
			while (used[y]){
				y = r.nextInt(24);
			}
			deck.push(allCards.get(y));
			used[y] = true;
		}
	}
	
	/*Removes the card off the top of deck and returns it*/
	public Card draw(){
		return deck.pop();
	}
	
	/*Deals 5 cards off the top of the deck to each player*/
	public void deal(){
		for (int x=0; x<5; x++){
			for(int y=0; y<4; y++){
				players.get(y).hand.add(draw());
			}
		}
	}
	
	
	public void playCard(Card c)
	{
		currentTrick.playCardForCurrentPlayer(c);
		if (currentTrick.isOver())
		{
			trickCount[currentTrick.currentWinner % 2] ++;
			trickHistory.add(currentTrick);
			currentTrick = currentTrick.getNextTrick();
		}
	}

	public Round getNextRound() {
		return new Round(allCards, players, (dealer+1) % 4);
	}


	String produceTrickHistoryText() {
		String text = "";
		if(!trickHistory.isEmpty()){
			text += "Previous Trick played:\n";
			text += trickHistory.get(trickHistory.size()-1).stringOfTrickPlayed();
		}
		text += "\nCurrent Trick:\n";
		text += currentTrick.stringOfTrickPlayed();
		return text;
	}
}