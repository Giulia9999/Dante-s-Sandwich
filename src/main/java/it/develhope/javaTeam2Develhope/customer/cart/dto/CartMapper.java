package it.develhope.javaTeam2Develhope.customer.cart.dto;

import it.develhope.javaTeam2Develhope.customer.cart.CartCustomer;
import it.develhope.javaTeam2Develhope.customer.customerCard.dto.CustomerCardDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {
    private final ModelMapper modelMapper;

    public CartMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CartDTO toDto(CartCustomer cartCustomer) {
        CartDTO cartDTO = modelMapper.map(cartCustomer, CartDTO.class);
        cartDTO.setName(cartCustomer.getCustomer().getName());
        cartDTO.setEmail(cartCustomer.getCustomer().getEmail());
        cartDTO.setBooksInTheCart(cartCustomer.getBookInTheCart());
        return cartDTO;
    }
}
