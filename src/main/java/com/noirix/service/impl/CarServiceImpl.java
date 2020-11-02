package com.noirix.service.impl;

import com.noirix.domain.Car;
import com.noirix.repository.CarRepository;
import com.noirix.service.CarService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public Car save(Car car) {
        return carRepository.save(car);
    }

    @Override
    public Car update(Car car) {
        return carRepository.update(car);
    }

    @Override
    public Long delete(Car car) {
        return carRepository.delete(car);
    }

    @Override
    public Car findById(Long carId) {
        return carRepository.findById(carId);
    }

    @Override
    public List<Car> search(String query) {
        return carRepository.search(query);
    }
}