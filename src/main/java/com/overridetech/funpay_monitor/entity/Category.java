package com.overridetech.funpay_monitor.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String name;

    private String urlForScrap;

    private String urlForExport;

    @Column(columnDefinition = "varchar default ''")
    private String ServerKeyWord;

    @Column(columnDefinition = "varchar default ''")
    private String TcSideKeyWord;

    @JoinColumn(name = "filter_id", referencedColumnName = "id", unique = true)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BaseFilterArgTable filterArgTable;

}
