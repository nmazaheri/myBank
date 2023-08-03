package com.example.demo.utils;

import com.example.demo.model.TransactionRequest;
import java.time.Instant;

public class TransactionRequestBuilder {

	private Integer accountId;
	private Integer amount;
	private Instant time;
	private Integer transactionId;
	private String description;
	private String category;

	public TransactionRequestBuilder setAccountId(Integer accountId) {
		this.accountId = accountId;
		return this;
	}

	public TransactionRequestBuilder setAmount(Integer amount) {
		this.amount = amount;
		return this;
	}

	public TransactionRequestBuilder setTime(Instant time) {
		this.time = time;
		return this;
	}

	public TransactionRequestBuilder setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
		return this;
	}

	public TransactionRequestBuilder setDescription(String description) {
		this.description = description;
		return this;
	}

	public TransactionRequestBuilder setCategory(String category) {
		this.category = category;
		return this;
	}

	public TransactionRequest build() {
		return new TransactionRequest(accountId, amount, time, transactionId, description, category);
	}
}