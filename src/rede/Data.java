package rede;

import javax.swing.*;
import java.sql.Connection;
import java.util.Vector;
import java.util.function.Function;

public class Data {

    static String nome;
    static Double[/*posição*/][/*valor*/]  valoresEntrada = null;
    static Double[/*posição*/][/*valor*/]  valoresSaida = null;
    Double[/*x*/][/*pesos*/]        pesosEntradas = null;
    Double[/*x*/][/*y*/][/*pesos*/] pesosDeep = null;
    Double[/*x*/][/*pesos*/]        pesosSaida = null;
    Double[/*pesos*/]               bias = null;
    static Double[/*x*/][/*y*/]            neuroniosDeep = null;
    static Double[/*x*/]                   neuroniosSaida = null;
    static Function<Double, Double>        funcaoAtivacaoDeep = null;
    static Function<Double, Double>        funcaoAtivacaoSaida = null;
    Double                          margemDeErro = null;
    static int numeroDeEntradas;
    static int numeroDeSaidas;
    static int numeroDeColunas;
    static int numeroDeFamilias;
    static String nomeFuncaoDeep;
    static String nomeFuncaoSaida;

    static Double[/*x*/][/*pesos*/]        pesosEntradasAlpha = null;
    static Double[/*x*/][/*y*/][/*pesos*/] pesosDeepAlpha = null;
    static Double[/*x*/][/*pesos*/]        pesosSaidaAlpha = null;
    static Double[/*pesos*/]               biasAlpha = null;
    static Double                          margemDeErroAlpha = null;

    static Vector<Integer> vetorColunas = new Vector<>();
    static Vector<Integer> vetorFamilias = new Vector<>();
    static Vector<Integer> vetorEntradas = new Vector<>();
    static Vector<Integer> vetorSaidas = new Vector<>();

    static double taxaAprendizagema = 0.1;
    static double margemErro = 0.1;
    static double temp0;
    static double temp1;
    static String stringMargemAnterior;
    static String stringMargemPosterior;

    static JFrame frame = null;
    static JButton info00 = new JButton("-");
    static JButton info01 = new JButton("-");
    static JButton info02 = new JButton("-");
    static JTextPane areaDeTexto = new JTextPane();

    static boolean forcarRetorno = false;
    static boolean salvar = false;

    static Connection mysql = null;

}
