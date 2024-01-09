package com.rakesh.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
/*This annotation indicates to the spring data JPA framework that this class is going to act as a superclass for
all my entities wherever I'm trying to extend this BaseEntity class*/
@MappedSuperclass
/*As we have two tables that have some common columns, hence we have declared the common columns here
as a base entity, so that we don't have to type the same column variables again in those two entity classes*/
public class BaseEntity {

  /*remove the underscore from the column name and follow camelcase, so that JPA will map the variables
  directly to the column name, else use @Column and specify the column name*/
  @Column(updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(updatable = false)
  private String createdBy;

  @Column(insertable = false)
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Column(insertable = false)
  private String updatedBy;
}
