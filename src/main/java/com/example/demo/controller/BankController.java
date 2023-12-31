package com.example.demo.controller;

import com.example.demo.model.BalanceResponse;
import com.example.demo.model.BankTransaction;
import com.example.demo.model.ShowTransactionsResponse;
import com.example.demo.model.TransactionRecord;
import com.example.demo.model.TransactionRequest;
import com.example.demo.model.TransactionResponse;
import com.example.demo.service.TransactionService;
import com.example.demo.util.BankUtils;
import com.example.demo.util.SortUtils;
import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.visitors.PredicateVisitor;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
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

	private static final QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();

	private TransactionService transactionService;

	public BankController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	/**
	 * Add or updates transactions based on if the {@link TransactionRequest#getId()} is set.
	 *
	 * @param transactionRequest contains details for creating a new transaction
	 * @return the new id and the balance at the current time
	 */
	@PostMapping(value = "add", consumes = MediaType.APPLICATION_JSON_VALUE)
	public TransactionResponse add(@Valid @RequestBody TransactionRequest transactionRequest) {
		BankTransaction bankTransaction = transactionService.save(transactionRequest);
		Integer amount = transactionService.getBalance(transactionRequest.getAccountId(), Instant.now());
		return TransactionResponse.of(amount, bankTransaction);
	}

	/**
	 * Retrieve the balance for a given account
	 *
	 * @param accountId the account which is required
	 * @param time      the time which defaults to the current time
	 * @return the balance and the time in which it was acquired
	 */
	@GetMapping("balance")
	public BalanceResponse balance(@RequestParam Integer accountId, @RequestParam(required = false) Instant time) {
		if (time == null) {
			time = BankUtils.getCurrentTime();
		}
		Integer amount = transactionService.getBalance(accountId, time);
		return BalanceResponse.of(amount, time);
	}

	@GetMapping("show")
	public ShowTransactionsResponse showTransactions(@RequestParam Integer accountId, @RequestParam(defaultValue = "id,desc") List<String> sort,
			@RequestParam(required = false) String search) {
		Predicate<TransactionRecord> predicate = getFilter(search);
		Comparator<TransactionRecord> comparator = SortUtils.getComparator(sort);

		List<TransactionRecord> transactions = transactionService.getTransactionRecords(accountId, predicate);
		int amount = transactions.stream().map(TransactionRecord::amount).mapToInt(Integer::intValue).sum();
		List<TransactionRecord> sortedTransactions = transactions.stream().sorted(comparator).toList();
		return ShowTransactionsResponse.of(amount, sortedTransactions);
	}

	private Predicate<TransactionRecord> getFilter(String search) {
		if (search == null) {
			return x -> true;
		}
		Condition<GeneralQueryBuilder> condition = pipeline.apply(search, TransactionRecord.class);
		return condition.query(new PredicateVisitor<>());
	}

}
