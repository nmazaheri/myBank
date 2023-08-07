package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.model.BalanceResponse;
import com.example.demo.model.TransactionRequest;
import com.example.demo.model.TransactionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@Transactional
public class BankControllerTest {

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	protected MockMvc mockMvc;

	protected ResultActions submitAmount(TransactionRequest transactionRequest) throws Exception {
		String json = getJson(transactionRequest);
		return mockMvc.perform(post("/api/add").contentType(MediaType.APPLICATION_JSON).content(json)).andDo(print());
	}

	public TransactionResponse validateTransactionResponse(ResultActions resultActions, int balance) throws Exception {
		resultActions.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
		TransactionResponse actual = getTransactionResponse(resultActions.andReturn());
		TransactionResponse expected = TransactionResponse.of(balance, 0);
		assertThat(actual.balance()).isEqualTo(expected.balance());
		return actual;
	}

	private TransactionResponse getTransactionResponse(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
		return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TransactionResponse.class);
	}

	protected ResultActions submitBalance(TransactionRequest transactionRequest) throws Exception {
		return mockMvc.perform(get("/api/balance").param("accountId", String.valueOf(transactionRequest.getAccountId()))
				.param("time", transactionRequest.getTime().toString())).andDo(print());
	}

	public BalanceResponse validateBalanceResponse(ResultActions resultActions, int balance) throws Exception {
		resultActions.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
		BalanceResponse actual = getBalanceResponse(resultActions.andReturn());
		BalanceResponse expected = BalanceResponse.of(balance, null);
		assertThat(actual.balance()).isEqualTo(expected.balance());
		return actual;
	}

	private BalanceResponse getBalanceResponse(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
		return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BalanceResponse.class);
	}

	protected String getJson(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}

}
