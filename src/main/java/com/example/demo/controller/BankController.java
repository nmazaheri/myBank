package com.example.demo.controller;

import com.example.demo.model.BalanceResponse;
import com.example.demo.model.TransactionEntity;
import com.example.demo.model.TransactionRequest;
import com.example.demo.model.TransactionResponse;
import com.example.demo.service.TransactionService;
import com.example.demo.util.BankUtils;
import jakarta.validation.Valid;
import java.time.Instant;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class BankController {

	private TransactionService transactionService;

	public BankController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	/**
	 * Add or updates transactions based on if the {@link TransactionRequest#getTransactionId()} is set.
	 *
	 * @param transactionRequest contains details for creating a new transaction
	 * @return the new transactionId and the balance at the current time
	 */
	@PostMapping(value = "add", consumes = MediaType.APPLICATION_JSON_VALUE)
	public TransactionResponse add(@Valid @RequestBody TransactionRequest transactionRequest) {
		TransactionEntity transactionEntity = transactionService.save(transactionRequest);
		Integer amount = transactionService.getBalance(transactionRequest.getAccountId(), Instant.now());
		return TransactionResponse.of(amount, transactionEntity);
	}

	/**
	 * Retrieve the balance for a given account
	 *
	 * @param accountId the account which is required
	 * @param time      the time which defaults to the current time
	 * @return the balance and the time in which it was acquired
	 */
	@GetMapping(value = "balance")
	public BalanceResponse balance(@RequestParam Integer accountId, @RequestParam(required = false) Instant time) {
		if (time == null) {
			time = BankUtils.getCurrentTime();
		}
		Integer amount = transactionService.getBalance(accountId, time);
		return BalanceResponse.of(amount, time);
	}

}
