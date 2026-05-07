package br.com.carstore.runner;

import br.com.carstore.dao.CarDao;
import br.com.carstore.dto.CarDTO;
import org.springframework.boot.CommandLineRunner;

public class StartupRunner implements CommandLineRunner {

    private final CarDao carDao;

    public StartupRunner(CarDao carDao) {

        this.carDao = carDao;

    }

    @Override
    public void run(String... args) throws Exception {

        CarDTO carOne = new CarDTO();
        carOne.setName("Gol");
        carOne.setColor("Branco");
        carOne.setBrand("Volkswagen");
        carDao.save(carOne);

        CarDTO carTwo= new CarDTO();
        carTwo.setName("Civic");
        carTwo.setColor("Civic");
        carTwo.setBrand("Honda");
        carDao.save(carTwo);

        System.out.println(carDao.findAll());

    }

}
