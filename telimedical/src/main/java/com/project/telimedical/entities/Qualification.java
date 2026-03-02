package com.project.telimedical.entities;


import java.time.LocalDate;

import jakarta.persistence.Embeddable;


@Embeddable
public class Qualification {

    private String degree;
    private LocalDate year_of_graduation;
    private String College;
    private String URL;

        


    public Qualification() {
        
    }
    public Qualification(String degree, LocalDate year_of_graduation, String college,String URL) {
        this.degree = degree;
        this.year_of_graduation = year_of_graduation;
        this.College = college;
        this.URL=URL;
    }
    public String getURL() {
        return URL;
    }
    public void setURL(String uRL) {
        URL = uRL;
    }
    public String getDegree() {
        return degree;
    }
    public void setDegree(String degree) {
        this.degree = degree;
    }
    public LocalDate getYear_of_graduation() {
        return year_of_graduation;
    }
    public void setYear_of_graduation(LocalDate year_of_graduation) {
        this.year_of_graduation = year_of_graduation;
    }
    public String getCollege() {
        return College;
    }
    public void setCollege(String college) {
        College = college;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((degree == null) ? 0 : degree.hashCode());
        result = prime * result + ((year_of_graduation == null) ? 0 : year_of_graduation.hashCode());
        result = prime * result + ((College == null) ? 0 : College.hashCode());
        result = prime * result + ((URL == null) ? 0 : URL.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Qualification other = (Qualification) obj;
        if (degree == null) {
            if (other.degree != null)
                return false;
        } else if (!degree.equals(other.degree))
            return false;
        if (year_of_graduation == null) {
            if (other.year_of_graduation != null)
                return false;
        } else if (!year_of_graduation.equals(other.year_of_graduation))
            return false;
        if (College == null) {
            if (other.College != null)
                return false;
        } else if (!College.equals(other.College))
            return false;
        if (URL == null) {
            if (other.URL != null)
                return false;
        } else if (!URL.equals(other.URL))
            return false;
        return true;
    }
    

    
}
