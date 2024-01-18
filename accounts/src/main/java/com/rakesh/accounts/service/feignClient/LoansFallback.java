package com.rakesh.accounts.service.feignClient;

import com.rakesh.accounts.dto.LoansDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoansFallback implements LoansFeignClient{
    @Override
    public ResponseEntity<LoansDto> fetchLoanDetails(String correlationId, String mobileNumber) {
        //We can write our own logic here to send back the response when the feign client is not available
        return null;
    }
}
