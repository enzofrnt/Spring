package fr.enzo_frnt.restau.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import fr.enzo_frnt.restau.model.Plat;
import fr.enzo_frnt.restau.repository.PlatRepository;
import fr.enzo_frnt.restau.repository.CategorieRepository;

@Controller
@RequestMapping("/plats")
public class PlatController {

    @Autowired
    private PlatRepository platRepository;
    
    @Autowired
    private CategorieRepository categorieRepository;

    @GetMapping
    public String listePlats(
            Model model,
            @RequestParam(name = "nom", defaultValue = "") String nom,
            @RequestParam(name = "categorie", required = false) Long categorieId,
            @RequestParam(name = "minCalories", required = false) Integer minCalories,
            @RequestParam(name = "maxCalories", required = false) Integer maxCalories,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Plat> plats;
        
        if (!nom.isEmpty()) {
            plats = platRepository.findByNomLike("%" + nom + "%", PageRequest.of(page, size));
        } else if (categorieId != null) {
            plats = platRepository.findByCategorie(categorieId, PageRequest.of(page, size));
        } else if (minCalories != null || maxCalories != null) {
            int min = minCalories != null ? minCalories : 0;
            int max = maxCalories != null ? maxCalories : Integer.MAX_VALUE;
            plats = platRepository.findByNbCaloriesBetween(min, max, PageRequest.of(page, size));
        } else {
            plats = platRepository.findAll(PageRequest.of(page, size));
        }

        model.addAttribute("plats", plats.getContent());
        model.addAttribute("categories", categorieRepository.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", plats.getTotalPages());
        model.addAttribute("totalItems", plats.getTotalElements());
        model.addAttribute("nom", nom);
        model.addAttribute("categorieId", categorieId);
        model.addAttribute("minCalories", minCalories);
        model.addAttribute("maxCalories", maxCalories);
        
        return "plats";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("plat", new Plat());
        model.addAttribute("categories", categorieRepository.findAll());
        return "plat-form";
    }

    @PostMapping("/create")
    public String createPlat(@ModelAttribute Plat plat) {
        platRepository.save(plat);
        return "redirect:/plats";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Plat plat = platRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid plat Id:" + id));
        model.addAttribute("plat", plat);
        model.addAttribute("categories", categorieRepository.findAll());
        return "plat-form";
    }

    @PostMapping("/edit/{id}")
    public String updatePlat(@PathVariable Long id, @ModelAttribute Plat plat) {
        platRepository.save(plat);
        return "redirect:/plats";
    }

    @GetMapping("/delete/{id}")
    public String deletePlat(@PathVariable Long id) {
        platRepository.deleteById(id);
        return "redirect:/plats";
    }
} 