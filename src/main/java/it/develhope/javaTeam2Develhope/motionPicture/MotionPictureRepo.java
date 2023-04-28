package it.develhope.javaTeam2Develhope.motionPicture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotionPictureRepo extends JpaRepository<MotionPicture,Long> {

}
