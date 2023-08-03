package com.example.demo.model;

import java.math.BigDecimal;

public record TransactionResponse(String balance, Integer transactionId) {

	public static TransactionResponse of(Integer amount, TransactionEntity transactionEntity) {
		return TransactionResponse.of(amount, transactionEntity.getTransactionId());
	}

	public static TransactionResponse of(Integer amount, Integer id) {
		String balance = getBalance(amount);
		return new TransactionResponse(balance, id);
	}

	private static String getBalance(Integer balance) {
		BigDecimal payment = new BigDecimal(balance).movePointLeft(2);
		return "$" + payment;
	}

}
