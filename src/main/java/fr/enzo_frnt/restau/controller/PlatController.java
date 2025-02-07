package fr.enzo_frnt.restau.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import fr.enzo_frnt.restau.model.Plat;
import fr.enzo_frnt.restau.repository.PlatRepository;
import fr.enzo_frnt.restau.repository.CategorieRepository;
import fr.enzo_frnt.restau.repository.MenuRepository;
import fr.enzo_frnt.restau.model.Menu;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Contrôleur gérant les opérations CRUD et la recherche pour les plats du restaurant.
 * Permet la gestion complète des plats incluant la création, modification, suppression et visualisation,
 * ainsi que des fonctionnalités de recherche et de filtrage avancées sur les valeurs nutritionnelles.
 */
@Controller
@RequestMapping("/plats")
public class PlatController {

    @Autowired
    private PlatRepository platRepository;
    
    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private MenuRepository menuRepository;

    /**
     * Affiche la liste des plats avec options de filtrage et pagination.
     *
     * @param model Modèle Spring pour transmettre les données à la vue
     * @param nom Filtre sur le nom du plat (optionnel)
     * @param categorieId Filtre par catégorie (optionnel)
     * @param minCalories Calories minimales pour le filtrage (optionnel)
     * @param maxCalories Calories maximales pour le filtrage (optionnel)
     * @param minGlucides Glucides minimaux pour le filtrage (optionnel)
     * @param maxGlucides Glucides maximaux pour le filtrage (optionnel)
     * @param minLipides Lipides minimaux pour le filtrage (optionnel)
     * @param maxLipides Lipides maximaux pour le filtrage (optionnel)
     * @param minProteines Protéines minimales pour le filtrage (optionnel)
     * @param maxProteines Protéines maximales pour le filtrage (optionnel)
     * @param page Numéro de la page courante (défaut: 0)
     * @param size Nombre d'éléments par page (défaut: 10)
     * @param sort Champ de tri (défaut: "nom")
     * @param direction Direction du tri (défaut: "asc")
     * @return Vue "plats" avec la liste filtrée et paginée
     */
    @GetMapping
    public String listePlats(
            Model model,
            @RequestParam(name = "nom", defaultValue = "") String nom,
            @RequestParam(name = "categorie", required = false) Long categorieId,
            @RequestParam(name = "minCalories", required = false) Integer minCalories,
            @RequestParam(name = "maxCalories", required = false) Integer maxCalories,
            @RequestParam(name = "minGlucides", required = false) Integer minGlucides,
            @RequestParam(name = "maxGlucides", required = false) Integer maxGlucides,
            @RequestParam(name = "minLipides", required = false) Integer minLipides,
            @RequestParam(name = "maxLipides", required = false) Integer maxLipides,
            @RequestParam(name = "minProteines", required = false) Integer minProteines,
            @RequestParam(name = "maxProteines", required = false) Integer maxProteines,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nom") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction dir = Sort.Direction.fromString(direction);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(dir, sort));
        
        Specification<Plat> spec = Specification.where(null);
        
        if (!nom.isEmpty()) {
            spec = spec.and((root, query, cb) -> 
                cb.like(cb.lower(root.get("nom")), "%" + nom.toLowerCase() + "%"));
        }
        
        if (categorieId != null) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(root.get("categorie").get("id"), categorieId));
        }
        
        if (minCalories != null) {
            spec = spec.and((root, query, cb) -> 
                cb.greaterThanOrEqualTo(root.get("nbCalories"), minCalories));
        }
        
        if (maxCalories != null) {
            spec = spec.and((root, query, cb) -> 
                cb.lessThanOrEqualTo(root.get("nbCalories"), maxCalories));
        }

        if (minGlucides != null) {
            spec = spec.and((root, query, cb) -> 
                cb.greaterThanOrEqualTo(root.get("nbGlucides"), minGlucides));
        }
        
        if (maxGlucides != null) {
            spec = spec.and((root, query, cb) -> 
                cb.lessThanOrEqualTo(root.get("nbGlucides"), maxGlucides));
        }

        if (minLipides != null) {
            spec = spec.and((root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("nbLipides"), minLipides));
        }

        if (maxLipides != null) {
            spec = spec.and((root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("nbLipides"), maxLipides));
        }

        if (minProteines != null) {
            spec = spec.and((root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("nbProteines"), minProteines));
        }

        if (maxProteines != null) {
            spec = spec.and((root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("nbProteines"), maxProteines));
        }
            
        
        Page<Plat> plats = platRepository.findAll(spec, pageRequest);

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
        model.addAttribute("minGlucides", minGlucides);
        model.addAttribute("maxGlucides", maxGlucides);
        model.addAttribute("minLipides", minLipides);
        model.addAttribute("maxLipides", maxLipides);
        model.addAttribute("minProteines", minProteines);
        model.addAttribute("maxProteines", maxProteines);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        
        return "plats";
    }

    /**
     * Affiche le formulaire de création d'un nouveau plat.
     *
     * @param model Modèle Spring pour transmettre les données à la vue
     * @return Vue "plat-form" pour la création
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("plat", new Plat());
        model.addAttribute("categories", categorieRepository.findAll());
        return "plat-form";
    }

    /**
     * Traite la création d'un nouveau plat.
     *
     * @param plat Plat à créer
     * @return Redirection vers la liste des plats
     */
    @PostMapping("/create")
    public String createPlat(@ModelAttribute Plat plat) {
        platRepository.save(plat);
        return "redirect:/plats";
    }

    /**
     * Affiche le formulaire de modification d'un plat existant.
     *
     * @param id ID du plat à modifier
     * @param model Modèle Spring
     * @return Vue "plat-form" pour la modification
     * @throws IllegalArgumentException si l'ID du plat est invalide
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Plat plat = platRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid plat Id:" + id));
        model.addAttribute("plat", plat);
        model.addAttribute("categories", categorieRepository.findAll());
        return "plat-form";
    }

    /**
     * Traite la modification d'un plat existant.
     *
     * @param id ID du plat à modifier
     * @param plat Plat modifié
     * @return Redirection vers la liste des plats
     */
    @PostMapping("/edit/{id}")
    public String updatePlat(@PathVariable Long id, @ModelAttribute Plat plat) {
        platRepository.save(plat);
        return "redirect:/plats";
    }

    /**
     * Supprime un plat et met à jour les menus associés.
     * Retire d'abord le plat des menus qui l'utilisent avant de le supprimer.
     *
     * @param id ID du plat à supprimer
     * @param redirectAttributes Pour ajouter des messages flash
     * @return Redirection vers la liste des plats
     */
    @GetMapping("/delete/{id}")
    public String deletePlat(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Plat plat = platRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid plat Id:" + id));
                
            // Supprimer d'abord les références dans les menus
            for (Menu menu : menuRepository.findByPlatsContaining(plat)) {
                menu.getPlats().remove(plat);
                menuRepository.save(menu);
            }
            
            // Puis supprimer le plat
            platRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Le plat a été supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Impossible de supprimer ce plat car il est utilisé dans un ou plusieurs menus");
        }
        return "redirect:/plats";
    }

    /**
     * Affiche les détails d'un plat spécifique.
     *
     * @param id ID du plat à afficher
     * @param model Modèle Spring
     * @return Vue "plat-details"
     * @throws IllegalArgumentException si l'ID du plat est invalide
     */
    @GetMapping("/{id}")
    public String showPlat(@PathVariable Long id, Model model) {
        Plat plat = platRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid plat Id:" + id));
        model.addAttribute("plat", plat);
        return "plat-details";
    }
} 