package com.domino.smerp.client;

import com.domino.smerp.client.dto.request.CreateClientRequest;
import com.domino.smerp.client.dto.request.UpdateClientRequest;
import com.domino.smerp.client.dto.response.ClientListResponse;
import com.domino.smerp.client.dto.response.ClientResponse;
import com.domino.smerp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

public interface ClientService {
    void createClient(CreateClientRequest request);

    void deleteClient(Long clientId);

    PageResponse<ClientListResponse> searchClients(String companyName, String businessNumber,
        Pageable pageable);

    ClientResponse findClient(Long clientId);

    void updateClient(Long clientId, UpdateClientRequest request);
}
