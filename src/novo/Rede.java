package novo;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;

import static java.lang.Thread.sleep;

public class Rede {
    static String nome;
    static Double[/*posição*/][/*valor*/]  valoresEntrada = null;
    static Double[/*posição*/][/*valor*/]  valoresSaida = null;
    static Double[/*x*/][/*pesos*/]        pesosEntradas = null;
    static Double[/*x*/][/*y*/][/*pesos*/] pesosDeep = null;
    static Double[/*x*/][/*pesos*/]        pesosSaida = null;
    static Double[/*pesos*/]               bias = null;
    static Double[/*x*/][/*y*/]            neuroniosDeep = null;
    static Double[/*x*/]                   neuroniosSaida = null;
    static Function<Double, Double>        funcaoAtivacaoDeep = null;
    static Function<Double, Double>        funcaoAtivacaoSaida = null;
    static Double                          margemDeErro = null;
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
    static double margemAnterior = 0.0;
    static double margemErro = 0.01;
    static double temp0;
    static double temp1;
    static double ajusteLeve = 0.01;
    static double ajustePesado = 0.025;
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

    public Rede(String nome){
    }

    public static void loadDados(String nomeRede){

    }

    public static void saveDados(String nomeRede){
        MySQL.saveDados(nomeRede);
    }

    public static void setMargemErroMinima(double margem){
        margemErro = margem;
    }

    public static void treinar(){
        if(margemDeErroAlpha == null){
            toolGetMargemErro();
            margemDeErroAlpha = margemDeErro;
            toolSaveAplhaGen();
        }
        toolLoadAplhaGen();
        toolGetMargemErro();
        toolSaveAplhaGen();
        taxaAprendizagema = 10.0;
        while(true){
            stringMargemAnterior = new DecimalFormat("#,##0.000").format(margemDeErro);
            for(int i = 0; i < 10; i++){
                temp0 = margemDeErro;
                toolReduzirMargem();
                temp1 = margemDeErro;
                if(margemDeErro < margemDeErroAlpha){
                    toolSaveAplhaGen();
                }
                if(temp0 == temp1){
                    break;
                }
            }
            stringMargemPosterior = new DecimalFormat("#,##0.000").format(margemDeErro);
            /*-----------------------------------------------------*/
            toolGetInfo();
            if(margemDeErroAlpha < margemErro || forcarRetorno){
                forcarRetorno = false;
                toolLoadAplhaGen();
                break;
            }
            if(salvar){
                salvar = false;
                saveDados(nome);
            }
            if(margemDeErro > margemDeErroAlpha * 10 && margemDeErroAlpha >= 10.0){
                toolLoadAplhaGen();
            }
            if(stringMargemAnterior.equals(stringMargemPosterior)){
                taxaAprendizagema /= 10;
                if(taxaAprendizagema <= 0.00000000001){
                    taxaAprendizagema = 0.1;
                    toolLoadAplhaGen();
                    /*Variação normal*/
                    toolVariarPesos(ajusteLeve);
                    /**/
                    if(margemDeErro == margemAnterior){
                        /*variação maior*/
                        toolVariarPesos(ajustePesado);
                        /**/
                    }
                    margemAnterior = margemDeErro;
                }
            }
            /*-----------------------------------------------------*/
        }

    }

    public static void getPartida(int testes){
        toolGetMargemErro();
        taxaAprendizagema = 1.0;
        if(margemDeErroAlpha == null) {
            margemDeErroAlpha = 9999999999999.0;
        }
        for(int i = 0; i < testes; i++){
            toolGerarPesosAleatorios();

            for(int j = 0; j < 20; j++){
                temp0 = margemDeErro;
                toolReduzirMargem();
                temp1 = margemDeErro;
                if(temp0 == temp1 && i > 0){
                    break;
                }
                toolGetMargemErro();
            }
            if(margemDeErro < margemDeErroAlpha){
                toolGetMargemErro();
                toolSaveAplhaGen();
            }

            if(margemDeErroAlpha <= margemErro){
                areaDeTexto.setText("Ciclo:"+i+"\n Margem Proxima Encontrada: "+margemDeErroAlpha);
                break;
            }
            areaDeTexto.setText(String.format("%d testes restantes...\nMelhor margem: %f\nMargem: %f",testes-i,margemDeErroAlpha,margemDeErro));
        }
        toolGetMargemErro();
        toolLoadAplhaGen();
        toolGetMargemErro();
        areaDeTexto.setText("-");
    }

