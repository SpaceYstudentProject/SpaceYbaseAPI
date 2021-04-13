package com.krealll.SpaceY.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="entities")
public class InteractionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


}
