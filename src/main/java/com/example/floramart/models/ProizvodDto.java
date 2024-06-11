package com.example.floramart.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class ProizvodDto {
    @NotEmpty(message = "Polje je obavezno")
    private String ime;

    @NotEmpty(message = "Polje je obavezno")
    private String kategorija;

    @Min(0)
    private double cena;

    public @Size(min = 10, message = "Opis treba da sadrzi minimum 10 karaktera") @Size(max = 2000, message = "Opis ne sme da bude duzi od 2000 karaktera") String getOpis() {
        return opis;
    }

    public void setOpis(@Size(min = 10, message = "Opis treba da sadrzi minimum 10 karaktera") @Size(max = 2000, message = "Opis ne sme da bude duzi od 2000 karaktera") String opis) {
        this.opis = opis;
    }

    @Min(0)
    public double getCena() {
        return cena;
    }

    public void setCena(@Min(0) double cena) {
        this.cena = cena;
    }

    public @NotEmpty(message = "Polje je obavezno") String getIme() {
        return ime;
    }

    public void setIme(@NotEmpty(message = "Polje je obavezno") String ime) {
        this.ime = ime;
    }

    @Size(min=10, message="Opis treba da sadrzi minimum 10 karaktera")
    @Size(max=2000, message = "Opis ne sme da bude duzi od 2000 karaktera")
    private String opis;

    private MultipartFile imageFile;

    public @NotEmpty(message = "Polje je obavezno") String getKategorija() {
        return kategorija;
    }

    public void setKategorija(@NotEmpty(message = "Polje je obavezno") String kategorija) {
        this.kategorija = kategorija;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
