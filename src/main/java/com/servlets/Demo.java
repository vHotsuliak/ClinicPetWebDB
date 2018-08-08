package com.servlets;

import com.models.Client;
import com.models.pets.Cat;
import com.models.pets.Dog;
import com.models.pets.Pet;
import com.service.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class Demo {
    final Settings settings = Settings.getInstance();
    private final AtomicInteger ids = new AtomicInteger();
    private final Connection connection;
    // SQL commands
    String insertClient = "INSERT into Clients VALUES ( ?, ? )";

    String insertPet = "INSERT into Pets VALUES ( ?, ?, ?, ? )";


    public Demo() {
        final Settings settings = Settings.getInstance();

        try {
            this.connection = DriverManager.getConnection(settings.value(
                    "jdbc.url"), settings.value("jdbc.username"), settings.value("jdbc.password"));
        }catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        try {
            Class.forName(settings.value("jdbc.driver_class"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addClient(Client client) {
        try(final PreparedStatement statement = this.connection.prepareStatement(insertClient)) {
            statement.setInt(1, client.getId());
            statement.setString(2, client.getClientName());
            statement.executeUpdate();
            addPet(client);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addPet(Client client) {
        try(final PreparedStatement statement = this.connection.prepareStatement(insertPet)) {
            statement.setInt(1,4);
            statement.setInt(2, client.getId());
            statement.setString(3, client.getPetName());
            statement.setString(4, client.getKindOfPet());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.addClient(new Client(4, "Abcd", new Pet("Abc")));
        demo.close();
    }

    private void close(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
