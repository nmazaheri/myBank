package com.example.demo.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;
import org.springframework.beans.BeanUtils;

@Entity
public class TransactionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer accountId;
	private Instant time;
	private Integer parentId;
	private Integer amount;
	private String description;
	private String category;

	public TransactionEntity() {
	}

	public TransactionEntity(TransactionRequest transactionRequest) {
		BeanUtils.copyProperties(transactionRequest, this);
		this.parentId = transactionRequest.getTransactionId();
	}

	public Integer getTransactionId() {
		if (parentId != null) {
			return parentId;
		}
		return id;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Instant getTime() {
		return time;
	}

	public void setTime(Instant time) {
		this.time = time;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
