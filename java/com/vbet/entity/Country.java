package com.vbet.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Size(max = 50, message = "Country name should be less than 50")
    private String name;

    @Column(name = "flag_path")
    @Size(max = 250, message = "Flag path should be less than 250")
    private String flagPath;
}
