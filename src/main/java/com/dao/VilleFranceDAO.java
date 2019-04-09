package com.dao;

import static com.dao.DAOUtilitaire.fermetures;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.blo.VilleFranceBLO;

public class VilleFranceDAO extends DAO<VilleFranceBLO>{
	
	private static final String NOM_ENTITE = "VilleFrance";
	private static final String ATTRIBUT_CODE_COMMUNE = "Code_commune_INSEE";
	private static final String ATTRIBUT_NOM_COMMUNE = "Nom_Commune";
	private static final String ATTRIBUT_CODE_POSTAL = "Code_Postal";
	private static final String ATTRIBUT_LIBELLE = "Libelle_acheminement";
	private static final String ATTRIBUT_LIGNE_5 = "Ligne_5";
	private static final String ATTRIBUT_LATITUDE= "Latitude";
	private static final String ATTRIBUT_LONGITUDE= "Longitude";
	
	private static final String[] ATTRIBUTS_NOMS = {ATTRIBUT_CODE_COMMUNE, ATTRIBUT_NOM_COMMUNE, 
			ATTRIBUT_CODE_POSTAL, ATTRIBUT_LIBELLE,ATTRIBUT_LIGNE_5,ATTRIBUT_LATITUDE,ATTRIBUT_LONGITUDE};
	
	private static Logger logger = Logger.getLogger(VilleFranceDAO.class.getName());
	
	public VilleFranceDAO(DAOFactory daoFactory) {
		super(daoFactory);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void creer(VilleFranceBLO objet) {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
           
            connection = this.creerConnexion();
          
            preparedStatement = connection.prepareStatement("INSERT INTO `ville_france`(`Code_commune_INSEE`, `Nom_commune`, `Code_postal`, `Libelle_acheminement`, "
            		+ "`Ligne_5`, `Latitude`, `Longitude`)" 
            + "VALUES ('" + objet.getCode_commune_INSEE() + "' ,'" + objet.getNom_commune() + "' ," +objet.getCode_postal() + " ," 
            		+ objet.getLibelle_acheminement() + " ," + objet.getLigne_5() + " ," + objet.getLatitude() + " ," 
            + objet.getLongitude() + ")");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.WARN, "Échec de la création de l'objet, aucune ligne ajoutée dans la table.", e);
        } finally {
            
            fermetures(preparedStatement, connection);
        }
		
	}

	@Override
	public List<VilleFranceBLO> trouver(VilleFranceBLO objet) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void modifierCodePostal(VilleFranceBLO objet, String string) {

		Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;
        String test = objet.getCode_postal();
        
        try {
            //Création d'une connexion grâce à la DAOFactory placée en attribut de la classe
            connection = this.creerConnexion();
            
            //Exécution des requêtes afin de valider une sur un sujet et annuler les candidatures des autres
            preparedStatement = connection.prepareStatement("UPDATE `ville_france` SET  `Code_postal` = " + string + " WHERE `Code_postal` = " + test);
        
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.WARN, "Échec du listage des objets.", e);
        } finally {
            fermetures(resultSet, preparedStatement, connection);
        }
		
	}

	@Override
	public void supprimer(VilleFranceBLO objet) {
		Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;
        String test = objet.getCode_postal();
        
        try {
            //Création d'une connexion grâce à la DAOFactory placée en attribut de la classe
            connection = this.creerConnexion();
            
            //Exécution des requêtes afin de valider une sur un sujet et annuler les candidatures des autres
            System.out.println(test); 
            preparedStatement = connection.prepareStatement("DELETE FROM `ville_france` WHERE Code_postal = " + test);
        
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.WARN, "Échec du listage des objets.", e);
        } finally {
            fermetures(resultSet, preparedStatement, connection);
        }
		
	}

	@Override
	public void modifier(VilleFranceBLO objet) {
		// TODO Auto-generated method stub
		
	}
	
	protected Connection creerConnexion() throws SQLException {
        return this.getDaoFactory().getConnection();
    }
	
	public VilleFranceBLO map(ResultSet resultSet) throws SQLException {
		VilleFranceBLO vf = new VilleFranceBLO();
		
        vf.setCode_commune_INSEE(resultSet.getString(ATTRIBUT_CODE_COMMUNE));
        vf.setNom_commune(resultSet.getString(ATTRIBUT_NOM_COMMUNE));
        vf.setCode_postal(resultSet.getString(ATTRIBUT_CODE_POSTAL));
        vf.setLibelle_acheminement(resultSet.getString(ATTRIBUT_LIBELLE));
        vf.setLigne_5(resultSet.getString(ATTRIBUT_LIGNE_5));
        vf.setLatitude(resultSet.getString(ATTRIBUT_LATITUDE));
        vf.setLongitude(resultSet.getString(ATTRIBUT_LONGITUDE));
        
        return vf;
    }

	public List<VilleFranceBLO> lister() {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<VilleFranceBLO> vfBLO = new ArrayList<VilleFranceBLO>();
        String resultat ="";
        try {
            // création d'une connexion grâce à la DAOFactory placée en attribut de la classe
            connection = this.creerConnexion();
            System.out.println(connection.toString());
            preparedStatement = connection.prepareStatement("SELECT `Code_commune_INSEE`, `Nom_commune`, `Code_postal`, "
            		+ "`Libelle_acheminement`, `Ligne_5`, `Latitude`, `Longitude` FROM `ville_france` ");
            resultSet = preparedStatement.executeQuery();
            // récupération des valeurs des attributs de la BDD pour les mettre dans une liste
            while (resultSet.next()) {
            	System.out.println(resultSet.toString());
            	vfBLO.add(this.map(resultSet));
            }
            
        } catch (SQLException e) {
            logger.log(Level.WARN, "Échec du listage des objets.", e);
        } finally {
            fermetures(resultSet, preparedStatement, connection);
        }
        
        return vfBLO;
	}
	
	
}
