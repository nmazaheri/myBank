package com.example.demo.model;

import java.math.BigDecimal;

public record TransactionResponse(String balance, Integer transactionId) {

	public static TransactionResponse of(Integer amount, TransactionEntity transactionEntity) {
		String balance = getBalance(amount);
		return new TransactionResponse(balance, transactionEntity.getTransactionId());
	}

	private static String getBalance(Integer balance) {
		BigDecimal payment = new BigDecimal(balance).movePointLeft(2);
		return "$" + payment;
	}

}
