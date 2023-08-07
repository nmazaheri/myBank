package com.example.demo.repository;

import com.example.demo.model.BankTransaction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;


public interface TransactionRepository extends ListPagingAndSortingRepository<BankTransaction, Integer>,
		ListCrudRepository<BankTransaction, Integer> {

	Optional<BankTransaction> findByAccountIdAndId(Integer accountId, Integer id);

	List<BankTransaction> findByAccountId(Integer accountId);


}
