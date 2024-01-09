package com.rakesh.accounts.repository;

import com.rakesh.accounts.entity.Accounts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Accounts, Long> {

  Optional<Accounts> findByCustomerId(Long customerId);

/*  Here we have used two annotations  @Transactional and @Modifying.
  @Modifying will tell to the spring data JPA framework that these method is going to modify the data.
  So that's why please execute the query of this method inside a Transaction.  That's why we are mentioning
  @Transactional annotation.
  When my spring data JPA runs my query inside a Transaction and if there is some error happens at the
  runtime, any partial change of data that is resulted due to the queries will be rolled back because
  the entire transaction will be rolled back by the spring data JPA, and we are in safe hands.
  It should not be a scenario where like we deleted the account but not the customer.
  So those kind of scenarios we need to make sure we are handling with the help of this transactional
  and modifying.
  And here you may have a question like we are also invoking these "deleteById" method inside the
  CustomerRepository then why you are not mentioning @Transactional and @Modifying for this method (Same
  goes for the save method when we use PUT to update the data).
  Please note that this method is from the framework. Framework is going to take care of it.
  But the below method we have returned with our own hands since framework cannot identify that.
  We need to let framework that this method is going to modify data and please run it inside a Transaction.
  And if there is some error happen, please roll back the Transaction. That's what we are trying to communicate
  with these two annotations.*/
  @Transactional
  @Modifying
  void deleteByCustomerId(Long customerId);
}
