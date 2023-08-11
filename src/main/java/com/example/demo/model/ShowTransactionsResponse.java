package com.example.demo.model;

import com.example.demo.util.BankUtils;
import java.time.Instant;
import java.util.List;

public record ShowTransactionsResponse(String balance, Instant time, List<BankTransaction> transactions) {

	public static ShowTransactionsResponse of(Integer amount, Instant time, List<BankTransaction> transactions) {
		String balance = BankUtils.convert(amount);
		return new ShowTransactionsResponse(balance, time, transactions);
	}

}
