package com.domino.smerp.client;

import com.domino.smerp.client.constants.TradeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @Column(nullable = false)
    private String businessNumber;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String ceoName;

    @Column(nullable = false)
    private String ceoPhone;

    @Column(nullable = false)
    private String name1st;

    @Column(nullable = false)
    private String phone1st;

    @Column(nullable = false)
    private String job1st;

    private String name2nd;

    private String phone2nd;

    private String job2nd;

    private String name3rd;

    private String phone3rd;

    private String job3rd;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String zipcode;

    @Column(nullable = false)
    private TradeType status;
}