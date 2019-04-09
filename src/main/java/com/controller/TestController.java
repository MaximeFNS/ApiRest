package com.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blo.VilleFranceBLO;
import com.dao.VilleFranceDAO;
import com.dao.DAOFactory;

@RestController
//@RequestMapping("/path");
public class TestController extends HttpServlet {

	VilleFranceDAO villefranceDAO;

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public List<String> get(@RequestParam(required = true, value = "value") String value,
			@RequestParam(required = false, value = "value2") String value2) {
		
		//Affichetoute les villes ou seulement le nom ou le nom et le code en fonction des paramètres
		DAOFactory daoFactory = new DAOFactory(
				"jdbc:mysql://localhost/maven?&LegacyDatetimeCode=false&serverTimezone=Europe/Paris", "maven", "maven");

		this.villefranceDAO = daoFactory.getVilleFranceDao();
		List<VilleFranceBLO> villes = villefranceDAO.lister();
		List<String> villests = new ArrayList();
		
		if (value.contains("nom")) {

			for (int i = 0; i < villes.size(); i++) {
				villests.add("Nom de la Ville n° " + i + " : " + villes.get(i).getNom_commune());
			}

		} else if (value.contains("code") && value2.contains("nom")) {

			for (int i = 0; i < villes.size(); i++) {
				villests.add("Code & Nom n° " + i + " : " + villes.get(i).getCode_commune_INSEE() + " , " 
			+ villes.get(i).getNom_commune());
			}
		} else {

			for (int i = 0; i < villes.size(); i++) {
				villests.add("Ville n° " + i + " : " + villes.get(i).toString());
			}
		}
		return villests;

	}

	@RequestMapping(value = "/post", method = RequestMethod.POST)
	@ResponseBody
	public void post(@RequestParam(required = false, value = "value") String nom, 
			@RequestParam(required = false, value = "value2") String nom2) {
		
		//ChangerCodePostal
		DAOFactory daoFactory = new DAOFactory(
				"jdbc:mysql://localhost/maven?&LegacyDatetimeCode=false&serverTimezone=Europe/Paris", "maven", "maven");

		this.villefranceDAO = daoFactory.getVilleFranceDao();
		VilleFranceBLO blo = new VilleFranceBLO();

		blo.setCode_postal(nom);
		String nouveauCP = nom2;
		this.villefranceDAO.modifierCodePostal(blo, nouveauCP);
		

	}

	@RequestMapping(value = "/put", method = RequestMethod.PUT)
	@ResponseBody
	public void put(@RequestParam(required = false, value = "value") String nom, 
			@RequestParam(required = false, value = "value2") String nom2) {
		
		//ChangerCodePostal
		DAOFactory daoFactory = new DAOFactory(
				"jdbc:mysql://localhost/maven?&LegacyDatetimeCode=false&serverTimezone=Europe/Paris", "maven", "maven");

		this.villefranceDAO = daoFactory.getVilleFranceDao();
		VilleFranceBLO blo = new VilleFranceBLO();

		blo.setCode_postal(nom);
		String nouveauCP = nom2;
		this.villefranceDAO.modifierCodePostal(blo, nouveauCP);
		

	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseBody
	public void delete(@RequestParam(required = false, value = "value")String code) {
		
		//suppression en fonction du code Postal
		DAOFactory daoFactory = new DAOFactory(
				"jdbc:mysql://localhost/maven?&LegacyDatetimeCode=false&serverTimezone=Europe/Paris", "maven", "maven");

		this.villefranceDAO = daoFactory.getVilleFranceDao();
		VilleFranceBLO blo = new VilleFranceBLO();

		blo.setCode_postal(code);
		
		this.villefranceDAO.supprimer(blo);

	}

}
