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
        CustomerCardDTO customerCardDTO = modelMapper.map(customerCard, CustomerCardDTO.class);
        customerCardDTO.setName(customerCard.getCustomer().getName());
        customerCardDTO.setEmail(customerCard.getCustomer().getEmail());
        return customerCardDTO;
    }
}
