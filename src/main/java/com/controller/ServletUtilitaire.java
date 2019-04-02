package com.controller;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

//import org.mindrot.jbcrypt.BCrypt;

import com.blo.VilleFranceBLO;


/**
 * Classe Utilitaire pour les servlets.
 *
 * <p>Cette classe centralise des attributs et des méthodes utilisés par les servlets.</p>
 *
 * @author Hugo MENARD et Maxime LENORMAND
 */
public final class ServletUtilitaire {

    protected static final String CONF_DAO_FACTORY = "daoFactory";

    /**
     * Constructeur caché par défaut (car c'est une classe finale utilitaire,
     * contenant uniquement des méthodes appelées de manière statique).
     */
    private ServletUtilitaire() {

    }

    /**
     * Récupère les informations choisies par l'utilisateur sous forme de String.
     *
     * @param request    la requête possédant les paramètres à récupérer.
     * @param listeInfos la liste de toutes les informations possibles.
     * @return infosChoisies les informations choisies par l'utilisateur sous forme de String.
     */
    protected static String getInfosChoisies(HttpServletRequest request, String[] listeInfos) {
        String infosChoisies = "";
        for (String choix : listeInfos) {
            if (request.getParameter(choix) != null) {
                infosChoisies += choix + ";";
            }
        }
        infosChoisies += "Autres;";
        infosChoisies += request.getParameter("autres");
        return infosChoisies;
    }

    /**
     * Permet d'avoir le nom du fichier passer en paramètre d'une requete doPost
     *
     * @param part, l'objet de type Part contenant le fichier
     * @return le nom du fichier
     */
    protected static String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf('=') + 2, s.length() - 1);
            }
        }
        return "";
    }

    /**
     * Retourne extension d'un fichier
     *
     * @param nomFichier
     * @return l'extension
     */
    protected static String avoirExtension(String nomFichier) {
        int i = nomFichier.lastIndexOf('.');
        if (i > 0 && i < nomFichier.length() - 1) {
            return nomFichier.substring(i + 1).toLowerCase();
        } else {
            return null;
        }
    }


    /**
     * Méthode vérifiant que le fichier rentré correspond bien à un csv
     *
     * @param nomDuFichier
     * @param fichier
     */
    protected static boolean verificationFichier(String nomDuFichier) {
        return ("csv".equals(ServletUtilitaire.avoirExtension(nomDuFichier)));
    }

    /**
     * Converit un InputStream en un String
     *
     * @param is, l'InputStram
     * @return la chaine de caractère
     */
	protected static String convertirStreamEnString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}