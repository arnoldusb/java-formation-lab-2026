package com.indra.jpa.transactions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.indra.jpa.transactions.exception.InsufficientFundsException;
import com.indra.jpa.transactions.model.Account;
import com.indra.jpa.transactions.model.TransferEvent;
import com.indra.jpa.transactions.repository.AccountRepository;
import com.indra.jpa.transactions.repository.TransferEventRepository;
import com.indra.jpa.transactions.service.TransferService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
		properties = "spring.jpa.properties.hibernate.hbm2ddl.halt_on_error=true")
class TransactionsApplicationTests {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransferEventRepository transferEventRepository;

	@Autowired
	private TransferService transferService;

	private Account source;
	private Account destination;

	@BeforeEach
	void setUp() {
		transferEventRepository.deleteAll();
		accountRepository.deleteAll();
		source = createAccount("Alice", "100.00");
		destination = createAccount("Bob", "50.00");
	}

	@Test
	void insufficientFundsRollsBackBothBalances() {
		assertThatThrownBy(() -> transferService.transfer(
				source.getId(), destination.getId(), new BigDecimal("150.00")))
				.isInstanceOf(InsufficientFundsException.class)
				.hasMessageContaining(source.getId().toString());

		assertBalance(source.getId(), "100.00");
		assertBalance(destination.getId(), "50.00");
	}

	@Test
	void auditSurvivesTransferRollback() {
		assertThatThrownBy(() -> transferService.transfer(
				source.getId(), destination.getId(), new BigDecimal("150.00")))
				.isInstanceOf(InsufficientFundsException.class);

		assertBalance(source.getId(), "100.00");
		assertBalance(destination.getId(), "50.00");
		assertThat(transferEventRepository.findAll())
				.singleElement()
				.satisfies(event -> assertTransferEvent(event, "150.00"));
	}

	@Test
	void successfulTransferUpdatesBothBalancesAndPersistsAudit() throws InsufficientFundsException {
		transferService.transfer(source.getId(), destination.getId(), new BigDecimal("25.00"));

		assertBalance(source.getId(), "75.00");
		assertBalance(destination.getId(), "75.00");
		assertThat(transferEventRepository.findAll())
				.singleElement()
				.satisfies(event -> assertTransferEvent(event, "25.00"));
	}

	private Account createAccount(String owner, String balance) {
		Account account = new Account();
		account.setOwner(owner);
		account.setBalance(new BigDecimal(balance));
		return accountRepository.saveAndFlush(account);
	}

	private void assertBalance(Long accountId, String expectedBalance) {
		assertThat(accountRepository.findById(accountId).orElseThrow().getBalance())
				.isEqualByComparingTo(expectedBalance);
	}

	private void assertTransferEvent(TransferEvent event, String expectedAmount) {
		assertThat(event.getFromAccountId()).isEqualTo(source.getId());
		assertThat(event.getToAccountId()).isEqualTo(destination.getId());
		assertThat(event.getAmount()).isEqualByComparingTo(expectedAmount);
		assertThat(event.getCreatedAt()).isNotNull();
	}
}
