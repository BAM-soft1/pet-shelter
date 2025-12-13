package org.pet.backendpetshelter.Service;


import jakarta.persistence.EntityNotFoundException;
import org.pet.backendpetshelter.DTO.VaccinationTypeResponse;
import org.pet.backendpetshelter.Entity.VaccinationType;
import org.pet.backendpetshelter.Repository.VaccinationTypeRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Profile({"mysql", "test"})
public class VaccinationTypeService {

    private final VaccinationTypeRepository vaccinationTypeRepository;

    public VaccinationTypeService(VaccinationTypeRepository vaccinationTypeRepository) {
        this.vaccinationTypeRepository = vaccinationTypeRepository;
    }


    /* Get All Vaccination Types */
    public Page<VaccinationTypeResponse> GetAllVaccinationTypes(Pageable pageable) {
        return vaccinationTypeRepository.findAll(pageable)
                .map(VaccinationTypeResponse::new);
    }
    
    /* Get All Vaccination Types with Filters */
    public Page<VaccinationTypeResponse> GetAllVaccinationTypesWithFilters(
            Boolean requiredForAdoption,
            String search,
            Pageable pageable) {
        
        return vaccinationTypeRepository.findAllWithFilters(requiredForAdoption, search, pageable)
                .map(VaccinationTypeResponse::new);
    }


    /* Get Specific Vaccination Type */
    public VaccinationTypeResponse GetVaccinationTypeById(Long id) {
        VaccinationType vaccinationType = vaccinationTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaccination Type not found with id: " + id));
        return new VaccinationTypeResponse(vaccinationType);
    }

    /* Add Vaccination Type */
    public VaccinationTypeResponse addVaccinationType(VaccinationTypeResponse request) {
        // Validate vaccination name
        if (request.getVaccineName() == null || request.getVaccineName().trim().isEmpty()) {
            throw new IllegalArgumentException("Vaccination name is required");
        }
        
        VaccinationType vaccinationType = new VaccinationType();

        vaccinationType.setVaccineName(request.getVaccineName());
        vaccinationType.setDescription(request.getDescription());
        vaccinationType.setDurationMonths(request.getDurationMonths());
        vaccinationType.setRequiredForAdoption(request.getRequiredForAdoption());

        vaccinationTypeRepository.save(vaccinationType);
        return new VaccinationTypeResponse(vaccinationType);


    }


    /* Update Vaccination Type */
    public VaccinationTypeResponse updateVaccinationType(Long id, VaccinationTypeResponse request) {
        VaccinationType vaccinationType = vaccinationTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaccination Type not found with id: " + id));

        // Validate vaccination name
        if (request.getVaccineName() == null || request.getVaccineName().trim().isEmpty()) {
            throw new IllegalArgumentException("Vaccination name is required");
        }

        vaccinationType.setVaccineName(request.getVaccineName());
        vaccinationType.setDescription(request.getDescription());
        vaccinationType.setDurationMonths(request.getDurationMonths());
        vaccinationType.setRequiredForAdoption(request.getRequiredForAdoption());

        vaccinationTypeRepository.save(vaccinationType);
        return new VaccinationTypeResponse(vaccinationType);
    }

    /* Delete Vaccination Type */
    public void deleteVaccinationType(Long id) {
        VaccinationType vaccinationType = vaccinationTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaccination Type not found with id: " + id));
        vaccinationTypeRepository.delete(vaccinationType);
    }



}