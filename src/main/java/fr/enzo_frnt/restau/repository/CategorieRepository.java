package fr.enzo_frnt.restau.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.enzo_frnt.restau.model.Categorie;

/**
 * Repository pour l'entité Categorie.
 * Gère les opérations de base de données pour les catégories de plats.
 */
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
}