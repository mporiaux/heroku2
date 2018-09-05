/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.controller;
import com.example.domain.Client;
import java.util.List;
import javax.servlet.ServletContext;
  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.example.domain.ClientServiceInterface;
  

  
@RestController
public class ClientRestController {


@Autowired
private ServletContext context;

 
       
    @Autowired
    ClientServiceInterface clientService;  //Service which will do all data retrieval/manipulation work
  
      
    //-------------------Retrieve All Clients--------------------------------------------------------
      
  @RequestMapping(value = "/Client/", method = RequestMethod.GET)
    public ResponseEntity<List<Client>> listAllClients() {
        List<Client> clients = clientService.findAllClients();
        if(clients.isEmpty()){
            return new ResponseEntity<List<Client>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Client>>(clients, HttpStatus.OK);
    }
  
  
    //-------------------Retrieve Single Client--------------------------------------------------------
      
    @RequestMapping(value = "/Client/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Client> getClient(@PathVariable("id") int id) {
        System.out.println("Fetching Client with id " + id);
        Client client = clientService.findById(id);
        if (client == null) {
            System.out.println("Client with id " + id + " not found");
            return new ResponseEntity<Client>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Client>(client, HttpStatus.OK);
    }
  
      
      
    //-------------------Create a Client--------------------------------------------------------
      
    @RequestMapping(value = "/Client/", method = RequestMethod.POST)
    public ResponseEntity<Client> createClient(@RequestBody Client client, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating Client "+client.getId()+" " + client.getNom()+" "+client.getPrenom());
  
        if (clientService.isClientExist(client)) {
            System.out.println("A Client with name " + client.getNom() + "  "+client.getPrenom()+" already exist");
            return new ResponseEntity<Client>(client,HttpStatus.CONFLICT);
        }
  
        int id= clientService.saveClient(client);
        client.setId(id);
        return new ResponseEntity<Client>(client, HttpStatus.CREATED);
    }
  
      
    //------------------- Update a Client --------------------------------------------------------
      
    @RequestMapping(value = "/Client/", method = RequestMethod.PUT)
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        System.out.println("Updating Client " + client.getId());
          
        Client currentClient = clientService.findById(client.getId());
          
        if (currentClient==null) {
            System.out.println("Client with id " + client.getId() + " not found");
            return new ResponseEntity<Client>(HttpStatus.NOT_FOUND);
        }
  
        currentClient.setNom(client.getNom());
        currentClient.setPrenom(client.getPrenom());
                
        clientService.updateClient(currentClient);
        return new ResponseEntity<Client>(currentClient, HttpStatus.OK);
    }
  
    //------------------- Delete a Client --------------------------------------------------------
      
    @RequestMapping(value = "/Client/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Client> deleteClient(@PathVariable("id") int id) {
        System.out.println("Fetching & Deleting Client with id " + id);
  
        Client client = clientService.findById(id);
        if (client == null) {
            System.out.println("Unable to delete. Client with id " + id + " not found");
            return new ResponseEntity<Client>(HttpStatus.NOT_FOUND);
        }
  
        clientService.deleteClientById(id);
        return new ResponseEntity<Client>(HttpStatus.NO_CONTENT);
    }
  
      
    //------------------- Delete All Clients --------------------------------------------------------
      
    @RequestMapping(value = "/Client/", method = RequestMethod.DELETE)
    public ResponseEntity<Client> deleteAllClients() {
        System.out.println("Deleting All Clients");
  
        clientService.deleteAllClients();
        return new ResponseEntity<Client>(HttpStatus.NO_CONTENT);
    }
    
          
}