    public static void getFuncao(){
        taxaAprendizagema = 1.0;
        double temp0 = 9999999999999.0;
        String temp1 = "";
        String temp2 = "";
        List<String> funcoes = new ArrayList<>(Arrays.asList("none", "sigmoide", "relu", "leaky relu", "tanh", "bit"));
        for(String entrada : funcoes){
            for(String saida : funcoes){
                setDados(nome ,numeroDeEntradas, numeroDeSaidas, numeroDeFamilias, numeroDeColunas, entrada, saida);
                margemDeErroAlpha = 9999999999999.0;
                for(int i = 0; i < 10; i++){
                    toolGerarPesosAleatorios();
                    for(int j = 0; j < 3; j++){
                        toolReduzirMargem();
                    }
                    if(margemDeErro < margemDeErroAlpha){
                        toolSaveAplhaGen();
                    }
                }
                areaDeTexto.setText(String.format("%s , %s = %f\n",entrada,saida,margemDeErroAlpha));
                if(margemDeErroAlpha < temp0) {
                    temp0 = margemDeErroAlpha;
                    temp1 = entrada;
                    temp2 = saida;
                }
            }
        }
        areaDeTexto.setText("\n\nMelhor resultado: "+temp1+"-"+temp2+" - "+temp0);
        try{sleep(5000);}catch (Exception ignore){}
        setDados(nome ,numeroDeEntradas, numeroDeSaidas, numeroDeFamilias, numeroDeColunas, temp1, temp2);
    }

    public static void setAlimentacao(Double[][] entradas, Double[][] saidas){
        valoresEntrada = entradas;
        valoresSaida = saidas;
    }

    public static void setDados(String nomeRede ,int entradas, int saidas, int familias, int colunas, String funcaoDeep, String funcaoSaida){
        /*iniciar uma rede do zero*/
        /*
        * 1- setar informações basicas
        * 2- criar o tamanho dos vetores de dados
        * 3- inicializar os vetores
        * */
        //1
        nome = nomeRede;
        numeroDeEntradas = entradas;
        numeroDeSaidas = saidas;
        numeroDeColunas = colunas;
        numeroDeFamilias = familias;
        nomeFuncaoDeep = funcaoDeep;
        nomeFuncaoSaida = funcaoSaida;
        //2
        pesosEntradas = new Double[numeroDeEntradas][numeroDeColunas];
        pesosDeep = new Double[numeroDeColunas][numeroDeFamilias-1][numeroDeColunas];
        pesosSaida = new Double[numeroDeSaidas][numeroDeColunas];
        bias = new Double[numeroDeFamilias+1];
        neuroniosDeep = new Double[numeroDeColunas][numeroDeFamilias];
        neuroniosSaida = new Double[numeroDeSaidas];
        funcaoAtivacaoDeep = toolGetFuncao(nomeFuncaoDeep);
        funcaoAtivacaoSaida = toolGetFuncao(nomeFuncaoSaida);

        pesosEntradasAlpha = new Double[numeroDeEntradas][numeroDeColunas];
        pesosDeepAlpha = new Double[numeroDeColunas][numeroDeFamilias-1][numeroDeColunas];
        pesosSaidaAlpha = new Double[numeroDeSaidas][numeroDeColunas];
        biasAlpha = new Double[numeroDeFamilias+1];

        //3
        toolGerarPesosAleatorios();
        toolZerarNeuronios();
        toolIniciarVetores();
    }

