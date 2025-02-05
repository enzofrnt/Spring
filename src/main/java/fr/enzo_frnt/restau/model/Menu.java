package fr.enzo_frnt.restau.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String nom;

    @Column(nullable = false)
    private double prix;

    @ManyToMany
    @JoinTable(
        name = "menu_plat",
        joinColumns = @JoinColumn(name = "menu_id"),
        inverseJoinColumns = @JoinColumn(name = "plat_id")
    )
    private Set<Plat> plats = new HashSet<>();

    public Menu() {
    }

    public Menu(String description, String nom, double prix) {
        this.description = description;
        this.nom = nom;
        this.prix = prix;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public Set<Plat> getPlats() {
        return plats;
    }

    public void setPlats(Set<Plat> plats) {
        this.plats = plats;
    }

    public void addPlat(Plat plat) {
        this.plats.add(plat);
    }

    public void removePlat(Plat plat) {
        this.plats.remove(plat);
    }
} 