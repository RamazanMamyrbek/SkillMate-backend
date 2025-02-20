package ru.skillmate.backend.entities.resources;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skillmate.backend.controllers.resources.enums.ResourceType;

import java.math.BigInteger;

@Entity
@Table(name = "resources")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String key;

    private String folder;

    private BigInteger size;

    @Enumerated(EnumType.STRING)
    private ResourceType contentType;
}
