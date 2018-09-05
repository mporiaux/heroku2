/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.domain;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service("clientService")
public class ClientServiceDB implements ClientServiceInterface {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Autowired
    private DataSource dataSource;

    @Override
    public List<Client> findAllClients() {
        List<Client> lc = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from Client");

            while (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int id = rs.getInt("id");
                System.out.println("touvé "+id+" "+nom+" "+prenom);
                lc.add(new Client(id, nom, prenom));
            }
          
        } catch (Exception e) {
            System.out.println("erreur de recherche de tous les clients "+e);
        }
        return lc;
    }

    @Override
    public int saveClient(Client client) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("insert into Client(nom,prenom) values(?,?)");
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            int n = stmt.executeUpdate();
            if (n > 0) {
                stmt = connection.prepareStatement("select id from client where nom=? and prenom = ?");
                stmt.setString(1, client.getNom());
                stmt.setString(2, client.getPrenom());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("id");
                    return id;
                }
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void updateClient(Client currentClient) {
        //non développé
    }

    @Override
    public Client findById(int id) {
        Client ctrouve = null;
        System.out.println("recherche de :"+id);
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("select * from client where id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                System.out.println("trouvé :"+nom+ " "+prenom);
                ctrouve=new Client(id,nom,prenom);
            }
            else System.out.println("client introuvable");
        } catch (Exception e) {
            System.out.println("erreur de recherche du client "+id+":"+e);
        }
        return ctrouve;
    }

    @Override
    public boolean isClientExist(Client client) {

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("select id from client where nom=? and prenom = ?");
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void deleteClientById(int id) {
        //non développé
    }

    @Override
    public void deleteAllClients() {
        //non développé
    }

    @Bean
    public DataSource dataSource() throws SQLException {
        if (dbUrl == null || dbUrl.isEmpty()) {
            return new HikariDataSource();
        } else {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(dbUrl);
            return new HikariDataSource(config);
        }

    }
}
