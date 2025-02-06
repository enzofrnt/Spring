package fr.enzo_frnt.restau.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import fr.enzo_frnt.restau.model.Plat;
import java.util.List;

public interface PlatRepository extends JpaRepository<Plat, Long> {
    @Query("SELECT p FROM Plat p WHERE p.nom LIKE :x")
    Page<Plat> findByNomLike(@Param("x") String nom, Pageable pageable);
    
    @Query("SELECT p FROM Plat p WHERE p.categorie.id = :categorieId")
    Page<Plat> findByCategorie(@Param("categorieId") Long categorieId, Pageable pageable);
    
    @Query("SELECT p FROM Plat p WHERE p.nbCalories BETWEEN :min AND :max")
    Page<Plat> findByNbCaloriesBetween(@Param("min") int min, @Param("max") int max, Pageable pageable);
    
    @Query("SELECT p FROM Plat p WHERE p.nom LIKE %:nom%")
    List<Plat> findByNomContaining(@Param("nom") String nom);
    
    @Query("SELECT p FROM Plat p WHERE p.categorie.id = :categorieId")
    List<Plat> findByCategorieId(@Param("categorieId") Long categorieId);
    
    @Query("SELECT p FROM Plat p WHERE p.nbCalories BETWEEN :min AND :max")
    List<Plat> findByNbCaloriesBetweenList(@Param("min") int min, @Param("max") int max);
} 