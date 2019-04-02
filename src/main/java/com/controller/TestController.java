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
	public List<String> get(@RequestParam(required = false, value = "value") String value,
			@RequestParam(required = false, value = "value2") String value2) {

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
	public String post(@RequestParam(required = false, value = "value") String value) {

		System.out.println("Appel POST");
		System.out.println("value :  " + value);

		return value;

	}

	@RequestMapping(value = "/put", method = RequestMethod.PUT)
	@ResponseBody
	public String put(@RequestParam(required = false, value = "value") String value) {

		System.out.println("Appel POST");
		System.out.println("value :  " + value);

		return value;

	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseBody
	public String delete(@RequestParam(required = false, value = "value") String value) {

		System.out.println("Appel POST");
		System.out.println("value :  " + value);

		return value;

	}

}
