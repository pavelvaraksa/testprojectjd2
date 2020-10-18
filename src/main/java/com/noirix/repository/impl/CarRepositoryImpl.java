package com.noirix.repository.impl;

import com.noirix.domain.Car;
import com.noirix.exception.EntityNotFoundException;
import com.noirix.repository.CarRepository;
import com.noirix.util.DatabasePropertiesReader;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.noirix.util.DatabasePropertiesReader.*;

public class CarRepositoryImpl implements CarRepository {

    public static final DatabasePropertiesReader reader = DatabasePropertiesReader.getInstance();

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

    private Car parseResultSet(ResultSet rs) throws SQLException {
        Car car = new Car();
        car.setId(rs.getLong(ID));
        car.setModel(rs.getString(MODEL));
        car.setYear(rs.getInt(YEAR));
        car.setColor(rs.getString(COLOR));
        car.setPrice(rs.getInt(PRICE));
        return car;
    }

    @Override
    public List<Car> search(String query) {
        return null;
    }

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
    public Car findById(Long key) {
        final String findByIdQuery = "select * from m_cars where id = ?";

        Connection connection;
        PreparedStatement statement;
        ResultSet rs;

        try {
            Class.forName(reader.getProperty(DATABASE_DRIVER_NAME));
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver Cannot be loaded!");
            throw new RuntimeException("JDBC Driver Cannot be loaded!");
        }

        try {
            connection = DriverManager.getConnection(reader.getProperty(DATABASE_URL),
                    reader.getProperty(DATABASE_LOGIN),
                    reader.getProperty(DATABASE_PASSWORD));
            statement = connection.prepareStatement(findByIdQuery);
            statement.setLong(1, key);

            rs = statement.executeQuery();

            if (rs.next()) {
                return parseResultSet(rs);
            } else {
                throw new EntityNotFoundException("Car with ID:" + key + "not found");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
    }

    @Override
    public Optional<Car> findOne(Long key) {
        return Optional.of(findById(key));
    }

    @Override
    public Long delete(Car car) {
        final String findByIdQuery = "delete from m_cars where id = ?";

        Connection connection;
        PreparedStatement statement;

        try {
            Class.forName(reader.getProperty(DATABASE_DRIVER_NAME));
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver Cannot be loaded!");
            throw new RuntimeException("JDBC Driver Cannot be loaded!");
        }

        try {
            connection = DriverManager.getConnection(reader.getProperty(DATABASE_URL),
                    reader.getProperty(DATABASE_LOGIN),
                    reader.getProperty(DATABASE_PASSWORD));
            statement = connection.prepareStatement(findByIdQuery);
            statement.setLong(1, car.getId());

            int deletedRows = statement.executeUpdate();
            return (long) deletedRows;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
    }

    @Override
    public Car update(Car car) {
        final String findByIdQuery = "update m_cars " +
                "set " +
                "model = ?,  " +
                "year = ?,  " +
                "color = ?,  " +
                "price = ?,  " +
                "where id = ?";

        Connection connection;
        PreparedStatement statement;

        try {
            Class.forName(reader.getProperty(DATABASE_DRIVER_NAME));
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver Cannot be loaded!");
            throw new RuntimeException("JDBC Driver Cannot be loaded!");
        }

        try {
            connection = DriverManager.getConnection(reader.getProperty(DATABASE_URL),
                    reader.getProperty(DATABASE_LOGIN),
                    reader.getProperty(DATABASE_PASSWORD));
            statement = connection.prepareStatement(findByIdQuery);
            statement.setString(1, car.getModel());
            statement.setInt(2, car.getYear());
            statement.setString(3,car.getColor());
            statement.setInt(4, car.getPrice());

            statement.executeUpdate();
            return findById(car.getId());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
    }

    @Override
    public Car save(Car car) {
        final String findByIdQuery = "insert into m_cars (model,year,color,price) " +
                "values (?,?,?,?)";

        Connection connection;
        PreparedStatement statement;

        try {
            Class.forName(reader.getProperty(DATABASE_DRIVER_NAME));
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver Cannot be loaded!");
            throw new RuntimeException("JDBC Driver Cannot be loaded!");
        }

        try {
            connection = DriverManager.getConnection(reader.getProperty(DATABASE_URL),
                                                     reader.getProperty(DATABASE_LOGIN),
                                                     reader.getProperty(DATABASE_PASSWORD));
            statement = connection.prepareStatement(findByIdQuery);
            PreparedStatement lastInsertId = connection.prepareStatement("SELECT currval('m_cars_id_seq') as last_insert_id;");

            statement.setString(1, car.getModel());
            statement.setInt(2, car.getYear());
            statement.setString(3, car.getColor());
            statement.setInt(4, car.getPrice());

            statement.executeUpdate();

            Long insertedId;
            ResultSet lastIdResultSet = lastInsertId.executeQuery();
            if (lastIdResultSet.next()) {
                insertedId = lastIdResultSet.getLong("last_insert_id");
            } else {
                throw new RuntimeException("We cannot read sequence last value during Car creation!");
            }

            return findById(insertedId);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
    }
}



