package it.develhope.javaTeam2Develhope.customer.dto;

import it.develhope.javaTeam2Develhope.customer.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    private final ModelMapper modelMapper;

    public CustomerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CustomerDTO toDto(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }
}
