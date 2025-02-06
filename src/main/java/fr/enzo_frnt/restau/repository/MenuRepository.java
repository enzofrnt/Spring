package fr.enzo_frnt.restau.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import fr.enzo_frnt.restau.model.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT m FROM Menu m WHERE m.prix BETWEEN :min AND :max")
    Page<Menu> findByPrixBetween(@Param("min") double min, @Param("max") double max, Pageable pageable);
    
    @Query("SELECT m FROM Menu m WHERE m.nom LIKE :x")
    Page<Menu> findByNomLike(@Param("x") String nom, Pageable pageable);
    
    @Query("SELECT m FROM Menu m WHERE (SELECT SUM(p.nbCalories) FROM m.plats p) BETWEEN :min AND :max")
    Page<Menu> findByTotalCaloriesBetween(@Param("min") int min, @Param("max") int max, Pageable pageable);
}