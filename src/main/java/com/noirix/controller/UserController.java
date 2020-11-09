package com.noirix.controller;

import com.noirix.controller.request.SearchCriteria;
import com.noirix.controller.request.UserCreateRequest;
import com.noirix.domain.User;
import com.noirix.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;

    public static final String USER_PAGE = "users";
    public static final String USERS_LIST_ATTRIBUTE = "users";

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    }

    @GetMapping
    public ModelAndView getAllUsers() {
        ModelAndView result = new ModelAndView();

        result.setViewName(USER_PAGE);
        result.addObject(USERS_LIST_ATTRIBUTE, userService.findAll());

        return result;
    }

    @GetMapping("/create")
    public ModelAndView getUserCreateRequest() {
        ModelAndView result = new ModelAndView();

        result.setViewName("createuser");
        result.addObject("userCreateRequest", new UserCreateRequest());

        return result;
    }

    @GetMapping(value = "/search")
    public ModelAndView search(@ModelAttribute SearchCriteria criteria) {

        ModelAndView result = new ModelAndView();

        result.setViewName(USER_PAGE);
        result.addObject(USERS_LIST_ATTRIBUTE, userService.search(criteria.getQuery()).stream().limit(criteria.getLimit()).collect(Collectors.toList()));

        return result;
    }

    @GetMapping(value = "/{id}")
    public ModelAndView search(@PathVariable("id") Long userId) {
        ModelAndView result = new ModelAndView();

        result.setViewName(USER_PAGE);
        result.addObject(USERS_LIST_ATTRIBUTE, userService.findAll());

        return result;
    }

    @PostMapping
    public ModelAndView createUser(@ModelAttribute UserCreateRequest userCreateRequest) {
        User user = new User();
        user.setGender(userCreateRequest.getGender());
        user.setName(userCreateRequest.getName());
        user.setSurname(userCreateRequest.getSurname());
        user.setBirthDate(userCreateRequest.getBirthDate());
        user.setWeight(userCreateRequest.getWeight());
        userService.save(user);

        ModelAndView result = new ModelAndView();

        result.setViewName(USER_PAGE);
        result.addObject(USERS_LIST_ATTRIBUTE, Collections.singletonList(userService.findAll()));

        return result;
    }

    @DeleteMapping(value = "/{id}")
    public ModelAndView deleteUser(@PathVariable("id") Long userId) {
        ModelAndView result = new ModelAndView();

        result.setViewName(USER_PAGE);
        result.addObject(USERS_LIST_ATTRIBUTE, userService.deleteById(userId));

        return result;
    }
}

