package com.example.demo.repository;

import com.example.demo.model.TransactionEntity;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;


public interface TransactionRepository extends ListPagingAndSortingRepository<TransactionEntity, Integer>,
		ListCrudRepository<TransactionEntity, Integer> {

	Optional<TransactionEntity> findByAccountIdAndId(Integer accountId, Integer id);

	List<TransactionEntity> findByAccountIdAndTimeLessThanEqual(Integer accountId, Instant before);


}
