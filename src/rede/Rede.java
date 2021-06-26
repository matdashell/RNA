package rede;
import java.util.*;

import static java.lang.Thread.sleep;

public class Rede extends Data{

    public static List<Data> redes = new ArrayList<>();

    public Rede(String nome) {
    }

    //pré configurações da rede para que ela se inicie sem erros
    public static void init(){
        //--index--//
        //0 -> rede modificavel
        //1 -> save gen
        for(int i = 0; i < 2; i++){
            redes.add(new Data());
            redes.get(i).pesosEntradas = new Double[numeroDeEntradas][numeroDeColunas];
            redes.get(i).pesosDeep = new Double[numeroDeColunas][numeroDeFamilias-1][numeroDeColunas];
            redes.get(i).pesosSaida = new Double[numeroDeSaidas][numeroDeColunas];
            redes.get(i).bias = new Double[numeroDeFamilias+1];
        }
        Tools.zerarNeuronios();
        Tools.iniciarVetores();
        Tools.gerarPesosAleatorios();
        Tools.loadAlphaGen();
        Tools.getInfo();
    }

    //carregar dados da rede e respectiuvos pesos dos vetores em bando de dados
    public static void loadDados(String nomeRede){
        MySQL.loadDados(nomeRede);
        Tools.loadAlphaGen();
    }

    //salvar dados da rede e respectivos pesos dos vetores em banco de dados
    public static void saveDados(String nomeRede){
        Tools.loadAlphaGen();
        MySQL.saveDados(nomeRede);
    }

    //metodo para treinamento da rede com base nos dados de entrada
    public static void treinar(int geracoes, int ciclos){

        double temp0, temp1;

        taxaAprendizagema = 1.0;
        Tools.getMargemErro();
        Tools.saveAlphaGen();

        //encontrar melhor configuração para iniciar a rede
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < ciclos; j++) {
                temp0 = redes.get(0).margemDeErro;
                Tools.reduzirMargem();
                temp1 = redes.get(0).margemDeErro;

                Tools.getInfo();

                if(temp0 == temp1){
                    break;
                }
            }
            if (redes.get(0).margemDeErro < margemDeErroAlpha) {
                Tools.saveAlphaGen();
            }
            if(i != 9){
                Tools.gerarPesosAleatorios();
            }else{
                Tools.loadAlphaGen();
            }
            Tools.getMargemErro();
            areaDeTexto.setText(String.format("[%d de 10]",i+1));
        }

        Tools.loadSaveGen();

        //refinar rede para que se possa encontrar a menor margem de erro
        while(margemDeErroAlpha > 1){
            for(int i = 0; i < geracoes; i++){
                for(int j = 0; j < ciclos; j++){
                    temp0 = redes.get(0).margemDeErro;
                    Tools.reduzirMargem();
                    temp1 = redes.get(0).margemDeErro;

                    Tools.getInfo();
                    areaDeTexto.setText(String.format("Gen: %d de %d [%d de %d]",i,geracoes,j,ciclos));

                    if(temp0 == temp1){
                        break;
                    }
                }
                if(redes.get(0).margemDeErro < margemDeErroAlpha){
                    Tools.saveAlphaGen();
                }
                if(i != geracoes-1) {
                    Tools.loadSaveGen();
                }
            }
            Tools.loadAlphaGen();
            taxaAprendizagema = taxaAprendizagema/5;
        }
    }

    //set de alimentação da rede no qual serão usados para o treinamento da rede
    public static void setAlimentacao(Double[][] entradas, Double[][] saidas){
        valoresEntrada = entradas;
        valoresSaida = saidas;
    }

    //set de dados que constituem as configurações da rede
    public static void setDados(String nomeRede ,int entradas, int saidas, int familias, int colunas, String funcaoDeep, String funcaoSaida){
        nome = nomeRede;
        numeroDeEntradas = entradas;
        numeroDeSaidas = saidas;
        numeroDeColunas = colunas;
        numeroDeFamilias = familias;
        nomeFuncaoDeep = funcaoDeep;
        nomeFuncaoSaida = funcaoSaida;
        funcaoAtivacaoDeep = Tools.getFuncao(nomeFuncaoDeep);
        funcaoAtivacaoSaida = Tools.getFuncao(nomeFuncaoSaida);

        neuroniosDeep = new Double[numeroDeColunas][numeroDeFamilias];
        neuroniosSaida = new Double[numeroDeSaidas];

        pesosEntradasAlpha = new Double[numeroDeEntradas][numeroDeColunas];
        pesosDeepAlpha = new Double[numeroDeColunas][numeroDeFamilias-1][numeroDeColunas];
        pesosSaidaAlpha = new Double[numeroDeSaidas][numeroDeColunas];
        biasAlpha = new Double[numeroDeFamilias+1];
    }

    //get processamento com função de processar dados de entrada com base na melhor rede disponível
    public static Double[] getProcessamento(Double[] valor) {
        Tools.zerarNeuronios();

        for (int i = 0; i < numeroDeColunas; i++) {
            for (int j = 0; j < numeroDeEntradas; j++) {
                neuroniosDeep[i][0] += pesosEntradasAlpha[j][i] * valor[j];
            }
            neuroniosDeep[i][0] = funcaoAtivacaoDeep.apply(neuroniosDeep[i][0] + biasAlpha[0]);
        }

        for (int i = 0; i < numeroDeFamilias - 1; i++) {
            for (int j = 0; j < numeroDeColunas; j++) {
                for (int k = 0; k < numeroDeColunas; k++) {
                    neuroniosDeep[j][i + 1] += pesosDeepAlpha[k][i][j] * neuroniosDeep[k][i];
                }
                neuroniosDeep[j][i + 1] = funcaoAtivacaoDeep.apply(neuroniosDeep[j][i + 1] + biasAlpha[i + 1]);
            }
        }

        valor = new Double[numeroDeSaidas];

        for (int i = 0; i < numeroDeSaidas; i++) {
            for (int j = 0; j < numeroDeColunas; j++) {
                neuroniosSaida[i] += pesosSaidaAlpha[i][j] * neuroniosDeep[j][numeroDeFamilias - 1];
            }
            valor[i] = funcaoAtivacaoSaida.apply(neuroniosSaida[i] + biasAlpha[biasAlpha.length - 1]);
        }

        return valor;
    }
}

