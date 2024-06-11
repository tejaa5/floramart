package com.example.floramart.controllers;
import com.example.floramart.models.ProizvodDto;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import com.example.floramart.models.Proizvod;
import com.example.floramart.services.ProizvodiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.*;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Path;

@Controller
@RequestMapping("/proizvodi")
public class ProizvodiController {
    @Autowired
    private ProizvodiRepository repo;

    @GetMapping({"", "/"})
    public String showProductList(Model model) {
        List<Proizvod> proizvodi = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("proizvodi", proizvodi);
        return "proizvodi/index";

    }

    @GetMapping("/kreiraj")
    public String showCreatePage(Model model) {
        ProizvodDto proizvodDto = new ProizvodDto();
        model.addAttribute("proizvodDto", proizvodDto);
        return "proizvodi/KreirajProizvod";
    }

    @PostMapping("/kreiraj")
    public String createProduct(
            @Valid @ModelAttribute ProizvodDto proizvodDto,
            BindingResult result
    ) {
        if (proizvodDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("proizvodDto", "imageFile", "Image file is empty"));
        }

        if (result.hasErrors()) {
            return "proizvodi/KreirajProizvod";
        }

        MultipartFile image = proizvodDto.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
        try {
            String uploadDir = "public/images/";
            java.nio.file.Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);

            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        Proizvod proizvod = new Proizvod();
        proizvod.setIme(proizvodDto.getIme());
        proizvod.setKategorija(proizvodDto.getKategorija());
        proizvod.setCena(proizvodDto.getCena());
        proizvod.setOpis(proizvodDto.getOpis());
        proizvod.setCreatedAt(createdAt);
        proizvod.setImageFileName(storageFileName);

        repo.save(proizvod);

        return "redirect:/proizvodi";
    }
    @GetMapping("/izmeni")
    public String showEditPage(
            Model model,
            @RequestParam int id

    ){
        try{
            Proizvod proizvod=repo.findById(id).get();
            model.addAttribute("proizvod", proizvod);

            ProizvodDto proizvodDto=new ProizvodDto();
            proizvodDto.setIme(proizvod.getIme());
            proizvodDto.setKategorija(proizvod.getKategorija());
            proizvodDto.setCena(proizvod.getCena());
            proizvodDto.setOpis(proizvod.getOpis());

            model.addAttribute("proizvodDto", proizvodDto);

        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return "redirect:/proizvodi";
        }
        return "proizvodi/IzmeniProizvod";

    }
    @PostMapping("/izmeni")
    public String updateProduct(
            Model model,
            @RequestParam int id,
            @Valid @ModelAttribute ProizvodDto proizvodDto,
            BindingResult result




    ){
        try{
            Proizvod proizvod=repo.findById(id).get();
            model.addAttribute("proizvod", proizvod);

            if(result.hasErrors()){
                return "proizvodi/IzmeniProizvod";
            }
            if(!proizvodDto.getImageFile().isEmpty()){
                String uploadDir="public/images";
                java.nio.file.Path oldImagePath = Paths.get(uploadDir + proizvod.getImageFileName());

                try{
                    Files.delete((java.nio.file.Path) oldImagePath);
                }catch(Exception ex){
                    System.out.println(ex.getMessage());
                }

                MultipartFile image = proizvodDto.getImageFile();
                Date createdAt = new Date();
                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                proizvod.setImageFileName(storageFileName);
            }
            proizvod.setIme(proizvodDto.getIme());
            proizvod.setKategorija(proizvodDto.getKategorija());
            proizvod.setCena(proizvodDto.getCena());
            proizvod.setOpis(proizvodDto.getOpis());

            repo.save(proizvod);

        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "redirect:/proizvodi";
    }

    @GetMapping("/obrisi")
    public String deleteProduct(
            @RequestParam int id

    ){
        try {
            Proizvod proizvod = repo.findById(id).get();

            java.nio.file.Path imagePath = Paths.get("public/images/" + proizvod.getImageFileName());

            try {
                Files.delete(imagePath);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());

            }

            repo.delete(proizvod);

        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "redirect:/proizvodi";
    }


}



