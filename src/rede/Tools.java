package rede;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;

public class Tools extends Data{

    private static Double numeroMaxDeep = null;
    private static Double numeroMaxSaida = null;

    //retropopagação com intuito de reduzir margem de erro da rede para que a mesma aprenda
    static void reduzirMargem(){

        Tools.getMargemErro();

        double margemPassada = Rede.redes.get(0).margemDeErro;
        double saveMargem;

        /*misturar posições para gerar combinações aleatorias*/
        Collections.shuffle(vetorColunas);
        Collections.shuffle(vetorFamilias);
        Collections.shuffle(vetorEntradas);
        Collections.shuffle(vetorSaidas);

        /*calibrar bias (deep)*/
        for(int i = 0; i < Rede.redes.get(0).bias.length-1; i++){
            saveMargem = Rede.redes.get(0).margemDeErro;
            Rede.redes.get(0).bias[i] += taxaAprendizagema;
            Tools.getMargemErro();
            if(Rede.redes.get(0).margemDeErro > margemPassada){
                Rede.redes.get(0).bias[i] -= 2*taxaAprendizagema;
                Tools.getMargemErro();
                if(Rede.redes.get(0).margemDeErro > margemPassada){
                    Rede.redes.get(0).bias[i] += taxaAprendizagema;
                    Rede.redes.get(0).margemDeErro = saveMargem;
                    margemPassada = saveMargem;
                }else{
                    margemPassada = Rede.redes.get(0).margemDeErro;
                }
            }else{
                margemPassada = Rede.redes.get(0).margemDeErro;
            }
        }

        /*calibrar pesos de entrada*/
        for(int i = 0; i < numeroDeEntradas; i++){
            for(int j = 0; j < numeroDeColunas; j++){
                saveMargem = Rede.redes.get(0).margemDeErro;
                Rede.redes.get(0).pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] += taxaAprendizagema;
                Tools.getMargemErro();
                if(Rede.redes.get(0).margemDeErro > margemPassada){
                    Rede.redes.get(0).pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] -= 2*taxaAprendizagema;
                    Tools.getMargemErro();
                    if(Rede.redes.get(0).margemDeErro > margemPassada){
                        Rede.redes.get(0).pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] += taxaAprendizagema;
                        Rede.redes.get(0).margemDeErro = saveMargem;
                        margemPassada = saveMargem;
                    }else{
                        margemPassada = Rede.redes.get(0).margemDeErro;
                    }
                }else{
                    margemPassada = Rede.redes.get(0).margemDeErro;
                }
                if(Rede.redes.get(0).pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] > numeroMaxDeep){
                    Rede.redes.get(0).pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] = numeroMaxDeep;
                }
                else if(Rede.redes.get(0).pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] < numeroMaxDeep*-1){
                    Rede.redes.get(0).pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] = numeroMaxDeep*-1;
                }
            }
        }

        /*calibrar pesos deep*/
        for(int i = 0; i < numeroDeFamilias-1; i++){
            for(int j = 0; j < numeroDeColunas; j++){
                for(int k = 0; k < numeroDeColunas; k++){
                    saveMargem = Rede.redes.get(0).margemDeErro;
                    Rede.redes.get(0).pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] += taxaAprendizagema;
                    Tools.getMargemErro();
                    if(Rede.redes.get(0).margemDeErro > margemPassada){
                        Rede.redes.get(0).pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] -= 2*taxaAprendizagema;
                        Tools.getMargemErro();
                        if(Rede.redes.get(0).margemDeErro > margemPassada){
                            Rede.redes.get(0).pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] += taxaAprendizagema;
                            Rede.redes.get(0).margemDeErro = saveMargem;
                            margemPassada = saveMargem;
                        }else{
                            margemPassada = Rede.redes.get(0).margemDeErro;
                        }
                    }else{
                        margemPassada = Rede.redes.get(0).margemDeErro;
                    }
                    if(Rede.redes.get(0).pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] > numeroMaxDeep){
                        Rede.redes.get(0).pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] = numeroMaxDeep;
                    }
                    else if(Rede.redes.get(0).pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] < numeroMaxDeep*-1){
                        Rede.redes.get(0).pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] = numeroMaxDeep*-1;
                    }
                }
            }
        }

        /*calibrar pesos de saida*/
        for(int i = 0; i < numeroDeSaidas; i++){
            for(int j = 0; j < numeroDeColunas; j++){
                saveMargem = Rede.redes.get(0).margemDeErro;
                Rede.redes.get(0).pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] += taxaAprendizagema;
                Tools.getMargemErro();
                if(Rede.redes.get(0).margemDeErro > margemPassada){
                    Rede.redes.get(0).pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] -= 2*taxaAprendizagema;
                    Tools.getMargemErro();
                    if(Rede.redes.get(0).margemDeErro > margemPassada){
                        Rede.redes.get(0).pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] += taxaAprendizagema;
                        Rede.redes.get(0).margemDeErro = saveMargem;
                        margemPassada = saveMargem;
                    }else{
                        margemPassada = Rede.redes.get(0).margemDeErro;
                    }
                }else{
                    margemPassada = Rede.redes.get(0).margemDeErro;
                }
                if(Rede.redes.get(0).pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] > numeroMaxSaida){
                    Rede.redes.get(0).pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] = numeroMaxSaida;
                }
                else if(Rede.redes.get(0).pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] < numeroMaxSaida*-1){
                    Rede.redes.get(0).pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] = numeroMaxSaida*-1;
                }
            }
        }

        /*calibrar bias (saida)*/
        for(int i = Rede.redes.get(0).bias.length-1; i < Rede.redes.get(0).bias.length; i++){
            saveMargem = Rede.redes.get(0).margemDeErro;
            Rede.redes.get(0).bias[i] += taxaAprendizagema;
            Tools.getMargemErro();
            if(Rede.redes.get(0).margemDeErro > margemPassada){
                Rede.redes.get(0).bias[i] -= 2*taxaAprendizagema;
                Tools.getMargemErro();
                if(Rede.redes.get(0).margemDeErro > margemPassada){
                    Rede.redes.get(0).bias[i] += taxaAprendizagema;
                    Rede.redes.get(0).margemDeErro = saveMargem;
                    margemPassada = saveMargem;
                }else{
                    margemPassada = Rede.redes.get(0).margemDeErro;
                }
            }else{
                margemPassada = Rede.redes.get(0).margemDeErro;
            }
        }
    }

    //obter margem de erro atual da rede para levantamento de informações durante o treinamento
    static void getMargemErro() {

        Rede.redes.get(0).margemDeErro = 0.0;

        for (int i = 0; i < valoresEntrada.length; i++) {
            zerarNeuronios();

            for (int j = 0; j < numeroDeColunas; j++) {
                for (int k = 0; k < numeroDeEntradas; k++) {
                    neuroniosDeep[j][0] += Rede.redes.get(0).pesosEntradas[k][j] * valoresEntrada[i][k];
                }
                neuroniosDeep[j][0] = funcaoAtivacaoDeep.apply(neuroniosDeep[j][0] + Rede.redes.get(0).bias[0]);
            }

            for (int j = 0; j < numeroDeFamilias - 1; j++) {
                for (int k = 0; k < numeroDeColunas; k++) {
                    for (int l = 0; l < numeroDeColunas; l++) {
                        neuroniosDeep[k][j + 1] += Rede.redes.get(0).pesosDeep[l][j][k] * neuroniosDeep[l][j];
                    }
                    neuroniosDeep[k][j + 1] = funcaoAtivacaoDeep.apply(neuroniosDeep[k][j + 1] + Rede.redes.get(0).bias[j + 1]);
                }
            }

            for (int j = 0; j < numeroDeSaidas; j++) {
                for (int k = 0; k < numeroDeColunas; k++) {
                    neuroniosSaida[j] += Rede.redes.get(0).pesosSaida[j][k] * neuroniosDeep[k][numeroDeFamilias - 1];
                }
                neuroniosSaida[j] = funcaoAtivacaoSaida.apply(neuroniosSaida[j] + Rede.redes.get(0).bias[Rede.redes.get(0).bias.length - 1]);

                double erro = Math.pow(valoresSaida[i][j] - neuroniosSaida[j] , 10);

                Rede.redes.get(0).margemDeErro += erro;
            }
        }
    }

    //salvar melhor geração da rede para que se possa dar continuidade ao treinamento usando 'herdeiros' da mesma
    public static void saveAlphaGen(){
        for(int i = 0; i < Rede.redes.get(0).pesosEntradas.length; i++){
            for(int j = 0; j < Rede.redes.get(0).pesosEntradas[i].length; j++){
                pesosEntradasAlpha[i][j] = Rede.redes.get(0).pesosEntradas[i][j];
            }
        }
        for(int i = 0; i < Rede.redes.get(0).pesosDeep.length; i++){
            for(int j = 0; j < Rede.redes.get(0).pesosDeep[i].length; j++){
                for(int k = 0; k < Rede.redes.get(0).pesosDeep[i][j].length; k++){
                    pesosDeepAlpha[i][j][k] = Rede.redes.get(0).pesosDeep[i][j][k];
                }
            }
        }
        for(int i = 0; i < Rede.redes.get(0).pesosSaida.length; i++){
            for(int j = 0; j < Rede.redes.get(0).pesosSaida[i].length; j++){
                pesosSaidaAlpha[i][j] = Rede.redes.get(0).pesosSaida[i][j];
            }
        }
        for(int i = 0; i < Rede.redes.get(0).bias.length; i++){
            biasAlpha[i] = Rede.redes.get(0).bias[i];
        }
        margemDeErroAlpha = Rede.redes.get(0).margemDeErro;
    }

    //carregar save contendo local de partida atual de treinamento
    static void loadSaveGen(){
        for(int i = 0; i < pesosEntradasAlpha.length; i++){
            for(int j = 0; j < pesosEntradasAlpha[i].length; j++){
                Rede.redes.get(0).pesosEntradas[i][j] = Rede.redes.get(1).pesosEntradas[i][j];
            }
        }
        for(int i = 0; i < pesosDeepAlpha.length; i++){
            for(int j = 0; j < pesosDeepAlpha[i].length; j++){
                for(int k = 0; k < pesosDeepAlpha[i][j].length; k++){
                    Rede.redes.get(0).pesosDeep[i][j][k] = Rede.redes.get(1).pesosDeep[i][j][k];
                }
            }
        }
        for(int i = 0; i < pesosSaidaAlpha.length; i++){
            for(int j = 0; j < pesosSaidaAlpha[i].length; j++){
                Rede.redes.get(0).pesosSaida[i][j] = Rede.redes.get(1).pesosSaida[i][j];
            }
        }
        for(int i = 0; i < biasAlpha.length; i++){
            Rede.redes.get(0).bias[i] = Rede.redes.get(1).bias[i];
        }
        Rede.redes.get(0).margemDeErro = Rede.redes.get(1).margemDeErro;
    }

    //carregar geração alpha juntamente com save para atualização de treinamento
    static void loadAlphaGen(){
        for(int i = 0; i < pesosEntradasAlpha.length; i++){
            for(int j = 0; j < pesosEntradasAlpha[i].length; j++){
                Rede.redes.get(1).pesosEntradas[i][j] = pesosEntradasAlpha[i][j];
            }
        }
        for(int i = 0; i < pesosDeepAlpha.length; i++){
            for(int j = 0; j < pesosDeepAlpha[i].length; j++){
                for(int k = 0; k < pesosDeepAlpha[i][j].length; k++){
                    Rede.redes.get(1).pesosDeep[i][j][k] = pesosDeepAlpha[i][j][k];
                }
            }
        }
        for(int i = 0; i < pesosSaidaAlpha.length; i++){
            for(int j = 0; j < pesosSaidaAlpha[i].length; j++){
                Rede.redes.get(1).pesosSaida[i][j] = pesosSaidaAlpha[i][j];
            }
        }
        for(int i = 0; i < biasAlpha.length; i++){
            Rede.redes.get(1).bias[i] = biasAlpha[i];
        }
        Rede.redes.get(1).margemDeErro = margemDeErroAlpha;

        Tools.loadSaveGen();
    }

    //gerar pesos aleatorios usado durante a fase inicial de treinamento para estipular melhor configuração incial
    static void gerarPesosAleatorios(){

        for(int i = 0; i < Rede.redes.get(0).pesosEntradas.length; i++){
            for (int j = 0; j < Rede.redes.get(0).pesosEntradas[i].length; j++) {
                pesosEntradasAlpha[i][j] = (Math.random() * (numeroMaxDeep * 2)) - numeroMaxDeep;
            }
        }
        for(int i = 0; i < Rede.redes.get(0).pesosDeep.length; i++){
            for (int j = 0; j < Rede.redes.get(0).pesosDeep[i].length; j++) {
                for (int k = 0; k < Rede.redes.get(0).pesosDeep[i][j].length; k++) {
                    pesosDeepAlpha[i][j][k] =  (Math.random() * (numeroMaxDeep * 2)) - numeroMaxDeep;
                }
            }
        }
        for(int i = 0; i < Rede.redes.get(0).pesosSaida.length; i++){
            for (int j = 0; j < Rede.redes.get(0).pesosSaida[i].length; j++) {
                pesosSaidaAlpha[i][j] =  (Math.random() * (numeroMaxSaida * 2)) - numeroMaxSaida;
            }
        }
        Arrays.fill(biasAlpha, 0.0);
    }

    //zerar vetores responsavel por armazenar a somatoria dos dados
    static void zerarNeuronios(){
        for (Double[] doubles : neuroniosDeep) {
            Arrays.fill(doubles, 0.0);
        }
        Arrays.fill(neuroniosSaida, 0.0);
    }

    //vetores com o respectivo tamanho da rede para que durante o teinamento seja possível treinar de formas diferentes
    protected static void iniciarVetores(){

        verificarValoresMax();

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

    //informações gráficas durente treinamento
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
            info00.setText(String.valueOf(Rede.redes.get(0).margemDeErro));
            info01.setText(String.valueOf(margemDeErroAlpha));
            info02.setText(String.valueOf(taxaAprendizagema));
        }
    }

    //obter função de ativação de acordo com a função escolhida para a rede
     static Function<Double, Double> getFuncao(String nomeFuncaoDeep){
        switch (nomeFuncaoDeep){
            case "none": return aDouble -> aDouble;
            case "bit": return aDouble -> {if(aDouble > 0.0){return 1.0;}return 0.0;};
            case "leaky relu": return aDouble -> { if(aDouble >= 0.0){ return aDouble; }return (aDouble * 0.001); };
            case "relu": return aDouble -> { if(aDouble >= 0.0){ return aDouble; }return 0.0; };
            case "tanh": return aDouble -> ( 2 / (1 + Math.pow(2.71828, (-2 * aDouble))) -1);
            case "sigmoide": return aDouble -> (1 / (1 + Math.pow(2.71828, -aDouble)));
            default : throw new NullPointerException("Função de ativação não encontrada");
        }
    }

    //verificação dos valores máx de ativação para aumentar eficiencia da geração de pesos aleatorios e controle de treinamento
    static void verificarValoresMax(){

        if(numeroMaxDeep == null || numeroMaxSaida == null){

            switch (nomeFuncaoDeep.toLowerCase()){
                case "none":
                case "relu":
                case "leaky relu":
                    numeroMaxDeep = 10.0;
                    break;
                case "tanh":
                    numeroMaxDeep = 8.0;
                    break;
                case "sigmoide":
                    numeroMaxDeep = 15.0;
                    break;
                case "bit":
                    numeroMaxDeep = 1.0;
                    break;
            }

            switch (nomeFuncaoSaida.toLowerCase()){
                case "none":
                case "relu":
                case "leaky relu":
                    numeroMaxSaida = 10.0;
                    break;
                case "tanh":
                    numeroMaxSaida = 8.0;
                    break;
                case "sigmoide":
                    numeroMaxSaida = 15.0;
                    break;
                case "bit":
                    numeroMaxSaida = 1.0;
                    break;
            }
        }
    }
}
