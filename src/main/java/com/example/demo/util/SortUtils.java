package com.example.demo.util;

import com.example.demo.model.SortOrder;
import com.example.demo.model.TransactionField;
import com.example.demo.model.TransactionRecord;
import java.util.Comparator;
import java.util.List;

public class SortUtils {

	public static Comparator<TransactionRecord> getComparator(List<String> sort) {
		if (!sort.get(0).contains(",")) {
			TransactionField transactionField = TransactionField.valueOf(sort.get(0));
			SortOrder sortOrder = SortOrder.valueOf(sort.get(1));
			return getBankTransactionComparator(null, transactionField, sortOrder);
		}

		Comparator<TransactionRecord> comparator = null;
		for (String s : sort) {
			String[] split = s.split(",");
			TransactionField transactionField = TransactionField.valueOf(split[0]);
			SortOrder sortOrder = SortOrder.valueOf(split[1]);
			comparator = getBankTransactionComparator(comparator, transactionField, sortOrder);
		}
		return comparator;
	}

	private static Comparator<TransactionRecord> getBankTransactionComparator(Comparator<TransactionRecord> original, TransactionField transactionField,
			SortOrder sortOrder) {
		Comparator<TransactionRecord> comparator = getComparator(transactionField);
		comparator = getOrder(sortOrder, comparator);
		if (original == null) {
			return comparator;
		}
		return original.thenComparing(comparator);
	}

	private static Comparator<TransactionRecord> getComparator(TransactionField transactionField) {
		return switch (transactionField) {
			case id -> Comparator.comparing(TransactionRecord::id);
			case category -> Comparator.comparing(TransactionRecord::category);
			case time -> Comparator.comparing(TransactionRecord::time);
			case amount -> Comparator.comparing(TransactionRecord::amount);
			case description -> Comparator.comparing(TransactionRecord::description);
		};
	}

	private static Comparator<TransactionRecord> getOrder(SortOrder sortOrder, Comparator<TransactionRecord> comparator) {
		return switch (sortOrder) {
			case asc -> comparator;
			case desc -> comparator.reversed();
		};
	}

}
