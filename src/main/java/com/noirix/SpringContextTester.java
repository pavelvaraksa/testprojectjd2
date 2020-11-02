package com.noirix;

import com.noirix.domain.Gender;
import com.noirix.domain.User;
import com.noirix.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SpringContextTester {
    private static final Logger log = Logger.getLogger(SpringContextTester.class);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext("com.noirix");

        UserService userService = annotationConfigApplicationContext.getBean(UserService.class);

        log.info(userService.findAll().stream().map(User::getName).collect(Collectors.joining(", ")));

        log.info(userService.findById(1L).toString());

        log.info(userService.search("Evgeny").toString());

        List<User> testCreate = userService.search("Alex");

        for (User user : testCreate) {
            log.info(user.toString());
        }

        User userForSave =
                User.builder()
                        .name("Evgeny")
                        .surname("Volkov")
                        .birthDate(new Date())
                        .created(new Timestamp(new Date().getTime()))
                        .changed(new Timestamp(new Date().getTime()))
                        .gender(Gender.MALE)
                        .weight(80f)
                        .build();

        log.info(userService.save(userForSave).toString());
    }
}
