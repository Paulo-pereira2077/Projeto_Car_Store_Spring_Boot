package br.com.carstore.dto;

import java.util.List;

public class CarResponseBody {


    private List<CarDTO> cars;

    public CarResponseBody(List<CarDTO> allCars) {
        this.cars = allCars;
    }

    public List<CarDTO> getCar() {
        return cars;
    }

    public void setCar(CarDTO car) {
        this.cars = cars;
    }
}
