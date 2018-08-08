package com.servlets;

import com.models.Client;
import com.models.pets.Cat;
import com.models.CreatePet;
import com.models.pets.Dog;
import com.models.pets.Pet;
import com.store.UserCache;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.models.CreatePet.createPet;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/*
This tests are calculate with that none will not doing some thing with clients date in the same moment!!!!!!!!!!!!!!!!!!!
 */
public class AVCDESClientServletsTest extends Mockito {
    private final UserCache USER_CACHE = UserCache.getInstance();

    /*
    Start testing CreatePet class
     */
    @Test
    public  void  CreatePetPet(){
        CreatePet createPet = new CreatePet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("kindOfPet")).thenReturn("Pet");
        when(request.getParameter("petName")).thenReturn("TestName0");
        verify(request, atLeast(0)).getParameter("kindOfPet");
        verify(request, atLeast(0)).getParameter("petName");
        assertEquals( new Pet("TestName0"), createPet.createPet(request));

    }

    @Test
    public  void  CreatePetDog(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("kindOfPet")).thenReturn("Dog");
        when(request.getParameter("petName")).thenReturn("TestName1");
        verify(request, atLeast(0)).getParameter("kindOfPet");
        verify(request, atLeast(0)).getParameter("petName");
        assertEquals( new Dog("TestName1"), createPet(request));

    }

    @Test
    public  void  CreatePetCat() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("kindOfPet")).thenReturn("Cat");
        when(request.getParameter("petName")).thenReturn("TestName2");
        verify(request, atLeast(0)).getParameter("kindOfPet");
        verify(request, atLeast(0)).getParameter("petName");
        assertEquals( new Cat("TestName2"), createPet(request));

    }
    /*
    Finished testing  CreatePet class
     */


    /*
     * Testing AddClientServlet...
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    /*@Test
    public void createClient() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("clientName")).thenReturn("test0");
        when(request.getParameter("petName")).thenReturn("test0");
        when(request.getParameter("kindOfPet")).thenReturn("Pet");


        assertTrue(this.USER_CACHE.values().isEmpty());

        AddClientServlet addClientServlet = new AddClientServlet();
        addClientServlet.doPost(request, response);

        verify(request, atLeast(1)).getParameter("clientName");
        verify(request, atLeast(1)).getParameter("petName");
        verify(request, atLeast(1)).getParameter("kindOfPet");
        assertEquals(new Client(1, "test0", new Pet("test0")), USER_CACHE.get(1));

        assertFalse(this.USER_CACHE.values().isEmpty());
        when(request.getParameter("clientName")).thenReturn("test1");
        when(request.getParameter("petName")).thenReturn("test1");
        when(request.getParameter("kindOfPet")).thenReturn("Cat");

        addClientServlet.doPost(request, response);

        verify(request, atLeast(1)).getParameter("clientName");
        verify(request, atLeast(1)).getParameter("petName");
        verify(request, atLeast(1)).getParameter("kindOfPet");
        assertEquals(new Client(2, "test1", new Cat("test1")), USER_CACHE.get(2));

        when(request.getParameter("clientName")).thenReturn("test2");
        when(request.getParameter("petName")).thenReturn("test2");
        when(request.getParameter("kindOfPet")).thenReturn("Dog");

        addClientServlet.doPost(request, response);

        verify(request, atLeast(1)).getParameter("clientName");
        verify(request, atLeast(1)).getParameter("petName");
        verify(request, atLeast(1)).getParameter("kindOfPet");
        assertEquals(new Client(3, "test2", new Dog("test2")), USER_CACHE.get(3));

        for (int i = 1, length = this.USER_CACHE.values().size(); i <= length; i++)
            this.USER_CACHE.delete(i);
    }*/

    /**
     * Testing ClientViewServlet...
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    @Test
    public void ClientViewServletTest() throws ServletException, IOException{


    }


    /*
    Start testing EditClientServlet
     */
    /**
     * Testing EditClientServletDoGetDoPost...
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    @Test
    public void EditClientServletChangeKindOFPetTest() throws ServletException, IOException{
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        int lastId  = this.USER_CACHE.getLastId();

        USER_CACHE.add(new Client(lastId + 1, "Test", new Pet("Test")));

        when(request.getRequestDispatcher("/views/client/EditClient.jsp")).thenReturn(dispatcher);
        when(request.getParameter("id")).thenReturn(String.valueOf(lastId + 1));

        verify(request, atLeast(0)).getParameter("id");
        verify(dispatcher, atLeast(0)).forward(request, response);

        EditClientServlet editClientServlet = new EditClientServlet();
        editClientServlet.doGet(request, response);

        when(request.getParameter("clientName")).thenReturn("test0");
        when(request.getParameter("petName")).thenReturn("test0");
        when(request.getParameter("kindOfPet")).thenReturn("Dog");

        verify(request, atLeast(0)).getParameter("clientName");
        verify(request, atLeast(0)).getParameter("petName");
        verify(request, atLeast(0)).getParameter("kindOfPet");
        verify(response, atLeast(0)).sendRedirect(String.format("%s%s", request.getContextPath(), "/client/view"));

        editClientServlet.doPost(request, response);

        assertEquals(new Client(lastId + 1,"test0", new Dog("test0")), USER_CACHE.get(lastId + 1));

        when(request.getParameter("clientName")).thenReturn("test1");
        when(request.getParameter("petName")).thenReturn("test1");
        when(request.getParameter("kindOfPet")).thenReturn("Cat");

        verify(request, atLeast(0)).getParameter("clientName");
        verify(request, atLeast(0)).getParameter("petName");
        verify(request, atLeast(0)).getParameter("kindOfPet");
        verify(response, atLeast(0)).sendRedirect(String.format("%s%s", request.getContextPath(), "/client/view"));

        editClientServlet.doPost(request, response);

        assertEquals(new Client(lastId + 1,"test1", new Cat("test1")), USER_CACHE.get(lastId + 1));

        this.USER_CACHE.delete(lastId + 1);
    }

    /**
     * Testing EditClientServletDoGetDoPost...
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    @Test
    public void DeleteClientServletTest() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        int size  = this.USER_CACHE.values().size();

        assertEquals( size, this.USER_CACHE.values().size());
        USER_CACHE.add(new Client(0, "Test", new Pet("Test")));
        assertEquals(size + 1, this.USER_CACHE.values().size());

        when(request.getRequestDispatcher("/views/client/DeleteClient.jsp")).thenReturn(dispatcher);
        when(request.getParameter("id")).thenReturn(String.valueOf(this.USER_CACHE.getLastId()));

        verify(request, atLeast(0)).getParameter("id");
        verify(dispatcher, atLeast(0)).forward(request, response);

        DeleteClientServlet deleteClientServlet = new DeleteClientServlet();

        when(request.getParameter("confirmation")).thenReturn("NO");
        verify(request, atLeast(0)).getParameter("confirmation");

        deleteClientServlet.doGet(request,response);
        deleteClientServlet.doPost(request,response);

        assertEquals(size + 1, this.USER_CACHE.values().size());

        when(request.getParameter("confirmation")).thenReturn("YES");
        verify(request, atLeast(0)).getParameter("confirmation");

        deleteClientServlet.doPost(request,response);

        assertEquals(size, this.USER_CACHE.values().size());
    }

    /*@Test
    public void  SearchClientServletTest() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        assertTrue(this.USER_CACHE.values().isEmpty());
        USER_CACHE.add(new Client(0, "Test", new Pet("Test")));
        USER_CACHE.add(new Client(1, "Test1", new Pet("Test1")));
        USER_CACHE.add(new Client(2, "Test2", new Pet("Test2")));
        USER_CACHE.add(new Client(3, "Test3", new Pet("Test3")));
        assertFalse(this.USER_CACHE.values().isEmpty());

        SearchClientServlet searchClientServlet = new SearchClientServlet();

        when(request.getRequestDispatcher("/views/client/SearchClient.jsp")).thenReturn(dispatcher);

        when(request.getParameter("clientName")).thenReturn("ghf");
        when(request.getParameter("petName")).thenReturn("sdfg");
        when(request.getParameter("kindOfPet")).thenReturn("sdfg");

        verify(request, atLeast(0)).getParameter("clientName");
        verify(request, atLeast(0)).getParameter("petName");
        verify(request, atLeast(0)).getParameter("kindOfPet");

        verify(dispatcher, atLeast(0)).forward(request, response);

        searchClientServlet.doGet(request, response);

        when(request.getParameter("clientName")).thenReturn(null);
        when(request.getParameter("petName")).thenReturn("");
        when(request.getParameter("kindOfPet")).thenReturn("");

        searchClientServlet.doGet(request, response);

        when(request.getParameter("clientName")).thenReturn("");
        when(request.getParameter("petName")).thenReturn(null);
        when(request.getParameter("kindOfPet")).thenReturn("");

        searchClientServlet.doGet(request, response);

        when(request.getParameter("clientName")).thenReturn("");
        when(request.getParameter("petName")).thenReturn("");
        when(request.getParameter("kindOfPet")).thenReturn(null);

        searchClientServlet.doGet(request, response);
        for (int i = 0, length = this.USER_CACHE.values().size(); i < length; i++)
            this.USER_CACHE.delete(i);

    }*/

}