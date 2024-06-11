package com.example.floramart.services;

import com.example.floramart.models.Proizvod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProizvodiRepository extends JpaRepository<Proizvod, Integer> {
}
