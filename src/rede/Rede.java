package rede;
import java.util.*;

import static java.lang.Thread.sleep;

public class Rede extends Data{

    static List<Thread> threads = new ArrayList<>();
    static Data save = new Data();

    public Rede(String nome) {
    }

    //pré configurações da rede para que ela se inicie sem erros
    public static void init(){
        Tools.iniciarVetores();
        Tools.gerarPesosAleatorios();
        Tools.getInfo();
    }

    //carregar dados da rede e respectiuvos pesos dos vetores em bando de dados
    public static void loadDados(String nomeRede){

    }

    //salvar dados da rede e respectivos pesos dos vetores em banco de dados
    public static void saveDados(String nomeRede){

    }

    //metodo para treinamento da rede com base nos dados de entrada
    public static void treinar(int geracoes, int ciclos){
        save = Tools.loadAlphaGen(save);
        save = Tools.getMargemErro(save);
        Tools.saveAlphaGen(save);

        while(margemDeErroAlpha > 10){
            for(int i = 0; i < geracoes; i++){
                threads.add(new Thread());
                threads.get(i).conf(ciclos);
            }
            for(int i = 0; i < geracoes; i++) {
                System.out.println(threads.get(i).obt().margemDeErro);
                if(threads.get(i).getData().margemDeErro < margemDeErroAlpha){
                    Tools.saveAlphaGen(threads.get(i).getData());
                }
            }
            save = Tools.loadAlphaGen(save);
            System.out.println(save.margemDeErro+"\n\n\n");
            threads.clear();
            Tools.getInfo();

            if(forcarRetorno){
                forcarRetorno = false;
                taxaAprendizagem = taxaAprendizagem/10;
            }
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

        save.neuroniosDeep = new Double[numeroDeColunas][numeroDeFamilias];
        save.neuroniosSaida = new Double[numeroDeSaidas];

        save.pesosEntradas = new Double[numeroDeEntradas][numeroDeColunas];
        save.pesosDeep = new Double[numeroDeColunas][numeroDeFamilias-1][numeroDeColunas];
        save.pesosSaida = new Double[numeroDeSaidas][numeroDeColunas];
        save.bias = new Double[numeroDeFamilias+1];

        pesosEntradasAlpha = new Double[numeroDeEntradas][numeroDeColunas];
        pesosDeepAlpha = new Double[numeroDeColunas][numeroDeFamilias-1][numeroDeColunas];
        pesosSaidaAlpha = new Double[numeroDeSaidas][numeroDeColunas];
        biasAlpha = new Double[numeroDeFamilias+1];
    }

    //get processamento com função de processar dados de entrada com base na melhor rede disponível
    public static Double[] getProcessamento(Double[] valor) {
        save = Tools.zerarNeuronios(save);

        for (int i = 0; i < numeroDeColunas; i++) {
            for (int j = 0; j < numeroDeEntradas; j++) {
                save.neuroniosDeep[i][0] += pesosEntradasAlpha[j][i] * valor[j];
            }
            save.neuroniosDeep[i][0] = funcaoAtivacaoDeep.apply(save.neuroniosDeep[i][0] + biasAlpha[0]);
        }

        for (int i = 0; i < numeroDeFamilias - 1; i++) {
            for (int j = 0; j < numeroDeColunas; j++) {
                for (int k = 0; k < numeroDeColunas; k++) {
                    save.neuroniosDeep[j][i + 1] += pesosDeepAlpha[k][i][j] * save.neuroniosDeep[k][i];
                }
                save.neuroniosDeep[j][i + 1] = funcaoAtivacaoDeep.apply(save.neuroniosDeep[j][i + 1] + biasAlpha[i + 1]);
            }
        }

        valor = new Double[numeroDeSaidas];

        for (int i = 0; i < numeroDeSaidas; i++) {
            for (int j = 0; j < numeroDeColunas; j++) {
                save.neuroniosSaida[i] += pesosSaidaAlpha[i][j] * save.neuroniosDeep[j][numeroDeFamilias - 1];
            }
            valor[i] = funcaoAtivacaoSaida.apply(save.neuroniosSaida[i] + biasAlpha[biasAlpha.length - 1]);
        }

        return valor;
    }
}

