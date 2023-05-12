package it.develhope.javaTeam2Develhope.game;

import org.springframework.stereotype.Service;
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

@Service
public class GameService {

    @Autowired
    private GameRepo gameRepo;

    public Page<Game> getAllGames(String title, String topic, String producer, Integer year, int page, int size) {
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
        return gameRepo.findAll(spec, paging);
    }

    public Game getGameById(long id) {
        Optional<Game> optionalGame = gameRepo.findById(id);
        return optionalGame.orElse(null);
    }

    public List<Game> addMultipleGames(List<Game> games) {
        return gameRepo.saveAllAndFlush(games);
    }

    public Game addSingleGame(Game game) {
        return gameRepo.saveAndFlush(game);
    }

    public Game updateGame(Long id, Game game) {
        Optional<Game> optionalGame = gameRepo.findById(id);

        if (optionalGame.isEmpty()) {
            return null;
        }

        game.setId(id);

        return gameRepo.save(game);
    }

    public Game patchGame(long id, Game game) {
        Optional<Game> optionalGame = gameRepo.findById(id);

        if (optionalGame.isEmpty()) {
            return null;
        }

        Game existingGame = optionalGame.get();

        BeanUtils.copyProperties(game, existingGame, getEmptyPropertyNames(game));
        return gameRepo.save(existingGame);
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

    public void deleteGame(long id) {
        gameRepo.deleteById(id);
    }

    public void deleteAllGames() {
        gameRepo.deleteAll();
    }
}