    private static void toolGetMargemErro() {
        /*metodo para obter margem de erro(usado no treino)*/
        /*
        * 1- resetar os valores dos neuronios e margem em cada execução
        * 2- processar os valores de entrada
        * 3- processar os valores deep
        * 4- processar os valores de saida
        * */
        //1
        margemDeErro = 0.0;
        for (int i = 0; i < valoresEntrada.length; i++) {
            toolZerarNeuronios();
            //2
            for (int j = 0; j < numeroDeColunas; j++) {
                for (int k = 0; k < numeroDeEntradas; k++) {
                    neuroniosDeep[j][0] += pesosEntradas[k][j] * valoresEntrada[i][k];
                }
                neuroniosDeep[j][0] = funcaoAtivacaoDeep.apply(neuroniosDeep[j][0] + bias[0]);
            }
            //3
            for (int j = 0; j < numeroDeFamilias - 1; j++) {
                for (int k = 0; k < numeroDeColunas; k++) {
                    for (int l = 0; l < numeroDeColunas; l++) {
                        neuroniosDeep[k][j + 1] += pesosDeep[l][j][k] * neuroniosDeep[l][j];
                    }
                    neuroniosDeep[k][j + 1] = funcaoAtivacaoDeep.apply(neuroniosDeep[k][j + 1] + bias[j + 1]);
                }
            }
            //4
            for (int j = 0; j < numeroDeSaidas; j++) {
                for (int k = 0; k < numeroDeColunas; k++) {
                    neuroniosSaida[j] += pesosSaida[j][k] * neuroniosDeep[k][numeroDeFamilias - 1];
                }
                neuroniosSaida[j] = funcaoAtivacaoSaida.apply(neuroniosSaida[j] + bias[bias.length - 1]);
                double erro = Math.pow(valoresSaida[i][j] - neuroniosSaida[j], 2);
                margemDeErro += erro;
            }
        }
    }

    public static Double[] getProcessamento(Double[] valor) {
        /*metodo para processar um valor(usando quando rede estiver treinada)*/
        /*
         * 2- resetar os valores dos neuronios e margem
         * 3- processar os valores de entrada
         * 4- processar os valores deep
         * 5- redimensionar a variavel para o tamanho dos valores de saida
         * 6- processar os valores de saida
         * 7- retornar o mesma variavel com os valores de saida
         * */
        //1
        toolZerarNeuronios();
        //2
        for (int i = 0; i < numeroDeColunas; i++) {
            for (int j = 0; j < numeroDeEntradas; j++) {
                neuroniosDeep[i][0] += pesosEntradas[j][i] * valor[j];
            }
            neuroniosDeep[i][0] = funcaoAtivacaoDeep.apply(neuroniosDeep[i][0] + bias[0]);
        }
        //3
        for (int i = 0; i < numeroDeFamilias - 1; i++) {
            for (int j = 0; j < numeroDeColunas; j++) {
                for (int k = 0; k < numeroDeColunas; k++) {
                    neuroniosDeep[j][i + 1] += pesosDeep[k][i][j] * neuroniosDeep[k][i];
                }
                neuroniosDeep[j][i + 1] = funcaoAtivacaoDeep.apply(neuroniosDeep[j][i + 1] + bias[i + 1]);
            }
        }
        //4
        valor = new Double[numeroDeSaidas];
        //5
        for (int i = 0; i < numeroDeSaidas; i++) {
            for (int j = 0; j < numeroDeColunas; j++) {
                neuroniosSaida[i] += pesosSaida[i][j] * neuroniosDeep[j][numeroDeFamilias - 1];
            }
            valor[i] = funcaoAtivacaoSaida.apply(neuroniosSaida[i] + bias[bias.length - 1]);
        }
        //6
        return valor;
    }

    private static Function<Double, Double> toolGetFuncao(String nomeFuncaoDeep){
        /*metodo para as funções dos neuronios*/
        switch (nomeFuncaoDeep){
            case "none": return aDouble -> aDouble;
            case "bit": return aDouble -> {if(aDouble > 0.0){return 1.0;}return 0.0;};
            case "leaky relu": return aDouble -> { if(aDouble >= 0.0){ return aDouble; }return (aDouble * 0.001); };
            case "relu": return aDouble -> { if(aDouble >= 0.0){ return aDouble; }return 0.0; };
            case "tanh": return aDouble -> ( 2 / (1 + Math.pow(2.7182818284590452353602874, (-2 * aDouble))) -1);
            case "sigmoide": return aDouble -> (1 / (1 + Math.pow(2.7182818284590452353602874, -aDouble)));
            default : throw new NullPointerException("Função de ativação não encontrada");
        }
    }

