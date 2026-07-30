package ru.leeeny.reviewsservice.config;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

@TestComponent
public class TransactionOpener {

	private final TransactionTemplate transactionTemplate;

	public TransactionOpener(PlatformTransactionManager txManager) {
		this.transactionTemplate = new TransactionTemplate(txManager);
		this.transactionTemplate.setPropagationBehavior(
				TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	}

	public <T> T runInNewTransaction(Supplier<T> action) {
		transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		return transactionTemplate.execute(spec -> action.get());
	}
}
