package com.store;

import com.models.Client;
import com.service.Settings;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.models.CreatePet.createPet;

public class JDBCStorage implements Storage {
    private final Connection connection;

    //last id're for clients and pets
    private final AtomicInteger idsClint = new AtomicInteger();

    public AtomicInteger getIdsClint() {
        return idsClint;
    }

    private final AtomicInteger idsPet = new AtomicInteger();

    // -----SQL commands-----

    //Insert
    private final String INSERT_CLIENT                  = "INSERT into Clients VALUES ( ?, ?)";
    private final String INSERT_PET                     = "INSERT into Pets VALUES ( ?, ?, ?, ?)";

    // Delete
    private final String DELETE_PET                     = "Delete From Pets where client_id = ?";
    private final String DELETE_CLIENT                  = "Delete From Clients where user_id = ?";

    // Update
    private final String UPDATE_CLIENT                  = "UPDATE Clients SET user_nic = ? Where user_id = ?";
    private final String UPDATE_PET                     = "UPDATE Pets SET nic = ?, kind_of_pet = ? Where client_id = ?";

    // Search
    // SQL search request by one parameter
    private final String SEARCH_CLIENT_BY_PET_NAME                = "SELECT * FROM Clients JOIN Pets ON Clients.user_id=Pets.client_id And Pets.nic = ?";
    private final String SEARCH_CLIENT_BY_KIND_OF_PET             = "SELECT * FROM Clients JOIN Pets ON Clients.user_id=Pets.client_id And Pets.kind_of_pet = ?";
    private final String SEARCH_CLIENT_BY_CLIENT_NAME             = "SELECT * FROM Clients JOIN Pets ON Clients.user_id=Pets.client_id And Clients.user_nic = ?";
    // SQL request by two parameters
    private final String SEARCH_CLIENT_BY_PET                     = "SELECT * FROM Clients JOIN Pets ON Clients.user_id=Pets.client_id And Pets.nic = ? And Pets.kind_of_pet = ?";
    private final String SEARCH_CLIENT_BY_PET_AND_CLIENT_NAME     = "SELECT * FROM Clients JOIN Pets ON Clients.user_id=Pets.client_id And Clients.user_nic = ? And Pets.nic = ?";
    private final String SEARCH_CLIENT_BY_KIND_OF_PET_AND_CLIENT  = "SELECT * FROM Clients JOIN Pets ON Clients.user_id=Pets.client_id And Clients.user_nic = ?  And Pets.kind_of_pet = ?";
    // SQL request by three parameters
    private final String SEARCH_CLIENT_BY_PET_AND_CLIENT          = "SELECT * FROM Clients JOIN Pets ON Clients.user_id=Pets.client_id And Client.user_nic = ? And Pets.nic = ? And Pets.kind_of_pet = ?";

    // -----SQL commands-----

