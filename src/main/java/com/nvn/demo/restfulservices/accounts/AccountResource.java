package com.nvn.demo.restfulservices.accounts;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nvn.demo.restfulservices.exception.AccountNotFoundException;
import com.nvn.demo.restfulservices.exception.InvalidInputException;

/**
 * @author Naveen
 * Account Resources Get  - /accounts - Returns all account
 * 					 Get  - /account/{id}/transactions - Returns all transaction for an account
 * 					 Post - /account - Creates an account
 * 					 Post - /account/{id}/transaction - Posts a transaction for the account
 * 				
 *
 */
@RestController
public class AccountResource {
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	/**
	 * @return all the accounts in the system
	 */
	@GetMapping("/accounts")
	@CrossOrigin(origins="*")
	public List<Account> getAllAccounts() {
		List<Account> accounts = accountRepository.findAll();
		if(accounts == null || accounts.size() < 1 ){
			throw new AccountNotFoundException("No Accounts found");
		}
		return accounts;
	}

	
	/**
	 * @param id - Account Id
	 * @return - All the transaction for the account id
	 */
	@GetMapping("/account/{id}/transactions")
	@CrossOrigin(origins="*")
	public List<Transaction> getAllTransactionsForAnAccount(@PathVariable int id){
		Optional<Account> account = this.accountRepository.findById(id);
		
		if(!account.isPresent()){
			throw new AccountNotFoundException("Account Not Found:"+id);
		}
		
	    List<Transaction> transactions = this.transactionRepository.findByAccount(account.get());
		return transactions;
	}		
	

	/**
	 * @param account
	 * @return - Creates a new account
	 */
	@PostMapping("/account")
	@CrossOrigin(origins="*")
	public ResponseEntity<Object> createAccount(@Valid @RequestBody Account account)
	{
		Optional<Account> find_account = this.accountRepository.findById(account.getAccountNumber());
	
		if(find_account.isPresent()){
			throw new InvalidInputException("Account already Exists:"+account.getAccountNumber());
		}
		account.setAvailableBalance(new BigDecimal(0));
		Account savedAccount = accountRepository.save(account);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("account/{id}/transactions").buildAndExpand(savedAccount.getAccountNumber())
				.toUri();

		return ResponseEntity.created(location).build();

	}
	
	/**
	 * Adds the transaction in a thread safe way and updates the available amount in the Account entity.
	 * @param id - Account ID
	 * @param transaction - transaction details - required: amount, transaction type, currency
	 * @return Response Entity with relevant HTTP Status
	 */
	@PostMapping("/account/{id}/transaction")
	@CrossOrigin(origins="*")
	public ResponseEntity<Object> postTransaction(@PathVariable int id, @RequestBody Transaction transaction)
	{
		URI location;
		Optional<Account> account = accountRepository.findById(id);
		
		if(!account.isPresent()){
			throw new AccountNotFoundException("Account Not Found:"+id);
		}
		
		if(!(account.get().getAccountCurrency().equalsIgnoreCase(transaction.getCurrency()))){
			throw new InvalidInputException("Account currency mismatch, account currency is "+account.get().getAccountCurrency());
		}
		
		if((!transaction.getTransactionType().equalsIgnoreCase("credit")) && !(transaction.getTransactionType().equalsIgnoreCase("debit"))) {
			throw new InvalidInputException("Transaction type mismatch, It should be either Credit or Debit ");
		}
		
		BigDecimal zero = new BigDecimal(0);
		if(transaction.getAmount().compareTo(zero)<1){
			throw new InvalidInputException("Transaction Amount need to be greater than zero ");
		} 
		
		transaction.setAccount(account.get());
		
	

		synchronized(this){
			if(transaction.getTransactionType().equalsIgnoreCase("credit")){
			BigDecimal newBalance = account.get().getAvailableBalance().add(transaction.getAmount());
			account.get().setAvailableBalance(newBalance);
			} else {
			BigDecimal newBalance = account.get().getAvailableBalance().subtract(transaction.getAmount());
			account.get().setAvailableBalance(newBalance);
			}
			transaction.setTransactionDate(new Date());
			Transaction savedTransaction = transactionRepository.save(transaction);
			accountRepository.save(account.get());
			location = ServletUriComponentsBuilder.fromCurrentRequest().path("account/{id}/transactions").buildAndExpand(savedTransaction.getAccount().getAccountNumber()).toUri();
		}
		

		return ResponseEntity.created(location).build();

	}
}
