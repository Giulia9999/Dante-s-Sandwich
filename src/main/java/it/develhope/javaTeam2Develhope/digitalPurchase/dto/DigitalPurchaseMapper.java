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
        DigitalPurchaseDTO digitalPurchaseDTO = modelMapper.map(digitalPurchase, DigitalPurchaseDTO.class);
        digitalPurchaseDTO.setEBook(digitalPurchase.getPurchasedBook().getEBook());
        digitalPurchaseDTO.setAudible(digitalPurchase.getPurchasedBook().getAudible());
        return digitalPurchaseDTO;
    }
}
