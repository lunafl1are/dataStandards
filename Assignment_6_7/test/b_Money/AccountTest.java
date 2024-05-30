package b_Money;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	
	@Test
	public void testAddRemoveTimedPayment() {
		var paymentId = "1";

		testAccount.addTimedPayment(paymentId, 5, 5, new Money(50, SEK), SweBank, "Alice");
		assertTrue("1. Payment should exist", testAccount.timedPaymentExists(paymentId));

		testAccount.removeTimedPayment(paymentId);
		assertFalse("2. Payment should not exist", testAccount.timedPaymentExists(paymentId));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		var paymentId = "1";

		testAccount.addTimedPayment(paymentId, 5, 2, new Money(500, SEK), SweBank, "Alice");
		assertTrue("1. Payment should exist", testAccount.timedPaymentExists(paymentId));

		// Emulate tick to perform timed payment
		testAccount.tick();
		testAccount.tick();

		assertEquals("99995.0 SEK", testAccount.getBalance().toString());
		assertEquals("10005.0 SEK", SweBank.getBalance("Alice").toString());
	}

	@Test
	public void testAddWithdraw() {
		testAccount.deposit(new Money(100, SEK));
		assertEquals("100001.0 SEK", testAccount.getBalance().toString());

		testAccount.withdraw(new Money(997, SEK));
		assertEquals("99991.06 SEK", testAccount.getBalance().toString());
	}
	
	@Test
	public void testGetBalance() {
		// Emulate money depositing
		testAccount.deposit(new Money(100, SEK));
		assertTrue(testAccount.getBalance().equals(new Money(10000100, SEK)));
		System.out.println(testAccount.getBalance().getAmount());

		// Emulate account withdraw
		testAccount.withdraw(new Money(997, SEK));
		System.out.println(testAccount.getBalance().getAmount());
		assertTrue(testAccount.getBalance().equals(new Money(9999103, SEK)));
	}
}
