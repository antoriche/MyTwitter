package ihm;

import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import business.Factory;
import business.ucc.TweetUcc;
import business.ucc.UserUcc;
import dal.DalServices;
import dal.dao.TweetDao;
import dal.dao.UserDao;

public class Main {

  /**
   * Demarrage de l'application.
   * 
   * @param args arguments entres
   */
  public static void main(String[] args) {
    try {

      Prop props = new Prop("prod.properties");

      String secret = props.getProperty("jwt_secret");
      String urlConn = props.getProperty("urlConn");
      String userConn = props.getProperty("userConn");
      String passwordConn = props.getProperty("passwordConn");

      Class<?> c = Class.forName(props.getProperty("DalServices"));
      DalServices dal =
          (DalServices) c.getDeclaredConstructor(String.class, String.class, String.class)
              .newInstance(urlConn, userConn, passwordConn);

      c = Class.forName(props.getProperty("Factory"));
      Factory factory = (Factory) c.newInstance();

      c = Class.forName(props.getProperty("UserDao"));
      UserDao userDao = (UserDao) c.getDeclaredConstructor(DalServices.class, Factory.class)
          .newInstance(dal, factory);

      c = Class.forName(props.getProperty("TweetDao"));
      TweetDao tweetDao = (TweetDao) c.getDeclaredConstructor(DalServices.class, Factory.class)
          .newInstance(dal, factory);


      c = Class.forName(props.getProperty("UserUcc"));
      UserUcc userUcc =
          (UserUcc) c.getDeclaredConstructor(UserDao.class, DalServices.class, Factory.class)
              .newInstance(userDao, dal, factory);

      c = Class.forName(props.getProperty("TweetUcc"));
      TweetUcc tweetUcc =
          (TweetUcc) c.getDeclaredConstructor(TweetDao.class, DalServices.class, Factory.class)
              .newInstance(tweetDao, dal, factory);

      Logger logger = Logger.getAnonymousLogger();

      WebAppContext context = new WebAppContext();


      c = Class.forName(props.getProperty("Servlet"));
      DispatchServlet servlet = (DispatchServlet) c.getDeclaredConstructor(Factory.class,
          String.class, Logger.class, UserUcc.class, TweetUcc.class)
          .newInstance(factory, secret, logger, userUcc, tweetUcc);
      context.setResourceBase("www");
      context.addServlet(new ServletHolder(servlet), "/");
      Server server = new Server(Integer.valueOf(props.getProperty("port")));
      server.setHandler(context);

      server.start();
    } catch (Exception exc) {
      // TODO Auto-generated catch block
      exc.printStackTrace();
    }
  }

}
