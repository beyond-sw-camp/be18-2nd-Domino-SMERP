package com.domino.smerp.client;

import com.domino.smerp.client.dto.request.CreateClientRequest;
import com.domino.smerp.client.dto.request.UpdateClientRequest;
import com.domino.smerp.client.dto.response.ClientListResponse;
import com.domino.smerp.client.dto.response.ClientResponse;
import com.domino.smerp.common.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public PageResponse<ClientListResponse> searchClients(@RequestParam(required = false) final String companyName, @RequestParam(required = false) final String businessNumber,@PageableDefault(size = 20, sort = "clientId", direction = Sort.Direction.DESC) Pageable pageable) {

        return clientService.searchClients(companyName,businessNumber,pageable);
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
