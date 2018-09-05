package com.example.domain;

import java.util.List;

public interface ClientServiceInterface {

    void deleteAllClients();

    void deleteClientById(int id);

    List<Client> findAllClients();

    Client findById(int id);

    boolean isClientExist(Client client);

    int saveClient(Client client);

    void updateClient(Client currentClient);
    
}
