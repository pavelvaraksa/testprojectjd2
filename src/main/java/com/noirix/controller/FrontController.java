package com.noirix.controller;

import com.noirix.controller.command.Commands;
import com.noirix.domain.User;
import com.noirix.repository.UserRepository;
import com.noirix.repository.impl.UserRepositoryImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.noirix.controller.command.Commands.FIND_ALL;
import static com.noirix.controller.command.Commands.FIND_BY_ID;

public class FrontController extends HttpServlet {

    public static final UserRepository userRepository = new UserRepositoryImpl();

    public FrontController() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processGetRequests(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processPostRequests(req, resp);
    }

//    private void doRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        RequestDispatcher dispatcher = req.getRequestDispatcher("/hello");
//        if (dispatcher != null) {
//            System.out.println("Forward will be done!");
//
//            req.setAttribute("userName", userRepository.findAll().stream().map(User::getName).collect(Collectors.joining(",")));
//
//            dispatcher.forward(req, resp);
//        }
//    }

    private void processGetRequests(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/hello");
            if (dispatcher != null) {
                resolveGetRequestCommands(req);
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

    private void resolveGetRequestCommands(HttpServletRequest req) {
        Commands commandName = Commands.findByCommandName(req.getParameter("command"));

        //http://localhost:8080/test/FrontController?command=findAll&page=0&limit=10 (add offset to query)

        switch (commandName) {
            //     http://localhost:8080/test/FrontController?command=findAll
            case FIND_ALL:
                String page = req.getParameter("page");
                String limit = req.getParameter("limit");

                req.setAttribute("users", userRepository.findAll());
                break;
            //     http://localhost:8080/test/FrontController?command=findById&id=10
            case FIND_BY_ID:
                String id = req.getParameter("id");
                long userId = Long.parseLong(id);
                req.setAttribute("users", Collections.singletonList(userRepository.findById(userId)));
                break;
            default:
                break;

        }
    }

    private void processPostRequests(HttpServletRequest req, HttpServletResponse resp) {

    }
}
