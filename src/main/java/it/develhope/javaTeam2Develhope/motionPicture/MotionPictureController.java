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
    private MotionPictureRepo motionPictureRepo;

    @GetMapping
    public ResponseEntity<Page<MotionPicture>> getAllMotionPictures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String producer,
            @RequestParam(required = false) Integer year) {

        Specification<MotionPicture> spec = Specification.where(null);

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
        Page<MotionPicture> motionPicturesPage = motionPictureRepo.findAll(spec, paging);
        return ResponseEntity.ok(motionPicturesPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotionPicture> getMotionPictureById(@PathVariable long id) {
        Optional<MotionPicture> optionalMotionPicture = motionPictureRepo.findById(id);

        if (optionalMotionPicture.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MotionPicture motionPicture = optionalMotionPicture.get();

        return ResponseEntity.ok(motionPicture);
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<MotionPicture>> addMultipleMotionPictures(@RequestBody List<MotionPicture> motionPictures) {
        List<MotionPicture> savedMotionPictures = motionPictureRepo.saveAllAndFlush(motionPictures);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMotionPictures);
    }

    @PostMapping("/single")
    public ResponseEntity<MotionPicture> addSingleMotionPicture(@RequestBody MotionPicture motionPicture) {
        MotionPicture savedMotionPicture = motionPictureRepo.saveAndFlush(motionPicture);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMotionPicture);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotionPicture> updateMotionPicture(@PathVariable Long id, @Valid @RequestBody MotionPicture motionPicture) {
        Optional<MotionPicture> optionalMotionPicture = motionPictureRepo.findById(id);

        if (optionalMotionPicture.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        motionPicture.setId(id);

        MotionPicture updatedMotionPicture = motionPictureRepo.save(motionPicture);
        return ResponseEntity.ok(updatedMotionPicture);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MotionPicture> updateMotionPicture(@PathVariable long id, @RequestBody MotionPicture motionPicture) {
        Optional<MotionPicture> optionalMotionPicture = motionPictureRepo.findById(id);

        if (optionalMotionPicture.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MotionPicture existingMotionPicture = optionalMotionPicture.get();

        BeanUtils.copyProperties(motionPicture, existingMotionPicture, getNullPropertyNames(motionPicture));
        MotionPicture savedMotionPicture = motionPictureRepo.save(existingMotionPicture);

        return ResponseEntity.ok(savedMotionPicture);
    }

    private String[] getNullPropertyNames(MotionPicture source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMotionPicture(@PathVariable long id) {
        Optional<MotionPicture> optionalMotionPicture = motionPictureRepo.findById(id);

        if (optionalMotionPicture.isEmpty()) {
            return ResponseEntity.notFound().build();

        }

        motionPictureRepo.deleteById(id);

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAllMotionPictures() {
        motionPictureRepo.deleteAll();

        return ResponseEntity.noContent().build();
    }
}