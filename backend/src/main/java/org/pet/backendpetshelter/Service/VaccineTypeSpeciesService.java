package org.pet.backendpetshelter.Service;

import jakarta.persistence.EntityNotFoundException;
import org.pet.backendpetshelter.DTO.VaccineTypeSpeciesRequest;
import org.pet.backendpetshelter.DTO.VaccineTypeSpeciesResponse;
import org.pet.backendpetshelter.Entity.VaccinationType;
import org.pet.backendpetshelter.Entity.VaccineTypeSpecies;
import org.pet.backendpetshelter.Repository.VaccinationTypeRepository;
import org.pet.backendpetshelter.Repository.VaccineTypeSpeciesRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile("mysql")
public class VaccineTypeSpeciesService {

    private final VaccineTypeSpeciesRepository vaccineTypeSpeciesRepository;
    private final VaccinationTypeRepository vaccinationTypeRepository;

    public VaccineTypeSpeciesService(
            VaccineTypeSpeciesRepository vaccineTypeSpeciesRepository,
            VaccinationTypeRepository vaccinationTypeRepository) {
        this.vaccineTypeSpeciesRepository = vaccineTypeSpeciesRepository;
        this.vaccinationTypeRepository = vaccinationTypeRepository;
    }

    /* Get All VaccineTypeSpecies */
    public List<VaccineTypeSpeciesResponse> GetAllVaccineTypeSpecies(){
        return vaccineTypeSpeciesRepository.findAll().stream()
                .map(VaccineTypeSpeciesResponse::new)
                .collect(Collectors.toList());
    }

    /* Get Specific VaccineTypeSpecies */
    public VaccineTypeSpeciesResponse GetVaccineTypeSpeciesById(Long id){
        VaccineTypeSpecies vaccineTypeSpecies = vaccineTypeSpeciesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VaccineTypeSpecies not found with id: " + id));
        return new VaccineTypeSpeciesResponse(vaccineTypeSpecies);
    }

    /* Add VaccineTypeSpecies */
    public VaccineTypeSpeciesResponse addVaccineTypeSpecies(VaccineTypeSpeciesRequest request){
        VaccinationType vaccinationType = vaccinationTypeRepository.findById(request.getVaccinationTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Vaccination type not found with id: " + request.getVaccinationTypeId()));

        VaccineTypeSpecies vaccineTypeSpecies = new VaccineTypeSpecies();
        vaccineTypeSpecies.setSpecies(request.getSpecies());
        vaccineTypeSpecies.setVaccinationType(vaccinationType);

        vaccineTypeSpeciesRepository.save(vaccineTypeSpecies);
        return new VaccineTypeSpeciesResponse(vaccineTypeSpecies);
    }

    /* Update VaccineTypeSpecies */
    public VaccineTypeSpeciesResponse updateVaccineTypeSpecies(Long id, VaccineTypeSpeciesRequest request){
        VaccineTypeSpecies vaccineTypeSpecies = vaccineTypeSpeciesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VaccineTypeSpecies not found with id: " + id));

        VaccinationType vaccinationType = vaccinationTypeRepository.findById(request.getVaccinationTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Vaccination type not found with id: " + request.getVaccinationTypeId()));

        vaccineTypeSpecies.setSpecies(request.getSpecies());
        vaccineTypeSpecies.setVaccinationType(vaccinationType);

        vaccineTypeSpeciesRepository.save(vaccineTypeSpecies);
        return new VaccineTypeSpeciesResponse(vaccineTypeSpecies);
    }

    /* Delete VaccineTypeSpecies */
    public void deleteVaccineTypeSpecies(Long id){
        VaccineTypeSpecies vaccineTypeSpecies = vaccineTypeSpeciesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VaccineTypeSpecies not found with id: " + id));
        vaccineTypeSpeciesRepository.delete(vaccineTypeSpecies);
    }
}