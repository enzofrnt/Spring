package fr.enzo_frnt.restau.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.enzo_frnt.restau.model.Categorie;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {
}