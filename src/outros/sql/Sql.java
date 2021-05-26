package outros.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Sql {

    private Connection connection = null;

    public Sql(String host, String user, String pass, String database){
        try {
            String url = String.format("jdbc:mysql://%s/%s", host, database);
            connection = DriverManager.getConnection(url, user, pass);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public String getOne(String table, String filter){
        int tipo = 0;
        try{
            PreparedStatement pstmt = connection.prepareStatement(
                    String.format("SELECT * FROM %s WHERE %s;",table,filter)
            );
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            if(tipo == 0) {
                try {
                    return rs.getString(1);
                } catch (Exception ignored) {
                    tipo += 1;
                }
            }
            if(tipo == 1) {
                try {
                    return String.valueOf(rs.getInt(1));
                } catch (Exception ignored) {
                    tipo += 1;
                }
            }
            if(tipo == 2) {
                try {
                    return String.valueOf(rs.getBigDecimal(1));
                } catch (Exception ignored) {
                }
            }
        }catch (Exception e){ System.out.println(e.getMessage()); }
        return null;
    }

    public List<String> getAll(String table, String filter){
        int tipo = 0;
        try{
            PreparedStatement pstmt = connection.prepareStatement(
                    String.format("SELECT * FROM %s WHERE %s;",table ,filter)
            );
            ResultSet rs = pstmt.executeQuery();
            List<String> ret = new ArrayList<>();
            try{
                while (rs.next()){
                    if(tipo == 0) {
                        try {
                            ret.add(rs.getString(1));
                        } catch (Exception ignored) {
                            tipo += 1;
                        }
                    }
                    if(tipo == 1) {
                        try {
                            ret.add(String.valueOf(rs.getInt(1)));
                        } catch (Exception ignored) {
                            tipo += 1;
                        }
                    }
                    if(tipo == 2) {
                        try {
                            ret.add(String.valueOf(rs.getBigDecimal(1)));
                        } catch (Exception ignored) {
                        }
                    }
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
            return ret;
        }catch (Exception e){ System.out.println(e.getMessage()); }
        return null;
    }

    public void createTable(String table, String cmd){
        try{
            PreparedStatement pstmt = connection.prepareStatement(
                    String.format("CREATE TABLE %s(%S);",table ,cmd)
            );
            pstmt.executeUpdate();
        }catch (Exception e){ System.out.println(e.getMessage()); }
    }

    public void insert(String table, String value){
        try{
            PreparedStatement pstmt = connection.prepareStatement(
                    String.format("INSERT INTO %s VALUES(%s);", table, value)
            );
            pstmt.executeUpdate();
        }catch (Exception e){ System.out.println(e.getMessage()); }
    }

    public void update(String table, String value, String condition){
        try{
            PreparedStatement pstmt = connection.prepareStatement(
                    String.format("UPDATE %s SET %s WHERE %s;", table, value, condition)
            );
            pstmt.executeUpdate();
        }catch (Exception e){ System.out.println(e.getMessage()); }
    }

    public void drop(String table){
        try{
            PreparedStatement pstmt = connection.prepareStatement(
                    String.format("DROP TABLE %s;", table)
            );
            pstmt.executeUpdate();
        }catch (Exception e){ System.out.println(e.getMessage()); }
    }

    public void codeSet(String code){
        try{
            PreparedStatement pstmt = connection.prepareStatement(code);
            pstmt.executeUpdate();
        }catch (Exception e){ System.out.println(e.getMessage()); }
    }
}
