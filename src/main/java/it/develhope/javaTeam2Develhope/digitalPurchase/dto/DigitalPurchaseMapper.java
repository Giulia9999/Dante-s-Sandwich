package it.develhope.javaTeam2Develhope.digitalPurchase.dto;

import it.develhope.javaTeam2Develhope.book.BookService;
import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchase;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DigitalPurchaseMapper {
    private final ModelMapper modelMapper;
    private final BookService bookService;

    public DigitalPurchaseMapper(ModelMapper modelMapper, BookService bookService) {
        this.modelMapper = modelMapper;
        this.bookService = bookService;
    }

    public DigitalPurchaseDTO toDto(DigitalPurchase digitalPurchase) {
        return modelMapper.map(digitalPurchase, DigitalPurchaseDTO.class);
    }
}
