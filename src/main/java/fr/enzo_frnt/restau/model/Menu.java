package fr.enzo_frnt.restau.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entité représentant un menu du restaurant.
 * Un menu est composé d'un ensemble de plats et possède des informations comme
 * son nom, sa description et son prix.
 */
@Entity
@Table(name = "menu")
public class Menu {

    /**
     * Identifiant unique du menu.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Description détaillée du menu.
     */
    private String description;

    /**
     * Nom du menu.
     */
    private String nom;

    /**
     * Prix du menu en euros.
     */
    @Column(nullable = false)
    private double prix;

    /**
     * Collection des plats composant le menu.
     * Relation many-to-many avec l'entité Plat.
     */
    @ManyToMany
    @JoinTable(
        name = "menu_plat",
        joinColumns = @JoinColumn(name = "menu_id"),
        inverseJoinColumns = @JoinColumn(name = "plat_id")
    )
    @OrderBy("nom ASC")
    private Set<Plat> plats = new HashSet<>();

    /**
     * Constructeur par défaut requis par JPA.
     */
    public Menu() {
    }

    /**
     * Constructeur avec paramètres.
     *
     * @param description Description du menu
     * @param nom Nom du menu
     * @param prix Prix du menu
     */
    public Menu(String description, String nom, double prix) {
        this.description = description;
        this.nom = nom;
        this.prix = prix;
    }

    /**
     * Récupère l'identifiant du menu.
     *
     * @return L'identifiant unique du menu
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant du menu.
     *
     * @param id Le nouvel identifiant du menu
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Récupère la description du menu.
     *
     * @return La description du menu
     */
    public String getDescription() {
        return description;
    }

    /**
     * Définit la description du menu.
     *
     * @param description La nouvelle description du menu
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Récupère le nom du menu.
     *
     * @return Le nom du menu
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom du menu.
     *
     * @param nom Le nouveau nom du menu
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Récupère le prix du menu.
     *
     * @return Le prix du menu en euros
     */
    public double getPrix() {
        return prix;
    }

    /**
     * Définit le prix du menu.
     *
     * @param prix Le nouveau prix du menu en euros
     */
    public void setPrix(double prix) {
        this.prix = prix;
    }

    /**
     * Récupère la collection des plats du menu.
     *
     * @return L'ensemble des plats composant le menu
     */
    public Set<Plat> getPlats() {
        return plats;
    }

    /**
     * Définit la collection des plats du menu.
     *
     * @param plats Le nouvel ensemble de plats du menu
     */
    public void setPlats(Set<Plat> plats) {
        this.plats = plats;
    }

    /**
     * Ajoute un plat au menu.
     *
     * @param plat Le plat à ajouter au menu
     */
    public void addPlat(Plat plat) {
        this.plats.add(plat);
    }

    /**
     * Retire un plat du menu.
     *
     * @param plat Le plat à retirer du menu
     */
    public void removePlat(Plat plat) {
        this.plats.remove(plat);
    }
} 