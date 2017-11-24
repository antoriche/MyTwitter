package business.user;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

public class UserImpl implements User {
  private int utilisateurId;
  private String email;
  private String pseudo;
  private String nom;
  private String prenom;
  private String motDePasse;
  private LocalDate dateInscription;

  private int version;


  public int getVersion() {
    return version;
  }


  public void setVersion(int version) {
    this.version = version;
  }



  public UserImpl() {
    super();
  }

  public int getUtilisateurId() {
    return utilisateurId;
  }


  public String getEmail() {
    return email;
  }


  public String getPseudo() {
    return pseudo;
  }


  public String getNom() {
    return nom;
  }


  public String getPrenom() {
    return prenom;
  }


  public String getMotDePasse() {
    return motDePasse;
  }


  public LocalDate getDateInscription() {
    return dateInscription;
  }


  public void setPseudo(String pseudo) {
    this.pseudo = pseudo;
  }

  public void setMotDePasse(String motDePasse) {
    this.motDePasse = motDePasse;
  }



  public void setUtilisateurId(int utilisateurId) {
    this.utilisateurId = utilisateurId;
  }


  public void setEmail(String email) {
    this.email = email;
  }


  public void setNom(String nom) {
    this.nom = nom;
  }


  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }



  public void setDateInscription(LocalDate dateInscription) {
    this.dateInscription = dateInscription;
  }


  @Override
  public boolean checkPassword(String password) {
    try {
      return BCrypt.checkpw(password, this.motDePasse);
    } catch (Exception exc) {
      return false;
    }
  }



  @Override
  public String toString() {
    return "UserImpl [utilisateurId=" + utilisateurId + ", email=" + email + ", pseudo=" + pseudo
        + ", nom=" + nom + ", prenom=" + prenom + ", motDePasse=" + motDePasse
        + ", dateInscription=" + dateInscription + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((pseudo == null) ? 0 : pseudo.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    UserImpl other = (UserImpl) obj;
    if (pseudo == null) {
      if (other.pseudo != null) {
        return false;
      }
    } else if (!pseudo.equals(other.pseudo)) {
      return false;
    }
    return true;
  }

  @Override
  public boolean checkValidite() {
    if (this.getDateInscription() == null || this.getEmail() == null || this.getMotDePasse() == null
        || this.getNom() == null || this.getPrenom() == null || this.getPseudo() == null) {
      return false;
    }

    final Pattern VALID_EMAIL_ADDRESS_REGEX =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(this.getEmail());
    if (!matcher.find()) {
      return false;
    }

    if (this.getEmail().length() > 50 || this.getPseudo().length() > 50
        || this.getNom().length() > 50 || this.getPrenom().length() > 50
        || this.getEmail().length() == 0 || this.getPseudo().length() == 0
        || this.getNom().length() == 0 || this.getPrenom().length() == 0) {
      return false;
    }
    return true;
  }



}
