package it.develhope.javaTeam2Develhope.game;

import io.micrometer.common.util.StringUtils;
import it.develhope.javaTeam2Develhope.book.Book;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameRepo gameRepo;

    @GetMapping
    public ResponseEntity<Page<Game>> getAllGames(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String producer,
            @RequestParam(required = false) Integer year) {

        Specification<Game> spec = Specification.where(null);

        if (title != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("title"), title));
        }
        if (topic != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("topic"), topic));
        }
        if (producer != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("producer"), producer));
        }
        if (year != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("year"), year));
        }

        Pageable paging = PageRequest.of(page, size);
        Page<Game> gamesPage = gameRepo.findAll(spec, paging);
        return ResponseEntity.ok(gamesPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable long id) {
        Optional<Game> optionalGame = gameRepo.findById(id);

        if (optionalGame.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Game game = optionalGame.get();

        return ResponseEntity.ok(game);
    }

    @PostMapping("/single")
    public ResponseEntity<Game> addSingleGame(@RequestBody Game game) {
        Game savedGame = gameRepo.saveAndFlush(game);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGame);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @Valid @RequestBody Game game) {
        Optional<Game> optionalGame = gameRepo.findById(id);

        if (optionalGame.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        game.setId(id);

        Game updatedGame = gameRepo.save(game);
        return ResponseEntity.ok(updatedGame);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable long id, @RequestBody Game game) {
        Optional<Game> optionalGame = gameRepo.findById(id);

        if (optionalGame.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Game existingGame = optionalGame.get();

        BeanUtils.copyProperties(game, existingGame, getEmptyPropertyNames(game));
        Game savedGame = gameRepo.save(existingGame);

        return ResponseEntity.ok(savedGame);
    }

    private String[] getEmptyPropertyNames(Game source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue instanceof String && StringUtils.isBlank((String) srcValue)) {
                // Ignore empty string values
                continue;
            }
            if (srcValue == null) {
                // Include null values
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable long id) {
        Optional<Game> optionalGame = gameRepo.findById(id);

        if (optionalGame.isEmpty()) {
            return ResponseEntity.notFound().build();

        }

        gameRepo.deleteById(id);

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAllGames() {
        gameRepo.deleteAll();

        return ResponseEntity.noContent().build();
    }
}
