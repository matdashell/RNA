package novo;

import javax.swing.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MySQL extends Rede{

    public MySQL(String nome) {
        super(nome);
    }

    public static void loadDados(String nomeRede){
        int contadorId = 0;
        List<Double> valores = new ArrayList<>();

        if(mysql == null){
            try {
                mysql = DriverManager.getConnection("jdbc:mysql://localhost/RNA", "birocronix", "abc");
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

        try{
            PreparedStatement data = mysql.prepareStatement(
                    String.format("SELECT * FROM %s_data;", nomeRede));
            ResultSet rs = data.executeQuery();
            rs.next();
            Rede.setDados(nomeRede
                    ,rs.getInt(1)
                    ,rs.getInt(2)
                    ,rs.getInt(3)
                    ,rs.getInt(4)
                    ,rs.getString(5)
                    ,rs.getString(6));

            data = mysql.prepareStatement(String.format("SELECT * FROM %s;", nomeRede));
            rs = data.executeQuery();
            while (rs.next()) {
                valores.add(rs.getDouble("valor"));
            }

            for(int i = 0; i < pesosEntradas.length; i++){
                for(int j = 0; j < pesosEntradas[i].length; j++){
                    pesosEntradas[i][j] = valores.get(contadorId);
                    contadorId ++;
                }
            }
            for(int i = 0; i < pesosDeep.length; i++){
                for(int j = 0; j < pesosDeep[i].length; j++){
                    for(int k = 0; k < pesosDeep[i][j].length; k++){
                        pesosDeep[i][j][k] = valores.get(contadorId);
                        contadorId ++;
                    }
                }
            }
            for(int i = 0; i < pesosSaida.length; i++){
                for(int j = 0; j < pesosSaida[i].length; j++){
                    pesosSaida[i][j] = valores.get(contadorId);
                    contadorId ++;
                }
            }
            for(int i = 0; i < bias.length; i++){
                bias[i] = valores.get(contadorId);
                contadorId ++;
            }

        }catch (SQLException sqlException) {
            JOptionPane.showMessageDialog(null,"Rede nao encontrada");
            System.exit(0);
        }
        toolIniciarVetores();
        toolSaveAplhaGen();
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
                        String.format("CREATE TABLE %s_Data (entradas int, saidas int, colunas int, familias int, funcaoEntrada varchar(15), funcaoSaida varchar(15));", nomeRede)
                );
                cmd.executeUpdate();
                cmd = mysql.prepareStatement(
                        String.format("INSERT INTO %s_Data VALUES(%d,%d,%d,%d,'%s','%s');", nomeRede, numeroDeEntradas, numeroDeSaidas, numeroDeColunas, numeroDeFamilias, nomeFuncaoDeep, nomeFuncaoSaida)
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
