package fr.enzo_frnt.restau.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import fr.enzo_frnt.restau.model.Plat;
import java.util.List;

/**
 * Repository pour la gestion des plats dans la base de données.
 * Fournit des méthodes de recherche avancées pour les plats, notamment sur le nom,
 * la catégorie et les valeurs nutritionnelles.
 */
public interface PlatRepository extends JpaRepository<Plat, Long>, JpaSpecificationExecutor<Plat> {
    
    /**
     * Recherche les plats dont le nom correspond exactement au motif fourni.
     *
     * @param nom Motif de recherche pour le nom
     * @param pageable Informations de pagination
     * @return Une page de plats correspondant au critère de nom
     */
    @Query("SELECT p FROM Plat p WHERE p.nom LIKE :x")
    Page<Plat> findByNomLike(@Param("x") String nom, Pageable pageable);
    
    /**
     * Recherche les plats appartenant à une catégorie spécifique.
     *
     * @param categorieId Identifiant de la catégorie recherchée
     * @param pageable Informations de pagination
     * @return Une page de plats de la catégorie spécifiée
     */
    @Query("SELECT p FROM Plat p WHERE p.categorie.id = :categorieId")
    Page<Plat> findByCategorie(@Param("categorieId") Long categorieId, Pageable pageable);
    
    /**
     * Recherche les plats dont le nombre de calories est compris dans une fourchette donnée.
     *
     * @param min Nombre minimum de calories
     * @param max Nombre maximum de calories
     * @param pageable Informations de pagination
     * @return Une page de plats correspondant aux critères caloriques
     */
    @Query("SELECT p FROM Plat p WHERE p.nbCalories BETWEEN :min AND :max")
    Page<Plat> findByNbCaloriesBetween(@Param("min") int min, @Param("max") int max, Pageable pageable);
    
    /**
     * Recherche les plats dont le nom contient la chaîne spécifiée.
     * Cette méthode effectue une recherche partielle (contient) contrairement à findByNomLike.
     *
     * @param nom Chaîne à rechercher dans le nom des plats
     * @return Liste des plats dont le nom contient la chaîne recherchée
     */
    @Query("SELECT p FROM Plat p WHERE p.nom LIKE %:nom%")
    List<Plat> findByNomContaining(@Param("nom") String nom);
    
    /**
     * Recherche tous les plats d'une catégorie spécifique.
     * Version non paginée de findByCategorie.
     *
     * @param categorieId Identifiant de la catégorie recherchée
     * @return Liste complète des plats de la catégorie spécifiée
     */
    @Query("SELECT p FROM Plat p WHERE p.categorie.id = :categorieId")
    List<Plat> findByCategorieId(@Param("categorieId") Long categorieId);
    
    /**
     * Recherche les plats dont le nombre de calories est compris dans une fourchette donnée.
     * Version non paginée de findByNbCaloriesBetween.
     *
     * @param min Nombre minimum de calories
     * @param max Nombre maximum de calories
     * @return Liste complète des plats correspondant aux critères caloriques
     */
    @Query("SELECT p FROM Plat p WHERE p.nbCalories BETWEEN :min AND :max")
    List<Plat> findByNbCaloriesBetweenList(@Param("min") int min, @Param("max") int max);
} 