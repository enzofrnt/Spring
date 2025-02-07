package fr.enzo_frnt.restau.model;

import jakarta.persistence.*;

/**
 * Entité représentant un plat du restaurant.
 * Un plat possède des informations nutritionnelles détaillées (calories, glucides, lipides, protéines)
 * et appartient à une catégorie spécifique.
 */
@Entity
@Table(name = "plat")
public class Plat {

    /**
     * Identifiant unique du plat.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de calories du plat.
     */
    @Column(name = "nb_calories", nullable = false)
    private int nbCalories;

    /**
     * Quantité de glucides en grammes.
     */
    @Column(name = "nb_glucides", nullable = false)
    private int nbGlucides;

    /**
     * Quantité de lipides en grammes.
     */
    @Column(name = "nb_lipides", nullable = false)
    private int nbLipides;

    /**
     * Quantité de protéines en grammes.
     */
    @Column(name = "nb_proteines", nullable = false)
    private int nbProteines;

    /**
     * Nom du plat.
     */
    private String nom;

    /**
     * Catégorie du plat.
     * Relation many-to-one avec l'entité Categorie.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    /**
     * Constructeur par défaut requis par JPA.
     */
    public Plat() {
    }

    /**
     * Constructeur avec paramètres.
     *
     * @param nbCalories Nombre de calories du plat
     * @param nbGlucides Quantité de glucides en grammes
     * @param nbLipides Quantité de lipides en grammes
     * @param nbProteines Quantité de protéines en grammes
     * @param nom Nom du plat
     * @param categorie Catégorie du plat
     */
    public Plat(int nbCalories, int nbGlucides, int nbLipides, int nbProteines, String nom, Categorie categorie) {
        this.nbCalories = nbCalories;
        this.nbGlucides = nbGlucides;
        this.nbLipides = nbLipides;
        this.nbProteines = nbProteines;
        this.nom = nom;
        this.categorie = categorie;
    }

    /**
     * Récupère l'identifiant du plat.
     *
     * @return L'identifiant unique du plat
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant du plat.
     *
     * @param id Le nouvel identifiant du plat
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Récupère le nombre de calories du plat.
     *
     * @return Le nombre de calories
     */
    public int getNbCalories() {
        return nbCalories;
    }

    /**
     * Définit le nombre de calories du plat.
     *
     * @param nbCalories Le nouveau nombre de calories
     */
    public void setNbCalories(int nbCalories) {
        this.nbCalories = nbCalories;
    }

    /**
     * Récupère la quantité de glucides du plat.
     *
     * @return La quantité de glucides en grammes
     */
    public int getNbGlucides() {
        return nbGlucides;
    }

    /**
     * Définit la quantité de glucides du plat.
     *
     * @param nbGlucides La nouvelle quantité de glucides en grammes
     */
    public void setNbGlucides(int nbGlucides) {
        this.nbGlucides = nbGlucides;
    }

    /**
     * Récupère la quantité de lipides du plat.
     *
     * @return La quantité de lipides en grammes
     */
    public int getNbLipides() {
        return nbLipides;
    }

    /**
     * Définit la quantité de lipides du plat.
     *
     * @param nbLipides La nouvelle quantité de lipides en grammes
     */
    public void setNbLipides(int nbLipides) {
        this.nbLipides = nbLipides;
    }

    /**
     * Récupère la quantité de protéines du plat.
     *
     * @return La quantité de protéines en grammes
     */
    public int getNbProteines() {
        return nbProteines;
    }

    /**
     * Définit la quantité de protéines du plat.
     *
     * @param nbProteines La nouvelle quantité de protéines en grammes
     */
    public void setNbProteines(int nbProteines) {
        this.nbProteines = nbProteines;
    }

    /**
     * Récupère le nom du plat.
     *
     * @return Le nom du plat
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom du plat.
     *
     * @param nom Le nouveau nom du plat
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Récupère la catégorie du plat.
     *
     * @return La catégorie du plat
     */
    public Categorie getCategorie() {
        return categorie;
    }

    /**
     * Définit la catégorie du plat.
     *
     * @param categorie La nouvelle catégorie du plat
     */
    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
} 