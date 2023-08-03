package com.example.demo.service;

import com.example.demo.model.TransactionEntity;
import com.example.demo.model.TransactionRequest;
import com.example.demo.repository.TransactionRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class TransactionService {

	private static final Comparator<TransactionEntity> transactionTimeComparator = Comparator.comparing(TransactionEntity::getTime);

	private TransactionRepository transactionRepository;

	public TransactionService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public TransactionEntity save(TransactionRequest transactionRequest) {
		verifyParentExists(transactionRequest);
		TransactionEntity entity = new TransactionEntity(transactionRequest);
		return transactionRepository.save(entity);
	}

	private void verifyParentExists(TransactionRequest transactionRequest) {
		Integer previousTransactionId = transactionRequest.getTransactionId();
		if (previousTransactionId == null) {
			return;
		}
		Optional<TransactionEntity> parentTransaction = transactionRepository.findByAccountIdAndId(transactionRequest.getAccountId(),
				previousTransactionId);
		if (parentTransaction.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction ID=" + previousTransactionId + " does not exist");
		}
	}

	public Integer getBalance(Integer accountId, Instant instant) {
		List<TransactionEntity> transactions = transactionRepository.findByAccountIdAndTimeLessThanEqual(accountId, instant);
		Map<Integer, List<TransactionEntity>> map = transactions.stream().collect(Collectors.groupingBy(TransactionEntity::getTransactionId));
//		int sum = map.values().stream().map(e -> e.stream().max(transactionTimeComparator)).filter(Optional::isPresent).map(Optional::get).map(TransactionEntity::getAmount).mapToInt(Integer::intValue).sum()

		List<TransactionEntity> unique = map.values()
				.stream()
				.map(e -> e.stream().max(transactionTimeComparator))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.toList();
		return unique.stream().map(TransactionEntity::getAmount).mapToInt(Integer::intValue).sum();
	}

}
