package com.triatlon.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Atleta extends Persona {

    private String categoria;

    private String especialidad;

    private Boolean modalidadCross;

    private String foto;
}
