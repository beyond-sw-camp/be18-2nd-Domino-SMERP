package com.domino.smerp.client;

import com.domino.smerp.client.dto.request.CreateClientRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public void createClient(CreateClientRequest request) {

        Client client = Client.builder()
                              .businessNumber(request.getBusinessNumber())
                              .companyName(request.getCompanyName())
                              .phone(request.getPhone())
                              .ceoName(request.getCeoName())
                              .ceoPhone(request.getCeoPhone())
                              .name1st(request.getName1st())
                              .phone1st(request.getPhone1st())
                              .job1st(request.getJob1st())
                              .name2nd(request.getName2nd())
                              .phone2nd(request.getPhone2nd())
                              .job2nd(request.getJob2nd())
                              .name3rd(request.getName3rd())
                              .phone3rd(request.getPhone3rd())
                              .job3rd(request.getJob3rd())
                              .address(request.getAddress())
                              .zipcode(request.getZipCode())
                              .status(request.getStatus())
                              .build();

        clientRepository.save(client);
    }

    @Override
    public void deleteClient(Long clientId) {
        clientRepository.deleteById(clientId);
    }
}