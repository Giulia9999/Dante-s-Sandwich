package it.develhope.javaTeam2Develhope.digitalPurchase.dto;

import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchase;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DigitalPurchaseMapper {
    private final ModelMapper modelMapper;

    public DigitalPurchaseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public DigitalPurchaseDTO toDto(DigitalPurchase digitalPurchase) {
        return modelMapper.map(digitalPurchase, DigitalPurchaseDTO.class);
    }
}
