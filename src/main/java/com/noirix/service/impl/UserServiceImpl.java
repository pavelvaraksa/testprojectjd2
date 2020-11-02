package com.noirix.service.impl;

import com.noirix.domain.User;
import com.noirix.repository.UserRepository;
import com.noirix.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.update(user);
    }
    @Override
    public Long delete(User user) {
        return userRepository.delete(user);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> search(String query) {
        return userRepository.search(query);
    }
}