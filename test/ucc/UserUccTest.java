package ucc;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import business.Factory;
import business.FactoryImpl;
import business.ucc.UserUcc;
import business.ucc.UserUccImpl;
import business.user.UserDto;
import dal.DalServices;
import dal.dao.UserDao;
import dal.mock.MockDalServices;
import dal.mock.MockUserDao;

public class UserUccTest {



  UserDto user1;
  UserDto user2;
  UserDto user3;

  Factory factory;
  UserDao userDao;
  DalServices dal;
  UserUcc ucc;



  @Before
  public void setUp() throws Exception {
    factory = new FactoryImpl();
    userDao = new MockUserDao();
    dal = new MockDalServices();
    ucc = new UserUccImpl(userDao, dal, factory);

    user1 = factory.getUserDto();
    user1.setUtilisateurId(1);
    user1.setPseudo("Anthony");
    user1.setMotDePasse("Pyck");
    user1.setEmail("anthony@gmail.com");
    user1.setPrenom("Anthony");
    user1.setNom("Pyck");
    user1.setDateInscription(LocalDate.of(2017, 3, 6));

    user2 = factory.getUserDto();
    user2.setUtilisateurId(2);
    user2.setPseudo("Johnny");
    user2.setMotDePasse("La");
    user2.setEmail("johnny@gmail.com");
    user2.setPrenom("Johnny");
    user2.setNom("La");
    user2.setDateInscription(LocalDate.of(2017, 4, 6));

    user3 = factory.getUserDto();
    user3.setPseudo("Antonin");
    user3.setMotDePasse("Riche");
  }

  @Test
  public void testConnect_success() {
    UserDto u1_ = factory.getUserDto();
    u1_.setPseudo(user1.getPseudo());
    u1_.setMotDePasse(BCrypt.hashpw(user1.getMotDePasse(), BCrypt.gensalt()));

    userDao.createUser(u1_);

    UserDto u = ucc.connect(user1);
    assertTrue(u1_.equals(u));
  }

  @Test
  public void testConnect_error() {
    UserDto u = ucc.connect(user1);
    assertTrue(u == null);
  }

  @Test(expected = Exception.class)
  public void testSignup_error() throws Exception {
    userDao.createUser(user1);

    ucc.signup(user1);
    fail("On aurait dû avoir une exception");
  }

  @Test(expected = Exception.class)
  public void testSignup_error2() throws Exception {
    userDao.createUser(user1);

    ucc.signup(user3);
    fail("On aurait dû avoir une exception");
  }

  @Test
  public void testSignup_success() throws Exception {
    UserDto u = ucc.signup(user1);
    assertTrue(u.equals(user1));
  }

  @Test
  public void testGetUserFromId_success() {
    userDao.createUser(user1);
    UserDto u = ucc.getUserFromId(user1.getUtilisateurId());
    assertTrue(user1.equals(u));
  }

  @Test
  public void testGetUserFromId_error() {
    UserDto u = ucc.getUserFromId(user1.getUtilisateurId());
    assertTrue(u == null);
  }

}
