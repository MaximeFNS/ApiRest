package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.blo.VilleFranceBLO;

public class VilleFranceDAO extends DAO<VilleFranceBLO>{
	
	private static final String NOM_ENTITE = "VilleFrance";
	private static final String ATTRIBUT_CODE_COMMUNE = "code_commune_INSEE";
	private static final String ATTRIBUT_NOM_COMMUNE = "nom_Commune";
	private static final String ATTRIBUT_CODE_POSTAL = "code_Postal";
	private static final String ATTRIBUT_LIBELLE = "libelle_acheminement";
	private static final String ATTRIBUT_LIGNE_5 = "ligne_5";
	private static final String ATTRIBUT_LATITUDE= "latitude";
	private static final String ATTRIBUT_LONGITUDE= "longitude";
	
	private static final String[] ATTRIBUTS_NOMS = {ATTRIBUT_CODE_COMMUNE, ATTRIBUT_NOM_COMMUNE, 
			ATTRIBUT_CODE_POSTAL, ATTRIBUT_LIBELLE,ATTRIBUT_LIGNE_5,ATTRIBUT_LATITUDE,ATTRIBUT_LONGITUDE};
	
	private static Logger logger = Logger.getLogger(VilleFranceDAO.class.getName());
	
	VilleFranceDAO(DAOFactory daoFactory) {
		super(daoFactory);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void creer(VilleFranceBLO objet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<VilleFranceBLO> trouver(VilleFranceBLO objet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modifier(VilleFranceBLO objet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void supprimer(VilleFranceBLO objet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<VilleFranceBLO> lister(){
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<VilleFranceBLO> vfBLO = new ArrayList<VilleFranceBLO>();
        try {
            // création d'une connexion grâce à la DAOFactory placée en attribut de la classe
            connection = this.creerConnexion();
            preparedStatement = connection.prepareStatement("SELECT * From Ville_France");
            resultSet = preparedStatement.executeQuery();
            // récupération des valeurs des attributs de la BDD pour les mettre dans une liste
            while (resultSet.next()) {
            	vfBLO.add(this.map(resultSet));
            }
            resultSet.close();
        } catch (SQLException e) {
            logger.log(Level.WARN, "Échec du listage des objets.", e);
        }
        return vfBLO;
	}
	
	protected Connection creerConnexion() throws SQLException {
        return this.getDaoFactory().getConnection();
    }
	
	private VilleFranceBLO map(ResultSet resultSet) throws SQLException {
		VilleFranceBLO vf = new VilleFranceBLO();
		
        vf.setCode_commune_INSEE(resultSet.getString(ATTRIBUT_CODE_COMMUNE));
        vf.setNom_Commune(resultSet.getString(ATTRIBUT_NOM_COMMUNE));
        vf.setCode_Postal(resultSet.getString(ATTRIBUT_CODE_POSTAL));
        vf.setLibelle_acheminement(resultSet.getString(ATTRIBUT_LIBELLE));
        vf.setLigne_5(resultSet.getString(ATTRIBUT_LIGNE_5));
        vf.setLatitude(resultSet.getString(ATTRIBUT_LATITUDE));
        vf.setLongitude(resultSet.getString(ATTRIBUT_LONGITUDE));
        
        return vf;
    }
	
	
}
