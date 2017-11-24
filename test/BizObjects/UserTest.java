package BizObjects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import business.Factory;
import business.FactoryImpl;
import business.user.User;

public class UserTest {

  private User user1;
  private User user2;
  private User user3;

  private Factory factory;


  @Before
  public void setUp() throws Exception {
    factory = new FactoryImpl();

    user1 = (User) factory.getUserDto();
    user1.setUtilisateurId(1);
    user1.setDateInscription(LocalDate.now());
    user1.setNom("Waits");
    user1.setPrenom("Tom");
    user1.setEmail("tom@waits.com");
    user1.setPseudo("Tom");
    user1.setMotDePasse(BCrypt.hashpw("password", BCrypt.gensalt()));

    user2 = (User) factory.getUserDto();
    user2.setUtilisateurId(1);
    user2.setDateInscription(LocalDate.now());
    user2.setNom("Meine");
    user2.setPrenom("Klaus");
    user2.setEmail("klaus@meine.com");
    user2.setMotDePasse(BCrypt.hashpw("password", BCrypt.gensalt()));

    user3 = (User) factory.getUserDto();
    user3.setUtilisateurId(1);
    user3.setDateInscription(LocalDate.now());
    user3.setNom("Cora");
    user3.setPrenom("");
    user3.setEmail("tom@Cora.com");
    user3.setPseudo("Tom");
    user3.setMotDePasse(BCrypt.hashpw("password", BCrypt.gensalt()));


  }

  @Test
  public void testCheckPassword1() {
    assertTrue(user1.checkPassword("password"));
  }

  @Test
  public void testCheckPassword2() {
    assertFalse(user1.checkPassword("motDePasse"));
  }

  @Test
  public void testCheckValidite1() {
    assertTrue(user1.checkValidite());
  }

  @Test
  public void testCheckValidite2() {
    assertFalse(user2.checkValidite());
  }

  @Test
  public void testCheckValidite3() {
    assertFalse(user3.checkValidite());
  }

  @Test
  public void testEquals1() {
    assertEquals(user1, user3);
    assertNotSame(user1, user3);
  }

  @Test
  public void testEquals2() {
    assertFalse(user1.equals(user2));
  }

}
