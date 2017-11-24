package business.user;

import java.time.LocalDate;

public interface UserDto {

  public int getUtilisateurId();

  public String getEmail();

  public String getPseudo();

  public String getNom();

  public String getPrenom();

  public String getMotDePasse();

  public LocalDate getDateInscription();

  public int getVersion();


  public void setVersion(int version);

  public void setPseudo(String pseudo);

  public void setMotDePasse(String motDePasse);

  public void setUtilisateurId(int utilisateurId);

  public void setEmail(String email);

  public void setNom(String nom);

  public void setPrenom(String prenom);

  public void setDateInscription(LocalDate dateInscription);


}
