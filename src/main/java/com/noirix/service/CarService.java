
package com.noirix.service;

import com.noirix.domain.Car;

import java.util.List;

public interface CarService {

    List<Car> findAll();

    Car save(Car car);

    Car update(Car car);

    Long delete(Car car);

    int deleteById(Long id);

    Car findById(Long carId);

    List<Car> search(String query);
}
