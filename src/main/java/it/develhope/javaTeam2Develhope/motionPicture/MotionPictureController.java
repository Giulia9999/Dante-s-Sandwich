package it.develhope.javaTeam2Develhope.motionPicture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MotionPictureController {

    @Autowired
    private MotionPictureRepo motionPictureRepo;

}