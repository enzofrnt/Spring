package fr.enzo_frnt.restau.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import fr.enzo_frnt.restau.model.Menu;
import fr.enzo_frnt.restau.model.Plat;
import fr.enzo_frnt.restau.repository.MenuRepository;
import fr.enzo_frnt.restau.repository.PlatRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Expression;
import fr.enzo_frnt.restau.repository.CategorieRepository;
import java.util.List;
import org.springframework.web.bind.WebDataBinder;
import java.beans.PropertyEditorSupport;
import java.util.Set;
import java.util.HashSet;

/**
 * Contrôleur gérant les opérations CRUD et la recherche pour les menus du restaurant.
 * Permet la gestion complète des menus incluant la création, modification, suppression et visualisation,
 * ainsi que des fonctionnalités de recherche et de filtrage avancées.
 */
@Controller
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;
    
    @Autowired
    private PlatRepository platRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    /**
     * Affiche la liste des menus avec options de filtrage et pagination.
     *
     * @param model Modèle Spring pour transmettre les données à la vue
     * @param nom Filtre sur le nom du menu (optionnel)
     * @param minPrix Prix minimum pour le filtrage (optionnel)
     * @param maxPrix Prix maximum pour le filtrage (optionnel)
     * @param minCalories Calories minimales pour le filtrage (optionnel)
     * @param maxCalories Calories maximales pour le filtrage (optionnel)
     * @param page Numéro de la page courante (défaut: 0)
     * @param size Nombre d'éléments par page (défaut: 10)
     * @param sort Champ de tri (défaut: "nom")
     * @param direction Direction du tri (défaut: "asc")
     * @return Vue "menus" avec la liste filtrée et paginée
     */
    @GetMapping
    public String listeMenus(
            Model model,
            @RequestParam(name = "nom", defaultValue = "") String nom,
            @RequestParam(name = "minPrix", required = false) Double minPrix,
            @RequestParam(name = "maxPrix", required = false) Double maxPrix,
            @RequestParam(name = "minCalories", required = false) Integer minCalories,
            @RequestParam(name = "maxCalories", required = false) Integer maxCalories,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nom") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Menu> menus;
        
        if (sort.equals("totalCalories")) {
            if (direction.equals("asc")) {
                menus = menuRepository.findAllTotalCaloriesAsc(pageRequest);
            } else {
                menus = menuRepository.findAllTotalCaloriesDesc(pageRequest);
            }
        } else {
            Sort.Direction dir = Sort.Direction.fromString(direction);
            pageRequest = PageRequest.of(page, size, Sort.by(dir, sort));
            menus = menuRepository.findAll(createSpecification(nom, minPrix, maxPrix, minCalories, maxCalories), pageRequest);
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
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        
        return "menus";
    }

    /**
     * Crée une spécification JPA pour le filtrage des menus.
     * 
     * @param nom Filtre sur le nom
     * @param minPrix Prix minimum
     * @param maxPrix Prix maximum
     * @param minCalories Calories minimales
     * @param maxCalories Calories maximales
     * @return Specification<Menu> pour le filtrage
     */
    private Specification<Menu> createSpecification(String nom, Double minPrix, Double maxPrix, 
                                                     Integer minCalories, Integer maxCalories) {
        Specification<Menu> spec = Specification.where(null);
        
        if (!nom.isEmpty()) {
            spec = spec.and((root, query, cb) -> 
                cb.like(cb.lower(root.get("nom")), "%" + nom.toLowerCase() + "%"));
        }
        
        if (minPrix != null) {
            spec = spec.and((root, query, cb) -> 
                cb.greaterThanOrEqualTo(root.get("prix"), minPrix));
        }
        
        if (maxPrix != null) {
            spec = spec.and((root, query, cb) -> 
                cb.lessThanOrEqualTo(root.get("prix"), maxPrix));
        }
        
        if (minCalories != null || maxCalories != null) {
            int min = minCalories != null ? minCalories : 0;
            int max = maxCalories != null ? maxCalories : Integer.MAX_VALUE;
            spec = spec.and((root, query, cb) -> {
                Join<Menu, Plat> platsJoin = root.join("plats");
                Expression<Long> totalCalories = cb.sum(platsJoin.get("nbCalories"));
                query.groupBy(root.get("id"));
                query.having(cb.and(
                    cb.greaterThanOrEqualTo(totalCalories, cb.literal((long) min)),
                    cb.lessThanOrEqualTo(totalCalories, cb.literal((long) max))
                ));
                return cb.conjunction();
            });
        }
        
        return spec;
    }

    /**
     * Affiche le formulaire de création/modification d'un menu.
     *
     * @param model Modèle Spring
     * @param id ID du menu à modifier (optionnel)
     * @param searchPlat Recherche de plats par nom (optionnel)
     * @param filterCategorieId Filtre par catégorie (optionnel)
     * @param minCalories Calories minimales (optionnel)
     * @param maxCalories Calories maximales (optionnel)
     * @return Vue "menu-form"
     */
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

    /**
     * Traite la création d'un nouveau menu.
     *
     * @param menu Menu à créer
     * @param platsIds Liste des IDs des plats à associer
     * @return Redirection vers la liste des menus
     */
    @PostMapping("/create")
    public String createMenu(@ModelAttribute Menu menu,
                             @RequestParam(value = "platsIds", required = false) List<Long> platsIds) {
        if (platsIds != null) {
            Set<Plat> plats = new HashSet<>();
            for (Long id : platsIds) {
                platRepository.findById(id).ifPresent(plats::add);
            }
            menu.setPlats(plats);
        }
        menuRepository.save(menu);
        return "redirect:/menus";
    }

    /**
     * Affiche le formulaire de modification d'un menu existant.
     *
     * @param id ID du menu à modifier
     * @param model Modèle Spring
     * @return Vue "menu-form"
     * @throws IllegalArgumentException si l'ID du menu est invalide
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Menu menu = menuRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid menu Id:" + id));
        model.addAttribute("menu", menu);
        model.addAttribute("plats", platRepository.findAll());
        model.addAttribute("categories", categorieRepository.findAll());
        return "menu-form";
    }

    /**
     * Traite la modification d'un menu existant.
     *
     * @param id ID du menu à modifier
     * @param menu Menu modifié
     * @param platsIds Liste des IDs des plats à associer
     * @return Redirection vers la liste des menus
     */
    @PostMapping("/edit/{id}")
    public String updateMenu(@PathVariable Long id, @ModelAttribute Menu menu,
                             @RequestParam(value = "platsIds", required = false) List<Long> platsIds) {
        if (platsIds != null) {
            Set<Plat> plats = new HashSet<>();
            for (Long pid : platsIds) {
                platRepository.findById(pid).ifPresent(plats::add);
            }
            menu.setPlats(plats);
        } else {
            menu.setPlats(new HashSet<>());
        }
        menuRepository.save(menu);
        return "redirect:/menus";
    }

    /**
     * Supprime un menu.
     *
     * @param id ID du menu à supprimer
     * @return Redirection vers la liste des menus
     */
    @GetMapping("/delete/{id}")
    public String deleteMenu(@PathVariable Long id) {
        menuRepository.deleteById(id);
        return "redirect:/menus";
    }

    /**
     * Affiche les détails d'un menu spécifique.
     *
     * @param id ID du menu à afficher
     * @param model Modèle Spring
     * @return Vue "menu-details"
     * @throws IllegalArgumentException si l'ID du menu est invalide
     */
    @GetMapping("/{id}")
    public String showMenu(@PathVariable Long id, Model model) {
        Menu menu = menuRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid menu Id:" + id));
        model.addAttribute("menu", menu);
        return "menu-details";
    }

    /**
     * Configure le binder pour la conversion des plats.
     *
     * @param binder WebDataBinder pour la configuration
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Plat.class, "plats", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text != null && !text.isEmpty()) {
                    Long id = Long.valueOf(text);
                    Plat plat = platRepository.findById(id).orElse(null);
                    setValue(plat);
                }
            }
        });
    }
} 