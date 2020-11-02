
package com.noirix.service;

import com.noirix.domain.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User save(User user);

    User update(User user);

    Long delete(User user);

    User findById(Long userId);

    List<User> search(String query);
}
