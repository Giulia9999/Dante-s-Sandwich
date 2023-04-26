package it.develhope.javaTeam2Develhope.motionPictures;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotionPicturesRepo extends JpaRepository<MotionPictures,Long> {

}
