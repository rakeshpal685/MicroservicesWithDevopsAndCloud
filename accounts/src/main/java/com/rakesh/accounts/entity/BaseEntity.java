package com.rakesh.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@ToString
/*This annotation indicates to the spring data JPA framework that this class is going to act as a superclass for
all my entities wherever I'm trying to extend this BaseEntity class*/
@MappedSuperclass
//This annotation is used to activate the auditing feature like createdBy
@EntityListeners(AuditingEntityListener.class)
/*As we have two tables that have some common columns, hence we have declared the common columns here
as a base entity, so that we don't have to type the same column variables again in those two entity classes*/
public class BaseEntity {

  /*remove the underscore from the column name and follow camelcase, so that JPA will map the variables
  directly to the column name, else use @Column and specify the column name*/
  @Column(updatable = false)
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(updatable = false)
  @CreatedBy
  private String createdBy;

  @Column(insertable = false)
  @LastModifiedDate
  private LocalDateTime updatedAt;

  @Column(insertable = false)
  @LastModifiedBy
  private String updatedBy;
}
