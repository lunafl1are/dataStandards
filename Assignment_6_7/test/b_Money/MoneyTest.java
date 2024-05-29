package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		assertEquals("1. Should be: 52", (Object)52, SEK.universalValue(350));
		assertEquals("2. Should be: 4", (Object)4, DKK.universalValue(20));
		assertEquals("3. Should be: 1500", (Object)1500, EUR.universalValue(1000));
	}

	@Test
	public void testGetCurrency() {
		assertSame(EUR, EUR20.getCurrency());
		assertSame(SEK, SEK0.getCurrency());
		assertSame(EUR, EUR0.getCurrency());
		assertSame(SEK, SEKn100.getCurrency());
	}

	@Test
	public void testToString() {
		EUR20 = EUR20.sub(new Money(667, EUR));

		assertEquals("1. Should be: 100.0 SEK", "100.0 SEK", SEK100.toString());
		assertEquals("2. Should be: 13.33 EUR", "13.33 EUR", EUR20.toString());
		assertEquals("3. Should be: -100.0 SEK", "-100.0 SEK", SEKn100.toString());
	}

	@Test
	public void testGlobalValue() {
		EUR0 = EUR0.add(new Money(667, EUR));

		assertEquals("1. Should be: 999", (Object)999, EUR0.universalValue());
		assertEquals("2. Should be: 3000", (Object)3000, EUR20.universalValue());
		assertEquals("3. Should be: 1500", (Object)1500, SEK100.universalValue());
	}

	@Test
	public void testEqualsMoney() {
		String tmsg = "Should be the same";
		String fmsg = "Should not be the same";

		assertTrue(tmsg, EUR20.equals(EUR20));
		assertTrue(tmsg, SEK0.equals(SEK0));
		assertTrue(tmsg, SEKn100.equals(SEKn100));

		assertFalse(fmsg,  SEK100.equals(SEK0));
		assertFalse(fmsg,  EUR10.equals(EUR20));
		assertFalse(fmsg, SEK200.equals(SEKn100));
		assertFalse(fmsg, EUR0.equals(SEK100));
	}

	@Test
	public void testAdd() {
		SEK100 = SEK100.add(new Money(123, EUR));
		EUR10 = EUR10.add(new Money(953, DKK));
		SEK200 = SEK200.add(new Money(883, SEK));
		EUR20 = EUR20.add(new Money(100, SEK));
		SEK0 = SEK0.add(new Money(-23, EUR));
		EUR0 = EUR0.add(new Money(-90, DKK));
		SEKn100 = SEKn100.add(new Money(-95, DKK));

		assertEquals("1. Should be: 1683", (Object)1683,  SEK100.universalValue());
		assertEquals("2. Should be: 1689", (Object)1689, EUR10.universalValue());
		assertEquals("3. Should be: 3132", (Object)3132, SEK200.universalValue());
		assertEquals("4. Should be: 3015", (Object)3015, EUR20.universalValue());
		assertEquals("5. Should be: -33", (Object)(-33), SEK0.universalValue());
		assertEquals("6. Should be: -18", (Object)(-18), EUR0.universalValue());
		assertEquals("7. Should be: -1518", (Object)(-1518), SEKn100.universalValue());
	}

	@Test
	public void testSub() {
		SEK100 = SEK100.sub(new Money(123, EUR));
		EUR10 = EUR10.sub(new Money(953, DKK));
		SEK200 = SEK200.sub(new Money(883, SEK));
		EUR20 = EUR20.sub(new Money(100, SEK));
		SEK0 = SEK0.sub(new Money(-23, EUR));
		EUR0 = EUR0.sub(new Money(-90, DKK));
		SEKn100 = SEKn100.sub(new Money(-95, DKK));

		assertEquals("1. Should be: 1315", (Object)1315, SEK100.universalValue());
		assertEquals("2. Should be: 1309", (Object)1309, EUR10.universalValue());
		assertEquals("3. Should be: 2868", (Object)2868, SEK200.universalValue());
		assertEquals("4. Should be: 2985", (Object)2985, EUR20.universalValue());
		assertEquals("5. Should be: 33", (Object)(33), SEK0.universalValue());
		assertEquals("6. Should be: 18", (Object)(18), EUR0.universalValue());
		assertEquals("7. Should be: -1480", (Object)(-1480), SEKn100.universalValue());
	}

	@Test
	public void testIsZero() {
		String fmsg = "Should not be zero";
		String tmsg = "Should be zero";

		assertFalse(fmsg, SEK100.isZero());
		assertFalse(fmsg, EUR10.isZero());
		assertFalse(fmsg, SEK200.isZero());
		assertFalse(fmsg, EUR20.isZero());
		assertTrue(tmsg, SEK0.isZero());
		assertTrue(tmsg, EUR0.isZero());
		assertFalse(fmsg, SEKn100.isZero());
	}

	@Test
	public void testNegate() {
		EUR10 = EUR10.negate();
		SEK200 = SEK200.negate();
		EUR20 = EUR20.negate();

		assertEquals("1. Should be: -1500", (Object)(-1500), EUR10.universalValue());
		assertEquals("2. Should be: -3000", (Object)(-3000), SEK200.universalValue());
		assertEquals("3. Should be: -3000", (Object)(-3000), EUR20.universalValue());
	}

	@Test
	public void testCompareTo() {
		assertEquals("1. Should be: 0", (Object)(0), SEK100.compareTo(SEK100));
		assertEquals("2. Should be: -1", (Object)(-1), EUR10.compareTo(SEK200));
		assertEquals("3. Should be: 0", (Object)(0), SEK200.compareTo(EUR20));
		assertEquals("4. Should be: 0", (Object)(0), EUR20.compareTo(EUR20));
		assertEquals("5. Should be: 0", (Object)(0), SEK0.compareTo(SEK0));
		assertEquals("6. Should be: 1", (Object)(1), EUR0.compareTo(SEKn100));
		assertEquals("7. Should be: -1", (Object)(-1), SEKn100.compareTo(EUR0));
	}
}
