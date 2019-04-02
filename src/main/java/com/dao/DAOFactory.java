package com.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


import com.dao.DAOException;

public class DAOFactory {
	
	private static final String FICHIER_PROPERTIES = "src/main/ressources/application.properties";
    private static final String PROPERTY_URL = "url";
    private static final String PROPERTY_DRIVER = "driver";
    private static final String PROPERTY_NOM_UTILISATEUR = "nomUtilisateur";
    private static final String PROPERTY_MOT_DE_PASSE = "motDePasse";

    private String url;
    private String nomUtilisateur;
    private String motDePasse;

    private HikariDataSource dataSource;

    /**
     * Constructeur de la Factory permettant une simple connexion à la base de données.
     *
     * @param daoFactory la Factory permettant la création d'une connexion à la BDD.
     */
    public DAOFactory(String url, String nomUtilisateur, String motDePasse) {
        this.url = url;
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
    }

    /**
     * Constructeur de la Factory permettant une connexion au pool de connexions.
     *
     * @param daoFactory la Factory permettant la création d'une connexion à la BDD.
     */
    private DAOFactory(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Récupère une instance de la Factory permettant une simple connexion à la base de données.
     *
     * @return l'instance de la Factory.
     */
    public static DAOFactory getInstance() {
        String[] proprietes = chargerProprietes();
        return new DAOFactory(proprietes[0], proprietes[1], proprietes[2]);
    }

    /**
     * Récupère une instance de la Factory permettant une connexion au pool de connexions.
     *
     * @return l'instance de la Factory.*/
     

    /**
     * Charge les propriétés de connexion à la base de données.
     *
     * @return le tableau de Strings contenant les propriétés de connexion à la base de données.
     * @throws DAOConfigurationException
     */
    
	private static String[] chargerProprietes() {
        Properties properties = new Properties();
        String url;
        String driver;
        String nomUtilisateur;
        String motDePasse;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream(FICHIER_PROPERTIES);

        if (fichierProperties == null) {
            throw new DAOException("Le fichier properties " + FICHIER_PROPERTIES + " est introuvable.");
        }

        try {
            properties.load(fichierProperties);
            url = properties.getProperty(PROPERTY_URL);
            driver = properties.getProperty(PROPERTY_DRIVER);
            nomUtilisateur = properties.getProperty(PROPERTY_NOM_UTILISATEUR);
            motDePasse = properties.getProperty(PROPERTY_MOT_DE_PASSE);
        } catch (FileNotFoundException e) {
            throw new DAOException("Le fichier properties " + FICHIER_PROPERTIES + " est introuvable.", e);
        } catch (IOException e) {
            throw new DAOException("Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e);
        }

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new DAOException("Le driver est introuvable dans le classpath.", e);
        }

        return new String[]{url, nomUtilisateur, motDePasse};
    }

    /**
     * Fournit une connexion à la base de données ou au pool de connexions.
     *
     * @return connection la connexion à la base de données ou au pool de connexions.
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        // si on souhaite une simple connexion à la BDD
        if (this.url != null || this.nomUtilisateur != null || this.motDePasse != null) {
            return DriverManager.getConnection(this.url, this.nomUtilisateur, this.motDePasse);
        } else { // sinon, on souhaite une connexion au pool de connexions
            return this.dataSource.getConnection();
        }
    }
    
    public VilleFranceDAO getVilleFranceDao() {
        return new VilleFranceDAO(this);
    }

}
