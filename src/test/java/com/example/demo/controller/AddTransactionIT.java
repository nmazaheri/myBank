package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.BankControllerTest;
import com.example.demo.model.TransactionRequest;
import com.example.demo.utils.TransactionRequestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class AddTransactionIT extends BankControllerTest {

	@Test
	public void missingAccountIdTest() throws Exception {
		TransactionRequest transactionRequest = new TransactionRequestBuilder().setAmount(100).build();
		String json = getJson(transactionRequest);
		mockMvc.perform(post("/api/add").contentType(MediaType.APPLICATION_JSON).content(json)).andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	public void missingTransactionTest() throws Exception {
		TransactionRequest transactionRequest = new TransactionRequestBuilder().setAccountId(1).build();
		String json = getJson(transactionRequest);

		mockMvc.perform(post("/api/add").contentType(MediaType.APPLICATION_JSON).content(json)).andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	public void basicTest() throws Exception {
		TransactionRequest transactionRequest = new TransactionRequestBuilder().setAccountId(1).setAmount(500).build();
		ResultActions resultActions = submitAmount(transactionRequest);
		validateTransactionResponse(resultActions, transactionRequest.getAmount());
	}


}