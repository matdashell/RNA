package rede;
import java.util.*;

import static java.lang.Thread.sleep;

public class Rede extends Data{

    public static List<Data> redes = new ArrayList<>();

    public Rede(String nome) {
    }

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



        });
    }

    public static void setAlimentacao(Double[][] entradas, Double[][] saidas){
        valoresEntrada = entradas;
        valoresSaida = saidas;
    }

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

    public static Double[] getProcessamento(Double[] valor) {
        Tools.zerarNeuronios();

        for (int i = 0; i < numeroDeColunas; i++) {
            for (int j = 0; j < numeroDeEntradas; j++) {
                neuroniosDeep[i][0] += redes.get(2).pesosEntradas[j][i] * valor[j];
            }
            neuroniosDeep[i][0] = funcaoAtivacaoDeep.apply(neuroniosDeep[i][0] + redes.get(2).bias[0]);
        }

        for (int i = 0; i < numeroDeFamilias - 1; i++) {
            for (int j = 0; j < numeroDeColunas; j++) {
                for (int k = 0; k < numeroDeColunas; k++) {
                    neuroniosDeep[j][i + 1] += redes.get(2).pesosDeep[k][i][j] * neuroniosDeep[k][i];
                }
                neuroniosDeep[j][i + 1] = funcaoAtivacaoDeep.apply(neuroniosDeep[j][i + 1] + redes.get(2).bias[i + 1]);
            }
        }

        valor = new Double[numeroDeSaidas];

        for (int i = 0; i < numeroDeSaidas; i++) {
            for (int j = 0; j < numeroDeColunas; j++) {
                neuroniosSaida[i] += redes.get(2).pesosSaida[i][j] * neuroniosDeep[j][numeroDeFamilias - 1];
            }
            valor[i] = funcaoAtivacaoSaida.apply(neuroniosSaida[i] + redes.get(2).bias[redes.get(2).bias.length - 1]);
        }

        return valor;
    }
}

