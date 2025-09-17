package com.domino.smerp.client;

import com.domino.smerp.client.dto.request.CreateClientRequest;
import com.domino.smerp.client.dto.request.UpdateClientRequest;
import com.domino.smerp.client.dto.response.ClientListResponse;
import com.domino.smerp.client.dto.response.ClientResponse;
import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public PageResponse<ClientListResponse> searchClients(String companyName, String businessNumber,
        Pageable pageable) {

        BooleanExpression companyNameCondition =
            (companyName != null && !companyName.isEmpty()) ? QClient.client.companyName.startsWith(
                companyName) : null;

        BooleanExpression businessNumberCondition =
            (businessNumber != null && !businessNumber.isEmpty())
                ? QClient.client.businessNumber.startsWith(businessNumber) : null;

        BooleanExpression condition = null;

        if (companyNameCondition != null && businessNumberCondition != null) {
            condition = companyNameCondition.and(businessNumberCondition);
        } else if (companyNameCondition != null) {
            condition = companyNameCondition;
        } else if (businessNumberCondition != null) {
            condition = businessNumberCondition;
        }

        Page<Client> page = (condition == null) ? clientRepository.findAll(pageable)
            : clientRepository.findAll(condition, pageable);

        Page<ClientListResponse> pageClient = page.map(client -> ClientListResponse.builder()
                                                                                   .companyName(client.getCompanyName())
                                                                                   .businessNumber(client.getBusinessNumber())
                                                                                   .phone(client.getPhone())
                                                                                   .ceoName(client.getCeoName())
                                                                                   .address(client.getAddress())
                                                                                   .zipCode(client.getZipCode())
                                                                                   .build());

        return PageResponse.from(pageClient);
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