package com.example.demo.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.BeanUtils;

@Entity
public class TransactionEntity {

	@Id
	@GeneratedValue
	private Integer id;
	private Integer parentId;
	@CreationTimestamp
	private Instant created;

	private Integer accountId;
	private Integer amount;
	private String description;
	private String category;

	public TransactionEntity() {
	}

	public TransactionEntity(TransactionRequest transactionRequest) {
		BeanUtils.copyProperties(transactionRequest, this);
		this.parentId = transactionRequest.transactionId();
		this.created = transactionRequest.time();
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getTransactionId() {
		if (parentId != null) {
			return parentId;
		}
		return id;
	}

	public void setParentId(Integer transactionId) {
		this.parentId = transactionId;
	}

	public Instant getCreated() {
		return created;
	}

	public void setCreated(Instant created) {
		this.created = created;
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
