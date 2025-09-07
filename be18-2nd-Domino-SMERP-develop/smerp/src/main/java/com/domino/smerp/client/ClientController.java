package com.domino.smerp.client;

import com.domino.smerp.client.dto.request.CreateClientRequest;
import com.domino.smerp.client.dto.request.UpdateClientRequest;
import com.domino.smerp.client.dto.response.ClientListResponse;
import com.domino.smerp.client.dto.response.ClientResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/clients")
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody CreateClientRequest request) {
        clientService.createClient(request);
    }

    @DeleteMapping("/{clientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long clientId) {
        clientService.deleteClient(clientId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ClientListResponse> findAllClients(){
        return clientService.findAllClients();
    }

    @GetMapping("/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public ClientResponse findClient(@PathVariable Long clientId){
        return clientService.findClient(clientId);
    }

    @PatchMapping("/{clientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateClient(@PathVariable Long clientId, @RequestBody UpdateClientRequest request){
        clientService.updateClient(clientId,request);
    }
}
