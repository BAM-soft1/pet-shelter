package org.pet.backendpetshelter.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.pet.backendpetshelter.Status;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "adoption_application", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "animal_id"}))
public class AdoptionApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adoption_application_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    private Date applicationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private Status status;

    @Column(name = "description", length = 2000, nullable = false) 
    private String description; 

    @ManyToOne
    @JoinColumn(name = "reviewed_by_user_id")
    private User reviewedByUser;

    private Boolean isActive;

}
