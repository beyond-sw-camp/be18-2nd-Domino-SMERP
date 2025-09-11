package com.domino.smerp.client;

import com.domino.smerp.client.dto.request.CreateClientRequest;
import com.domino.smerp.client.dto.request.UpdateClientRequest;
import com.domino.smerp.client.dto.response.ClientListResponse;
import com.domino.smerp.client.dto.response.ClientResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/clients")
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@Valid @RequestBody final CreateClientRequest request) {

        clientService.createClient(request);
    }

    @DeleteMapping("/{clientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable final Long clientId) {

        clientService.deleteClient(clientId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ClientListResponse> findAllClients(@RequestParam(required = false) final String companyName, @RequestParam(required = false) final String businessNumber) {

        return clientService.findAllClients(companyName,businessNumber);
    }

    @GetMapping("/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public ClientResponse findClient(@PathVariable final Long clientId) {

        return clientService.findClient(clientId);
    }

    @PatchMapping("/{clientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateClient(@PathVariable final Long clientId,
        @Valid @RequestBody final UpdateClientRequest request) {

        clientService.updateClient(clientId, request);
    }
}
