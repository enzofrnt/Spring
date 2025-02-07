package fr.enzo_frnt.restau.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur gérant la page d'accueil de l'application.
 * Point d'entrée principal de l'application web de gestion de restaurant.
 */
@Controller
public class HomeController {

    /**
     * Affiche la page d'accueil.
     * Gère la route racine "/" de l'application.
     *
     * @return Le nom de la vue "home" à afficher
     */
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
