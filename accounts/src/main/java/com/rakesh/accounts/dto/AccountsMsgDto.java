package com.rakesh.accounts.dto;

/**
 * @param accountNumber
 * @param name
 * @param email
 * @param mobileNumber
 * This Dto is created to send message using RabbitMQ
 */
public record AccountsMsgDto(Long accountNumber, String name, String email, String mobileNumber) {}
