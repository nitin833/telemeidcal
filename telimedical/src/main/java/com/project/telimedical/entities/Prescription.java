package com.project.telimedical.entities;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Table
@Entity(name = "prescription")
public class Prescription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID prescribtion_id;


    //for storing list of elements
     @ElementCollection
    @CollectionTable(
        name = "prescription_medicines",
        joinColumns = @JoinColumn(name = "prescription_id")
    )
    @Column(name = "medicine")
    private List<String> medicines;

    //to tell jpa,sql it is long text
    @Lob
    @Column(columnDefinition = "TEXT")
    private String notes;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String disease;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String describtion;

    public UUID getPrescribtion_id() {
        return prescribtion_id;
    }


    public List<String> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<String> medicines) {
        this.medicines = medicines;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getDescribtion() {
        return describtion;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }


    public Prescription() {
    }

    

    


}
