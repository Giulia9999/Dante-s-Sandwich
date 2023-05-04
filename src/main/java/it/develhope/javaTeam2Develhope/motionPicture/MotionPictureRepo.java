package it.develhope.javaTeam2Develhope.motionPicture;

import it.develhope.javaTeam2Develhope.game.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotionPictureRepo extends JpaRepository<MotionPicture,Long> {
    Page<MotionPicture> findAll(Specification<MotionPicture> spec, Pageable paging);

}
