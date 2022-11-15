package ua.nix.akolovych.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Column(name = "created")
    private Instant created;

    @Column(name = "updated")
    private Instant updated;
}
