package it.develhope.javaTeam2Develhope.customer.cart;

import it.develhope.javaTeam2Develhope.book.BookNotFoundException;
import it.develhope.javaTeam2Develhope.customer.cart.dto.CartDTO;
import it.develhope.javaTeam2Develhope.customer.cart.dto.CartMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/cart")
public class CartController {
    private final CartService cartService;
    private final CartMapper cartMapper;
    private final CartRepo cartRepo;
    public CartController(CartService cartService, CartMapper cartMapper, CartRepo cartRepo) {
        this.cartService = cartService;
        this.cartMapper = cartMapper;
        this.cartRepo = cartRepo;
    }

    @GetMapping("/show/{customerId}")
    public ResponseEntity<CartDTO> showCart(@PathVariable("customerId") Long customerId){
        try{
            CartCustomer cartCustomer = cartRepo.getReferenceById(customerId);
            CartDTO cartDTO = cartMapper.toDto(cartCustomer);
            return ResponseEntity.ok(cartDTO);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/add/{customerId}")
    public ResponseEntity<CartDTO> addToCart(@PathVariable("customerId") Long customerId,
                                             @RequestParam("bookId") Long bookId) throws BookNotFoundException {
        try {
            CartCustomer cartCustomer = cartService.addToCart(customerId, bookId);
            CartDTO cartDTO = cartMapper.toDto(cartCustomer);
            return ResponseEntity.ok(cartDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/remove/{customerId}")
    public ResponseEntity<CartDTO> removeFromCart(@PathVariable("customerId") Long customerId,
                                                  @RequestParam("bookId") Long bookId) throws BookNotFoundException {
        try {
            CartCustomer cartCustomer = cartService.removeFromCart(customerId, bookId);
            CartDTO cartDTO = cartMapper.toDto(cartCustomer);
            return ResponseEntity.ok(cartDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
