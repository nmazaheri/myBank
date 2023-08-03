package com.example.demo.model;

import com.example.demo.util.BankUtils;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class TransactionRequest {

	private @NotNull Integer accountId;
	private @NotNull Integer amount;
	private @NotNull Instant time;
	private Integer transactionId;
	private String description;
	private String category;

	public TransactionRequest(Integer accountId, Integer amount, Instant time, Integer transactionId, String description,
			String category) {
		this.accountId = accountId;
		this.amount = amount;
		this.transactionId = transactionId;
		this.description = description;
		this.category = category;
		setTime(time);
	}

	private void setTime(Instant time) {
		if (time != null) {
			this.time = time;
			return;
		}
		this.time = BankUtils.getCurrentTime();
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Instant getTime() {
		return time;
	}

	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
