package com.example.demo.service;

import com.example.demo.model.TransactionEntity;
import com.example.demo.model.TransactionRequest;
import com.example.demo.repository.TransactionRepository;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class TransactionService {

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
		Integer previousTransactionId = transactionRequest.transactionId();
		if (previousTransactionId == null) {
			return;
		}
		Optional<TransactionEntity> parentTransaction = transactionRepository.findById(previousTransactionId);
		if (parentTransaction.isEmpty()) {
			throw new RuntimeException("Transaction ID=" + previousTransactionId + " does not exist");
		}

	}
}
