package fr.enzo_frnt.restau.model;

import jakarta.persistence.*;

@Entity
@Table(name = "plat")
public class Plat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nb_calories", nullable = false)
    private int nbCalories;

    @Column(name = "nb_glucides", nullable = false)
    private int nbGlucides;

    @Column(name = "nb_lipides", nullable = false)
    private int nbLipides;

    @Column(name = "nb_proteines", nullable = false)
    private int nbProteines;

    private String nom;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    public Plat() {
    }

    public Plat(int nbCalories, int nbGlucides, int nbLipides, int nbProteines, String nom, Categorie categorie) {
        this.nbCalories = nbCalories;
        this.nbGlucides = nbGlucides;
        this.nbLipides = nbLipides;
        this.nbProteines = nbProteines;
        this.nom = nom;
        this.categorie = categorie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNbCalories() {
        return nbCalories;
    }

    public void setNbCalories(int nbCalories) {
        this.nbCalories = nbCalories;
    }

    public int getNbGlucides() {
        return nbGlucides;
    }

    public void setNbGlucides(int nbGlucides) {
        this.nbGlucides = nbGlucides;
    }

    public int getNbLipides() {
        return nbLipides;
    }

    public void setNbLipides(int nbLipides) {
        this.nbLipides = nbLipides;
    }

    public int getNbProteines() {
        return nbProteines;
    }

    public void setNbProteines(int nbProteines) {
        this.nbProteines = nbProteines;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
} 