// src/main/java/br/com/carstore/controller/HomeController.java
package br.com.carstore.controller;

import br.com.carstore.dto.CarDTO;
import br.com.carstore.service.CarServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
public class AdminController {

    private final CarServiceImpl service;

    public AdminController(CarServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/admin")
    public String index(Model model) {
        model.addAttribute("carDTO", new CarDTO());
        return "/admin/index";
    }

    @PreAuthorize("hasRole('ADMIN')") // <-- Bloqueia a criação/edição
    @PostMapping("/admin/cars")
    public String createCar(CarDTO carDTO, BindingResult result) {
        if (carDTO.getId() != null && !carDTO.getId().isEmpty()) {
            service.update(carDTO.getId(), carDTO);
        } else {
            service.save(carDTO);
        }
        return "redirect:/admin/cars";
    }

    @GetMapping("/admin/cars")
    public String getCars(Model model) {
        List<CarDTO> allCars = service.findAll();
        model.addAttribute("cars", allCars);
        return "/admin/dashboard";
    }

    @GetMapping("/admin/cars/edit")
    public String editCar(@RequestParam("id") String id, Model model) {

        CarDTO car = service.findById(id);

        if (car == null) {

            return "redirect:/admin/cars";

        }
        model.addAttribute("carDTO", car);

        return "admin/index";

    }

    @PreAuthorize("hasRole('ADMIN')") // <-- Bloqueia a exclusão
    @PostMapping("/admin/cars/delete")
    public String deleteCar(@RequestParam("id") String id, Model model) {

        service.deleteById(id);

        List<CarDTO> cars = service.findAll();
        model.addAttribute("cars", cars);

        return "redirect:/admin/cars";

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/stats")
    public ResponseEntity<?> stats() { return ResponseEntity.ok().build(); }

}
