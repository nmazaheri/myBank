package com.example.demo.controller;

import com.example.demo.model.TransactionEntity;
import com.example.demo.model.TransactionRequest;
import com.example.demo.model.TransactionResponse;
import com.example.demo.service.BalanceService;
import com.example.demo.service.TransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class BankController {

	private TransactionService transactionService;

	private BalanceService balanceService;

	public BankController(TransactionService transactionService, BalanceService balanceService) {
		this.transactionService = transactionService;
		this.balanceService = balanceService;
	}

	@PostMapping(value = "add")
	public TransactionResponse add(@RequestBody TransactionRequest transactionRequest) {
		TransactionEntity transactionEntity = transactionService.save(transactionRequest);
		Integer amount = balanceService.getBalance(transactionRequest);
		return TransactionResponse.of(amount, transactionEntity);
	}

}
