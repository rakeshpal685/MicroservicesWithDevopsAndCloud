package com.rakesh.accounts.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer extends BaseEntity {

  @Id
  /*Here generator is native means I'm telling to the spring data JPA framework,whatever database that
  I'm using, please try to generate the sequence number or the primary key value based upon the native
  style of my database.*/
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native")
  private Long customerId;

  private String name;

  private String email;

  private String mobileNumber;
}
