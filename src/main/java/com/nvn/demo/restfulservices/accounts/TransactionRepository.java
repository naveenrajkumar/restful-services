package com.nvn.demo.restfulservices.accounts;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer>  {

	public List<Transaction> findByAccount(Account account);
}