    public JDBCStorage() {
        final Settings settings = Settings.getInstance();

        // Didn't work without it and must be before connection
        try {
            Class.forName(settings.value("jdbc.driver_class"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            this.connection = DriverManager.getConnection(settings.value("jdbc.url"),
                    settings.value("jdbc.username"), settings.value("jdbc.password"));
        }catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        try(final Statement statement = this.connection.createStatement();
            final ResultSet rsClient = statement.executeQuery("SELECT MAX(user_id) as max FROM Clients WHERE user_id is not null")) {
                while (rsClient.next())
                    idsClint.set(rsClient.getInt("max"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try(final Statement statement = this.connection.createStatement();
            final ResultSet rsPet = statement.executeQuery("SELECT MAX(pet_id) as max FROM Pets WHERE pet_id is not null")) {
                while (rsPet.next())
                    idsPet.set(rsPet.getInt("max"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Collection<Client> values() {
        final List<Client> clients = new ArrayList<>();
        try(final Statement statement = this.connection.createStatement();
            final ResultSet rs = statement.executeQuery("SELECT * FROM Clients JOIN Pets ON Clients.user_id=Pets.client_id")) {
            while (rs.next())
                clients.add(new Client(rs.getInt("user_id"), rs.getString("user_nic"), createPet(rs.getString("kind_of_pet"), rs.getString("nic"))));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (clients.size() == 0)
            idsClint.set(0);
        return clients;
    }

    @Override
    public void add(Client client) {
        addPet(new Client(createID(client), client.getClientName(),
                createPet(client.getKindOfPet(), client.getPetName())));
    }

    private int createID(Client client) {
        try(final PreparedStatement statement = this.connection.prepareStatement(INSERT_CLIENT)) {
            AtomicInteger ids = new AtomicInteger(idsClint.incrementAndGet());
            statement.setInt(1, ids.get());
            statement.setString(2, client.getClientName());
            statement.executeUpdate();
            return ids.get();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("Could not create new user");
    }

    private void addPet(Client client) {
        try(final PreparedStatement statement = this.connection.prepareStatement(INSERT_PET)) {
            AtomicInteger ids = new AtomicInteger(idsPet.incrementAndGet());
            statement.setInt(1, ids.get());
            statement.setInt(2, client.getId());
            statement.setString(3, client.getPetName());
            statement.setString(4, client.getKindOfPet());
            statement.executeUpdate();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("Could not create new pet");
    }

    @Override
    public void edit(Client client) {
        try(final PreparedStatement statement = this.connection.prepareStatement(UPDATE_CLIENT)) {
            statement.setString(1, client.getClientName());
            statement.setInt(2, client.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try(final PreparedStatement statement = this.connection.prepareStatement(UPDATE_PET)) {
            statement.setString(1, client.getPetName());
            statement.setString(2, client.getKindOfPet());
            statement.setInt(3, client.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        deletePet(id);
        deleteClient(id);
    }

    private void deleteClient(int id) {
        try(final PreparedStatement statement = this.connection.prepareStatement(DELETE_CLIENT)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("Could not delete client");
    }

    private void deletePet(int id) {
        try(final PreparedStatement statement = this.connection.prepareStatement(DELETE_PET)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("Could not delete pet");
    }

    @Override
    public Client get(int id) {
        Client client = null;
        try (final PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM Clients JOIN Pets ON Clients.user_id=Pets.client_id AND Clients.user_id = ?")) {
            statement.setInt(1, id);
            try(final ResultSet rs = statement.executeQuery()){
                while (rs.next())
                client = new Client(rs.getInt("user_id"), rs.getString("user_nic"), createPet(rs.getString("kind_of_pet"), rs.getString("nic")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return client;
        }
    }

    @Override
    public Collection<Client> searchClient(final String clientName, final String petName, final String kindOfPet) {
        List<Client> clients = new ArrayList<>();

        if (!clientName.isEmpty() && petName.isEmpty() && kindOfPet.isEmpty())
            clients = oneCondition(clientName, SEARCH_CLIENT_BY_CLIENT_NAME); // Client.user_nic = ?"
        else if (clientName.isEmpty() && !petName.isEmpty() && kindOfPet.isEmpty())
            clients = oneCondition(petName, SEARCH_CLIENT_BY_PET_NAME); // Pets.nic = ?
        else if (clientName.isEmpty() && petName.isEmpty() && !kindOfPet.isEmpty())
            clients = oneCondition(kindOfPet, SEARCH_CLIENT_BY_KIND_OF_PET); // Pets.kind_of_pet = ?
        else if (!clientName.isEmpty() && !petName.isEmpty() && kindOfPet.isEmpty())
            clients = twoCondition(clientName, petName, SEARCH_CLIENT_BY_PET_AND_CLIENT_NAME); // Pets.nic = ? And Client.user_nic = ?"
        else if (clientName.isEmpty() && !petName.isEmpty() && !kindOfPet.isEmpty())
            clients = twoCondition(petName, kindOfPet, SEARCH_CLIENT_BY_PET); // Pets.nic = ? And Pets.kind_of_pet = ?
        else if (!clientName.isEmpty() && petName.isEmpty() && !kindOfPet.isEmpty())
            clients = twoCondition(clientName, kindOfPet, SEARCH_CLIENT_BY_KIND_OF_PET_AND_CLIENT); // Client.user_nic = ?  And Pets.kind_of_pet = ?
        else if (!clientName.isEmpty() && petName.isEmpty() && !kindOfPet.isEmpty())
            clients = threeCondition(clientName, petName, kindOfPet, SEARCH_CLIENT_BY_PET_AND_CLIENT); // Client.user_nic = ?  And Pets.kind_of_pet = ?
        return clients;
    }



    private List<Client> oneCondition(final String condition1, final String SQLSearchRequest) {
        final List<Client> clients = new ArrayList<>();
        try(final PreparedStatement statement = this.connection.prepareStatement(SQLSearchRequest)) {
            statement.setString(1, condition1);
            try (final ResultSet rs = statement.executeQuery()) {
                while (rs.next())
                    clients.add(new Client(rs.getInt("user_id"), rs.getString("user_nic"), createPet(rs.getString("kind_of_pet"), rs.getString("nic"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    private List<Client> twoCondition(String condition1, String condition2, String SQLSearchRequest) {
        final List<Client> clients = new ArrayList<>();
        try(final PreparedStatement statement = this.connection.prepareStatement(SQLSearchRequest)) {
            statement.setString(1, condition1);
            statement.setString(2, condition2);
            try (final ResultSet rs = statement.executeQuery()) {
                while (rs.next())
                    clients.add(new Client(rs.getInt("user_id"), rs.getString("user_nic"), createPet(rs.getString("kind_of_pet"), rs.getString("nic"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    private List<Client> threeCondition(String condition1, String condition2, String condition3, String SQLSearchRequest) {
        final List<Client> clients = new ArrayList<>();
        try(final PreparedStatement statement = this.connection.prepareStatement(SQLSearchRequest)) {
            statement.setString(1, condition1);
            statement.setString(2, condition2);
            statement.setString(3, condition3);
            try (final ResultSet rs = statement.executeQuery()) {
                while (rs.next())
                    clients.add(new Client(rs.getInt("user_id"), rs.getString("user_nic"), createPet(rs.getString("kind_of_pet"), rs.getString("nic"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }
    public int getLastId(){return this.idsClint.get();}

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    private List<Client> oneCondition(String clientName, String petName, String kindOfPet) {
        final String condition = condition(clientName, petName, kindOfPet, 1);

    }*/

    /*
     * Return i-th not null string parameter, if there aren't i-th parameter then null. Available 'i' is 1st or 2nd!!!
     * @param clientName contains client name.
     * @param petName contains pet name.
     * @param kindOfPet contains kind of pet.
     * @param i how mach parameters, at list, should be not null.
     * @return i-th not zero string parameter. If there aren't i-th parameter then null.
     *
    private String condition(String clientName, String petName, String kindOfPet, int i) {
        int counter = 1;

        if (clientName != null)
            if (i == counter)
                return clientName;
            else
                counter++;
        if (petName != null)
            if (i == counter)
                return petName;
            else
                counter++;
        if (kindOfPet != null)
            if (i == counter)
                return kindOfPet;
            else
                counter++;

        return null;
    }*/
}
