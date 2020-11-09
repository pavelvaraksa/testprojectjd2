package com.noirix.repository.impl;

import com.noirix.domain.Gender;
import com.noirix.domain.User;
import com.noirix.repository.UserColumns;
import com.noirix.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Primary
public class UserRepositoryJdbcTemplateImpl implements UserRepository {

    private static final Logger log = Logger.getLogger(UserRepositoryJdbcTemplateImpl.class);

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserRepositoryJdbcTemplateImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private User getUserRowMapper(ResultSet rs, int i) throws SQLException {
        User user = new User();
        user.setId(rs.getLong(UserColumns.ID));
        user.setName(rs.getString(UserColumns.NAME));
        user.setSurname(rs.getString(UserColumns.SURNAME));
        user.setBirthDate(rs.getDate(UserColumns.BIRTH_DATE));
        user.setGender(Gender.valueOf(rs.getString(UserColumns.GENDER)));
        user.setCreated(rs.getTimestamp(UserColumns.CREATED));
        user.setChanged(rs.getTimestamp(UserColumns.CHANGED));
        user.setWeight(rs.getFloat(UserColumns.WEIGHT));
        return user;
    }

    @Override
    public List<User> search(String query) {
        log.info("invoking search method");
        log.info(query);
        return jdbcTemplate.query("select * from m_users where name like ?", new Object[]{query}, this::getUserRowMapper);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("select * from m_users", this::getUserRowMapper);
    }

    @Override
    public User findById(Long key) {
        return jdbcTemplate.queryForObject("select * from m_users where id = ?", new Object[]{key}, this::getUserRowMapper);
    }

    @Override
    public Optional<User> findOne(Long key) {
        return Optional.empty();
    }

    @Override
    public User save(User entity) {
        final String createQuery = "insert into m_users (name, surname, birth_date, gender, created, changed, weight) " +
                "values (:name, :surname, :birth_date, :gender, :created, :changed, :weight);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", entity.getName());
        params.addValue("surname", entity.getSurname());
        params.addValue("birth_date", entity.getBirthDate());
        params.addValue("gender", entity.getGender().name());
        params.addValue("created", entity.getCreated());
        params.addValue("changed", entity.getChanged());
        params.addValue("weight", entity.getWeight());

        namedParameterJdbcTemplate.update(createQuery, params, keyHolder, new String[]{"id"});

        long createdUserId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return findById(createdUserId);
    }

    @Override
    public User update(User entity) {
        final String updateQuery = "update m_users set " +
                "name = ?, " +
                "surname = ?, " +
                "birth_date = ?, " +
                "gender = ?, " +
                "created = ?, " +
                "changed = ?, " +
                "weight = ?) " +
                "where id = ?";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", entity.getName());
        params.addValue("surname", entity.getSurname());
        params.addValue("birthDate", new Date(entity.getBirthDate().getTime()));
        params.addValue("gender", entity.getGender());
        params.addValue("created", entity.getCreated());
        params.addValue("changed", entity.getChanged());
        params.addValue("weight", entity.getCreated());

        namedParameterJdbcTemplate.update(updateQuery, params);

        return findById(entity.getId());
    }

    @Override
    public Long delete(User entity) {
        final String deleteQuery = "delete from m_users where id = ?";

        return (long) jdbcTemplate.update(deleteQuery, entity.getId());

    }

    @Override
    public int deleteById(Long id) {
        final String deleteQuery = "delete from m_users where id = ?";

        return jdbcTemplate.update(deleteQuery, id);

    }
}
