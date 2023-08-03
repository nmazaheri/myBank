package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.example.demo.BankControllerTest;
import com.example.demo.model.TransactionRequest;
import com.example.demo.model.TransactionResponse;
import com.example.demo.utils.TransactionRequestBuilder;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class BalanceIT extends BankControllerTest {

	@Test
	public void basicTest() throws Exception {
		TransactionRequest transactionRequest = new TransactionRequestBuilder().setAccountId(1).setAmount(100).build();
		submitAmount(transactionRequest);
		ResultActions resultActions = submitBalance(transactionRequest);
		validateBalanceResponse(resultActions, transactionRequest.getAmount());
	}

	@Test
	public void defaultTimeTest() throws Exception {
		TransactionRequest transactionRequest = new TransactionRequestBuilder().setAccountId(1).setAmount(100).build();
		submitAmount(transactionRequest);
		ResultActions resultActions = mockMvc.perform(get("/api/balance").param("accountId", String.valueOf(transactionRequest.getAccountId())));
		validateBalanceResponse(resultActions, transactionRequest.getAmount());
	}

	@Test
	public void accumulatingBalanceTest() throws Exception {
		int days = 6;

		List<TransactionRequest> transactionRequests = new LinkedList<>();
		Instant instant = Instant.now().minus(7, ChronoUnit.DAYS);
		int amount = 100;
		for (int i = 0; i < days; i++) {
			instant = instant.plus(1, ChronoUnit.DAYS);
			amount += 100;
			TransactionRequest transactionRequest = new TransactionRequestBuilder().setAccountId(1).setAmount(amount).setTime(instant).build();
			transactionRequests.add(transactionRequest);
		}
		List<TransactionRequest> sortedTransactionRequests = List.copyOf(transactionRequests);

		Collections.shuffle(transactionRequests);
		for (TransactionRequest transactionRequest : transactionRequests) {
			submitAmount(transactionRequest);
		}

		amount = 0;
		for (TransactionRequest transactionRequest : sortedTransactionRequests) {
			ResultActions resultActions = submitBalance(transactionRequest);
			amount += transactionRequest.getAmount();
			validateBalanceResponse(resultActions, amount);
		}
	}

	@Test
	public void updatingBalanceTest() throws Exception {
		int days = 6;

		List<TransactionRequest> transactionRequests = new LinkedList<>();
		Instant instant = Instant.now().minus(7, ChronoUnit.DAYS);
		int amount = 100;
		TransactionRequest initialTransactionRequest = new TransactionRequestBuilder().setAccountId(1).setAmount(amount).setTime(instant).build();
		ResultActions resultActions = submitAmount(initialTransactionRequest);
		TransactionResponse transactionResponse = validateTransactionResponse(resultActions, amount);
		Integer parentId = transactionResponse.transactionId();

		for (int i = 0; i < days; i++) {
			instant = instant.plus(1, ChronoUnit.DAYS);
			amount += 100;
			TransactionRequest transactionRequest = new TransactionRequestBuilder().setAccountId(1)
					.setAmount(amount)
					.setTransactionId(parentId)
					.setTime(instant)
					.build();
			transactionRequests.add(transactionRequest);
		}
		List<TransactionRequest> sortedTransactionRequests = List.copyOf(transactionRequests);

		Collections.shuffle(transactionRequests);
		for (TransactionRequest transactionRequest : transactionRequests) {
			submitAmount(transactionRequest);
		}

		for (TransactionRequest transactionRequest : sortedTransactionRequests) {
			resultActions = submitBalance(transactionRequest);
			validateBalanceResponse(resultActions, transactionRequest.getAmount());
		}

	}

}