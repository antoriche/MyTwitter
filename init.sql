DROP SCHEMA jsb CASCADE;
CREATE SCHEMA jsb;

CREATE TABLE jsb.utilisateurs(
utilisateur_id SERIAL PRIMARY KEY,
email VARCHAR(50) NOT NULL,
pseudo VARCHAR(50) NOT NULL,
mot_de_passe VARCHAR(200) NOT NULL,
date_inscription TIMESTAMP NOT NULL,
nom VARCHAR(50) NOT NULL,
prenom VARCHAR(50) NOT NULL,
version_utilisateur INT NOT NULL);

CREATE TABLE jsb.tweet(
tweet_id SERIAL PRIMARY KEY,
date_tweet TIMESTAMP NOT NULL,
text_tweet VARCHAR(140) NOT NULL,
proprietaire INTEGER NOT NULL REFERENCES jsb.utilisateurs(utilisateur_id),
version_utilisateur INT NOT NULL);



--INSERT INTO jsb.utilisateurs 
--VALUES (DEFAULT, 'antoriche.ar@gmail.com', 'anto', '$2a$10$gqVw2JEjIeKGi7ktgqV2vOh9GXrsnwd/bHarGjJSMgyjNpVYjTWnm', '2000-01-01', 'Antonin', 'Riche', 0);
