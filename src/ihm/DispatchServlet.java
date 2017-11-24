package ihm;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.servlet.DefaultServlet;
import org.mindrot.jbcrypt.BCrypt;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.owlike.genson.Genson;

import business.Factory;
import business.exception.BizException;
import business.exception.FatalException;
import business.exception.LockException;
import business.ucc.TweetUcc;
import business.ucc.UserUcc;
import business.user.TweetDto;
import business.user.UserDto;

public class DispatchServlet extends DefaultServlet {

  private Factory factory;
  private UserUcc userUcc;
  private TweetUcc tweetUcc;
  private Logger logger;

  private final String secret;

  /**
   * Constructeur du servlet.
   * 
   * @param factory pour creer les dto0
   * @param secret pour JWT
   * @param logger .
   * @param userUcc controleur de user
   * @param journeeUcc controleur de journee
   * @param participationUcc controleur de participation
   * @param participationContactUcc controleur de participationContact
   * @param entrepriseUcc controleur d entreprise
   */
  public DispatchServlet(Factory factory, String secret, Logger logger, UserUcc userUcc,
      TweetUcc tweetUcc) {
    super();


    this.factory = factory;
    this.logger = logger;
    this.userUcc = userUcc;
    this.tweetUcc = tweetUcc;
    this.secret = secret;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    if (req.getParameter("action") == null)
      super.doGet(req, resp);
    else
      doPost(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setCharacterEncoding("UTF-8");
    String action = req.getParameter("action");
    if (action == null) {
      return;
    }

    logger.log(Level.INFO, "Request " + action);

    Object response = null;

    try {
      switch (action) {
        case "login":
          response = login(req, resp);
          if (response == null) {
            throw new BizException("Pseudo ou mot de passe invalide");
          }
          break;
        case "isLogged":
          response = userData(req);
          if (response == null) {
            response = false;
          }
          break;
        case "inscription":
          response = inscription(req, resp);
          if (response == null) {
            throw new BizException("Erreur lors de l'inscription");
          }
          break;
        case "getUser":
          response = getUser(req);
          break;
      }

      if (response == null && userData(req) == null) {
        throw new BizException("Vous ne pouvez pas effectuer cette action");
      }

      switch (action) {
        case "logout":
          response = logout(req, resp);
          break;
        case "envoyerTweet":
          response = envoyer_tweet(req, resp);
          break;
        case "listTweet":
          response = list_tweet(req, resp);
          break;
      }

      resp.setStatus(200);
    } catch (BizException exc) {
      logger.log(Level.WARNING, exc.getMessage());
      response = exc.getMessage();
      resp.setStatus(401);
    } catch (FatalException exc) {
      logger.log(Level.SEVERE, exc.getMessage());
      // exc.printStackTrace();
      response = exc.getMessage();
      resp.setStatus(500);
    } catch (LockException exc) {
      logger.log(Level.WARNING, exc.getMessage());
      // exc.printStackTrace();
      response =
          "Un autre utilisateur a fait des opérations en même temps que vous, vos modifications ne seront pas prise en compte.";
      resp.setStatus(409);
    } catch (Exception exc) {
      logger.log(Level.SEVERE, exc.getMessage());
      exc.printStackTrace();
      response = "Erreur inconnue";
      resp.setStatus(520);
    }

    if (response instanceof UserDto) {
      ((UserDto) response).setMotDePasse(null);
    }

    resp.getWriter().print(new Genson().serialize(response));

  }

  private Object list_tweet(HttpServletRequest req, HttpServletResponse resp) {
    Integer last =
        (req.getParameter("last") == null) ? null : Integer.valueOf(req.getParameter("last"));
    int num = Integer.valueOf(req.getParameter("num"));
    Integer respondTo = (req.getParameter("respondTo") == null) ? null
        : Integer.valueOf(req.getParameter("respondTo"));
    return tweetUcc.listTweets(last, num, respondTo);
  }

  private Object envoyer_tweet(HttpServletRequest req, HttpServletResponse resp) {
    TweetDto dto = factory.getTweetDto();
    dto.setText(req.getParameter("text"));
    dto.setProprietaire(userData(req).getUtilisateurId());
    dto.setDate(LocalDateTime.now());
    dto.setResponseTo(
        (req.getParameter("responseTo") == null || req.getParameter("responseTo").isEmpty()) ? null
            : Integer.valueOf(req.getParameter("responseTo")));
    return tweetUcc.envoyerTweet(dto);
  }

  private Object getUser(HttpServletRequest req) {
    int id = -1;
    try {
      id = Integer.valueOf(req.getParameter("id"));
    } catch (NumberFormatException exc) {
      throw new BizException("L'ID n'est pas un nombre valide");
    }
    return userUcc.getUserFromId(id);
  }


  private UserDto inscription(HttpServletRequest req, HttpServletResponse resp) {
    UserDto dto = factory.getUserDto();
    dto.setPseudo(req.getParameter("pseudo"));
    dto.setNom(req.getParameter("nom"));
    dto.setPrenom(req.getParameter("prenom"));
    dto.setEmail(req.getParameter("email"));
    dto.setMotDePasse(BCrypt.hashpw(req.getParameter("password"), BCrypt.gensalt()));
    dto.setDateInscription(LocalDate.now());

    userUcc.signup(dto);
    return login(req, resp);
  }

  private boolean logout(HttpServletRequest req, HttpServletResponse resp) {
    req.getSession().setAttribute("user", null);
    Cookie cookie = findCookie(req, "user");
    if (cookie != null) {
      cookie.setMaxAge(0);
      resp.addCookie(cookie);
    } else {
      throw new FatalException("Impossible de se déconnecter !");
    }
    return true;
  }

  private UserDto login(HttpServletRequest req, HttpServletResponse resp) {
    UserDto user;
    UserDto dto = this.factory.getUserDto();
    dto.setPseudo(req.getParameter("pseudo"));
    dto.setMotDePasse(req.getParameter("password"));
    user = userUcc.connect(dto);
    req.getSession().setAttribute("user", user);
    HashMap<String, Object> map = new HashMap();
    map.put("user", user);
    map.put("ip", req.getRemoteAddr());
    Cookie cookie = new Cookie("user", new JWTSigner(secret).sign(map));
    cookie.setPath("/");
    cookie.setMaxAge(60 * 60 * 24 * 365);
    resp.addCookie(cookie);
    return user;
  }

  private UserDto userData(HttpServletRequest req) {
    Integer userId = null;
    UserDto dto = null;
    try {
      dto = (UserDto) req.getSession().getAttribute("user");
    } catch (ClassCastException exc) {
      return null;
    }
    if (dto != null) {
      return dto;
    }
    Cookie cookie = findCookie(req, "user");
    if (cookie == null) {
      return null;
    }
    Map map;
    try {
      map = new JWTVerifier(secret).verify(cookie.getValue());
      if (map == null) {
        return null;
      }
    } catch (InvalidKeyException | NoSuchAlgorithmException | IllegalStateException
        | SignatureException | IOException | JWTVerifyException exc) {
      return null;
    }
    if (!req.getRemoteAddr().equals(map.get("ip"))) {
      cookie.setMaxAge(0);
      return null;
    }
    try {
      userId = (Integer) map.get("login");
    } catch (ClassCastException exc) {
      return null;
    }
    if (userId == null) {
      return null;
    }
    dto = userUcc.getUserFromId(userId);
    if (dto == null) {
      cookie.setMaxAge(0);
      return null;
    }

    req.getSession().setAttribute("user", dto);

    return dto;
  }

  private Cookie findCookie(HttpServletRequest req, String name) {
    Cookie[] cookies = req.getCookies();
    Cookie ret = null;
    if (cookies == null) {
      return null;
    }
    for (Cookie c : cookies) {
      if (name.equals(c.getName()) && c.getSecure()) {
        ret = c;
      } else if (name.equals(c.getName()) && ret == null) {
        ret = c;
      }
    }
    return ret;
  }


}
