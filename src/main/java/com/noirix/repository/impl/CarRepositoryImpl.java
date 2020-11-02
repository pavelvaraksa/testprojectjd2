package com.noirix.repository.impl;

import com.noirix.domain.Car;
import com.noirix.exception.EntityNotFoundException;
import com.noirix.repository.CarColumns;
import com.noirix.repository.CarRepository;
import com.noirix.util.DatabasePropertiesReader;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.noirix.util.DatabasePropertiesReader.DATABASE_DRIVER_NAME;
import static com.noirix.util.DatabasePropertiesReader.DATABASE_LOGIN;
import static com.noirix.util.DatabasePropertiesReader.DATABASE_PASSWORD;
import static com.noirix.util.DatabasePropertiesReader.DATABASE_URL;


@Repository
public class CarRepositoryImpl implements CarRepository {

    private static final Logger log = Logger.getLogger(UserRepositoryImpl.class);

    public static final DatabasePropertiesReader reader = DatabasePropertiesReader.getInstance();


    private Car parseResultSet(ResultSet rs) throws SQLException {
        Car car = new Car();
        car.setId(rs.getLong(CarColumns.ID));
        car.setModel(rs.getString(CarColumns.MODEL));
        car.setYear(rs.getInt(CarColumns.YEAR));
        car.setColor(rs.getString(CarColumns.COLOR));
        car.setPrice(rs.getInt(CarColumns.PRICE));
        car.setUserId(rs.getLong(CarColumns.USER_ID));
        return car;
    }

    @Override
    public List<Car> search(String query) {
        return null;
    }

    @Override
    public Car save(Car car) {
        final String findByIdQuery = "insert into m_cars (model,year,color,price,user_id) " +
                "values (?,?,?,?,?)";

        Connection connection;
        PreparedStatement statement;

        try {
            Class.forName(reader.getProperty(DATABASE_DRIVER_NAME));
        } catch (ClassNotFoundException e) {
            log.error("JDBC Driver Cannot be loaded!");
            throw new RuntimeException("JDBC Driver Cannot be loaded!");
        }

        try {
            connection = DriverManager.getConnection(reader.getProperty(DATABASE_URL),
                    reader.getProperty(DATABASE_LOGIN),
                    reader.getProperty(DATABASE_PASSWORD));
            statement = connection.prepareStatement(findByIdQuery);
            PreparedStatement lastInsertId = connection.prepareStatement("select currval('m_cars_id_seq') as last_insert_id;");

            statement.setString(1, car.getModel());
            statement.setInt(2, car.getYear());
            statement.setString(3, car.getColor());
            statement.setInt(4, car.getPrice());
            statement.setLong(5, car.getUserId());

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
            log.error(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
    }

    @Override
    public List<Car> findAll() {
        final String findAllQuery = "select * from m_cars order by price desc";

        List<Car> result = new ArrayList<>();

        Connection connection;
        Statement statement;
        ResultSet rs;

        try {
            Class.forName(reader.getProperty(DATABASE_DRIVER_NAME));
        } catch (ClassNotFoundException e) {
            log.error("JDBC Driver Cannot be loaded!");
            throw new RuntimeException("JDBC Driver Cannot be loaded!");
        }

        try {
            connection = DriverManager.getConnection(reader.getProperty(DATABASE_URL), reader.getProperty(DATABASE_LOGIN), reader.getProperty(DATABASE_PASSWORD));
            statement = connection.createStatement();
            rs = statement.executeQuery(findAllQuery);

            while (rs.next()) {
                Car car = new Car();
                car.setId(rs.getLong(CarColumns.ID));
                car.setModel(rs.getString(CarColumns.MODEL));
                car.setYear(rs.getInt(CarColumns.YEAR));
                car.setColor(rs.getString(CarColumns.COLOR));
                car.setPrice(rs.getInt(CarColumns.PRICE));
                car.setUserId(rs.getLong(CarColumns.USER_ID));

                result.add(car);
            }

            return result;
        } catch (SQLException e) {
            log.error(e.getMessage());
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
            log.error("JDBC Driver Cannot be loaded!");
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
                throw new EntityNotFoundException("Car with ID:" + key + " not found");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
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
            log.error("JDBC Driver Cannot be loaded!");
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
            log.error(e.getMessage());
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
                "user_id = ?, " +
                "where id = ?";

        Connection connection;
        PreparedStatement statement;

        try {
            Class.forName(reader.getProperty(DATABASE_DRIVER_NAME));
        } catch (ClassNotFoundException e) {
            log.error("JDBC Driver Cannot be loaded!");
            throw new RuntimeException("JDBC Driver Cannot be loaded!");
        }

        try {
            connection = DriverManager.getConnection(reader.getProperty(DATABASE_URL),
                    reader.getProperty(DATABASE_LOGIN),
                    reader.getProperty(DATABASE_PASSWORD));
            statement = connection.prepareStatement(findByIdQuery);
            statement.setString(1, car.getModel());
            statement.setInt(2, car.getYear());
            statement.setString(3, car.getColor());
            statement.setInt(4, car.getPrice());
            statement.setLong(5, car.getUserId());

            statement.executeUpdate();
            return findById(car.getId());
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
    }
}





