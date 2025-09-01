package com.domino.smerp.client;

import com.domino.smerp.client.dto.request.CreateClientRequest;

public interface ClientService {
    void createClient(CreateClientRequest request);
    void deleteClient(Long clientId);
}
