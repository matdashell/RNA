package rede;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;

public class Tools extends Data{

    static void variarPesos(){

        for(int i = 0; i < pesosEntradas.length; i++){
            for (int j = 0; j < pesosEntradas[i].length; j++) {
                if(Math.random()>0.4) {
                    pesosEntradas[i][j] += pesosEntradas[i][j] * 0.03;
                }
            }
        }

        for(int i = 0; i < pesosDeep.length; i++){
            for (int j = 0; j < pesosDeep[i].length; j++) {
                for (int k = 0; k < pesosDeep[i][j].length; k++) {
                    if(Math.random()>0.4) {
                        pesosDeep[i][j][k] += pesosDeep[i][j][k] * 0.03;
                    }
                }
            }
        }

        for(int i = 0; i < pesosSaida.length; i++){
            for (int j = 0; j < pesosSaida[i].length; j++) {
                if(Math.random()>0.4) {
                    pesosSaida[i][j] += pesosSaida[i][j] * 0.03;
                }
            }
        }
    }

     static void reduzirMargem(){
        double numeroMaxDeep = 0.0;
        double numeroMaxSaida = 0.0;

        Tools.getMargemErro();

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
            Tools.getMargemErro();
            if(margemDeErro > margemPassada){
                bias[i] -= 2*taxaAprendizagema;
                Tools.getMargemErro();
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
                Tools.getMargemErro();
                if(margemDeErro > margemPassada){
                    pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] -= 2*taxaAprendizagema;
                    Tools.getMargemErro();
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
                    Tools.getMargemErro();
                    if(margemDeErro > margemPassada){
                        pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] -= 2*taxaAprendizagema;
                        Tools.getMargemErro();
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
                Tools.getMargemErro();
                if(margemDeErro > margemPassada){
                    pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] -= 2*taxaAprendizagema;
                    Tools.getMargemErro();
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
            Tools.getMargemErro();
            if(margemDeErro > margemPassada){
                bias[i] -= 2*taxaAprendizagema;
                Tools.getMargemErro();
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

    static void getMargemErro() {
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
            zerarNeuronios();
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
                double erro = Math.pow(valoresSaida[i][j] - neuroniosSaida[j] , 10);
                margemDeErro += erro;
            }
        }
    }

    protected static void saveAlphaGen(){
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

    static void loadAlphaGen(){
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

    static void gerarPesosAleatorios(){
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

    static void zerarNeuronios(){
        for (Double[] doubles : neuroniosDeep) {
            Arrays.fill(doubles, 0.0);
        }
        Arrays.fill(neuroniosSaida, 0.0);
    }

    protected static void iniciarVetores(){
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

    static void getInfo(){
        if(frame == null){
            frame = new JFrame("Informativo");
            frame.setLayout(null);
            frame.setSize(615,170);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(false);

            JButton margem = new JButton("Margem^10:");
            JButton margemAlpha = new JButton("Margem Alpha^10:");
            JButton ajuste = new JButton("Ajuste:");
            JButton forcarRetorno = new JButton("Forçar Retorno Alpha");
            JButton salvar = new JButton("salvar Dados Alpha");

            margem.setBounds(0,0,200,25);
            margemAlpha.setBounds(200,0,200,25);
            ajuste.setBounds(400,0,200,25);
            info00.setBounds(0,25,200,25);
            info01.setBounds(200,25,200,25);
            info02.setBounds(400,25,200,25);
            forcarRetorno.setBounds(0,50,300,25);
            salvar.setBounds(300,50,300,25);
            areaDeTexto.setBounds(0,75, 600,50);

            frame.add(margem);
            frame.add(margemAlpha);
            frame.add(ajuste);
            frame.add(info00);
            frame.add(info01);
            frame.add(info02);
            frame.add(forcarRetorno);
            frame.add(salvar);
            frame.add(areaDeTexto);
            margem.setEnabled(false);
            margemAlpha.setEnabled(false);
            ajuste.setEnabled(false);
            info00.setEnabled(false);
            info01.setEnabled(false);
            info02.setEnabled(false);

            forcarRetorno.addActionListener(button -> Rede.forcarRetorno = true);
            salvar.addActionListener(button -> Rede.salvar = true);

            frame.repaint();
            frame.validate();

        }else{
            info00.setText(new DecimalFormat("#,##0.000").format(margemDeErro));
            info01.setText(new DecimalFormat("#,##0.000").format(margemDeErroAlpha));
            info02.setText(String.valueOf(taxaAprendizagema));
        }
    }

     static Function<Double, Double> getFuncao(String nomeFuncaoDeep){
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
}
