package it.develhope.javaTeam2Develhope.motionPicture;

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
@RequestMapping("/motion-pictures")
public class MotionPictureController {
    @Autowired
    private MotionPictureService motionPictureService;

    @GetMapping
    public ResponseEntity<Page<MotionPicture>> getAllMotionPictures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String producer,
            @RequestParam(required = false) Integer year) {

        Page<MotionPicture> motionPicturesPage = motionPictureService.getAllMotionPictures(page, size, title, topic, producer, year);
        return ResponseEntity.ok(motionPicturesPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotionPicture> getMotionPictureById(@PathVariable long id) {
        MotionPicture motionPicture = motionPictureService.getMotionPictureById(id);
        return ResponseEntity.ok(motionPicture);
    }

    @PostMapping("/single")
    public ResponseEntity<MotionPicture> addSingleMotionPicture(@RequestBody MotionPicture motionPicture) {
        MotionPicture savedMotionPicture = motionPictureService.addSingleMotionPicture(motionPicture);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMotionPicture);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotionPicture> updateMotionPicture(@PathVariable Long id, @Valid @RequestBody MotionPicture motionPicture) {
        MotionPicture updatedMotionPicture = motionPictureService.updateMotionPicture(id, motionPicture);
        return ResponseEntity.ok(updatedMotionPicture);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MotionPicture> patchMotionPicture(@PathVariable long id, @RequestBody MotionPicture motionPicture) {
        MotionPicture savedMotionPicture = motionPictureService.patchMotionPicture(id, motionPicture);
        return ResponseEntity.ok(savedMotionPicture);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMotionPicture(@PathVariable long id) {
        motionPictureService.deleteMotionPicture(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAllMotionPictures() {
        motionPictureService.deleteAllMotionPictures();
        return ResponseEntity.noContent().build();
    }
}