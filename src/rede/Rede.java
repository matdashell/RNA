package rede;
import java.text.DecimalFormat;
import java.util.*;

import static java.lang.Thread.sleep;

public class Rede extends Data{

    public Rede(String nome) {
    }

    public static void init(){
        Tools.getInfo();
    }

    public static void loadDados(String nomeRede){
        MySQL.loadDados(nomeRede);
    }

    public static void saveDados(String nomeRede){
        MySQL.saveDados(nomeRede);
    }

    public static void setMargemErroMinima(double margem){
        margemErro = margem;
    }

    public static void treinar(){

        List<Integer> parallel = new ArrayList<>();
        parallel.add(0);

        parallel.parallelStream().forEach(x -> {

            if(margemDeErroAlpha == null){
                Tools.getMargemErro();
                margemDeErroAlpha = margemDeErro;
                Tools.saveAlphaGen();
            }
            Tools.loadAlphaGen();
            Tools.getMargemErro();
            Tools.saveAlphaGen();
            taxaAprendizagema = 10.0;

            while(true){
                stringMargemAnterior = new DecimalFormat("#,##0.000").format(margemDeErro);
                for(int i = 0; i < 10; i++){
                    temp0 = margemDeErro;
                    Tools.reduzirMargem();
                    temp1 = margemDeErro;
                    if(margemDeErro < margemDeErroAlpha){
                        Tools.saveAlphaGen();
                        contadorE = 0;
                    }
                    if(temp0 == temp1){
                        break;
                    }
                }

                stringMargemPosterior = new DecimalFormat("#,##0.000").format(margemDeErro);
                Tools.getInfo();
                if(margemDeErroAlpha < margemErro || forcarRetorno){
                    forcarRetorno = false;
                    Tools.loadAlphaGen();
                    break;
                }
                if(salvar){
                    salvar = false;
                    saveDados(nome);
                }
                if(stringMargemAnterior.equals(stringMargemPosterior)) {
                    taxaAprendizagema /= 10;
                }
                if(taxaAprendizagema <= 0.000001 || margemDeErro > margemDeErroAlpha * 2.0){
                    taxaAprendizagema = 0.1;
                    Tools.loadAlphaGen();
                    Tools.variarPesos();
                    contadorE++;
                    if(contadorE >= 10){
                        getPartida(100,true);
                    }
                }
            }

        });
    }

    public static void getPartida(int testes, boolean refinarRede){
        Tools.getMargemErro();
        if(refinarRede){
            taxaAprendizagema = 0.01;
        }else{
            taxaAprendizagema = 1.0;
        }


        if(margemDeErroAlpha == null) {
            margemDeErroAlpha = margemDeErro*100;
        }else{
            margemDeErroAlpha = margemDeErro;
        }

        for(int i = 0; i < testes; i++){

            if(refinarRede){
                Tools.loadAlphaGen();
                Tools.variarPesos();
            }else{
                Tools.gerarPesosAleatorios();
            }

            for(int j = 0; j < 50; j++){
                temp0 = margemDeErro;
                Tools.reduzirMargem();
                temp1 = margemDeErro;
                if(temp0 == temp1 && i > 0){
                    break;
                }
            }
            if(margemDeErro < margemDeErroAlpha){
                Tools.saveAlphaGen();
                contadorE = 0;
            }

            if(margemDeErroAlpha <= margemErro){
                areaDeTexto.setText("Ciclo:"+i+"\n Margem Proxima Encontrada: "+margemDeErroAlpha);
                break;
            }
            areaDeTexto.setText(String.format("%d testes restantes...\nMelhor margem: %f\nMargem: %f",testes-i,margemDeErroAlpha,margemDeErro));
        }
        Tools.getMargemErro();
        Tools.loadAlphaGen();
        Tools.getMargemErro();
        areaDeTexto.setText("-");
    }

    public static void getFuncao(){
        taxaAprendizagema = 1.0;
        Tools.getMargemErro();
        double temp0 = margemDeErro;
        String temp1 = "";
        String temp2 = "";
        List<String> funcoes = new ArrayList<>(Arrays.asList("none", "sigmoide", "relu", "leaky relu", "tanh", "bit"));
        for(String entrada : funcoes){
            for(String saida : funcoes){
                setDados(nome ,numeroDeEntradas, numeroDeSaidas, numeroDeFamilias, numeroDeColunas, entrada, saida);
                margemDeErroAlpha = margemDeErro*100;
                for(int i = 0; i < 10; i++){
                    Tools.gerarPesosAleatorios();
                    for(int j = 0; j < 3; j++){
                        Tools.reduzirMargem();
                    }
                    if(margemDeErro < margemDeErroAlpha){
                        Tools.saveAlphaGen();
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
        funcaoAtivacaoDeep = Tools.getFuncao(nomeFuncaoDeep);
        funcaoAtivacaoSaida = Tools.getFuncao(nomeFuncaoSaida);

        pesosEntradasAlpha = new Double[numeroDeEntradas][numeroDeColunas];
        pesosDeepAlpha = new Double[numeroDeColunas][numeroDeFamilias-1][numeroDeColunas];
        pesosSaidaAlpha = new Double[numeroDeSaidas][numeroDeColunas];
        biasAlpha = new Double[numeroDeFamilias+1];

        //3
        Tools.gerarPesosAleatorios();
        Tools.zerarNeuronios();
        Tools.iniciarVetores();
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
        Tools.zerarNeuronios();
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
}

