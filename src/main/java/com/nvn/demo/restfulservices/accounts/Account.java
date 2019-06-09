package com.nvn.demo.restfulservices.accounts;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;



/**
 * @author Naveen
 * Account Entity Class
 *
 */
@ApiModel(description="Details about the accounts")
@Entity
public class Account {

	@Id
	@NotNull
	@Positive(message="Account number need to be positive")
	private Integer accountNumber;
	private String accountName;
	@NotNull
	@ApiModelProperty(notes="Valid values Savings/Checking")
	private String accountType;
	@Size(min=2, max=6,message="Currency needs to be in 2 to 6 characters")
	@NotNull
	private String accountCurrency;
	@ApiModelProperty(notes="Defaults to zero")
	private BigDecimal availableBalance;


	protected Account() {

	}

	public Account(Integer accountNumber, String accountName,
			String accountType, String currency, BigDecimal amount,
			Date transactionDate) {
		super();
		this.setAccountNumber(accountNumber);
		this.accountName = accountName;
		this.accountType = accountType;

	}

	public Integer getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Integer accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountName() {
		return accountName;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public String getAccountType() {
		return accountType;
	}
	
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public String getAccountCurrency() {
		return accountCurrency;
	}

	public void setAccountCurrency(String accountCurrency) {
		this.accountCurrency = accountCurrency;
	}

	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}
	

}
