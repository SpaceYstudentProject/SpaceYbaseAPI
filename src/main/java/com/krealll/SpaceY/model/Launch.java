package com.krealll.SpaceY.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "launches")
public class Launch {

    @Id
    @Column(name = "id_api_launch")
    private String id;

    @JoinColumn(name = "entities_id", referencedColumnName = "id")
    @Column(name = "entities_id")
    private Integer entitiesId;
}
