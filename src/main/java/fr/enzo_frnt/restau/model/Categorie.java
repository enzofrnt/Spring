package fr.enzo_frnt.restau.model;

import jakarta.persistence.*;

/**
 * Entité représentant une catégorie de plats dans le restaurant.
 * Permet de classifier les différents plats proposés (entrée, plat principal, dessert, etc.).
 */
@Entity
@Table(name = "categorie")
public class Categorie {

    /**
     * Identifiant unique de la catégorie.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom de la catégorie.
     */
    private String nom;

    /**
     * Constructeur par défaut requis par JPA.
     */
    public Categorie() {
    }

    /**
     * Constructeur avec paramètre.
     *
     * @param nom Le nom de la catégorie
     */
    public Categorie(String nom) {
        this.nom = nom;
    }

    /**
     * Récupère l'identifiant de la catégorie.
     *
     * @return L'identifiant unique de la catégorie
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant de la catégorie.
     *
     * @param id Le nouvel identifiant de la catégorie
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Récupère le nom de la catégorie.
     *
     * @return Le nom de la catégorie
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de la catégorie.
     *
     * @param nom Le nouveau nom de la catégorie
     */
    public void setNom(String nom) {
        this.nom = nom;
    }
} 