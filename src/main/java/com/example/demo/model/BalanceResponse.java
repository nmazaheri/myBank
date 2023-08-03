package com.example.demo.model;

import com.example.demo.util.BankUtils;
import java.time.Instant;

public record BalanceResponse(String balance, Instant time) {

	public static BalanceResponse of(Integer amount, Instant time) {
		String balance = BankUtils.convert(amount);
		return new BalanceResponse(balance, time);
	}

}
