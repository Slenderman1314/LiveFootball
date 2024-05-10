package ifp.project.livefootball.Account;

import junit.framework.TestCase;

public class UserTest extends TestCase { //Probamos los retornos de User

    public void testGetName() {
        User u = new User("andrei", "123456", "Entrenador");
        String nombre = u.getName();
        assertEquals("andrei", nombre);
    }

    public void testGetPass() {
        User u = new User("andrei", "123456", "Entrenador");
        String pass = u.getPass();
        assertEquals("123456", pass);
    }

    public void testGetRole() {
        User u = new User("andrei", "123456", "Entrenador");
        String role = u.getRole();
        assertEquals("Entrenador", role);
    }
}