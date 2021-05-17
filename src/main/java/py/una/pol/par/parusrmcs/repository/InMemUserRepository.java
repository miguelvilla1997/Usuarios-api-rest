/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.una.pol.par.parusrmcs.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import py.una.pol.par.commons.entity.Entity;
import py.una.pol.par.commons.util.DBUtils;
import py.una.pol.par.parusrmcs.model.entity.User;

/**
 *
 * @author mauricio
 */
public class InMemUserRepository implements UserRepository<User, Integer> {

    private static ArrayList<User> users = new ArrayList<>();

    @Override
    public boolean containsNameLastname(String name, String lastname, Collection<User> users) {
        return (this.findByNameLastname(name, lastname, users).size() > 0);
    }

    @Override
    public Collection<User> findByNameLastname(String name, String lastname, Collection<User> users) {
        ArrayList retValue = new ArrayList<>();
        for (User user : users) {
            if (name.equals(user.getName()) && lastname.equals(user.getLastname())) {
                retValue.add(user);
            }
        }
        return retValue;
    }

    @Override
    public void add(User entity) {
        try {
            Integer max = 0;
            Connection con = DBUtils.getConnection();
            String idMax = "SELECT COALESCE(MAX(u.user_id),0) FROM \"Users\" u; ";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(idMax);
            if (rs.next()) {
                max = rs.getInt(1);
            }
            String query = "INSERT INTO public.\"Users\"(\n"
                    + "	user_id, name, lastname , email , login_name , passwd , user_type, status )\n"
                    + "	VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, max + 1);
            pstmt.setString(2, entity.getName());
            pstmt.setString(3, entity.getLastname());
            pstmt.setString(4, entity.getEmail());
            pstmt.setString(5, entity.getLoginName());
            pstmt.setString(6, entity.getPasswd());
            pstmt.setInt(7, entity.getClientType());
            pstmt.setString(8, "A");

            pstmt.executeUpdate();
            rs.close();
            pstmt.close();
            DBUtils.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(InMemUserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void remove(Integer id) {
        try {
            Connection con = DBUtils.getConnection();
            String query = "DELETE FROM \"Users\" p WHERE p.user_id = ? ;";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();
            DBUtils.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(InMemUserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(User entity) {
        Connection con;
        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            String query = "UPDATE public.\"Users\" p\n"
                    + "	SET name=?, lastname=?, status=?, login_name=?, passwd=?, email=?, user_type=?\n"
                    + "	WHERE user_id=?;";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(8, entity.getId());
            pstmt.setString(1, entity.getName());
            pstmt.setString(2, entity.getLastname());
            pstmt.setString(6, entity.getEmail());
            pstmt.setString(4, entity.getLoginName());
            pstmt.setString(5, entity.getPasswd());
            pstmt.setInt(7, entity.getClientType());
            pstmt.setString(3, "A");
            pstmt.executeUpdate();
            pstmt.close();
            DBUtils.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(InMemUserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean contains(Integer id) {
        boolean retValue = false;
        for (User user : users) {
            if (Objects.equals(user.getId(), id)) {
                retValue = true;
            }
        }
        return retValue;
    }

    @Override
    public Entity get(Integer id) {
        Connection con;
        ResultSet rs = null;
        User p = new User();
        try {
            con = DBUtils.getConnection();
            String query = "SELECT * FROM \"Users\" p WHERE p.user_id = ?;";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String lastName = rs.getString("lastname");
                String status = rs.getString("status");
                String email = rs.getString("email");
                String loginName = rs.getString("login_name");
                String pass = rs.getString("passwd");
                int userType = rs.getInt("user_type");

                p.setId(id);
                p.setName(name);
                p.setLastname(lastName);
                p.setStatus(status);
                p.setEmail(email);
                p.setPasswd(pass);
                p.setClientType(userType);
                p.setLoginName(loginName);
            }
            rs.close();
            pstmt.close();
            DBUtils.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(InMemUserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return p;
    }

    @Override
    public Collection<User> getAll() {
        Connection con;
        ResultSet rs = null;
        Statement stmt = null;
        users = new ArrayList<>();
        try {
            con = DBUtils.getConnection();
            String query = "SELECT * FROM public.\"Users\";";
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("user_id");
                String name = rs.getString("name");
                String lastName = rs.getString("lastname");
                String status = rs.getString("status");
                String email = rs.getString("email");
                String loginName = rs.getString("login_name");
                String pass = rs.getString("passwd");
                int userType = rs.getInt("user_type");

                User u = new User();
                u.setId(id);
                u.setName(name);
                u.setLastname(lastName);
                u.setStatus(status);
                u.setEmail(email);
                u.setPasswd(pass);
                u.setClientType(userType);
                u.setLoginName(loginName);
                users.add(u);
            }

            rs.close();
            stmt.close();
            DBUtils.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(InMemUserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return users;
    }

    @Override
    public Entity findByLoginName(String loginName) {
        try {
            Connection con = DBUtils.getConnection();
            users = (ArrayList<User>) this.getAll();
            for (User user : users) {
                if (user.getLoginName().equals(loginName)) {
                    DBUtils.closeConnection(con);
                    return user;
                }
            }
            DBUtils.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(InMemUserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new User();
    }

}
