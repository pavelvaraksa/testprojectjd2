package com.noirix.repository;

import com.noirix.domain.User;

import java.util.List;

public interface UserRepository extends CrudRepository<Long, User> {

    List<User> search(String query);
}