    private static void toolReduzirMargem(){
        double numeroMaxDeep = 0.0;
        double numeroMaxSaida = 0.0;

        toolGetMargemErro();

        /*limitar o valor maximo que cada peso pode atingir*/
        switch (nomeFuncaoDeep.toLowerCase()){
            case "none":
            case "relu":
            case "leaky relu":
                numeroMaxDeep = 10;
                break;
            case "tanh":
                numeroMaxDeep = 8;
                break;
            case "sigmoide":
                numeroMaxDeep = 15;
                break;
            case "bit":
                numeroMaxDeep = 1;
                break;
        }

        switch (nomeFuncaoSaida.toLowerCase()){
            case "none":
            case "relu":
            case "leaky relu":
                numeroMaxSaida = 10;
                break;
            case "tanh":
                numeroMaxSaida = 8;
                break;
            case "sigmoide":
                numeroMaxSaida = 15;
                break;
            case "bit":
                numeroMaxSaida = 1;
                break;
        }

        double margemPassada = margemDeErro;
        double saveMargem;

        /*misturar posições para gerar combinações aleatorias*/
        Collections.shuffle(vetorColunas);
        Collections.shuffle(vetorFamilias);
        Collections.shuffle(vetorEntradas);
        Collections.shuffle(vetorSaidas);

        /*calibrar bias (deep)*/
        for(int i = 0; i < bias.length-1; i++){
            saveMargem = margemDeErro;
            bias[i] += taxaAprendizagema;
            toolGetMargemErro();
            if(margemDeErro > margemPassada){
                bias[i] -= 2*taxaAprendizagema;
                toolGetMargemErro();
                if(margemDeErro > margemPassada){
                    bias[i] += taxaAprendizagema;
                    margemDeErro = saveMargem;
                    margemPassada = saveMargem;
                }else{
                    margemPassada = margemDeErro;
                }
            }else{
                margemPassada = margemDeErro;
            }
        }

        /*calibrar pesos de entrada*/
        for(int i = 0; i < numeroDeEntradas; i++){
            for(int j = 0; j < numeroDeColunas; j++){
                saveMargem = margemDeErro;
                pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] += taxaAprendizagema;
                toolGetMargemErro();
                if(margemDeErro > margemPassada){
                    pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] -= 2*taxaAprendizagema;
                    toolGetMargemErro();
                    if(margemDeErro > margemPassada){
                        pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] += taxaAprendizagema;
                        margemDeErro = saveMargem;
                        margemPassada = saveMargem;
                    }else{
                        margemPassada = margemDeErro;
                    }
                }else{
                    margemPassada = margemDeErro;
                }
                if(pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] > numeroMaxDeep){
                    pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] = numeroMaxDeep;
                }
                else if(pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] < numeroMaxDeep*-1){
                    pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] = numeroMaxDeep*-1;
                }
            }
        }

        /*calibrar pesos deep*/
        for(int i = 0; i < numeroDeFamilias-1; i++){
            for(int j = 0; j < numeroDeColunas; j++){
                for(int k = 0; k < numeroDeColunas; k++){
                    saveMargem = margemDeErro;
                    pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] += taxaAprendizagema;
                    toolGetMargemErro();
                    if(margemDeErro > margemPassada){
                        pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] -= 2*taxaAprendizagema;
                        toolGetMargemErro();
                        if(margemDeErro > margemPassada){
                            pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] += taxaAprendizagema;
                            margemDeErro = saveMargem;
                            margemPassada = saveMargem;
                        }else{
                            margemPassada = margemDeErro;
                        }
                    }else{
                        margemPassada = margemDeErro;
                    }
                    if(pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] > numeroMaxDeep){
                        pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] = numeroMaxDeep;
                    }
                    else if(pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] < numeroMaxDeep*-1){
                        pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] = numeroMaxDeep*-1;
                    }
                }
            }
        }

        /*calibrar pesos de saida*/
        for(int i = 0; i < numeroDeSaidas; i++){
            for(int j = 0; j < numeroDeColunas; j++){
                saveMargem = margemDeErro;
                pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] += taxaAprendizagema;
                toolGetMargemErro();
                if(margemDeErro > margemPassada){
                    pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] -= 2*taxaAprendizagema;
                    toolGetMargemErro();
                    if(margemDeErro > margemPassada){
                        pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] += taxaAprendizagema;
                        margemDeErro = saveMargem;
                        margemPassada = saveMargem;
                    }else{
                        margemPassada = margemDeErro;
                    }
                }else{
                    margemPassada = margemDeErro;
                }
                if(pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] > numeroMaxSaida){
                    pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] = numeroMaxSaida;
                }
                else if(pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] < numeroMaxSaida*-1){
                    pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] = numeroMaxSaida*-1;
                }
            }
        }

        /*calibrar bias (saida)*/
        for(int i = bias.length-1; i < bias.length; i++){
            saveMargem = margemDeErro;
            bias[i] += taxaAprendizagema;
            toolGetMargemErro();
            if(margemDeErro > margemPassada){
                bias[i] -= 2*taxaAprendizagema;
                toolGetMargemErro();
                if(margemDeErro > margemPassada){
                    bias[i] += taxaAprendizagema;
                    margemDeErro = saveMargem;
                    margemPassada = saveMargem;
                }else{
                    margemPassada = margemDeErro;
                }
            }else{
                margemPassada = margemDeErro;
            }
        }
    }

    private static void toolSaveAplhaGen(){
        for(int i = 0; i < pesosEntradas.length; i++){
            for(int j = 0; j < pesosEntradas[i].length; j++){
                pesosEntradasAlpha[i][j] = pesosEntradas[i][j];
            }
        }
        for(int i = 0; i < pesosDeep.length; i++){
            for(int j = 0; j < pesosDeep[i].length; j++){
                for(int k = 0; k < pesosDeep[i][j].length; k++){
                    pesosDeepAlpha[i][j][k] = pesosDeep[i][j][k];
                }
            }
        }
        for(int i = 0; i < pesosSaida.length; i++){
            for(int j = 0; j < pesosSaida[i].length; j++){
                pesosSaidaAlpha[i][j] = pesosSaida[i][j];
            }
        }
        for(int i = 0; i < bias.length; i++){
            biasAlpha[i] = bias[i];
        }
        margemDeErroAlpha = margemDeErro;
    }

    private static void toolLoadAplhaGen(){
        for(int i = 0; i < pesosEntradasAlpha.length; i++){
            for(int j = 0; j < pesosEntradasAlpha[i].length; j++){
                pesosEntradas[i][j] = pesosEntradasAlpha[i][j];
            }
        }
        for(int i = 0; i < pesosDeepAlpha.length; i++){
            for(int j = 0; j < pesosDeepAlpha[i].length; j++){
                for(int k = 0; k < pesosDeepAlpha[i][j].length; k++){
                    pesosDeep[i][j][k] = pesosDeepAlpha[i][j][k];
                }
            }
        }
        for(int i = 0; i < pesosSaidaAlpha.length; i++){
            for(int j = 0; j < pesosSaidaAlpha[i].length; j++){
                pesosSaida[i][j] = pesosSaidaAlpha[i][j];
            }
        }
        for(int i = 0; i < biasAlpha.length; i++){
            bias[i] = biasAlpha[i];
        }
        margemDeErro = margemDeErroAlpha;
    }

    private static void toolGerarPesosAleatorios(){
        double mult = 0, sub = 0;

        switch (nomeFuncaoDeep.toLowerCase()){
            case "tanh":
                mult = 16.0; sub = 8;
                break;
            case "sigmoide":
                mult = 30.0; sub = 15;
                break;
            case "relu":
            case "leaky relu":
            case "none":
                mult = 2.0; sub = 1.0;
                break;
            case "bit": mult = 1.0; sub = 0.5;
                break;
        }

        for(int i = 0; i < pesosEntradas.length; i++){
            for (int j = 0; j < pesosEntradas[i].length; j++) {
                pesosEntradas[i][j] = Math.random() * mult - sub;
            }
        }

        for(int i = 0; i < pesosDeep.length; i++){
            for (int j = 0; j < pesosDeep[i].length; j++) {
                for (int k = 0; k < pesosDeep[i][j].length; k++) {
                    pesosDeep[i][j][k] = Math.random() * mult - sub;
                }
            }
        }

        switch (nomeFuncaoSaida.toLowerCase()){
            case "tanh":
                mult = 16.0; sub = 8;
                break;
            case "sigmoide":
                mult = 30.0; sub = 15;
                break;
            case "relu":
            case "leaky relu":
            case "none":
                mult = 2.0; sub = 1.0;
                break;
            case "bit": mult = 1.0; sub = 0.5;
                break;
        }

        for(int i = 0; i < pesosSaida.length; i++){
            for (int j = 0; j < pesosSaida[i].length; j++) {
                pesosSaida[i][j] = Math.random() * mult - sub;
            }
        }

        Arrays.fill(bias, 0.0);
    }

    private static void toolZerarNeuronios(){
        for (Double[] doubles : neuroniosDeep) {
            Arrays.fill(doubles, 0.0);
        }
        Arrays.fill(neuroniosSaida, 0.0);
    }

    protected static void toolIniciarVetores(){
        for(int i = 0; i < numeroDeColunas; i++){
            vetorColunas.add(i);
        }
        for(int i = 0; i < numeroDeFamilias-1; i++){
            vetorFamilias.add(i);
        }
        for(int i = 0; i < numeroDeEntradas; i++){
            vetorEntradas.add(i);
        }
        for(int i = 0; i < numeroDeSaidas; i++){
            vetorSaidas.add(i);
        }
    }

    private static void toolVariarPesos(double peso){

        double numeroMaxDeep = 0.0;
        double numeroMaxSaida = 0.0;

        switch (nomeFuncaoDeep.toLowerCase()){
            case "none":
            case "relu":
            case "leaky relu":
                numeroMaxDeep = 1.5;
                break;
            case "tanh":
                numeroMaxDeep = 8;
                break;
            case "sigmoide":
                numeroMaxDeep = 15;
                break;
            case "bit":
                numeroMaxDeep = 1;
                break;
        }

        switch (nomeFuncaoSaida.toLowerCase()){
            case "none":
            case "relu":
            case "leaky relu":
                numeroMaxSaida = 1.5;
                break;
            case "tanh":
                numeroMaxSaida = 8;
                break;
            case "sigmoide":
                numeroMaxSaida = 15;
                break;
            case "bit":
                numeroMaxSaida = 1;
                break;
        }

        for(int i = 0; i < pesosEntradas.length; i++){
            for (int j = 0; j < pesosEntradas[i].length; j++) {
                if(pesosEntradas[i][j] >= numeroMaxDeep){
                    pesosEntradas[i][j] -= 0.5 / 2 * peso;
                }else if(pesosEntradas[i][j] >= numeroMaxDeep*-1){
                    pesosEntradas[i][j] += 0.5 / 2 * peso;
                }else{
                    pesosEntradas[i][j] += ((Math.random() - 0.5 / 2) * peso);
                }
            }
        }

        for(int i = 0; i < pesosDeep.length; i++){
            for (int j = 0; j < pesosDeep[i].length; j++) {
                for (int k = 0; k < pesosDeep[i][j].length; k++) {
                    if(pesosDeep[i][j][k] >= numeroMaxDeep){
                        pesosDeep[i][j][k] -= 0.5 / 2 * peso;
                    }else if(pesosDeep[i][j][k] >= numeroMaxDeep*-1){
                        pesosDeep[i][j][k] += 0.5 / 2 * peso;
                    }else{
                        pesosDeep[i][j][k] += ((Math.random() - 0.5 / 2) * peso);
                    }
                }
            }
        }

        for(int i = 0; i < pesosSaida.length; i++){
            for (int j = 0; j < pesosSaida[i].length; j++) {
                if(pesosSaida[i][j] >= numeroMaxSaida){
                    pesosSaida[i][j] -= 0.5 / 2 * peso;
                }else if(pesosSaida[i][j] >= numeroMaxSaida*-1){
                    pesosSaida[i][j] += 0.5 / 2 * peso;
                }else{
                    pesosSaida[i][j] += ((Math.random() - 0.5 / 2) * peso);
                }
            }
        }

        Arrays.fill(bias, 0.0);
    }

    private static void toolGetInfo(){
        if(frame == null){
            frame = new JFrame("Informativo");
            frame.setLayout(null);
            frame.setSize(615,220);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(false);

            JButton margem = new JButton("Margem:");
            JButton margemAlpha = new JButton("Margem Alpha:");
            JButton ajuste = new JButton("Ajuste:");
            JButton forcarRetorno = new JButton("Forçar Retorno Alpha");
            JButton salvar = new JButton("salvar Dados Alpha");
            JButton menosModMe = new JButton("-");
            JButton infoModMe = new JButton("0.0");
            JButton maisModMe = new JButton("+");
            JButton menosModMa = new JButton("-");
            JButton infoModMa = new JButton("0.0");
            JButton maisModMa = new JButton("+");
            JButton anularPesoAleatorio = new JButton("Anular peso aleatório");

            margem.setBounds(0,0,200,25);
            margemAlpha.setBounds(200,0,200,25);
            ajuste.setBounds(400,0,200,25);
            info00.setBounds(0,25,200,25);
            info01.setBounds(200,25,200,25);
            info02.setBounds(400,25,200,25);
            forcarRetorno.setBounds(0,50,300,25);
            salvar.setBounds(300,50,300,25);
            menosModMe.setBounds(0,75, 50, 25);
            infoModMe.setBounds(50,75, 200, 25);
            maisModMe.setBounds(250,75, 50, 25);
            menosModMa.setBounds(300,75, 50, 25);
            infoModMa.setBounds(350,75, 200, 25);
            maisModMa.setBounds(550,75, 50, 25);
            anularPesoAleatorio.setBounds(0,100, 600, 25);
            areaDeTexto.setBounds(0,125, 600,50);

            frame.add(margem);
            frame.add(margemAlpha);
            frame.add(ajuste);
            frame.add(info00);
            frame.add(info01);
            frame.add(info02);
            frame.add(forcarRetorno);
            frame.add(salvar);
            frame.add(areaDeTexto);
            frame.add(menosModMe);
            frame.add(infoModMe);
            frame.add(maisModMe);
            frame.add(menosModMa);
            frame.add(infoModMa);
            frame.add(maisModMa);
            frame.add(anularPesoAleatorio);

            margem.setEnabled(false);
            margemAlpha.setEnabled(false);
            ajuste.setEnabled(false);
            info00.setEnabled(false);
            info01.setEnabled(false);
            info02.setEnabled(false);
            infoModMe.setEnabled(false);
            infoModMa.setEnabled(false);

            infoModMe.setText(new DecimalFormat("#,##0.000").format(ajusteLeve));
            infoModMa.setText(new DecimalFormat("#,##0.000").format(ajustePesado));

            forcarRetorno.addActionListener(button -> Rede.forcarRetorno = true);
            salvar.addActionListener(button -> Rede.salvar = true);
            menosModMe.addActionListener(button -> {
                ajusteLeve -= 0.01;
                infoModMe.setText(new DecimalFormat("#,##0.000").format(ajusteLeve));
                if(ajusteLeve <= 0.01){ajusteLeve = 0.0;}
            });
            maisModMe.addActionListener(button -> {
                ajusteLeve += 0.01;
                infoModMe.setText(new DecimalFormat("#,##0.000").format(ajusteLeve));
            });
            menosModMa.addActionListener(button -> {
                ajustePesado -= 0.025;
                infoModMa.setText(new DecimalFormat("#,##0.000").format(ajustePesado));
                if(ajustePesado <= 0.01){ajustePesado = 0.0;}
            });
            maisModMa.addActionListener(button -> {
                ajustePesado += 0.025;
                infoModMa.setText(new DecimalFormat("#,##0.000").format(ajustePesado));
            });
            anularPesoAleatorio.addActionListener(button -> {
                int i = (int) Math.round(Math.random() * pesosDeep.length - 1);
                int j = (int) Math.round(Math.random() * pesosDeep[0].length - 1);
                int k = (int) Math.round(Math.random() * pesosDeep[0][0].length - 1);

                pesosDeep[i][j][k] = 0.0;
            });

            frame.repaint();
            frame.validate();

            frame.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyChar() == 'a'){
                        menosModMe.doClick();
                    }
                    if(e.getKeyChar() == 'd'){
                        maisModMe.doClick();
                    }
                    if(e.getKeyChar() == 's'){
                        menosModMa.doClick();
                    }
                    if(e.getKeyChar() == 'w'){
                        maisModMa.doClick();
                    }
                    if(e.getKeyChar() == 'f'){
                        forcarRetorno.doClick();
                    }
                    if(e.getKeyChar() == 'r'){
                        anularPesoAleatorio.doClick();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });
        }else{
            info00.setText(new DecimalFormat("#,##0.000").format(margemDeErro));
            info01.setText(new DecimalFormat("#,##0.000").format(margemDeErroAlpha));
            info02.setText(String.valueOf(taxaAprendizagema));
        }
    }
}

