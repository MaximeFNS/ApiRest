package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Classe Utilitaire pour les DAO.
 *
 * <p>Cette classe centralise des attributs et des méthodes utilisés par les DAO.</p>
 *
 * @author Thomas MENARD et Maxime LENORMAND
 */
public final class DAOUtilitaire {

    // attributs des NoteInteret
    protected static final String ATTRIBUT_ID_PROFESSEUR = "idProfesseur";
    protected static final String ATTRIBUT_ID_SUJET = "idSujet";
    protected static final String ATTRIBUT_NOTE = "note";

    private static final String SELECT = "SELECT";
    private static final String UPDATE = "UPDATE";
    private static final String DELETE = "DELETE";
    private static final String WHERE = " WHERE ";
    private static final String WHERE_ID = " WHERE id";
    private static final String AND = " AND ";
    private static final String AND_ID = " AND id";

    private static Logger logger = Logger.getLogger(DAOUtilitaire.class.getName());

    /**
     * Constructeur caché par défaut (car c'est une classe finale utilitaire,
     * contenant uniquement des méthodes appelées de manière statique).
     */
    private DAOUtilitaire() {

    }


    // ########################################################################################################
    // #                            Methodes pour la fermeture des ressources                                 #
    // ########################################################################################################

    /**
     * Ferme le resultset.
     *
     * @param resultSet le resultSet à fermer.
     */
    protected static void fermeture(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                logger.log(Level.WARN, "Echec de la fermeture du ResultSet : " + e.getMessage(), e);
            }
        }
    }

    /**
     * Ferme le statement.
     *
     * @param statement le statement à fermer.
     */
    protected static void fermeture(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.log(Level.WARN, "Echec de la fermeture du Statement : " + e.getMessage(), e);
            }
        }
    }

    /**
     * Ferme la connection.
     *
     * @param connection la connection à fermer.
     */
    protected static void fermeture(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.log(Level.WARN, "Echec de la fermeture de la connexion : " + e.getMessage(), e);
            }
        }
    }

    /**
     * Ferme le statement et la connection.
     *
     * @param statement  le statement à fermer.
     * @param connection la connection à fermer.
     */
    protected static void fermetures(Statement statement, Connection connection) {
        fermeture(statement);
        fermeture(connection);
    }

    /**
     * Ferme le resultSet, le statement et la connection.
     *
     * @param resultSet  le resultSet à fermer.
     * @param statement  le statement à fermer.
     * @param connection la connection à fermer.
     */
    protected static void fermetures(ResultSet resultSet, Statement statement, Connection connection) {
        fermeture(resultSet);
        fermeture(statement);
        fermeture(connection);
    }


    // ########################################################################################################
    // #                            Methodes pour la création des requêtes préparées                          #
    // ########################################################################################################

    /**
     * Initialise une requête préparée.
     *
     * @param connection          la connexion à la BDD.
     * @param sql                 la requête SQL.
     * @param returnGeneratedKeys le boolean qui permet de générer des ID ou pas.
     * @param objets              la liste d'objets à insérer dans la requête.
     * @return preparedStatement la requête préparée initialisée.
     * @throws SQLException
     */
    protected static PreparedStatement initialisationRequetePreparee(Connection connection, String sql,
                                                                     boolean returnGeneratedKeys, Object... objets) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql,
                returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
        for (int i = 0; i < objets.length; i++) {
            preparedStatement.setObject(i + 1, objets[i]);
        }
        return preparedStatement;
    }

    /**
     * Initialise une requête préparée modulable.
     *
     * @param connection          la connexion à la BDD.
     * @param choixCRUD           le choix de la méthode CRUD à appliquer.
     * @param nomEntite           le nom de l'entité de la BDD concernée.
     * @param attributs           le tableau de Strings regroupant tous les noms des attributs d'un objet
     *                            ainsi que les valeurs correspondantes.
     * @param returnGeneratedKeys le boolean qui permet de générer des ID ou pas.
     * @return preparedStatement la requête préparée initialisée.
     * @throws SQLException
     */
    protected static PreparedStatement initialisationRequetePreparee(Connection connection, String choixCRUD,
                                                                     String nomEntite, String[][] attributs, boolean returnGeneratedKeys) throws SQLException {
        // création de la requête préparée
        List<String> valeursAttributs = creationRequete(choixCRUD, nomEntite, attributs);
        String sql = valeursAttributs.remove(0);

        // changement de la structure de la requête si le choix de la méthode CRUD est "UPDATE"
        if (UPDATE.equals(choixCRUD)) {
            sql = sql.replace(AND_ID + nomEntite + " = ?", "");
            sql = sql.replace("AND", ", ");
            sql += WHERE_ID + nomEntite + " = ?";
        }

        // remplacement des "?" par les valeurs des attributs
        PreparedStatement preparedStatement = connection.prepareStatement(sql,
                returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
        for (int i = 0; i < valeursAttributs.size(); i++) {
            preparedStatement.setObject(i + 1, valeursAttributs.get(i));
        }

        return preparedStatement;
    }

    /**
     * Initialise une requête préparée modulable.
     * <p>Cas du traitement des ID mais aussi des Strings. Attention : nomColonne doit posséder valeurColonne.</p>
     *
     * @param connection    la connexion à la BDD.
     * @param choixCRUD     le choix de la méthode CRUD à appliquer.
     * @param nomEntite     le nom de l'entité de la BDD concernée.
     * @param idEntite      l'ID de l'entité.
     * @param nomColonne    le nom de la colonne manipulée.
     * @param valeurColonne la valeur recherchée dans la colonne.
     * @param attributs     le tableau de Strings regroupant tous les noms des attributs d'un objet
     *                      ainsi que les valeurs correspondantes.
     * @return preparedStatement la requête préparée.
     * @throws SQLException
     */
    protected static PreparedStatement initialisationRequetePreparee(Connection connection, String choixCRUD,
                                                                     String nomEntite, String idEntite, String nomColonne, String valeurColonne, String[][] attributs,
                                                                     boolean returnGeneratedKeys) throws SQLException {
        // création de la requête préparée
        List<String> valeursAttributs = creationRequete(choixCRUD, nomEntite, attributs);
        String sql = valeursAttributs.remove(0);

        // changement de la structure de la requête si le choix de la méthode CRUD est "UPDATE" ou "DELETE"
        if (UPDATE.equals(choixCRUD)) {
            sql = sql.replace("AND " + idEntite + " = ?", "");
            sql = sql.replace(AND, ", ");
            sql += WHERE + idEntite + " = ?";
            sql += AND + nomColonne + " LIKE '%" + valeurColonne + "%'";
        } else if (DELETE.equals(choixCRUD)) {
            sql += AND + nomColonne + " LIKE '%" + valeurColonne + "%'";
        }

        // remplacement des "?" par les valeurs des attributs
        PreparedStatement preparedStatement = connection.prepareStatement(sql,
                returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
        for (int i = 0; i < valeursAttributs.size(); i++) {
            preparedStatement.setObject(i + 1, valeursAttributs.get(i));
        }

        return preparedStatement;
    }

    /**
     * Initialise une requête préparée modulable.
     * <p>Cas de deux ID comme clés primaires.</p>
     *
     * @param connection   la connexion à la BDD.
     * @param choixCRUD    le choix de la méthode CRUD à appliquer.
     * @param nomEntite    le nom de l'entité de la BDD concernée.
     * @param attributs    le tableau de Strings regroupant tous les noms des attributs d'un objet
     *                     ainsi que les valeurs correspondantes.
     * @param nomEntiteId1 nom de l'entité possédant la première clé primaire.
     * @param nomEntiteId2 nom de l'entité possédant la seconde clé primaire.
     * @return preparedStatement la requête préparée initialisée.
     * @throws SQLException
     */
    protected static PreparedStatement initialisationRequetePreparee(Connection connection, String choixCRUD,
                                                                     String nomEntite, String[][] attributs, String nomEntiteId1, String nomEntiteId2,
                                                                     boolean returnGeneratedKeys) throws SQLException {
        // création de la requête préparée
        List<String> valeursAttributs = creationRequete(choixCRUD, nomEntite, attributs);
        String sql = valeursAttributs.remove(0);

        // changement de la structure de la requête si le choix de la méthode CRUD est "UPDATE"
        if (UPDATE.equals(choixCRUD)) {
            sql = sql.replace(AND_ID + nomEntiteId1 + " = ?", "");
            sql = sql.replace(AND_ID + nomEntiteId2 + " = ?", "");
            sql = sql.replace(AND, ", ");
            sql += WHERE_ID + nomEntiteId1 + " = ?";
            sql += AND_ID + nomEntiteId2 + " = ?";
        }

        // remplacement des "?" par les valeurs des attributs
        PreparedStatement preparedStatement = connection.prepareStatement(sql,
                returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
        for (int i = 0; i < valeursAttributs.size(); i++) {
            preparedStatement.setObject(i + 1, valeursAttributs.get(i));
        }

        return preparedStatement;
    }


    /**
     * Initialise une requête préparée modulable.
     * <p>Cas de trois ID comme clés primaires.</p>
     *
     * @param connection   la connexion à la BDD.
     * @param choixCRUD    le choix de la méthode CRUD à appliquer.
     * @param nomEntite    le nom de l'entité de la BDD concernée.
     * @param attributs    le tableau de Strings regroupant tous les noms des attributs d'un objet
     *                     ainsi que les valeurs correspondantes.
     * @param nomEntiteId1 nom de l'entité possédant la première clé primaire.
     * @param nomEntiteId2 nom de l'entité possédant la seconde clé primaire.
     * @param nomEntiteId3 nom de l'entité possédant la troisième clé primaire.
     * @return preparedStatement la requête préparée initialisée.
     * @throws SQLException
     */
    protected static PreparedStatement initialisationRequetePreparee(Connection connection, String choixCRUD, String nomEntite, String[][] attributs, String nomEntiteId1, String nomEntiteId2, String nomEntiteId3, boolean returnGeneratedKeys) throws SQLException {
        // création de la requête préparée
        List<String> valeursAttributs = creationRequete(choixCRUD, nomEntite, attributs);
        String sql = valeursAttributs.remove(0);

        // changement de la structure de la requête si le choix de la méthode CRUD est "UPDATE"
        if (UPDATE.equals(choixCRUD)) {
            sql = sql.replace(AND_ID + nomEntiteId1 + " = ?", "");
            sql = sql.replace(AND_ID + nomEntiteId2 + " = ?", "");
            sql = sql.replace(AND_ID + nomEntiteId3 + " = ?", "");
            sql = sql.replace(AND, ", ");
            sql += WHERE_ID + nomEntiteId1 + " = ?";
            sql += AND_ID + nomEntiteId2 + " = ?";
            sql += AND_ID + nomEntiteId3 + " = ?";
        }

        // remplacement des "?" par les valeurs des attributs
        PreparedStatement preparedStatement = connection.prepareStatement(sql, returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
        for (int i = 0; i < valeursAttributs.size(); i++) {
            preparedStatement.setObject(i + 1, valeursAttributs.get(i));
        }

        return preparedStatement;
    }

    /**
     * Traite la mise à jour de la BDD induite par la méthode void modifier(T objet).
     *
     * @param daoFactory la DAOFactory qui permet de créer une connexion.
     * @param nomEntite  le nom de l'entité à traiter.
     * @param attributs  les attributs de l'entité à traiter.
     * @param logger     le logger associé à l'entité à traiter.
     */
    protected static void traitementUpdate(DAOFactory daoFactory, String nomEntite, String[][] attributs,
                                           Logger logger) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // création d'une connexion grâce à la DAOFactory placée en attribut de la classe
            connection = daoFactory.getConnection();
            // mise en forme de la requête UPDATE en fonction des attributs de l'objet
            preparedStatement = initialisationRequetePreparee(connection, "UPDATE", nomEntite, attributs,
                    false);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.WARN, "Échec de la mise à jour de l'objet, aucune ligne modifiée dans la table.", e);
        } finally {
            // fermeture des ressources utilisées
            fermetures(preparedStatement, connection);
        }
    }

    /**
     * Crée la requête préparée.
     *
     * @param choixCRUD le choix de la méthode CRUD à appliquer.
     * @param nomEntite le nom de l'entité de la BDD concernée.
     * @param attributs le tableau de Strings regroupant tous les noms des attributs d'un objet
     *                  ainsi que les valeurs correspondantes.
     * @return valeursAttributs la requête préparée créée et les valeurs des attributs.
     */
    private static ArrayList<String> creationRequete(String choixCRUD, String nomEntite, String[][] attributs) {
        // début de la requête en fonction du choix de la méthode CRUD
        String sql = creationDebutRequete(choixCRUD, nomEntite);

        // création de la requête et stockage des valeurs des attributs
        ArrayList<String> valeursAttributs = new ArrayList<String>();
        boolean dejaFait = false;
        for (int i = 0; i < attributs[0].length; i++) {
            // si la valeur de l'attribut courant n'est pas nulle
            if (attributs[1][i] != null && attributs[1][i] != "null") {
                // on la stocke dans une liste
                valeursAttributs.add(attributs[1][i]);
                // on continue la création de la requête
                if (!dejaFait) {
                    sql += attributs[0][i] + " = ?";
                    dejaFait = true;
                } else {
                    sql += AND + attributs[0][i] + " = ?";
                }
            }
        }
        valeursAttributs.add(0, sql);

        return valeursAttributs;
    }

    /**
     * Crée le début de la requête préparée.
     *
     * @param choixCRUD le choix de la méthode CRUD à appliquer.
     * @param nomEntite le nom de l'entité de la BDD concernée.
     * @return sql le début de la requête créée.
     */
    private static String creationDebutRequete(String choixCRUD, String nomEntite) {
        String sql;

        if (SELECT.equals(choixCRUD)) {
            sql = "SELECT * FROM " + nomEntite + WHERE;
        } else if (DELETE.equals(choixCRUD)) {
            sql = "DELETE FROM " + nomEntite + WHERE;
        } else if (UPDATE.equals(choixCRUD)) {
            sql = "UPDATE " + nomEntite + " SET ";
        } else {
            throw new DAOException("Erreur : choixCRUD ne peut être que 'SELECT', 'DELETE' ou 'UPDATE'");
        }

        return sql;
    }

}