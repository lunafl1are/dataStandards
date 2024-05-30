package b_Money;

import static org.junit.Assert.*;

import b_Money.AccountDoesNotExistException;
import b_Money.AccountExistsException;
import b_Money.Bank;
import b_Money.Currency;
import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	@Test
	public void testGetName() {
		assertEquals("1. Should be: SweBank", "SweBank", SweBank.getName());
		assertEquals("2. Should be: Nordea", "Nordea", Nordea.getName());
		assertEquals("3. Should be: DanskeBank", "DanskeBank", DanskeBank.getName());
	}

	@Test
	public void testGetCurrency() {
		assertEquals("1. Should be: SEK", SEK, SweBank.getCurrency());
		assertEquals("2. Should be: SEK", SEK, Nordea.getCurrency());
		assertEquals("3. Should be: DKK", DKK, DanskeBank.getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		try {
			SweBank.openAccount("Ulrika");
			System.out.println("Account should not be opened");
			throw new AccountExistsException();
		}
		catch (AccountExistsException e) {
		}


		try {
			SweBank.openAccount("Ulrika 1");
		}
		catch (AccountExistsException e) {
			System.out.println("Account should be created");
			throw new AccountDoesNotExistException();
		}
		assertEquals( "Account amount should be zero", (Object)0, SweBank.getBalance("Ulrika 1").getAmount());
	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException {
		assertEquals("Account amount should be zero", (Object)0, SweBank.getBalance("Ulrika").getAmount());

		SweBank.deposit("Ulrika", new Money(1000, SEK));
		assertEquals("Account amount should be: 10.0 SEK", "10.0 SEK", SweBank.getBalance("Ulrika").toString());
	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		assertEquals("Account amount should be zero", (Object)0, SweBank.getBalance("Ulrika").getAmount());

		SweBank.withdraw("Ulrika", new Money(10, SEK));
		assertEquals("Account amount should be: -0.1 SEK", "-0.1 SEK", SweBank.getBalance("Ulrika").toString());
	}
	
	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		assertTrue("Account amount should be zero", SweBank.getBalance("Ulrika").equals(new Money(0, SEK)));

		SweBank.withdraw("Ulrika", new Money(10, SEK));
		assertTrue("Account amount should be: -0.1 SEK", SweBank.getBalance("Ulrika").equals(new Money(-10, SEK)));

		SweBank.deposit("Ulrika", new Money(1000, SEK));
		assertTrue("Account amount should be: 9.90 SEK", SweBank.getBalance("Ulrika").equals(new Money(990, SEK)));
	}
	
	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		SweBank.transfer("Ulrika", "Bob", new Money(100, SEK));
		assertTrue("Account amount should be: -1.0 SEK", SweBank.getBalance("Ulrika").equals(new Money(-100, SEK)));
		assertTrue("Account amount should be: 1.0 SEK", SweBank.getBalance("Bob").equals(new Money(100, SEK)));

		SweBank.transfer("Ulrika", Nordea, "Bob", new Money(100, SEK));
		assertTrue("Account amount should be: -2.0 SEK", SweBank.getBalance("Ulrika").equals(new Money(-200, SEK)));
		assertTrue("Account amount should be: 1.0 SEK", Nordea.getBalance("Bob").equals(new Money(100, SEK)));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		var paymentId = "1";

		SweBank.addTimedPayment("Ulrika", paymentId, 2, 2, new Money(500, SEK), SweBank, "Bob");
		// Emulate payment tick
		SweBank.tick();
		SweBank.tick();

		assertEquals("-5.0 SEK", SweBank.getBalance("Ulrika").toString());
		assertEquals("5.0 SEK", SweBank.getBalance("Bob").toString());

		SweBank.removeTimedPayment("Ulrika", paymentId);
		// Emulate payment tick
		SweBank.tick();
		SweBank.tick();

		assertEquals("-5.0 SEK", SweBank.getBalance("Ulrika").toString());
		assertEquals("5.0 SEK", SweBank.getBalance("Bob").toString());
	}
}
