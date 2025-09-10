package com.domino.smerp.client;

import com.domino.smerp.client.dto.request.CreateClientRequest;
import com.domino.smerp.client.dto.request.UpdateClientRequest;
import com.domino.smerp.client.dto.response.ClientListResponse;
import com.domino.smerp.client.dto.response.ClientResponse;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public void createClient(final CreateClientRequest request) {

        if (clientRepository.existsByCompanyName(request.getCompanyName())) {
            throw new CustomException(ErrorCode.DUPLICATE_COMPANY_NAME);
        }
        if (clientRepository.existsByBusinessNumber(request.getBusinessNumber())) {
            throw new CustomException(ErrorCode.DUPLICATE_BUSINESS_NUMBER);
        }
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
                              .zipCode(request.getZipCode())
                              .status(request.getStatus())
                              .build();

        clientRepository.save(client);
    }

    @Override
    @Transactional
    public void deleteClient(final Long clientId) {

        Client client = clientRepository.findById(clientId)
                                        .orElseThrow(
                                            () -> new CustomException(ErrorCode.CLIENT_NOT_FOUND));
        clientRepository.deleteById(clientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientListResponse> findAllClients() {

        List<Client> clients = clientRepository.findAll();

        return clients.stream()
                      .map(client -> ClientListResponse.builder()
                                                       .companyName(client.getCompanyName())
                                                       .businessNumber(client.getBusinessNumber())
                                                       .ceoName(client.getCeoName())
                                                       .phone(client.getPhone())
                                                       .address(client.getAddress())
                                                       .zipCode(client.getZipCode())
                                                       .build())
                      .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponse findClient(final Long clientId) {

        Client client = clientRepository.findById(clientId)
                                        .orElseThrow(
                                            () -> new CustomException(ErrorCode.CLIENT_NOT_FOUND));

        return ClientResponse.builder()
                             .businessNumber(client.getBusinessNumber())
                             .companyName(client.getCompanyName())
                             .phone(client.getPhone())
                             .ceoName(client.getCeoName())
                             .ceoPhone(client.getCeoPhone())
                             .name1st(client.getName1st())
                             .phone1st(client.getPhone1st())
                             .job1st(client.getJob1st())
                             .name2nd(client.getName2nd())
                             .phone2nd(client.getPhone2nd())
                             .job2nd(client.getJob2nd())
                             .name3rd(client.getName3rd())
                             .phone3rd(client.getPhone3rd())
                             .job3rd(client.getJob3rd())
                             .address(client.getAddress())
                             .zipCode(client.getZipCode())
                             .status(String.valueOf(client.getStatus()))
                             .build();
    }

    @Override
    @Transactional
    public void updateClient(final Long clientId, final UpdateClientRequest request) {

        Client client = clientRepository.findById(clientId)
                                        .orElseThrow(() -> new CustomException(
                                            ErrorCode.CLIENT_NOT_FOUND));
        client.updateClient(request);
    }
}