package b_Money;

import static org.junit.Assert.*;

import b_Money.Currency;
import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, EUR;
	
	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		assertEquals("1. Should be: SEK", "SEK", SEK.getName());
		assertEquals("2. Should be: DKK", "DKK", DKK.getName());
		assertEquals("3. Should be: EUR", "EUR", EUR.getName());
	}
	
	@Test
	public void testGetRate() {
		assertEquals("1. Should be: 0.15", 0.15, SEK.getRate(), 0.0001);
		assertEquals("2. Should be: 0.2", 0.20, DKK.getRate(), 0.0001);
		assertEquals("3. Should be: 1.5", 1.5, EUR.getRate(), 0.0001);
	}
	
	@Test
	public void testSetRate() {
		SEK.setRate(0.33);
		DKK.setRate(0.74);
		EUR.setRate(0.91);

		assertEquals("1. Should be: 0.33", 0.33, SEK.getRate(), 0.0001);
		assertEquals("2. Should be: 0.74", 0.74, DKK.getRate(), 0.0001);
		assertEquals("3. Should be: 0.91", 0.91, EUR.getRate(), 0.0001);
	}
	
	@Test
	public void testGlobalValue() {
		assertEquals("1. Should be: 52", (Object)52, SEK.universalValue(350));
		assertEquals("2. Should be: 4", (Object)4, DKK.universalValue(20));
		assertEquals("3. Should be: 1500", (Object)1500, EUR.universalValue(1000));
	}
	
	@Test
	public void testValueInThisCurrency() {
		assertEquals("1. Should be: 466", (Object)466, SEK.valueInThisCurrency(350, DKK));
		assertEquals("2. Should be: 150", (Object)150, DKK.valueInThisCurrency(20, EUR));
		assertEquals("3. Should be: 100", (Object)100, EUR.valueInThisCurrency(1000, SEK));
	}

}
