import static org.junit.Assert.*;

import org.junit.Test;

public class CardTest {

	@Test
	public void testCard() {
		Card c = new Card(Card.SUIT.CLUBS, 9);
		assertNotNull(c);
	}

	@Test
	public void testEqualsObject() {
		Card c1 = new Card(Card.SUIT.CLUBS, 9);
		Card c2 = new Card(Card.SUIT.CLUBS, 9);
		assertEquals(c1,c2);
		Card c3 = new Card(Card.SUIT.HEARTS, 11);
		Card c4 = new Card(Card.SUIT.HEARTS, 11);
		assertEquals(c3,c4);
		Card c5 = new Card(Card.SUIT.SPADES, 13);
		Card c6 = new Card(Card.SUIT.SPADES, 13);
		assertEquals(c5,c6);
		Card c7 = new Card(Card.SUIT.DIAMONDS, 14);
		Card c8 = new Card(Card.SUIT.DIAMONDS, 14);
		assertEquals(c7,c8);
	}
	
	@Test
	public void testNotEqualsObjectSameNumber() {
		Card c1 = new Card(Card.SUIT.CLUBS, 9);
		Card c2 = new Card(Card.SUIT.HEARTS, 9);
		assertFalse(c1.equals(c2));
	}
	
	@Test
	public void testNotEqualsObjectSameSuit() {
		Card c1 = new Card(Card.SUIT.DIAMONDS, 10);
		Card c2 = new Card(Card.SUIT.DIAMONDS, 9);
		assertFalse(c1.equals(c2));
	}
	
	@Test
	public void testNotEqualsObjectDifferentEverything() {
		Card c1 = new Card(Card.SUIT.SPADES, 13);
		Card c2 = new Card(Card.SUIT.CLUBS, 9);
		assertFalse(c1.equals(c2));
	}
	
	@Test
	public void testCardValueNonTrump(){
		Card c = new Card(Card.SUIT.SPADES, 12);
		assertEquals(c.cardValue(Card.SUIT.HEARTS),12);
	}

	@Test
	public void testCardValueTrumpNonJack(){
		Card c = new Card(Card.SUIT.SPADES, 12);
		assertEquals(c.cardValue(Card.SUIT.SPADES),26);
	}
	
	@Test
	public void testCardValueTrumpJack(){
		Card c = new Card(Card.SUIT.SPADES, 11);
		assertEquals(c.cardValue(Card.SUIT.SPADES),30);
	}
	
	@Test
	public void testCardValueSameColorJack(){
		Card c = new Card(Card.SUIT.CLUBS, 11);
		assertEquals(c.cardValue(Card.SUIT.SPADES),29);
	}
	
	@Test
	public void testGreaterNoTrump(){
		Card c1 = new Card(Card.SUIT.SPADES, 12);
		Card c2 = new Card(Card.SUIT.SPADES, 10);
		assert(c1.greater(c2, Card.SUIT.HEARTS));
	}
	
	@Test
	public void testGreaterTrump(){
		Card c1 = new Card(Card.SUIT.SPADES, 12);
		Card c2 = new Card(Card.SUIT.SPADES, 10);
		assert(c1.greater(c2, Card.SUIT.SPADES));
	}
	
	@Test
	public void testGreaterTrumpAndNoTrump(){
		Card c1 = new Card(Card.SUIT.SPADES, 10);
		Card c2 = new Card(Card.SUIT.HEARTS, 12);
		assert(c1.greater(c2, Card.SUIT.SPADES));
	}
}
