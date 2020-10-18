package com.noirix.controller;

import com.google.gson.Gson;
import com.noirix.controller.command.Commands;
import com.noirix.domain.User;
import com.noirix.repository.UserRepository;
import com.noirix.repository.impl.UserRepositoryImpl;
import org.apache.commons.io.IOUtils;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.stream.Collectors;

public class UserFrontController extends HttpServlet {

    public static final UserRepository userRepository = new UserRepositoryImpl();

    public UserFrontController() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processGetRequests(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processGetRequests(req, resp);
    }

    private void doRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/user");
        if (dispatcher != null) {
            System.out.println("Forward will be done!");

            req.setAttribute("userName", userRepository.findAll().stream().map(User::getName).collect(Collectors.joining(",")));

            dispatcher.forward(req, resp);
        }
    }

    private void processGetRequests(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Commands commandName = Commands.findByCommandName(req.getParameter("command"));
        try {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/user");
            if (dispatcher != null) {
                resolveGetRequestCommands(req, commandName);
                dispatcher.forward(req, resp);
            }
        } catch (Exception e) {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/error");
            if (dispatcher != null) {
                req.setAttribute("trace", e.getMessage());
                dispatcher.forward(req, resp);
            }
        }
    }

    private void resolveGetRequestCommands(HttpServletRequest req, Commands commandName) {

        //http://localhost:8080/UserFrontController?command=findAll&page=0&limit=10 (add offset to query)

        switch (commandName) {
                 //http://localhost:8080/UserFrontController?command=findAll
            case FIND_ALL:
                String page = req.getParameter("page");
                String limit = req.getParameter("limit");

                req.setAttribute("users", userRepository.findAll());
                break;
            //     http://localhost:8080/UserFrontController?command=findById&id=10
            case FIND_BY_ID:
                String id = req.getParameter("id");
                long userId = Long.parseLong(id);
                req.setAttribute("users", Collections.singletonList(userRepository.findById(userId)));
                req.setAttribute("singleUser", userRepository.findById(userId));
                break;
            default:
                break;
        }
    }

    private void processPostRequests(HttpServletRequest req, HttpServletResponse resp) {
        Commands commandName = Commands.findByCommandName(req.getParameter("command"));
        try {
            switch (commandName) {
                case CREATE:
                    String body = IOUtils.toString(req.getInputStream(), Charset.defaultCharset());
                    User user = new Gson().fromJson(body, User.class);
                    req.setAttribute("users", Collections.singletonList(userRepository.save(user)));
                    break;
                case UPDATE:
                    String updateBody = IOUtils.toString(req.getInputStream(), Charset.defaultCharset());
                    User updateUser = new Gson().fromJson(updateBody, User.class);
                    req.setAttribute("users", Collections.singletonList(userRepository.update(updateUser)));
                    break;
                case DELETE:
                    String id = req.getParameter("id");
                    long userId = Long.parseLong(id);
                    userRepository.delete(userRepository.findById(userId));

                    req.setAttribute("users", userRepository.findAll());
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
