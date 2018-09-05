/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Service;

//@Service("clientService")
public class ClientService implements ClientServiceInterface {

    private  List<Client> lc = new ArrayList<>();

    @Override
    public List<Client> findAllClients() {
        return lc;
    }

    @Override
    public int saveClient(Client client) {
        System.out.println("====> sauvegarde du client :" +client);
        lc.add(client);
        System.out.println("====> contenu de la liste :"+lc);
        return client.getId();
    }

    @Override
    public void updateClient(Client currentClient) {
        Client ctrouve = null;
        for (Client c : lc) {
            if (c.getId() == currentClient.getId()) {
                break;
            }
        }
        if (ctrouve != null) {
            ctrouve.setNom(currentClient.getNom());
            ctrouve.setPrenom(currentClient.getPrenom());
        }
    }

    @Override
    public Client findById(int id) {
        Client ctrouve = null;
        System.out.println("===> recherche dans la liste :"+lc);
        for (Client c : lc) {
            if (c.getId() == id) {
                ctrouve=c;
                break;
            }
        }
        return ctrouve;
    }

    @Override
    public boolean isClientExist(Client client) {

        for (Client c : lc) {
            if (c.getNom().equals(client.getNom()) && c.getPrenom().equals(client.getPrenom())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteClientById(int id) {
        Iterator<Client> itc = lc.iterator();
        while (itc.hasNext()) {
             Client c = itc.next();
             if(c.getId()==id) {
                 itc.remove();
                 break;
             }
        }
    }
    

    @Override
    public void deleteAllClients() {
        lc.clear();
    }

}
