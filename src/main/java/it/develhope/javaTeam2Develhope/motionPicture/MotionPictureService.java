package it.develhope.javaTeam2Develhope.motionPicture;

import io.micrometer.common.util.StringUtils;
import it.develhope.javaTeam2Develhope.game.Game;
import org.springframework.stereotype.Service;
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


import java.util.*;

@Service
public class MotionPictureService {
    @Autowired
    private MotionPictureRepo motionPictureRepo;

    public Page<MotionPicture> getAllMotionPictures(int page, int size, String title, String topic, String producer, Integer year) {
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
        return motionPictureRepo.findAll(spec, paging);
    }

    public MotionPicture getMotionPictureById(long id) {
        Optional<MotionPicture> optionalMotionPicture = motionPictureRepo.findById(id);

        if (optionalMotionPicture.isEmpty()) {
            throw new NoSuchElementException("Motion picture not found with id " + id);
        }

        return optionalMotionPicture.get();
    }

    public List<MotionPicture> addMultipleMotionPictures(List<MotionPicture> motionPictures) {
        return motionPictureRepo.saveAllAndFlush(motionPictures);
    }

    public MotionPicture addSingleMotionPicture(MotionPicture motionPicture) {
        return motionPictureRepo.saveAndFlush(motionPicture);
    }

    public MotionPicture updateMotionPicture(Long id, MotionPicture motionPicture) {
        Optional<MotionPicture> optionalMotionPicture = motionPictureRepo.findById(id);

        if (optionalMotionPicture.isEmpty()) {
            throw new NoSuchElementException("Motion picture not found with id " + id);
        }

        motionPicture.setId(id);

        return motionPictureRepo.save(motionPicture);
    }

    public MotionPicture patchMotionPicture(long id, MotionPicture motionPicture) {
        Optional<MotionPicture> optionalMotionPicture = motionPictureRepo.findById(id);

        if (optionalMotionPicture.isEmpty()) {
            throw new NoSuchElementException("Motion picture not found with id " + id);
        }

        MotionPicture existingMotionPicture = optionalMotionPicture.get();

        BeanUtils.copyProperties(motionPicture, existingMotionPicture, getEmptyPropertyNames(motionPicture));
        return motionPictureRepo.save(existingMotionPicture);
    }
    private String[] getEmptyPropertyNames(MotionPicture source) {
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

    public void deleteMotionPicture(long id) {
        Optional<MotionPicture> optionalMotionPicture = motionPictureRepo.findById(id);

        if (optionalMotionPicture.isEmpty()) {
            throw new NoSuchElementException("Motion picture not found with id " + id);
        }

        motionPictureRepo.deleteById(id);
    }

    public void deleteAllMotionPictures() {
        motionPictureRepo.deleteAll();
    }


}
