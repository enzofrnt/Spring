package fr.enzo_frnt.restau.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import fr.enzo_frnt.restau.model.Menu;
import fr.enzo_frnt.restau.model.Plat;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository pour la gestion des menus dans la base de données.
 * Fournit des méthodes de recherche avancées pour les menus, notamment sur les prix
 * et les valeurs nutritionnelles totales.
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {
    
    /**
     * Recherche les menus dont le prix est compris dans une fourchette donnée.
     *
     * @param min Prix minimum en euros
     * @param max Prix maximum en euros
     * @param pageable Informations de pagination
     * @return Une page de menus correspondant aux critères de prix
     */
    @Query("SELECT m FROM Menu m WHERE m.prix BETWEEN :min AND :max")
    Page<Menu> findByPrixBetween(@Param("min") double min, @Param("max") double max, Pageable pageable);
    
    /**
     * Recherche les menus dont le nom contient une chaîne donnée.
     *
     * @param nom Chaîne à rechercher dans le nom des menus
     * @param pageable Informations de pagination
     * @return Une page de menus dont le nom correspond au critère
     */
    @Query("SELECT m FROM Menu m WHERE m.nom LIKE :x")
    Page<Menu> findByNomLike(@Param("x") String nom, Pageable pageable);
    
    /**
     * Recherche les menus dont le total des calories est compris dans une fourchette donnée.
     * Le total des calories est calculé en sommant les calories de tous les plats du menu.
     *
     * @param min Nombre minimum de calories
     * @param max Nombre maximum de calories
     * @param pageable Informations de pagination
     * @return Une page de menus correspondant aux critères caloriques
     */
    @Query("SELECT m FROM Menu m WHERE (SELECT SUM(p.nbCalories) FROM m.plats p) BETWEEN :min AND :max")
    Page<Menu> findByTotalCaloriesBetween(@Param("min") int min, @Param("max") int max, Pageable pageable);

    /**
     * Récupère tous les menus triés par total de calories croissant.
     *
     * @param pageable Informations de pagination
     * @return Une page de menus triés par calories croissantes
     */
    @Query("SELECT m FROM Menu m LEFT JOIN m.plats p GROUP BY m ORDER BY SUM(p.nbCalories) ASC")
    Page<Menu> findAllTotalCaloriesAsc(Pageable pageable);

    /**
     * Récupère tous les menus triés par total de calories décroissant.
     *
     * @param pageable Informations de pagination
     * @return Une page de menus triés par calories décroissantes
     */
    @Query("SELECT m FROM Menu m LEFT JOIN m.plats p GROUP BY m ORDER BY SUM(p.nbCalories) DESC")
    Page<Menu> findAllTotalCaloriesDesc(Pageable pageable);

    /**
     * Recherche tous les menus contenant un plat spécifique.
     *
     * @param plat Le plat à rechercher dans les menus
     * @return Liste des menus contenant le plat spécifié
     */
    List<Menu> findByPlatsContaining(Plat plat);
}