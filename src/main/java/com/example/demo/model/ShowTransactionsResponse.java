package com.example.demo.model;

import com.example.demo.util.BankUtils;
import java.util.List;

public record ShowTransactionsResponse(String balance, List<TransactionRecord> transactions) {

	public static ShowTransactionsResponse of(Integer amount, List<TransactionRecord> transactions) {
		String balance = BankUtils.convert(amount);
		return new ShowTransactionsResponse(balance, transactions);
	}

}
