package com.rakesh.accounts.service.feignClient;

import com.rakesh.accounts.dto.CardsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardsFallback implements CardsFeignClient{
/**
*
 * @param correlationId
 * @param mobileNumber
 * @return
*/
    @Override
    public ResponseEntity<CardsDto> fetchCardDetails(String correlationId, String mobileNumber) {
        //We can write our own logic here to send back the response when the feign client is not available
        return null;
    }
}
