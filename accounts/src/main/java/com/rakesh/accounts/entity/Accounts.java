package com.rakesh.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Accounts extends BaseEntity {

  @Id private Long accountNumber;

  private Long customerId;

  private String accountType;

  private String branchAddress;

  @Column(name = "communication_sw")
  private Boolean communicationSw;
}
