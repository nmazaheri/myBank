package com.example.demo.model;

import java.time.Instant;

public record TransactionRequest(Integer accountId, Integer amount, Instant time, Integer transactionId, String description, String category) {


}
