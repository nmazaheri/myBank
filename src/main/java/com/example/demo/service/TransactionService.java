package com.example.demo.service;

import com.example.demo.model.TransactionEntity;
import com.example.demo.model.TransactionRequest;
import com.example.demo.repository.TransactionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class TransactionService {

	private TransactionRepository transactionRepository;

	public TransactionService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public TransactionEntity save(TransactionRequest transactionRequest) {
		TransactionEntity entity = new TransactionEntity(transactionRequest);
		return transactionRepository.save(entity);
	}
}
