package com.domino.smerp.client;

import com.domino.smerp.client.dto.request.CreateClientRequest;
import com.domino.smerp.client.dto.request.UpdateClientRequest;
import com.domino.smerp.client.dto.response.ClientListResponse;
import com.domino.smerp.client.dto.response.ClientResponse;
import java.util.List;

public interface ClientService {
    void createClient(CreateClientRequest request);

    void deleteClient(Long clientId);

    List<ClientListResponse> findAllClients();

    ClientResponse findClient(Long clientId);

    void updateClient(Long clientId, UpdateClientRequest request);
}
