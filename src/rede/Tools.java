package rede;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;

public class Tools extends Data{

    private static Double numeroMaxDeep = null;
    private static Double numeroMaxSaida = null;

    //retropopagação com intuito de reduzir margem de erro da rede para que a mesma aprenda
    static Data reduzirMargem(Data data){

        data = Tools.getMargemErro(data);

        double margemPassada = data.margemDeErro;
        double saveMargem;

        /*misturar posições para gerar combinações aleatorias*/
        Collections.shuffle(vetorColunas);
        Collections.shuffle(vetorFamilias);
        Collections.shuffle(vetorEntradas);
        Collections.shuffle(vetorSaidas);

        /*calibrar pesos deep*/
        for(int i = 0; i < numeroDeFamilias-1; i++){
            for(int j = 0; j < numeroDeColunas; j++){
                for(int k = 0; k < numeroDeColunas; k++){
                    saveMargem = data.margemDeErro;
                    data.pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] += taxaAprendizagem;
                    data = Tools.getMargemErro(data);
                    if(data.margemDeErro > margemPassada){
                        data.pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] -= 2* taxaAprendizagem;
                        data = Tools.getMargemErro(data);
                        if(data.margemDeErro > margemPassada){
                            data.pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] += taxaAprendizagem;
                            data.margemDeErro = saveMargem;
                            margemPassada = saveMargem;
                        }else{
                            margemPassada = data.margemDeErro;
                        }
                    }else{
                        margemPassada = data.margemDeErro;
                    }
                    if(data.pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] > numeroMaxDeep){
                        data.pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] = numeroMaxDeep;
                    }
                    else if(data.pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] < numeroMaxDeep*-1){
                        data.pesosDeep[vetorColunas.get(k)][vetorFamilias.get(i)][vetorColunas.get(j)] = numeroMaxDeep*-1;
                    }
                }
            }
        }

        /*calibrar bias (deep)*/
        for(int i = 0; i < data.bias.length-1; i++){
            saveMargem = data.margemDeErro;
            data.bias[i] += taxaAprendizagem;
            data = Tools.getMargemErro(data);
            if(data.margemDeErro > margemPassada){
                data.bias[i] -= 2* taxaAprendizagem;
                data = Tools.getMargemErro(data);
                if(data.margemDeErro > margemPassada){
                    data.bias[i] += taxaAprendizagem;
                    data.margemDeErro = saveMargem;
                    margemPassada = saveMargem;
                }else{
                    margemPassada = data.margemDeErro;
                }
            }else{
                margemPassada = data.margemDeErro;
            }
        }

        /*calibrar pesos de entrada*/
        for(int i = 0; i < numeroDeEntradas; i++){
            for(int j = 0; j < numeroDeColunas; j++){
                saveMargem = data.margemDeErro;
                data.pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] += taxaAprendizagem;
                data = Tools.getMargemErro(data);
                if(data.margemDeErro > margemPassada){
                    data.pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] -= 2* taxaAprendizagem;
                    data = Tools.getMargemErro(data);
                    if(data.margemDeErro > margemPassada){
                        data.pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] += taxaAprendizagem;
                        data.margemDeErro = saveMargem;
                        margemPassada = saveMargem;
                    }else{
                        margemPassada = data.margemDeErro;
                    }
                }else{
                    margemPassada = data.margemDeErro;
                }
                if(data.pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] > numeroMaxDeep){
                    data.pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] = numeroMaxDeep;
                }
                else if(data.pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] < numeroMaxDeep*-1){
                    data.pesosEntradas[vetorEntradas.get(i)][vetorColunas.get(j)] = numeroMaxDeep*-1;
                }
            }
        }

        /*calibrar pesos de saida*/
        for(int i = 0; i < numeroDeSaidas; i++){
            for(int j = 0; j < numeroDeColunas; j++){
                saveMargem = data.margemDeErro;
                data.pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] += taxaAprendizagem;
                data = Tools.getMargemErro(data);
                if(data.margemDeErro > margemPassada){
                    data.pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] -= 2* taxaAprendizagem;
                    data = Tools.getMargemErro(data);
                    if(data.margemDeErro > margemPassada){
                        data.pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] += taxaAprendizagem;
                        data.margemDeErro = saveMargem;
                        margemPassada = saveMargem;
                    }else{
                        margemPassada = data.margemDeErro;
                    }
                }else{
                    margemPassada = data.margemDeErro;
                }
                if(data.pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] > numeroMaxSaida){
                    data.pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] = numeroMaxSaida;
                }
                else if(data.pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] < numeroMaxSaida*-1){
                    data.pesosSaida[vetorSaidas.get(i)][vetorColunas.get(j)] = numeroMaxSaida*-1;
                }
            }
        }

        /*calibrar bias (saida)*/
        for(int i = data.bias.length-1; i < data.bias.length; i++){
            saveMargem = data.margemDeErro;
            data.bias[i] += taxaAprendizagem;
            data = Tools.getMargemErro(data);
            if(data.margemDeErro > margemPassada){
                data.bias[i] -= 2* taxaAprendizagem;
                data = Tools.getMargemErro(data);
                if(data.margemDeErro > margemPassada){
                    data.bias[i] += taxaAprendizagem;
                    data.margemDeErro = saveMargem;
                    margemPassada = saveMargem;
                }else{
                    margemPassada = data.margemDeErro;
                }
            }else{
                margemPassada = data.margemDeErro;
            }
        }
        return data;
    }

    //obter margem de erro atual da rede para levantamento de informações durante o treinamento
    static Data getMargemErro(Data data) {

        data.margemDeErro = 0.0;

        for (int i = 0; i < valoresEntrada.length; i++) {

            data = zerarNeuronios(data);

            for (int j = 0; j < numeroDeColunas; j++) {
                for (int k = 0; k < numeroDeEntradas; k++) {
                    data.neuroniosDeep[j][0] += data.pesosEntradas[k][j] * valoresEntrada[i][k];
                }
                data.neuroniosDeep[j][0] = funcaoAtivacaoDeep.apply(data.neuroniosDeep[j][0] + data.bias[0]);
            }

            for (int j = 0; j < numeroDeFamilias - 1; j++) {
                for (int k = 0; k < numeroDeColunas; k++) {
                    for (int l = 0; l < numeroDeColunas; l++) {
                        data.neuroniosDeep[k][j + 1] += data.pesosDeep[l][j][k] * data.neuroniosDeep[l][j];
                    }
                    data.neuroniosDeep[k][j + 1] = funcaoAtivacaoDeep.apply(data.neuroniosDeep[k][j + 1] + data.bias[j + 1]);
                }
            }

            for (int j = 0; j < numeroDeSaidas; j++) {
                for (int k = 0; k < numeroDeColunas; k++) {
                    data.neuroniosSaida[j] += data.pesosSaida[j][k] * data.neuroniosDeep[k][numeroDeFamilias - 1];
                }
                data.neuroniosSaida[j] = funcaoAtivacaoSaida.apply(data.neuroniosSaida[j] + data.bias[data.bias.length - 1]);

                double erro = Math.pow(valoresSaida[i][j] - data.neuroniosSaida[j] , 2);

                data.margemDeErro += erro;
            }
        }
        return data;
    }

    //definir tamanho dos vetores
    public static Data setSize(Data data){
        data.neuroniosDeep = new Double[Rede.save.neuroniosDeep.length][Rede.save.neuroniosDeep[0].length];
        data.neuroniosSaida = new Double[Rede.save.neuroniosSaida.length];
        data.pesosEntradas = new Double[pesosEntradasAlpha.length][pesosEntradasAlpha[0].length];
        data.pesosDeep = new Double[pesosDeepAlpha.length][pesosDeepAlpha[0].length][pesosDeepAlpha[0][0].length];
        data.pesosSaida = new Double[pesosSaidaAlpha.length][pesosSaidaAlpha[0].length];
        data.bias = new Double[biasAlpha.length];

        return data;
    }

    //salvar melhor geração da rede para que se possa dar continuidade ao treinamento usando 'herdeiros' da mesma
    public static void saveAlphaGen(Data data){
        for(int i = 0; i < data.pesosEntradas.length; i++){
            for(int j = 0; j < data.pesosEntradas[i].length; j++){
                pesosEntradasAlpha[i][j] = data.pesosEntradas[i][j];
            }
        }
        for(int i = 0; i < data.pesosDeep.length; i++){
            for(int j = 0; j < data.pesosDeep[i].length; j++){
                for(int k = 0; k < data.pesosDeep[i][j].length; k++){
                    pesosDeepAlpha[i][j][k] = data.pesosDeep[i][j][k];
                }
            }
        }
        for(int i = 0; i < data.pesosSaida.length; i++){
            for(int j = 0; j < data.pesosSaida[i].length; j++){
                pesosSaidaAlpha[i][j] = data.pesosSaida[i][j];
            }
        }
        for(int i = 0; i < data.bias.length; i++){
            biasAlpha[i] = data.bias[i];
        }
        margemDeErroAlpha = data.margemDeErro;
    }

    //carregar geração alpha juntamente com save para atualização de treinamento
    static Data loadAlphaGen(Data data){
        for(int i = 0; i < pesosEntradasAlpha.length; i++){
            for(int j = 0; j < pesosEntradasAlpha[i].length; j++){
                data.pesosEntradas[i][j] = pesosEntradasAlpha[i][j];
            }
        }
        for(int i = 0; i < pesosDeepAlpha.length; i++){
            for(int j = 0; j < pesosDeepAlpha[i].length; j++){
                for(int k = 0; k < pesosDeepAlpha[i][j].length; k++){
                    data.pesosDeep[i][j][k] = pesosDeepAlpha[i][j][k];
                }
            }
        }
        for(int i = 0; i < pesosSaidaAlpha.length; i++){
            for(int j = 0; j < pesosSaidaAlpha[i].length; j++){
                data.pesosSaida[i][j] = pesosSaidaAlpha[i][j];
            }
        }
        for(int i = 0; i < biasAlpha.length; i++){
            data.bias[i] = biasAlpha[i];
        }
        data.margemDeErro = margemDeErroAlpha;

        return data;
    }

    //carregar geração save
    static Data loadSaveGen(Data data){
        for(int i = 0; i < pesosEntradasAlpha.length; i++){
            for(int j = 0; j < pesosEntradasAlpha[i].length; j++){
                data.pesosEntradas[i][j] = Rede.save.pesosEntradas[i][j];
            }
        }
        for(int i = 0; i < pesosDeepAlpha.length; i++){
            for(int j = 0; j < pesosDeepAlpha[i].length; j++){
                for(int k = 0; k < pesosDeepAlpha[i][j].length; k++){
                    data.pesosDeep[i][j][k] = Rede.save.pesosDeep[i][j][k];
                }
            }
        }
        for(int i = 0; i < pesosSaidaAlpha.length; i++){
            for(int j = 0; j < pesosSaidaAlpha[i].length; j++){
                data.pesosSaida[i][j] = Rede.save.pesosSaida[i][j];
            }
        }
        for(int i = 0; i < biasAlpha.length; i++){
            data.bias[i] = Rede.save.bias[i];
        }
        data.margemDeErro = Rede.save.margemDeErro;

        return data;
    }

    //gerar pesos aleatorios usado durante a fase inicial de treinamento para estipular melhor configuração incial
    static void gerarPesosAleatorios(){

        for(int i = 0; i < pesosEntradasAlpha.length; i++){
            for (int j = 0; j < pesosEntradasAlpha[i].length; j++) {
                pesosEntradasAlpha[i][j] = (Math.random() * (numeroMaxDeep * 2)) - numeroMaxDeep;
            }
        }
        for(int i = 0; i < pesosDeepAlpha.length; i++){
            for (int j = 0; j < pesosDeepAlpha[i].length; j++) {
                for (int k = 0; k < pesosDeepAlpha[i][j].length; k++) {
                    pesosDeepAlpha[i][j][k] =  (Math.random() * (numeroMaxDeep * 2)) - numeroMaxDeep;
                }
            }
        }
        for(int i = 0; i < pesosSaidaAlpha.length; i++){
            for (int j = 0; j < pesosSaidaAlpha[i].length; j++) {
                pesosSaidaAlpha[i][j] =  (Math.random() * (numeroMaxSaida * 2)) - numeroMaxSaida;
            }
        }
        Arrays.fill(biasAlpha, 0.0);
    }

    //zerar vetores responsavel por armazenar a somatoria dos dados
    static Data zerarNeuronios(Data data){

        for (Double[] doubles : data.neuroniosDeep) {
            Arrays.fill(doubles, 0.0);
        }
        Arrays.fill(data.neuroniosSaida, 0.0);

        return data;
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
//            info00.setText(String.valueOf(Rede.redes.get(0).margemDeErro));
            info01.setText(String.valueOf(margemDeErroAlpha));
            info02.setText(String.valueOf(taxaAprendizagem));
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
