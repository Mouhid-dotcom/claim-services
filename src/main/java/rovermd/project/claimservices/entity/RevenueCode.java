package rovermd.project.claimservices.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "RevenueCode")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

//RevenueCode
public class RevenueCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "RevCode")
    private String revCode;

    @Column(name = "RevDescription")
    private String RevDescription;

    @Column(name = "Price")
    private BigDecimal Price;

}
