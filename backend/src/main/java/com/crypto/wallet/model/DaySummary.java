package com.crypto.wallet.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DaySummary {
	private String date;
	private Double opening;
	private Double closing;
	private Double lowest;
	private Double highest;
	private Double volume;
	private Double quantity;
	private int amount;
	private Double avg_price;
}
