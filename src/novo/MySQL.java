package novo;

import javax.swing.*;
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

        try{
            PreparedStatement tamanho = mysql.prepareStatement(
                    String.format("SELECT COUNT(idPrimario) FROM %s WHERE idPrimario = 0;", nomeRede));
        }catch (SQLException sqlException) {
            JOptionPane.showMessageDialog(null,"Rede nao encontrada");
            System.exit(0);
        }

        toolIniciarVetores();
    }

    public static void saveDados(String nomeRede){
        int contadorId = 0;
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
                        String.format("CREATE TABLE %s (id int, valor double);", nomeRede)
                );
                cmd.executeUpdate();
                cmd = mysql.prepareStatement(
                        String.format("CREATE TABLE %s_Data (entradas int, saidas int, colunas int, familias int);", nomeRede)
                );
                cmd.executeUpdate();
                cmd = mysql.prepareStatement(
                        String.format("INSERT INTO %s_Data VALUES(%d,%d,%d,%d);", nomeRede, numeroDeEntradas, numeroDeSaidas, numeroDeColunas, numeroDeFamilias)
                );
                cmd.executeUpdate();
                novo = true;
            } catch (SQLException c) {
                System.out.println(c.getMessage());
            }
        }
        for (Double[] doubles : pesosEntradasAlpha) {
            for (Double aDouble : doubles) {
                try {
                    if (novo) {
                        throw new SQLException();
                    }
                    cmd = mysql.prepareStatement(
                            String.format("UPDATE %s SET valor = %s WHERE id = %d;"
                                    , nomeRede, new DecimalFormat("#,##0.0000000000000000000000").format(aDouble).replace(",", "."), contadorId));
                    cmd.executeUpdate();
                } catch (SQLException e) {
                    try {
                        cmd = mysql.prepareStatement(
                                String.format("INSERT INTO %s VALUES(%d, %s);"
                                        , nomeRede, contadorId, new DecimalFormat("#,##0.0000000000000000000000").format(aDouble).replace(",", ".")
                                ));
                        cmd.executeUpdate();
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
                contadorId++;
            }
        }
        for (Double[][] doubles : pesosDeepAlpha) {
            for (Double[] aDouble : doubles) {
                for (Double value : aDouble) {
                    try {
                        if (novo) {
                            throw new SQLException();
                        }
                        cmd = mysql.prepareStatement(
                                String.format("UPDATE %s SET valor = %s WHERE id = %d;"
                                        , nomeRede, new DecimalFormat("#,##0.0000000000000000000000").format(value).replace(",", "."), contadorId));
                        cmd.executeUpdate();
                    } catch (SQLException e) {
                        try {
                            cmd = mysql.prepareStatement(
                                    String.format("INSERT INTO %s VALUES(%d, %s);"
                                            , nomeRede, contadorId, new DecimalFormat("#,##0.0000000000000000000000").format(value).replace(",", ".")
                                    ));
                            cmd.executeUpdate();
                        } catch (SQLException sqlException) {
                            sqlException.printStackTrace();
                        }
                    }
                    contadorId++;
                }
            }
        }
        for (Double[] doubles : pesosSaidaAlpha) {
            for (Double aDouble : doubles) {
                try {
                    if (novo) {
                        throw new SQLException();
                    }
                    cmd = mysql.prepareStatement(
                            String.format("UPDATE %s SET valor = %s WHERE id = %d;"
                                    , nomeRede, new DecimalFormat("#,##0.0000000000000000000000").format(aDouble).replace(",", "."), contadorId));
                    cmd.executeUpdate();
                } catch (SQLException e) {
                    try {
                        cmd = mysql.prepareStatement(
                                String.format("INSERT INTO %s VALUES(%d, %s);"
                                        , nomeRede, contadorId, new DecimalFormat("#,##0.0000000000000000000000").format(aDouble).replace(",", ".")
                                ));
                        cmd.executeUpdate();
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
                contadorId++;
            }
        }
        for (Double aDouble : biasAlpha) {
            try {
                if (novo) {
                    throw new SQLException();
                }
                cmd = mysql.prepareStatement(
                        String.format("UPDATE %s SET valor = %s WHERE id = %d;"
                                , nomeRede, new DecimalFormat("#,##0.0000000000000000000000").format(aDouble).replace(",", "."), contadorId));
                cmd.executeUpdate();
            } catch (SQLException e) {
                try {
                    cmd = mysql.prepareStatement(
                            String.format("INSERT INTO %s VALUES(%d, %s);"
                                    , nomeRede, contadorId, new DecimalFormat("#,##0.0000000000000000000000").format(aDouble).replace(",", ".")
                            ));
                    cmd.executeUpdate();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
            contadorId++;
        }
    }
}
