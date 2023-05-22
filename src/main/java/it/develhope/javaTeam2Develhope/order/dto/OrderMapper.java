package it.develhope.javaTeam2Develhope.order.dto;

import it.develhope.javaTeam2Develhope.order.Order;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    private final ModelMapper modelMapper;

    public OrderMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public OrderDTO toDto(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }

}
