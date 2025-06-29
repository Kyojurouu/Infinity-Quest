package Database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Objects.Admin;
import Objects.User;
import Objects.Content;
import Objects.Actor;
import Objects.Role;
import Objects.Cast;
import Objects.Director;

public class DatabaseHandler {
    private static DatabaseHandler handler = null;
    private static Statement stmt = null;
    private static PreparedStatement pstatement = null;

    public static String dburl = DatabaseCredentials.ignoreDburl;
    public static String userName = DatabaseCredentials.ignoreUserName;
    public static String password = DatabaseCredentials.ignorePassword;

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    public static Connection getDBConnection()
    {
        Connection connection = null;

        try
        {
            connection = DriverManager.getConnection(dburl, userName, password);

        } catch (Exception e){
            e.printStackTrace();
        }

        return connection;
    }

    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = getDBConnection().createStatement();
            result = stmt.executeQuery(query);
        }
        catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        }
        finally {
        }
        return result;
    }

// ============================ VALIDATE ADMIN LOGIN ===============================

    public static boolean validateadminLogin(String adminName, String adminPassword){

        getInstance();
        String query = "SELECT * FROM Admin WHERE adminName = '" + adminName + "' AND adminPassword = '" + adminPassword + "'";
            
        System.out.println(query);
    
        ResultSet result = handler.execQuery(query);
        try {
            if (result.next()) {
                return true;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

// ============================ CRUD ADMIN ===============================

    public static ResultSet getAdmin() {
        ResultSet result = null;
        try {
            String query = "SELECT * FROM Admin";
            result = handler.execQuery(query);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean createAdmin(Admin admin) {
        try {
            pstatement = getDBConnection().prepareStatement("INSERT INTO Admin (adminName, adminPassword) VALUES (?, ?)");
            pstatement.setString(1, admin.getAdminName());
            pstatement.setString(2, admin.getAdminPassword());

            return pstatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteAdmin(Admin admin) {
        try {
            pstatement = getDBConnection().prepareStatement("DELETE FROM Admin WHERE adminID = ?");
            pstatement.setInt(1, admin.getAdminID());

            int res = pstatement.executeUpdate();
            if (res > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateAdmin(Admin admin) {
        try {
            pstatement = getDBConnection().prepareStatement("UPDATE Admin SET adminName = ?, adminPassword = ? WHERE adminID = ?");
            pstatement.setString(1, admin.getAdminName());
            pstatement.setString(2, admin.getAdminPassword());
            pstatement.setInt(3, admin.getAdminID());

            int res = pstatement.executeUpdate();
            if (res > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

// ============================ CRUD USER ===============================

    public static ResultSet getUser() { 
    getInstance();
    ResultSet result = null;

        try {
            String query = "SELECT * FROM User";
            result = handler.execQuery(query);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static boolean createUser(User user) {
        try {
            pstatement = getDBConnection().prepareStatement("INSERT INTO User (userName, userPassword, userEmail, userBio, userProfile) VALUES (?, ?, ?, ?, ?)");
            pstatement.setString(1, user.getUserName());
            pstatement.setString(2, user.getUserPassword());
            pstatement.setString(3, user.getUserEmail());
            pstatement.setString(4, user.getUserBio());
            pstatement.setString(5, user.getUserProfile());

            return pstatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteUser(User user) {
        try {
            pstatement = getDBConnection().prepareStatement("DELETE FROM User WHERE userName = ?");
            pstatement.setString(1, user.getUserName());

            int res = pstatement.executeUpdate();
            if (res > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateUser(User user) {
        String sql = "UPDATE User SET userPassword=?, userEmail=?, userBio=?, userProfile=?, userName=? WHERE userID=?";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUserPassword());
            stmt.setString(2, user.getUserEmail());
            stmt.setString(3, user.getUserBio());
            stmt.setString(4, user.getUserProfile());
            stmt.setString(5, user.getUserName());
            stmt.setInt(6, user.getUserID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

// ============================ CRUD CONTENT ===============================

    public static ResultSet getContent() { 
        getInstance();
        ResultSet result = null;

    try {
        String query = "SELECT * FROM Content";
        result = handler.execQuery(query);
        }
     catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

public static ResultSet getAllDirectors() {
    getInstance();
    ResultSet result = null;

    try {
        String query = "SELECT directorID, directorName FROM Director ORDER BY directorName ASC";
        result = handler.execQuery(query);
    } catch (Exception e) {
        e.printStackTrace();
    }

    return result;
}

    public static boolean createContent(Content content) {
        try {
            pstatement = getDBConnection().prepareStatement(
                "INSERT INTO Content (contentTitle, contentRuntime, contentSeason, contentEpisode, contentReleaseDate, contentSynopsis, directorID, contentPhase, contentAgeRating, contentChronologicalOrder, contentPoster, contentTrailer) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            pstatement.setString(1, content.getContentTitle());
            pstatement.setString(2, content.getContentRuntime());

            if (content.getContentSeason() != null) {
                pstatement.setInt(3, content.getContentSeason());
            } else {
                pstatement.setNull(3, java.sql.Types.INTEGER);
            }

            if (content.getContentEpisode() != null) {
                pstatement.setInt(4, content.getContentEpisode());
            } else {
                pstatement.setNull(4, java.sql.Types.INTEGER);
            }

            if (content.getContentReleaseDate() != null) {
                pstatement.setString(5, content.getContentReleaseDate().toString());
            } else {
                pstatement.setNull(5, java.sql.Types.VARCHAR);
            }

            pstatement.setString(6, content.getContentSynopsis());
            pstatement.setInt(7, content.getDirectorID());
            pstatement.setInt(8, content.getContentPhase());
            pstatement.setString(9, content.getContentAgeRating());
            pstatement.setInt(10, content.getContentChronologicalOrder());
            pstatement.setString(11, content.getContentPoster());
            pstatement.setString(12, content.getContentTrailer());

            return pstatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public static boolean deleteContent(Content content) {

        try {
            pstatement = getDBConnection().prepareStatement("DELETE FROM Content WHERE contentID = ?");
            pstatement.setInt(1, content.getContentID());

            int res = pstatement.executeUpdate();
            if (res > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateContent(Content content) {
        try {
            pstatement = getDBConnection().prepareStatement(
                "UPDATE Content SET contentTitle = ?, contentRuntime = ?, contentSeason = ?, contentEpisode = ?, contentReleaseDate = ?, contentSynopsis = ?, directorID = ?, ContentPhase = ?, contentAgeRating = ?, contentChronologicalOrder = ?, contentPoster = ?, contentTrailer = ? WHERE contentID = ?"
            );

            pstatement.setString(1, content.getContentTitle());
            pstatement.setString(2, content.getContentRuntime());

            if (content.getContentSeason() != null) {
                pstatement.setInt(3, content.getContentSeason());
            } else {
                pstatement.setNull(3, java.sql.Types.INTEGER);
            }

            if (content.getContentEpisode() != null) {
                pstatement.setInt(4, content.getContentEpisode());
            } else {
                pstatement.setNull(4, java.sql.Types.INTEGER);
            }

            if (content.getContentReleaseDate() != null) {
                pstatement.setString(5, content.getContentReleaseDate().toString());
            } else {
                pstatement.setNull(5, java.sql.Types.VARCHAR);
            }

            pstatement.setString(6, content.getContentSynopsis());
            pstatement.setInt(7, content.getDirectorID());
            pstatement.setInt(8, content.getContentPhase());
            pstatement.setString(9, content.getContentAgeRating());
            pstatement.setInt(10, content.getContentChronologicalOrder());
            pstatement.setString(11, content.getContentPoster());
            pstatement.setString(12, content.getContentTrailer());
            pstatement.setInt(13, content.getContentID());

            int res = pstatement.executeUpdate();
            if (res > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

// ============================ CRUD ACTOR ===============================

    public static ResultSet getActor() {
        
        ResultSet result = null;
        try {
            String query = "SELECT * FROM Actor";
            result = handler.execQuery(query);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean createActor(Actor actor) {
        try {
            pstatement = getDBConnection().prepareStatement("INSERT INTO Actor (actorName) VALUES (?)");
            pstatement.setString(1, actor.getActorName());

            return pstatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteActor(Actor actor) {
        try {
            pstatement = getDBConnection().prepareStatement("DELETE FROM Actor WHERE actorID = ?");
            pstatement.setInt(1, actor.getActorID());

            int res = pstatement.executeUpdate();
            if (res > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateActor(Actor actor) {
        try {
            pstatement = getDBConnection().prepareStatement("UPDATE Actor SET actorName = ? WHERE actorID = ?");
            pstatement.setString(1, actor.getActorName());
            pstatement.setInt(2, actor.getActorID());

            int res = pstatement.executeUpdate();
            if (res > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ============================ CRUD ROLE ===============================

    public static ResultSet getRole() {
        
        ResultSet result = null;
        try {
            String query = "SELECT * FROM Role";
            result = handler.execQuery(query);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean createRole(Role role) {
        try {
            pstatement = getDBConnection().prepareStatement("INSERT INTO Role (roleName) VALUES (?)");
            pstatement.setString(1, role.getRoleName());

            return pstatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteRole(Role role) {
        try {
            pstatement = getDBConnection().prepareStatement("DELETE FROM Role WHERE roleID = ?");
            pstatement.setInt(1, role.getRoleID());

            int res = pstatement.executeUpdate();
            if (res > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateRole(Role role) {
        try {
            pstatement = getDBConnection().prepareStatement("UPDATE Role SET roleName = ? WHERE roleID = ?");
            pstatement.setString(1, role.getRoleName());
            pstatement.setInt(2, role.getRoleID());

            int res = pstatement.executeUpdate();
            if (res > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

// ============================ CRUD CAST ===============================

    public static ResultSet getCast() {
        getInstance();
        String query = "SELECT c.castID, c.actorID, c.roleID, c.contentID, " +
                    "a.actorName, r.roleName, con.contentTitle " +
                    "FROM Cast c " +
                    "JOIN Actor a ON c.actorID = a.actorID " +
                    "JOIN Role r ON c.roleID = r.roleID " +
                    "JOIN Content con ON c.contentID = con.contentID";
        return handler.execQuery(query);
    }

    public static boolean createCast(int actorID, int roleID, int contentID) {
        try {
            pstatement = getDBConnection().prepareStatement(
                "INSERT INTO Cast (actorID, roleID, contentID) VALUES (?, ?, ?)");
            pstatement.setInt(1, actorID);
            pstatement.setInt(2, roleID);
            pstatement.setInt(3, contentID);
            int result = pstatement.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateCast(int castID, int actorID, int roleID, int contentID) {
        try {
            pstatement = getDBConnection().prepareStatement(
                "UPDATE Cast SET actorID = ?, roleID = ?, contentID = ? WHERE castID = ?");
            pstatement.setInt(1, actorID);
            pstatement.setInt(2, roleID);
            pstatement.setInt(3, contentID);
            pstatement.setInt(4, castID);
            return pstatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteCast(int castID) {
        try {
            pstatement = getDBConnection().prepareStatement(
                "DELETE FROM Cast WHERE castID = ?");
            pstatement.setInt(1, castID);
            return pstatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateCast(Cast cast) {
        try {
            pstatement = getDBConnection().prepareStatement(
                "UPDATE Cast SET actorID = ?, roleID = ?, contentID = ? WHERE castID = ?");
            pstatement.setInt(1, cast.getActorID());
            pstatement.setInt(2, cast.getRoleID());
            pstatement.setInt(3, cast.getContentID());
            pstatement.setInt(4, cast.getCastID());

            int res = pstatement.executeUpdate();
            if (res > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }   


// ============================ CRUD DIRECTOR ===============================

    public static ResultSet getDirector() {
        
        ResultSet result = null;
        try {
            String query = "SELECT * FROM Director";
            result = handler.execQuery(query);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean createDirector(Director director) {
        try {
            pstatement = getDBConnection().prepareStatement("INSERT INTO Director (directorName) VALUES (?)");
            pstatement.setString(1, director.getDirectorName());

            return pstatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteDirector(Director director) {
        try {
            pstatement = getDBConnection().prepareStatement("DELETE FROM Director WHERE directorID = ?");
            pstatement.setInt(1, director.getDirectorID());

            int res = pstatement.executeUpdate();
            if (res > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateDirector(Director director) {
        try {
            pstatement = getDBConnection().prepareStatement("UPDATE Director SET directorName = ? WHERE directorID = ?");
            pstatement.setString(1, director.getDirectorName());
            pstatement.setInt(2, director.getDirectorID());

            int res = pstatement.executeUpdate();
            if (res > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

// ============================ VALIDATE USER-LOGIN ===============================

    public static boolean validateUserLogin(String username, String password) {
        getInstance();
        String query = "SELECT * FROM User WHERE userName = '" + username + "' AND userPassword = '" + password + "'";
        
        System.out.println(query);
    
        ResultSet result = handler.execQuery(query);
        try {
            if (result.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ============================ GET DIRECTOR BY ID ===============================

    public static ResultSet getDirectorById(int directorID) {
        getInstance();
        String query = "SELECT * FROM Director WHERE directorID = " + directorID;
        return handler.execQuery(query);
    }

// ============================ (try) VIEW WATCHLIST (join) ===============================

    public static ResultSet getWatchlist() {
        getInstance();
        String query = "SELECT wl.watchlistID, usr.userName, contnt.contentTitle, wl.watchlistDateTime " +
                       "FROM Watchlist wl " +
                       "JOIN User usr ON wl.userID = usr.userID " +
                       "JOIN Content contnt ON wl.contentID = contnt.contentID;";
        
                       System.out.println("Executing SQL Query for watchlist: [" + query + "]"); 

         return handler.execQuery(query);
         }
 
// ============================ (try) VIEW WATCHED (join) ===============================
    
    public static ResultSet getWatched() {
        getInstance();
        String query =  "SELECT w.watchedID, usr.userName, contnt.contentTitle, w.watchedDateTime" +
                        " FROM Watched w " + 
                        "JOIN User usr ON w.userID = usr.userID " +
                        "JOIN Content contnt ON w.contentID = contnt.contentID;";

                        System.out.println("Executing SQL Query for watched: [" + query + "]");

         return handler.execQuery(query);

         }

// ============================ (try) VIEW RATING (join)  ===============================

    public static ResultSet getRating() {
        getInstance();
        String query =  "SELECT r.ratingID, usr.userName, contnt.contentTitle, r.star" +
                        " FROM Rating r " + 
                        "JOIN User usr ON r.userID = usr.userID " +
                        "JOIN Content contnt ON r.contentID = contnt.contentID;";

                        System.out.println("Executing SQL Query for rating: [" + query + "]");

        return handler.execQuery(query);
        
         }
// ============================ (try) VIEW REVIEW (join)  ===============================

    public static ResultSet getReview() {
        getInstance();
        String query =  "SELECT rev.reviewID, usr.userName, contnt.contentTitle, rev.reviewText " +
                        "FROM Review rev " +
                        "JOIN User usr ON rev.userID = usr.userID " +
                        "JOIN Content contnt ON rev.contentID = contnt.contentID;";

                        System.out.println("Executing SQL Query for Review: [" + query + "]");
        
        return handler.execQuery(query);
        }
// ============================ (try) VIEW LIKE (join)  ===============================

    public static ResultSet getLike() {
        getInstance();
        String query =  "SELECT l.likeID, usr.userName, contnt.contentTitle " +  
                        "FROM userLike l " +  
                        "JOIN User usr ON l.userID = usr.userID " +  
                        "JOIN Content contnt ON l.contentID = contnt.contentID; ";  

                        System.out.println("Executing SQL Query for Like: [" + query + "]");  
        
        return handler.execQuery(query);
}
// ============================ (try) VIEW DISLIKE (join)  ===============================

     public static ResultSet getDislike() {
        getInstance();
        String query =  "SELECT dl.dislikeID, usr.userName, contnt.contentTitle " +  
                        "FROM userDislike dl " +  
                        "JOIN User usr ON dl.userID = usr.userID " +  
                        "JOIN Content contnt ON dl.contentID = contnt.contentID ";   

                        System.out.println("Executing SQL Query for Dislike: [" + query + "]");  
        
        return handler.execQuery(query);
    }
    
// ============================ CHECK IF CONTENT IS IN WATCHLIST ===============================

    public static boolean isContentInWatchlist(String username, int contentID) {
        getInstance();
        try {
            // Get the userID from the username
            String userQuery = "SELECT userID FROM User WHERE userName = ?";
            PreparedStatement userStmt = getDBConnection().prepareStatement(userQuery);
            userStmt.setString(1, username);
            ResultSet userResult = userStmt.executeQuery();
            
            if (userResult.next()) {
                int userID = userResult.getInt("userID");
                
                // Check if the content is already in the watchlist
                String checkQuery = "SELECT COUNT(*) AS count FROM Watchlist WHERE userID = ? AND contentID = ?";
                PreparedStatement checkStmt = getDBConnection().prepareStatement(checkQuery);
                checkStmt.setInt(1, userID);
                checkStmt.setInt(2, contentID);
                ResultSet checkResult = checkStmt.executeQuery();
                
                if (checkResult.next()) {
                    int count = checkResult.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

// ============================ ADD TO WATCHLIST ===============================

    public static boolean addToWatchlist(String username, int contentID) {
        getInstance();
        
        // First check if the content is already in the watchlist
        if (isContentInWatchlist(username, contentID)) {
            return false; // Content already in watchlist
        }
        
        try {
            // Get the userID from the username
            String userQuery = "SELECT userID FROM User WHERE userName = ?";
            PreparedStatement userStmt = getDBConnection().prepareStatement(userQuery);
            userStmt.setString(1, username);
            ResultSet userResult = userStmt.executeQuery();
            
            if (userResult.next()) {
                int userID = userResult.getInt("userID");
                
                // Now insert into the Watchlist table
                String insertQuery = "INSERT INTO Watchlist (userID, contentID, watchlistDateTime) VALUES (?, ?, NOW())";
                PreparedStatement insertStmt = getDBConnection().prepareStatement(insertQuery);
                insertStmt.setInt(1, userID);
                insertStmt.setInt(2, contentID);
                
                int result = insertStmt.executeUpdate();
                return result > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

// ============================ REMOVE FROM WATCHLIST ===============================

    public static boolean removeFromWatchlist(String username, int contentID) {
        getInstance();
        
        try {
            // Get the userID from the username
            String userQuery = "SELECT userID FROM User WHERE userName = ?";
            PreparedStatement userStmt = getDBConnection().prepareStatement(userQuery);
            userStmt.setString(1, username);
            ResultSet userResult = userStmt.executeQuery();
            
            if (userResult.next()) {
                int userID = userResult.getInt("userID");
                
                // Delete from the Watchlist table
                String deleteQuery = "DELETE FROM Watchlist WHERE userID = ? AND contentID = ?";
                PreparedStatement deleteStmt = getDBConnection().prepareStatement(deleteQuery);
                deleteStmt.setInt(1, userID);
                deleteStmt.setInt(2, contentID);
                
                int result = deleteStmt.executeUpdate();
                return result > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

// ============================ ADD REVIEW ===============================

    public static boolean addReview(String username, int contentID, String reviewText) {
        getInstance();
        try {
            // Get the userID from the username
            String userQuery = "SELECT userID FROM User WHERE userName = ?";
            PreparedStatement userStmt = getDBConnection().prepareStatement(userQuery);
            userStmt.setString(1, username);
            ResultSet userResult = userStmt.executeQuery();
            
            if (userResult.next()) {
                int userID = userResult.getInt("userID");
                
                // Insert into the Review table
                String insertQuery = "INSERT INTO Review (userID, contentID, reviewText) VALUES (?, ?, ?)";
                PreparedStatement insertStmt = getDBConnection().prepareStatement(insertQuery);
                insertStmt.setInt(1, userID);
                insertStmt.setInt(2, contentID);
                insertStmt.setString(3, reviewText);
                
                int result = insertStmt.executeUpdate();
                return result > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

// ============================ ADD RATING ===============================

    public static boolean addRating(String username, int contentID, int rating) {
        getInstance();
        try {
            // Get the userID from the username
            String userQuery = "SELECT userID FROM User WHERE userName = ?";
            PreparedStatement userStmt = getDBConnection().prepareStatement(userQuery);
            userStmt.setString(1, username);
            ResultSet userResult = userStmt.executeQuery();
            
            if (userResult.next()) {
                int userID = userResult.getInt("userID");
                
                // Check if user already rated this content
                String checkQuery = "SELECT ratingID FROM Rating WHERE userID = ? AND contentID = ?";
                PreparedStatement checkStmt = getDBConnection().prepareStatement(checkQuery);
                checkStmt.setInt(1, userID);
                checkStmt.setInt(2, contentID);
                ResultSet checkResult = checkStmt.executeQuery();
                
                if (checkResult.next()) {
                    // Update existing rating
                    int ratingID = checkResult.getInt("ratingID");
                    String updateQuery = "UPDATE Rating SET star = ?, ratingDateTime = NOW() WHERE ratingID = ?";
                    PreparedStatement updateStmt = getDBConnection().prepareStatement(updateQuery);
                    updateStmt.setInt(1, rating);
                    updateStmt.setInt(2, ratingID);
                    
                    int result = updateStmt.executeUpdate();
                    return result > 0;
                } else {
                    // Insert new rating
                    String insertQuery = "INSERT INTO Rating (userID, contentID, star) VALUES (?, ?, ?)";
                    PreparedStatement insertStmt = getDBConnection().prepareStatement(insertQuery);
                    insertStmt.setInt(1, userID);
                    insertStmt.setInt(2, contentID);
                    insertStmt.setInt(3, rating);
                    
                    int result = insertStmt.executeUpdate();
                    return result > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

// ============================ ADD LIKE ===============================

    public static boolean addLike(String username, int contentID) {
        getInstance();
        try {
            // Get the userID from the username
            String userQuery = "SELECT userID FROM User WHERE userName = ?";
            PreparedStatement userStmt = getDBConnection().prepareStatement(userQuery);
            userStmt.setString(1, username);
            ResultSet userResult = userStmt.executeQuery();
            
            if (userResult.next()) {
                int userID = userResult.getInt("userID");
                
                // Remove any existing dislike
                String deleteDislikeQuery = "DELETE FROM userDislike WHERE userID = ? AND contentID = ?";
                PreparedStatement deleteDislikeStmt = getDBConnection().prepareStatement(deleteDislikeQuery);
                deleteDislikeStmt.setInt(1, userID);
                deleteDislikeStmt.setInt(2, contentID);
                deleteDislikeStmt.executeUpdate();
                
                // Check if user already liked this content
                String checkQuery = "SELECT likeID FROM userLike WHERE userID = ? AND contentID = ?";
                PreparedStatement checkStmt = getDBConnection().prepareStatement(checkQuery);
                checkStmt.setInt(1, userID);
                checkStmt.setInt(2, contentID);
                ResultSet checkResult = checkStmt.executeQuery();
                
                if (checkResult.next()) {
                    // Like already exists
                    return true;
                } else {
                    // Insert new like
                    String insertQuery = "INSERT INTO userLike (userID, contentID) VALUES (?, ?)";
                    PreparedStatement insertStmt = getDBConnection().prepareStatement(insertQuery);
                    insertStmt.setInt(1, userID);
                    insertStmt.setInt(2, contentID);
                    
                    int result = insertStmt.executeUpdate();
                    return result > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

// ============================ ADD DISLIKE ===============================

    public static boolean addDislike(String username, int contentID) {
        getInstance();
        try {
            // Get the userID from the username
            String userQuery = "SELECT userID FROM User WHERE userName = ?";
            PreparedStatement userStmt = getDBConnection().prepareStatement(userQuery);
            userStmt.setString(1, username);
            ResultSet userResult = userStmt.executeQuery();
            
            if (userResult.next()) {
                int userID = userResult.getInt("userID");
                
                // Remove any existing like
                String deleteLikeQuery = "DELETE FROM userLike WHERE userID = ? AND contentID = ?";
                PreparedStatement deleteLikeStmt = getDBConnection().prepareStatement(deleteLikeQuery);
                deleteLikeStmt.setInt(1, userID);
                deleteLikeStmt.setInt(2, contentID);
                deleteLikeStmt.executeUpdate();
                
                // Check if user already disliked this content
                String checkQuery = "SELECT dislikeID FROM userDislike WHERE userID = ? AND contentID = ?";
                PreparedStatement checkStmt = getDBConnection().prepareStatement(checkQuery);
                checkStmt.setInt(1, userID);
                checkStmt.setInt(2, contentID);
                ResultSet checkResult = checkStmt.executeQuery();
                
                if (checkResult.next()) {
                    // Dislike already exists
                    return true;
                } else {
                    // Insert new dislike
                    String insertQuery = "INSERT INTO userDislike (userID, contentID) VALUES (?, ?)";
                    PreparedStatement insertStmt = getDBConnection().prepareStatement(insertQuery);
                    insertStmt.setInt(1, userID);
                    insertStmt.setInt(2, contentID);
                    
                    int result = insertStmt.executeUpdate();
                    return result > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ============================ ADD TO WATCHED ===============================

    public static boolean addToWatched(String username, int contentID) {
        getInstance();
        
        try {
            // Get the userID from the username
            String userQuery = "SELECT userID FROM User WHERE userName = ?";
            PreparedStatement userStmt = getDBConnection().prepareStatement(userQuery);
            userStmt.setString(1, username);
            ResultSet userResult = userStmt.executeQuery();
            
            if (userResult.next()) {
                int userID = userResult.getInt("userID");
                
                // Now insert into the Watched table
                String insertQuery = "INSERT INTO Watched (userID, contentID, watchedDateTime) VALUES (?, ?, NOW())";
                PreparedStatement insertStmt = getDBConnection().prepareStatement(insertQuery);
                insertStmt.setInt(1, userID);
                insertStmt.setInt(2, contentID);
                
                int result = insertStmt.executeUpdate();
                return result > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isContentWatched(String username, int contentID) {
        getInstance();
        try {
            // Get the userID from the username
            String userQuery = "SELECT userID FROM User WHERE userName = ?";
            PreparedStatement userStmt = getDBConnection().prepareStatement(userQuery);
            userStmt.setString(1, username);
            ResultSet userResult = userStmt.executeQuery();
            
            if (userResult.next()) {
                int userID = userResult.getInt("userID");
                
                // Check if the content has been watched by the user
                String checkQuery = "SELECT COUNT(*) AS count FROM Watched WHERE userID = ? AND contentID = ?";
                PreparedStatement checkStmt = getDBConnection().prepareStatement(checkQuery);
                checkStmt.setInt(1, userID);
                checkStmt.setInt(2, contentID);
                ResultSet checkResult = checkStmt.executeQuery();
                
                if (checkResult.next()) {
                    int count = checkResult.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    // ============================ USER SESSION HANDLERS ===============================


    // ====== For Displaying the User in Profile Page
    public static User getUserByUsername(String username) {
        getInstance();
        String query = "SELECT * FROM User WHERE userName = ?";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userID = rs.getInt("userID");
                String userName = rs.getString("userName");
                String userPassword = rs.getString("userPassword");
                String userEmail = rs.getString("userEmail");
                String userBio = rs.getString("userBio");
                String userProfile = rs.getString("userProfile");
                return new User(userID, userName, userPassword, userEmail, userBio, userProfile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ====== For Displaying the current User Watched in Profile Page
        public static int getWatchedCount(int userID) {
            String query = "SELECT COUNT(*) FROM Watched WHERE userID = ?";
            try (Connection conn = getDBConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

    // ====== For Displaying the Current available Content in Profile Page
        public static int getTotalContentCount() {
            String query = "SELECT COUNT(*) FROM Content";
            try (Connection conn = getDBConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

    public static List<String> getWatchedTitles(int userID) {
        List<String> watched = new ArrayList<>();
        String query = "SELECT c.contentTitle FROM Watched w JOIN Content c ON w.contentID = c.contentID WHERE w.userID = ?";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                watched.add(rs.getString("contentTitle"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return watched;
    }

    public static List<String> getWatchlistTitles(int userID) {
        List<String> watchlist = new ArrayList<>();
        String query = "SELECT c.contentTitle FROM Watchlist w JOIN Content c ON w.contentID = c.contentID WHERE w.userID = ?";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                watchlist.add(rs.getString("contentTitle"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return watchlist;
    }

    public static List<String> getReviews(int userID) {
        List<String> reviews = new ArrayList<>();
        String query = "SELECT r.reviewText, c.contentTitle FROM Review r JOIN Content c ON r.contentID = c.contentID WHERE r.userID = ?";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Editable, pede k maglagay ng title sa review, e.g. "Title: Review"
                reviews.add(rs.getString("contentTitle") + ": " + rs.getString("reviewText"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    // ============================ USER PROFILE SESSION HANDLER ===============================

    // ====== For HBox
    public static List<Content> getRecentWatchedContent(int userID, int limit) {
        List<Content> recentWatched = new ArrayList<>();
        String query = "SELECT c.* FROM Watched w JOIN Content c ON w.contentID = c.contentID WHERE w.userID = ? ORDER BY w.watchedDateTime DESC LIMIT ?";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Content content = new Content(
                    rs.getInt("contentID"),
                    rs.getString("contentTitle"),
                    rs.getString("contentRuntime"),
                    rs.getObject("contentSeason", Integer.class),
                    rs.getObject("contentEpisode", Integer.class),
                    rs.getDate("contentReleaseDate") != null ? rs.getDate("contentReleaseDate").toLocalDate() : null,
                    rs.getString("contentSynopsis"),
                    rs.getInt("directorID"),
                    rs.getInt("contentPhase"),
                    rs.getString("contentAgeRating"),
                    rs.getInt("contentChronologicalOrder"),
                    rs.getString("contentPoster"),
                    rs.getString("contentTrailer")
                );
                recentWatched.add(content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recentWatched;
    }

    // ====== For clicking Watched Content Button
    public static List<Content> getWatchedContent(int userID) {
        List<Content> watchedContent = new ArrayList<>();
        String query = "SELECT c.* FROM Watched w JOIN Content c ON w.contentID = c.contentID WHERE w.userID = ?";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Content content = new Content(
                    rs.getInt("contentID"),
                    rs.getString("contentTitle"),
                    rs.getString("contentRuntime"),
                    rs.getObject("contentSeason", Integer.class),
                    rs.getObject("contentEpisode", Integer.class),
                    rs.getDate("contentReleaseDate") != null ? rs.getDate("contentReleaseDate").toLocalDate() : null,
                    rs.getString("contentSynopsis"),
                    rs.getInt("directorID"),
                    rs.getInt("contentPhase"),
                    rs.getString("contentAgeRating"),
                    rs.getInt("contentChronologicalOrder"),
                    rs.getString("contentPoster"),
                    rs.getString("contentTrailer")
                );
                watchedContent.add(content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return watchedContent;
    }

    // ====== For clicking Watchlist Content Button
    public static List<Content> getWatchlistContent(int userID) {
        List<Content> watchlistContent = new ArrayList<>();
        String query = "SELECT c.* FROM Watchlist w JOIN Content c ON w.contentID = c.contentID WHERE w.userID = ?";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Content content = new Content(
                    rs.getInt("contentID"),
                    rs.getString("contentTitle"),
                    rs.getString("contentRuntime"),
                    rs.getObject("contentSeason", Integer.class),
                    rs.getObject("contentEpisode", Integer.class),
                    rs.getDate("contentReleaseDate") != null ? rs.getDate("contentReleaseDate").toLocalDate() : null,
                    rs.getString("contentSynopsis"),
                    rs.getInt("directorID"),
                    rs.getInt("contentPhase"),
                    rs.getString("contentAgeRating"),
                    rs.getInt("contentChronologicalOrder"),
                    rs.getString("contentPoster"),
                    rs.getString("contentTrailer")
                );
                watchlistContent.add(content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return watchlistContent;
    }

    // ====== For clicking Review Content Button (Updated now to the proper fxml file, also with both content and review text)   
    public static List<Object[]> getReviewedContentAndText(int userID) {
        List<Object[]> reviewed = new ArrayList<>();
        String query = "SELECT c.*, r.reviewText FROM Review r JOIN Content c ON r.contentID = c.contentID WHERE r.userID = ?";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Content content = new Content(
                    rs.getInt("contentID"),
                    rs.getString("contentTitle"),
                    rs.getString("contentRuntime"),
                    rs.getObject("contentSeason", Integer.class),
                    rs.getObject("contentEpisode", Integer.class),
                    rs.getDate("contentReleaseDate") != null ? rs.getDate("contentReleaseDate").toLocalDate() : null,
                    rs.getString("contentSynopsis"),
                    rs.getInt("directorID"),
                    rs.getInt("contentPhase"),
                    rs.getString("contentAgeRating"),
                    rs.getInt("contentChronologicalOrder"),
                    rs.getString("contentPoster"),
                    rs.getString("contentTrailer")
                );
                String reviewText = rs.getString("reviewText");
                reviewed.add(new Object[]{content, reviewText});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviewed;
    }

    // ====== For clicking Rating Content Button (Updated now with proper fxml file, also with both content and rating value )
    public static List<Object[]> getRatedContentAndRating(int userID) {
        List<Object[]> rated = new ArrayList<>();
        String query = "SELECT c.*, r.star FROM Rating r JOIN Content c ON r.contentID = c.contentID WHERE r.userID = ?";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Content content = new Content(
                    rs.getInt("contentID"),
                    rs.getString("contentTitle"),
                    rs.getString("contentRuntime"),
                    rs.getObject("contentSeason", Integer.class),
                    rs.getObject("contentEpisode", Integer.class),
                    rs.getDate("contentReleaseDate") != null ? rs.getDate("contentReleaseDate").toLocalDate() : null,
                    rs.getString("contentSynopsis"),
                    rs.getInt("directorID"),
                    rs.getInt("contentPhase"),
                    rs.getString("contentAgeRating"),
                    rs.getInt("contentChronologicalOrder"),
                    rs.getString("contentPoster"),
                    rs.getString("contentTrailer")
                );
                int ratingValue = rs.getInt("star");
                rated.add(new Object[]{content, ratingValue});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rated;
    }

    // ====== For clicking Liked Content Button
    public static List<Content> getLikedContent(int userID) {
        List<Content> likedContent = new ArrayList<>();
        String query = "SELECT c.* FROM userLike l JOIN Content c ON l.contentID = c.contentID WHERE l.userID = ?";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Content content = new Content(
                    rs.getInt("contentID"),
                    rs.getString("contentTitle"),
                    rs.getString("contentRuntime"),
                    rs.getObject("contentSeason", Integer.class),
                    rs.getObject("contentEpisode", Integer.class),
                    rs.getDate("contentReleaseDate") != null ? rs.getDate("contentReleaseDate").toLocalDate() : null,
                    rs.getString("contentSynopsis"),
                    rs.getInt("directorID"),
                    rs.getInt("contentPhase"),
                    rs.getString("contentAgeRating"),
                    rs.getInt("contentChronologicalOrder"),
                    rs.getString("contentPoster"),
                    rs.getString("contentTrailer")
                );
                likedContent.add(content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return likedContent;
    }

    // ====== For clicking Disliked Content Button
    public static List<Content> getDislikedContent(int userID) {
        List<Content> dislikedContent = new ArrayList<>();
        String query = "SELECT c.* FROM userDislike dl JOIN Content c ON dl.contentID = c.contentID WHERE dl.userID = ?";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Content content = new Content(
                    rs.getInt("contentID"),
                    rs.getString("contentTitle"),
                    rs.getString("contentRuntime"),
                    rs.getObject("contentSeason", Integer.class),
                    rs.getObject("contentEpisode", Integer.class),
                    rs.getDate("contentReleaseDate") != null ? rs.getDate("contentReleaseDate").toLocalDate() : null,
                    rs.getString("contentSynopsis"),
                    rs.getInt("directorID"),
                    rs.getInt("contentPhase"),
                    rs.getString("contentAgeRating"),
                    rs.getInt("contentChronologicalOrder"),
                    rs.getString("contentPoster"),
                    rs.getString("contentTrailer")
                );
                dislikedContent.add(content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dislikedContent;
    } 
    
    // ====== For getting rating distribution for a specific content
    public static Map<Integer, Integer> getContentRatingDistribution(int contentID) {
        Map<Integer, Integer> ratingDistribution = new HashMap<>();
        
        for (int i = 1; i <= 5; i++) {
            ratingDistribution.put(i, 0);
        }
        
        String query = "SELECT star, COUNT(*) as count FROM Rating WHERE contentID = ? GROUP BY star ORDER BY star";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, contentID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int rating = rs.getInt("star");
                int count = rs.getInt("count");
                ratingDistribution.put(rating, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratingDistribution;
    }

    // ====== Get cast members for a specific content
    public static List<Cast> getCastByContentID(int contentID) {
        List<Cast> castList = new ArrayList<>();
        String query = "SELECT c.castID, c.actorID, c.roleID, c.contentID, " +
                    "a.actorName, r.roleName, con.contentTitle " +
                    "FROM Cast c " +
                    "JOIN Actor a ON c.actorID = a.actorID " +
                    "JOIN Role r ON c.roleID = r.roleID " +
                    "JOIN Content con ON c.contentID = con.contentID " +
                    "WHERE c.contentID = ?";
        try (Connection conn = getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, contentID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cast cast = new Cast(
                    rs.getInt("castID"),
                    rs.getInt("actorID"),
                    rs.getInt("roleID"),
                    rs.getInt("contentID"),
                    rs.getString("actorName"),
                    rs.getString("roleName"),
                    rs.getString("contentTitle")
                );
                castList.add(cast);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return castList;
    }
    
    // ====== Get reviews for a specific content
    public static List<Object[]> getReviewsByContentID(int contentID) {
        List<Object[]> reviews = new ArrayList<>();
        String query = "SELECT r.reviewText, u.userName FROM Review r " +
                       "JOIN User u ON r.userID = u.userID " +
                       "WHERE r.contentID = ? ";
        try (Connection conn = getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, contentID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String reviewText = rs.getString("reviewText");
                String userName = rs.getString("userName");
                reviews.add(new Object[]{userName, reviewText});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    // ============================ SEARCH CONTENT ===============================
    
    /**
     * Search for content based on search text, matching against title, director name, actor name, or role name
     * @param searchText The text to search for
     * @return List of Content objects that match the search criteria
     */
    public static List<Content> searchContent(String searchText) {
        List<Content> results = new ArrayList<>();
        String searchPattern = "%" + searchText + "%";
        
        try {
            // Create a query that searches across different tables using joins
            String query = "SELECT DISTINCT c.* FROM Content c " +
                          "LEFT JOIN Director d ON c.directorID = d.directorID " +
                          "LEFT JOIN Cast ca ON c.contentID = ca.contentID " +
                          "LEFT JOIN Actor a ON ca.actorID = a.actorID " +
                          "LEFT JOIN Role r ON ca.roleID = r.roleID " +
                          "WHERE c.contentTitle LIKE ? " +
                          "OR d.directorName LIKE ? " +
                          "OR a.actorName LIKE ? " +
                          "OR r.roleName LIKE ?";
            
            pstatement = getDBConnection().prepareStatement(query);
            pstatement.setString(1, searchPattern);
            pstatement.setString(2, searchPattern);
            pstatement.setString(3, searchPattern);
            pstatement.setString(4, searchPattern);
            
            ResultSet rs = pstatement.executeQuery();
            
            while (rs.next()) {
                int contentID = rs.getInt("contentID");
                String contentTitle = rs.getString("contentTitle");
                String contentRuntime = rs.getString("contentRuntime");
                
                // Handle nullable fields
                Integer contentSeason = null;
                try {
                    contentSeason = rs.getObject("contentSeason") != null ? rs.getInt("contentSeason") : null;
                } catch (SQLException e) {
                    // Column might not exist or have a different name
                }
                
                Integer contentEpisode = null;
                try {
                    contentEpisode = rs.getObject("contentEpisode") != null ? rs.getInt("contentEpisode") : null;
                } catch (SQLException e) {
                    // Column might not exist or have a different name
                }
                
                // Handle date
                LocalDate contentReleaseDate = null;
                try {
                    java.sql.Date date = rs.getDate("contentReleaseDate");
                    contentReleaseDate = date != null ? date.toLocalDate() : null;
                } catch (SQLException e) {
                    // Column might not exist or have a different name
                }
                
                String contentSynopsis = rs.getString("contentSynopsis");
                
                // Handle nullable director ID
                int directorID = 0;
                try {
                    directorID = rs.getInt("directorID");
                } catch (SQLException e) {
                    // Column might not exist or have a different name
                }
                
                // Handle other fields
                int contentPhase = 0;
                try {
                    contentPhase = rs.getInt("contentPhase");
                } catch (SQLException e) {
                    // Column might not exist or have a different name
                }
                
                String contentAgeRating = rs.getString("contentAgeRating");
                
                int contentChronologicalOrder = 0;
                try {
                    contentChronologicalOrder = rs.getInt("contentChronologicalOrder");
                } catch (SQLException e) {
                    // Column might not exist or have a different name
                }
                
                String contentPoster = rs.getString("contentPoster");
                String contentTrailer = rs.getString("contentTrailer");
                
                Content content = new Content(
                    contentID, contentTitle, contentRuntime, contentSeason, contentEpisode,
                    contentReleaseDate, contentSynopsis, directorID, contentPhase,
                    contentAgeRating, contentChronologicalOrder, contentPoster, contentTrailer
                );
                
                results.add(content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return results;
    }
}