package com.nimbleways.springboilerplate.entities;

import com.nimbleways.springboilerplate.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "lead_time")
    private Integer leadTime;

    @Column(name = "available")
    private Integer available;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ProductType type;

    @Column(name = "name")
    private String name;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "season_start_date")
    private LocalDate seasonStartDate;

    @Column(name = "season_end_date")
    private LocalDate seasonEndDate;

    public boolean isAvailable() {
        return available > 0;
    }

    public void decreamentStock() {
        available--;
    }

    public boolean isInSeasonPeriod() {
        return LocalDate.now().isAfter(getSeasonStartDate()) && LocalDate.now().isBefore(getSeasonEndDate());
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(getExpiryDate());
    }

    public void markOutOfStock() {
        available = 0;
    }

    public boolean isSeasonNotStarted() {
        return LocalDate.now().isBefore(getSeasonStartDate());
    }

    public boolean isSeasonEndingBeforeRestock() {
        return LocalDate.now().plusDays(getLeadTime()).isAfter(getSeasonEndDate());
    }
}
