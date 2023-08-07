package com.example.demo.service;

import com.example.demo.model.BankTransaction;
import com.example.demo.model.TransactionRequest;
import com.example.demo.repository.TransactionRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class TransactionService {

	private static final Comparator<BankTransaction> transactionTimeComparator = Comparator.comparing(BankTransaction::getTime);

	private TransactionRepository transactionRepository;

	public TransactionService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public BankTransaction save(TransactionRequest transactionRequest) {
		verifyParentExists(transactionRequest);
		BankTransaction entity = new BankTransaction(transactionRequest);
		return transactionRepository.save(entity);
	}

	private void verifyParentExists(TransactionRequest transactionRequest) {
		Integer previousTransactionId = transactionRequest.getTransactionId();
		if (previousTransactionId == null) {
			return;
		}
		Optional<BankTransaction> parentTransaction = transactionRepository.findByAccountIdAndId(transactionRequest.getAccountId(),
				previousTransactionId);
		if (parentTransaction.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction ID=" + previousTransactionId + " does not exist");
		}
	}

	public Integer getBalance(Integer accountId, Instant maxTime) {
		List<BankTransaction> unique = getBankTransactions(accountId);
		List<BankTransaction> filtered = unique.stream().filter(lessThenOrEqual(maxTime)).toList();
		return filtered.stream().map(BankTransaction::getAmount).mapToInt(Integer::intValue).sum();
	}

	private static Predicate<BankTransaction> lessThenOrEqual(Instant maxTime) {
		return i -> !i.getTime().isAfter(maxTime);
	}

	private List<BankTransaction> getBankTransactions(Integer accountId) {
		List<BankTransaction> transactions = transactionRepository.findByAccountId(accountId);
		Map<Integer, List<BankTransaction>> transacationMap = transactions.stream().collect(Collectors.groupingBy(BankTransaction::getTransactionId));
		return transacationMap.values()
				.stream()
				.map(e -> e.stream().max(transactionTimeComparator))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.toList();
	}

}
