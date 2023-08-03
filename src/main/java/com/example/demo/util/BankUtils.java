package com.example.demo.util;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class BankUtils {

	public static String convert(Integer amount) {
		BigDecimal payment = new BigDecimal(amount).movePointLeft(2);
		return "$" + payment;
	}

	public static Instant getCurrentTime() {
		return Instant.now().truncatedTo(ChronoUnit.MILLIS);
	}
}
