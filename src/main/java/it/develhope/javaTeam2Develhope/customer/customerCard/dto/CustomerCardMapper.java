package it.develhope.javaTeam2Develhope.customer.customerCard.dto;

import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCard;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CustomerCardMapper {
    private final ModelMapper modelMapper;

    public CustomerCardMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CustomerCardDTO toDto(CustomerCard customerCard) {
        return modelMapper.map(customerCard, CustomerCardDTO.class);
    }
}
