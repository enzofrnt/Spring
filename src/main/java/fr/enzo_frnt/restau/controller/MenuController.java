package fr.enzo_frnt.restau.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import fr.enzo_frnt.restau.model.Menu;
import fr.enzo_frnt.restau.model.Plat;
import fr.enzo_frnt.restau.repository.MenuRepository;
import fr.enzo_frnt.restau.repository.PlatRepository;
import fr.enzo_frnt.restau.repository.CategorieRepository;
import java.util.List;

@Controller
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;
    
    @Autowired
    private PlatRepository platRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @GetMapping
    public String listeMenus(
            Model model,
            @RequestParam(name = "nom", defaultValue = "") String nom,
            @RequestParam(name = "minPrix", required = false) Double minPrix,
            @RequestParam(name = "maxPrix", required = false) Double maxPrix,
            @RequestParam(name = "minCalories", required = false) Integer minCalories,
            @RequestParam(name = "maxCalories", required = false) Integer maxCalories,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Menu> menus;
        
        if (!nom.isEmpty()) {
            menus = menuRepository.findByNomLike("%" + nom + "%", PageRequest.of(page, size));
        } else if (minPrix != null || maxPrix != null) {
            double min = minPrix != null ? minPrix : 0;
            double max = maxPrix != null ? maxPrix : Double.MAX_VALUE;
            menus = menuRepository.findByPrixBetween(min, max, PageRequest.of(page, size));
        } else if (minCalories != null || maxCalories != null) {
            int min = minCalories != null ? minCalories : 0;
            int max = maxCalories != null ? maxCalories : Integer.MAX_VALUE;
            menus = menuRepository.findByTotalCaloriesBetween(min, max, PageRequest.of(page, size));
        } else {
            menus = menuRepository.findAll(PageRequest.of(page, size));
        }

        model.addAttribute("menus", menus.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", menus.getTotalPages());
        model.addAttribute("totalItems", menus.getTotalElements());
        model.addAttribute("nom", nom);
        model.addAttribute("minPrix", minPrix);
        model.addAttribute("maxPrix", maxPrix);
        model.addAttribute("minCalories", minCalories);
        model.addAttribute("maxCalories", maxCalories);
        
        return "menus";
    }

    @GetMapping("/create")
    public String showCreateForm(
            Model model,
            @RequestParam(required = false) Long id,
            @RequestParam(name = "searchPlat", defaultValue = "") String searchPlat,
            @RequestParam(name = "filterCategorie", required = false) Long filterCategorieId,
            @RequestParam(name = "minCalories", required = false) Integer minCalories,
            @RequestParam(name = "maxCalories", required = false) Integer maxCalories) {
        
        Menu menu = id != null ? menuRepository.findById(id).orElse(new Menu()) : new Menu();
        model.addAttribute("menu", menu);
        
        List<Plat> plats;
        if (!searchPlat.isEmpty()) {
            plats = platRepository.findByNomContaining(searchPlat);
        } else if (filterCategorieId != null) {
            plats = platRepository.findByCategorieId(filterCategorieId);
        } else if (minCalories != null || maxCalories != null) {
            int min = minCalories != null ? minCalories : 0;
            int max = maxCalories != null ? maxCalories : Integer.MAX_VALUE;
            plats = platRepository.findByNbCaloriesBetweenList(min, max);
        } else {
            plats = platRepository.findAll();
        }
        
        model.addAttribute("plats", plats);
        model.addAttribute("categories", categorieRepository.findAll());
        model.addAttribute("searchPlat", searchPlat);
        model.addAttribute("filterCategorieId", filterCategorieId);
        model.addAttribute("minCalories", minCalories);
        model.addAttribute("maxCalories", maxCalories);
        
        return "menu-form";
    }

    @PostMapping("/create")
    public String createMenu(@ModelAttribute Menu menu) {
        menuRepository.save(menu);
        return "redirect:/menus";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Menu menu = menuRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid menu Id:" + id));
        model.addAttribute("menu", menu);
        model.addAttribute("plats", platRepository.findAll());
        model.addAttribute("categories", categorieRepository.findAll());
        return "menu-form";
    }

    @PostMapping("/edit/{id}")
    public String updateMenu(@PathVariable Long id, @ModelAttribute Menu menu) {
        menuRepository.save(menu);
        return "redirect:/menus";
    }

    @GetMapping("/delete/{id}")
    public String deleteMenu(@PathVariable Long id) {
        menuRepository.deleteById(id);
        return "redirect:/menus";
    }
} 