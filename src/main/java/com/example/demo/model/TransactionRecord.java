package com.example.demo.model;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public record TransactionRecord(Integer id, String time, Integer amount, String description, String category) {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault());

	public static TransactionRecord of(BankTransaction bankTransaction) {
		return new TransactionRecord(bankTransaction.getTransactionId(), formatter.format(bankTransaction.getTime().truncatedTo(ChronoUnit.SECONDS)),
				bankTransaction.getAmount(), bankTransaction.getDescription(), bankTransaction.getCategory());
	}

}
