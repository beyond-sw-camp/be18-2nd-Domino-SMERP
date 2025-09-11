package com.domino.smerp.client;

import com.domino.smerp.client.constants.TradeType;
import com.domino.smerp.client.dto.request.UpdateClientRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @Column(nullable = false, length = 12, unique = true)
    private String businessNumber;

    @Column(nullable = false, length = 60, unique = true)
    private String companyName;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(nullable = false, length = 30)
    private String ceoName;

    @Column(nullable = false, length = 15)
    private String ceoPhone;

    @Column(nullable = false, length = 30)
    private String name1st;

    @Column(nullable = false, length = 20)
    private String phone1st;

    @Column(nullable = false, length = 10)
    private String job1st;

    @Column(length = 30)
    private String name2nd;

    @Column(length = 20)
    private String phone2nd;

    @Column(length = 20)
    private String job2nd;

    @Column(length = 30)
    private String name3rd;

    @Column(length = 20)
    private String phone3rd;

    @Column(length = 10)
    private String job3rd;

    @Column(nullable = false, length = 120)
    private String address;

    @Column(nullable = false, length = 100)
    private String zipCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TradeType status;

    public void updateClient(UpdateClientRequest request) {

        if (request.getCompanyName() != null) {
            this.companyName = request.getCompanyName();
        }
        if (request.getPhone() != null) {
            this.phone = request.getPhone();
        }
        if (request.getCeoName() != null) {
            this.ceoName = request.getCeoName();
        }
        if (request.getCeoPhone() != null) {
            this.ceoPhone = request.getCeoPhone();
        }
        if (request.getName1st() != null) {
            this.name1st = request.getName1st();
        }
        if (request.getPhone1st() != null) {
            this.phone1st = request.getPhone1st();
        }
        if (request.getJob1st() != null) {
            this.job1st = request.getJob1st();
        }
        if (request.getName2nd() != null) {
            this.name2nd = request.getName2nd();
        }
        if (request.getPhone2nd() != null) {
            this.phone2nd = request.getPhone2nd();
        }
        if (request.getJob2nd() != null) {
            this.job2nd = request.getJob2nd();
        }
        if (request.getName3rd() != null) {
            this.name3rd = request.getName3rd();
        }
        if (request.getPhone3rd() != null) {
            this.phone3rd = request.getPhone3rd();
        }
        if (request.getJob3rd() != null) {
            this.job3rd = request.getJob3rd();
        }
        if (request.getAddress() != null) {
            this.address = request.getAddress();
        }
        if (request.getZipCode() != null) {
            this.zipCode = request.getZipCode();
        }
        if (request.getStatus() != null) {
            this.status = request.getStatus();
        }
    }
}