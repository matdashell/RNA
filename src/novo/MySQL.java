package novo;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class MySQL extends Rede{

    public MySQL(String nome) {
        super(nome);
    }

    public static void loadDados(String nomeRede){
        if(mysql == null){
            try {
                mysql = DriverManager.getConnection("jdbc:mysql://localhost/RNA", "birocronix", "abc");
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        toolIniciarVetores();
    }

    public static void saveDados(String nomeRede){
        boolean novo = false;
        if(mysql == null){
            try {
                mysql = DriverManager.getConnection("jdbc:mysql://localhost/RNA", "birocronix", "abc");
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        PreparedStatement cmd;
        try{
            cmd = mysql.prepareStatement(String.format("SELECT * FROM %s;", nomeRede));
            cmd.executeQuery();
        }catch (Exception e) {
            try {
                cmd = mysql.prepareStatement(
                        String.format("CREATE TABLE %s (idPrimario int, idSecundario int, valor double);", nomeRede)
                );
                cmd.executeUpdate();
                novo = true;
            } catch (SQLException c) {
                System.out.println(c.getMessage());
            }
        }
        for(int i = 0; i < pesosEntradasAlpha.length; i++){
            for (int j = 0; j < pesosEntradasAlpha[i].length; j++) {
                try {
                    if(novo){throw new SQLException();}
                    cmd = mysql.prepareStatement(
                            String.format("UPDATE %s SET valor = %s WHERE idPrimario = 0 AND idSecundario = %d;"
                                    ,nomeRede, new DecimalFormat("#,##0.0000000000000000000000").format(pesosEntradasAlpha[i][j]).replace(",","."),(i * pesosEntradasAlpha.length) + j));
                    cmd.executeUpdate();
                }catch (SQLException e) {
                    try {
                        cmd = mysql.prepareStatement(
                                String.format("INSERT INTO %s VALUES(0, %d, %s);"
                                        ,nomeRede, (i * pesosEntradasAlpha.length) + j, new DecimalFormat("#,##0.0000000000000000000000").format(pesosEntradasAlpha[i][j]).replace(",",".")
                                ));
                        cmd.executeUpdate();
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
            }
        }
        for(int i = 0; i < pesosDeepAlpha.length; i++){
            for (int j = 0; j < pesosDeepAlpha[i].length; j++) {
                for(int k = 0; k < pesosDeepAlpha[i][j].length; k++) {
                    try {
                        if(novo){throw new SQLException();}
                        cmd = mysql.prepareStatement(
                                String.format("UPDATE %s SET valor = %s WHERE idPrimario = 1 AND idSecundario = %d;"
                                        , nomeRede, new DecimalFormat("#,##0.0000000000000000000000").format(pesosDeepAlpha[i][j][k]).replace(",","."), (i * pesosDeepAlpha.length + j * pesosDeepAlpha[i].length) + k));
                        cmd.executeUpdate();
                    } catch (SQLException e) {
                        try {
                            cmd = mysql.prepareStatement(
                                    String.format("INSERT INTO %s VALUES(1, %d, %s);"
                                            , nomeRede, (i * pesosDeepAlpha.length + j * pesosDeepAlpha[i].length) + k, new DecimalFormat("#,##0.0000000000000000000000").format(pesosDeepAlpha[i][j][k]).replace(",",".")
                                    ));
                            cmd.executeUpdate();
                        } catch (SQLException sqlException) {
                            sqlException.printStackTrace();
                        }
                    }
                }
            }
        }
        for(int i = 0; i < pesosSaidaAlpha.length; i++){
            for (int j = 0; j < pesosSaidaAlpha[i].length; j++) {
                try {
                    if(novo){throw new SQLException();}
                    cmd = mysql.prepareStatement(
                            String.format("UPDATE %s SET valor = %s WHERE idPrimario = 2 AND idSecundario = %d;"
                                    ,nomeRede, new DecimalFormat("#,##0.0000000000000000000000").format(pesosSaidaAlpha[i][j]).replace(",","."),(i * pesosSaidaAlpha.length) + j));
                    cmd.executeUpdate();
                }catch (SQLException e) {
                    try {
                        cmd = mysql.prepareStatement(
                                String.format("INSERT INTO %s VALUES(2, %d, %s);"
                                        ,nomeRede, (i * pesosSaidaAlpha.length) + j, new DecimalFormat("#,##0.0000000000000000000000").format(pesosSaidaAlpha[i][j]).replace(",",".")
                                ));
                        cmd.executeUpdate();
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
            }
        }
        for(int i = 0; i < biasAlpha.length; i++){
            try {
                if(novo){throw new SQLException();}
                cmd = mysql.prepareStatement(
                        String.format("UPDATE %s SET valor = %s WHERE idPrimario = 3 AND idSecundario = %d;"
                                ,nomeRede, new DecimalFormat("#,##0.0000000000000000000000").format(bias[i]).replace(",","."), i));
                cmd.executeUpdate();
            }catch (SQLException e) {
                try {
                    cmd = mysql.prepareStatement(
                            String.format("INSERT INTO %s VALUES(3, %d, %s);"
                                    ,nomeRede, i, new DecimalFormat("#,##0.0000000000000000000000").format(bias[i]).replace(",",".")
                            ));
                    cmd.executeUpdate();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        }
    }
}
