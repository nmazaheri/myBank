package com.example.demo.util;

import com.example.demo.model.BankTransaction;
import com.example.demo.model.SortOrder;
import com.example.demo.model.TransactionField;
import java.util.Comparator;
import java.util.List;

public class SortUtils {

	public static Comparator<BankTransaction> getComparator(List<String> sort) {
		if (!sort.get(0).contains(",")) {
			TransactionField transactionField = TransactionField.valueOf(sort.get(0));
			SortOrder sortOrder = SortOrder.valueOf(sort.get(1));
			return getBankTransactionComparator(null, transactionField, sortOrder);
		}

		Comparator<BankTransaction> comparator = null;
		for (String s : sort) {
			String[] split = s.split(",");
			TransactionField transactionField = TransactionField.valueOf(split[0]);
			SortOrder sortOrder = SortOrder.valueOf(split[1]);
			comparator = getBankTransactionComparator(comparator, transactionField, sortOrder);
		}
		return comparator;
	}

	private static Comparator<BankTransaction> getBankTransactionComparator(Comparator<BankTransaction> original, TransactionField transactionField,
			SortOrder sortOrder) {
		Comparator<BankTransaction> comparator = getComparator(transactionField);
		comparator = getOrder(sortOrder, comparator);
		if (original == null) {
			return comparator;
		}
		return original.thenComparing(comparator);
	}

	private static Comparator<BankTransaction> getComparator(TransactionField transactionField) {
		return switch (transactionField) {
			case id -> Comparator.comparing(BankTransaction::getTransactionId);
			case category -> Comparator.comparing(BankTransaction::getCategory);
			case time -> Comparator.comparing(BankTransaction::getTime);
			case amount -> Comparator.comparing(BankTransaction::getAmount);
			case description -> Comparator.comparing(BankTransaction::getDescription);
		};
	}

	private static Comparator<BankTransaction> getOrder(SortOrder sortOrder, Comparator<BankTransaction> comparator) {
		return switch (sortOrder) {
			case asc -> comparator;
			case desc -> comparator.reversed();
		};
	}

}
