// src/main/java/br/com/carstore/controller/HomeController.java
package br.com.carstore.carstore.controller;

import br.com.carstore.carstore.dto.CarDTO;
import br.com.carstore.carstore.service.CarServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    private final CarServiceImpl service;

    public HomeController(CarServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("carDTO", new CarDTO());
        return "index";
    }

    @PostMapping("/cars")
    public String createCar(CarDTO carDTO, BindingResult result) {
        service.save(carDTO);
        return "redirect:/cars";
    }

    @GetMapping("/cars")
    public String getCars(Model model) {
        List<CarDTO> allCars = service.findAll();
        model.addAttribute("cars", allCars);
        return "dashboard";
    }

    @PutMapping("/cars/{id}")
    public String updateCar(@PathVariable String id, CarDTO carDTO){
        service.update(id, carDTO);
        return "redirect:/cars";
    }

    @DeleteMapping("/cars/{id}")
    public String deleteCar(@PathVariable String id){
        service.deleteById(id);
        return "redirect:/cars";
    }
}
