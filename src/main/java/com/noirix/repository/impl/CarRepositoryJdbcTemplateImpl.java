package com.noirix.repository.impl;

import com.noirix.domain.Car;
import com.noirix.repository.CarColumns;
import com.noirix.repository.CarRepository;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Primary
public class CarRepositoryJdbcTemplateImpl implements CarRepository {

    private static final Logger log = Logger.getLogger(UserRepositoryJdbcTemplateImpl.class);

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CarRepositoryJdbcTemplateImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private Car getCarRowMapper(ResultSet rs, int i) throws SQLException {
        Car car = new Car();
        car.setId(rs.getLong(CarColumns.ID));
        car.setModel(rs.getString(CarColumns.MODEL));
        car.setYear(rs.getInt(CarColumns.YEAR));
        car.setColor(rs.getString(CarColumns.COLOR));
        car.setPrice(rs.getInt(rs.getString(CarColumns.PRICE)));
        car.setUserId(rs.getLong(CarColumns.USER_ID));
        return car;
    }

    @Override
    public List<Car> search(String query) {
        log.info("invoking search method");
        log.info(query);
        return jdbcTemplate.query("select * from m_cars where model like ?", new Object[]{query}, this::getCarRowMapper);
    }

    @Override
    public List<Car> findAll() {
        return jdbcTemplate.query("select * from m_cars", this::getCarRowMapper);
    }

    @Override
    public Car findById(Long key) {

        return jdbcTemplate.queryForObject("select * from m_cars where id = ?",
                new Object[]{key},
                this::getCarRowMapper);
    }

    @Override
    public Optional<Car> findOne(Long key) {
        return Optional.empty();
    }

    @Override
    public Car save(Car entity) {
        final String createQuery = "insert into m_cars (model, year, color, price, user_id) " +
                "values (:model, :year, :color, :price, :user_id);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("model", entity.getModel());
        params.addValue("year", entity.getYear());
        params.addValue("color", entity.getColor());
        params.addValue("price", entity.getPrice());
        params.addValue("user_id", entity.getUserId());

        namedParameterJdbcTemplate.update(createQuery, params, keyHolder, new String[]{"id"});

        long createdCarId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return findById(createdCarId);
    }

    @Override
    public Car update(Car entity) {
        final String updateQuery = "update m_cars set " +
                "model = ?, " +
                "year = ?, " +
                "color = ?, " +
                "price = ?, " +
                "user_id = ?) " +
                "where id = ?";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("model", entity.getModel());
        params.addValue("year", entity.getYear());
        params.addValue("color", entity.getColor());
        params.addValue("price", entity.getPrice());
        params.addValue("user_id", entity.getUserId());

        namedParameterJdbcTemplate.update(updateQuery, params);

        return findById(entity.getId());
    }

    @Override
    public Long delete(Car entity) {
        final String deleteQuery = "delete from m_cars where id = ?";

        return (long) jdbcTemplate.update(deleteQuery);
    }
}