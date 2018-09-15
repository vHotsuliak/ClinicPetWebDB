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

    private final AtomicInteger idsPet = new AtomicInteger();

    // -----SQL commands-----

    //Insert
    private final String INSERT_CLIENT                  = "INSERT into clients VALUES ( ?, ?)";
    private final String INSERT_PET                     = "INSERT into pets VALUES ( ?, ?, ?, ?)";

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
    private final String SEARCH_CLIENT_BY_PET_AND_CLIENT          = "SELECT * FROM Clients JOIN Pets ON Clients.user_id=Pets.client_id And Clients.user_nic = ? And Pets.nic = ? And Pets.kind_of_pet = ?";

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

        try(final PreparedStatement statement = this.connection.prepareStatement("SELECT MAX(user_id) as max FROM Clients");
            final ResultSet rsClient = statement.executeQuery()) {
                while (rsClient.next())
                    idsClint.set(rsClient.getInt("max"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try(final PreparedStatement statement = this.connection.prepareStatement("SELECT MAX(pet_id) as max FROM Pets");
            final ResultSet rsPet = statement.executeQuery()) {
                while (rsPet.next())
                    idsPet.set(rsPet.getInt("max"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Return list of all clients.
     * @return list of all clients.
     */
    @Override
    public Collection<Client> values() {
        final List<Client> clients = new ArrayList<>();
        try(final PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM Clients JOIN Pets ON Clients.user_id=Pets.client_id");
            final ResultSet rs = statement.executeQuery()) {
            while (rs.next())
                clients.add(new Client(rs.getInt("user_id"), rs.getString("user_nic"), createPet(rs.getString("kind_of_pet"), rs.getString("nic"))));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (clients.size() == 0)
            idsClint.set(0);
        return clients;
    }

    /**
     * Add one client and his pet to database.
     * @param client contains client's id, name and pet.
     */
    @Override
    public void add(Client client) {
        addPet(new Client(createIDAndClient(client), client.getClientName(),
                createPet(client.getKindOfPet(), client.getPetName())));
    }

    /**
     * Create client in database. Create id for this client in database. Return client id in database.
     * @param client contains client data.
     * @return client id in database.
     */
    private int createIDAndClient(Client client) {
        AtomicInteger ids = new AtomicInteger(idsClint.incrementAndGet());
        try(final PreparedStatement statement = this.connection.prepareStatement(INSERT_CLIENT)) {
            statement.setInt(1, ids.get());
            statement.setString(2, client.getClientName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ids.get();
    }

    /**
     * Create pet in database which is connected with this client.
     * @param client contains client data include pet.
     */
    private void addPet(Client client) {
        try(final PreparedStatement statement = this.connection.prepareStatement(INSERT_PET)) {
            AtomicInteger ids = new AtomicInteger(idsPet.incrementAndGet());
            statement.setInt(1, ids.get());
            statement.setInt(2, client.getId());
            statement.setString(3, client.getPetName());
            statement.setString(4, client.getKindOfPet());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Editing client data in database, also editing pet data in database. Id won't be updated.
     * @param client contains client data include pet.
     */
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

    /**
     * Deleting client and his pet.
     * @param id client id
     */
    @Override
    public void delete(int id) {
        deletePet(id);
        deleteClient(id);
    }

    /**
     * Deleting client from a database.
     * @param id client id
     */
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

    /**
     * Deleting client's pet from a database.
     * @param id client id.
     */
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

    /**
     * Get client by his id.
     * @param id client id.
     * @return client which have this id. If client with this id isn't exist then return null.
     */
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
        }
        return client;

    }

    /**
     * Choosing way to searching clients. Return list of clients with these parameters.
     * @param clientName contains client's name.
     * @param petName contains pet's name.
     * @param kindOfPet contains kind of pet
     * @return list of clients with these parameters.
     */
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
        else if (!clientName.isEmpty() && !petName.isEmpty() && !kindOfPet.isEmpty())
            clients = threeCondition(clientName, petName, kindOfPet); // Client.user_nic = ?  And Pets.kind_of_pet = ?
        return clients;
    }


    /**
     * Searching clients by one parameter.
     * @param condition1 1st condition.
     * @param SQLSearchRequest contains SQL search request with one parameter.
     * @return list of clients with this parameter.
     */
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

    /**
     * Searching clients by two parameters.
     * @param condition1 1st condition.
     * @param condition2 2nd condition.
     * @param SQLSearchRequest contains SQL search request with two parameters.
     * @return list of clients with these parameters.
     */
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

    /**
     * Searching clients by three parameters.
     * @param condition1 1st condition.
     * @param condition2 2nd condition.
     * @param condition3 3rd condition.
     * @return list of clients with these parameters.
     */
    private List<Client> threeCondition(String condition1, String condition2, String condition3) {
        final List<Client> clients = new ArrayList<>();
        try(final PreparedStatement statement = this.connection.prepareStatement(SEARCH_CLIENT_BY_PET_AND_CLIENT)) {
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

    /**
     * Return last client id.
     * @return last client id.
     */
    public int getClientLastID(){return this.idsClint.get();}

    /**
     * Close connection to database.
     */
    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
