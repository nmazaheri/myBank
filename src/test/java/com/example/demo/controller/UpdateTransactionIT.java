package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.BankControllerTest;
import com.example.demo.model.TransactionRequest;
import com.example.demo.model.TransactionResponse;
import com.example.demo.utils.TransactionRequestBuilder;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class UpdateTransactionIT extends BankControllerTest {

	@Test
	public void unknownTransactionTest() throws Exception {
		TransactionRequest transactionRequest = new TransactionRequestBuilder().setAccountId(1).setAmount(100).setTransactionId(1).build();
		String json = getJson(transactionRequest);
		mockMvc.perform(post("/api/add").contentType(MediaType.APPLICATION_JSON).content(json)).andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	public void updateWrongAccountTest() throws Exception {
		TransactionRequest transactionRequest = new TransactionRequestBuilder().setAccountId(2).setAmount(100).build();
		submitAmount(transactionRequest);

		transactionRequest = new TransactionRequestBuilder().setAccountId(1).setAmount(100).setTransactionId(1).build();
		ResultActions resultActions = submitAmount(transactionRequest);
		resultActions.andExpect(status().isBadRequest());
	}

	@Test
	public void basicTest() throws Exception {
		TransactionRequest transactionRequest = new TransactionRequestBuilder().setAccountId(1).setAmount(100).build();
		ResultActions resultActions = submitAmount(transactionRequest);
		TransactionResponse response = validateTransactionResponse(resultActions, transactionRequest.getAmount());
		transactionRequest = new TransactionRequestBuilder().setAccountId(1).setAmount(200).setTransactionId(response.id()).build();
		resultActions = submitAmount(transactionRequest);
		validateTransactionResponse(resultActions, 200);
	}

	@Test
	public void previousTransactionIgnoredTest() throws Exception {
		TransactionRequest transactionRequest = new TransactionRequestBuilder().setAccountId(1).setAmount(100).build();
		ResultActions resultActions = submitAmount(transactionRequest);
		TransactionResponse response = validateTransactionResponse(resultActions, 100);
		Integer parentTransactionId = response.id();

		transactionRequest = new TransactionRequestBuilder().setAccountId(1).setAmount(200).setTransactionId(parentTransactionId).build();
		resultActions = submitAmount(transactionRequest);
		validateTransactionResponse(resultActions, 200);

		Instant yesterday = Instant.now().minus(1, ChronoUnit.DAYS);
		transactionRequest = new TransactionRequestBuilder().setAccountId(1)
				.setTime(yesterday)
				.setAmount(300)
				.setTransactionId(parentTransactionId)
				.build();
		resultActions = submitAmount(transactionRequest);
		validateTransactionResponse(resultActions, 200);
	}

}