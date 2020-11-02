package com.noirix.repository.impl;

import com.noirix.domain.Gender;
import com.noirix.domain.User;
import com.noirix.exception.EntityNotFoundException;
import com.noirix.repository.UserColumns;
import com.noirix.repository.UserRepository;
import com.noirix.util.DatabasePropertiesReader;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
public class UserRepositoryImpl implements UserRepository {

    private static final Logger log = Logger.getLogger(UserRepositoryImpl.class);

    public static final DatabasePropertiesReader reader = DatabasePropertiesReader.getInstance();

    private User parseResultSet(ResultSet rs) throws SQLException {
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
        return null;
    }

    @Override
    public User save(User user) {
        final String findByIdQuery = "insert into m_users (name, surname, birth_date, gender, created, changed, weight) " +
                "values (?,?,?,?,?,?,?)";

        Connection connection;
        PreparedStatement statement;

        try {
            Class.forName(reader.getProperty(DATABASE_DRIVER_NAME));
        } catch (ClassNotFoundException e) {
            log.error("JDBC Driver Cannot be loaded!");
            throw new RuntimeException("JDBC Driver Cannot be loaded!");
        }

        try {
            connection = DriverManager.getConnection(reader.getProperty(DATABASE_URL), reader.getProperty(DATABASE_LOGIN), reader.getProperty(DATABASE_PASSWORD));
            statement = connection.prepareStatement(findByIdQuery);
            PreparedStatement lastInsertId = connection.prepareStatement("select currval('m_users_id_seq') as last_insert_id;");

            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setDate(3, new Date(user.getBirthDate().getTime()));
            statement.setString(4, user.getGender().name());
            statement.setTimestamp(5, user.getCreated());
            statement.setTimestamp(6, user.getChanged());
            statement.setFloat(7, user.getWeight());

            statement.executeUpdate();

            Long insertedId;
            ResultSet lastIdResultSet = lastInsertId.executeQuery();
            if (lastIdResultSet.next()) {
                insertedId = lastIdResultSet.getLong("last_insert_id");
            } else {
                throw new RuntimeException("We cannot read sequence last value during User creation!");
            }

            return findById(insertedId);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
    }

    @Override
    public List<User> findAll() {
        final String findAllQuery = "select * from m_users order by id";

        List<User> result = new ArrayList<>();

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
                result.add(parseResultSet(rs));
            }

            return result;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
    }

    @Override
    public User findById(Long key) {
        final String findByIdQuery = "select * from m_users where id = ?";

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
            connection = DriverManager.getConnection(reader.getProperty(DATABASE_URL), reader.getProperty(DATABASE_LOGIN), reader.getProperty(DATABASE_PASSWORD));
            statement = connection.prepareStatement(findByIdQuery);
            statement.setLong(1, key);

            rs = statement.executeQuery();

            if (rs.next()) {
                return parseResultSet(rs);
            } else {
                throw new EntityNotFoundException("User with ID:" + key + "not found");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
    }

    @Override
    public Optional<User> findOne(Long key) {
        return Optional.of(findById(key));
    }

    @Override
    public User update(User user) {
        final String findByIdQuery = "update m_users " +
                "set " +
                "name = ?,  " +
                "surname = ?,  " +
                "birth_date = ?,  " +
                "gender = ?,  " +
                "created = ?,  " +
                "changed = ?,  " +
                "weight = ?  " +
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
            connection = DriverManager.getConnection(reader.getProperty(DATABASE_URL), reader.getProperty(DATABASE_LOGIN), reader.getProperty(DATABASE_PASSWORD));
            statement = connection.prepareStatement(findByIdQuery);
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setDate(3, new Date(user.getBirthDate().getTime()));
            statement.setString(4, user.getGender().name());
            statement.setTimestamp(5, user.getCreated());
            statement.setTimestamp(6, user.getChanged());
            statement.setFloat(7, user.getWeight());
            statement.setLong(8, user.getId());

            statement.executeUpdate();
            return findById(user.getId());
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
    }

    @Override
    public Long delete(User user) {
        final String findByIdQuery = "delete from m_users where id = ?";

        Connection connection;
        PreparedStatement statement;

        try {
            Class.forName(reader.getProperty(DATABASE_DRIVER_NAME));
        } catch (ClassNotFoundException e) {
            log.error("JDBC Driver Cannot be loaded!");
            throw new RuntimeException("JDBC Driver Cannot be loaded!");
        }

        try {
            connection = DriverManager.getConnection(reader.getProperty(DATABASE_URL), reader.getProperty(DATABASE_LOGIN), reader.getProperty(DATABASE_PASSWORD));
            statement = connection.prepareStatement(findByIdQuery);
            statement.setLong(1, user.getId());

            int deletedRows = statement.executeUpdate();
            return (long) deletedRows;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
    }
}
