package com.noirix.repository.impl;

import com.noirix.domain.Car;
import com.noirix.repository.CarRepository;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarRepositoryImpl implements CarRepository {

    public static final String POSTGRES_DRIVER_NAME = "org.postgresql.Driver";
    public static final String DATABASE_URL = "jdbc:postgresql://localhost:";
    public static final int DATABASE_PORT = 5432;
    public static final String DATABASE_NAME = "/webinar_database";
    public static final String DATABASE_LOGIN = "postgres";
    public static final String DATABASE_PASSWORD = "postgres";

    private static final String ID = "id";
    private static final String MODEL = "model";
    private static final String YEAR = "year";
    private static final String COLOR = "color";
    private static final String PRICE = "price";

    @Override
    public List<Car> findAll() {
        final String findAllQuery = "select * from m_cars order by price DESC";

        List<Car> result = new ArrayList<>();

        Connection connection;
        Statement statement;
        ResultSet rs;

        try {
            Class.forName(POSTGRES_DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver Cannot be loaded!");
            throw new RuntimeException("JDBC Driver Cannot be loaded!");
        }

        String jdbcURL = StringUtils.join(DATABASE_URL, DATABASE_PORT, DATABASE_NAME);

        try {
            connection = DriverManager.getConnection(jdbcURL, DATABASE_LOGIN, DATABASE_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery(findAllQuery);

            while (rs.next()) {
                Car car = new Car();
                car.setId(rs.getLong(ID));
                car.setModel(rs.getString(MODEL));
                car.setYear(rs.getInt(YEAR));
                car.setColor(rs.getString(COLOR));
                car.setPrice(rs.getInt(PRICE));

                result.add(car);
            }

            return result;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
    }

    @Override
    public List<Car> search(String query) {
        return null;
    }

    @Override
    public Car findById(Long key) {
        return null;
    }

    @Override
    public Optional<Car> findOne(Long key) {
        return Optional.empty();
    }

    @Override
    public Car save(Car object) {
        return null;
    }

    @Override
    public Car update(Car object) {
        return null;
    }

    @Override
    public Long delete(Car object) {
        return null;
    }
}
