package com.vbet.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "national_championship")
public class NationalChampionship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Size(max = 150, message = "Championship name should be less than 50")
    private String name;

    @Column(name = "emblem_path")
    @Size(max = 250, message = "Emblem path should be less than 250")
    private String emblemPath;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "championship")
    private List<Season> seasons;
}
