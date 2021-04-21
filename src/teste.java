import novo.Rede;

import java.util.ArrayList;
import java.util.Arrays;

public class teste {
    public static void main(String[] args) {
        Rede rede = new Rede("teste");
        rede.setDados(2,1,3,5,"leaky relu","leaky relu");

        Double[][] entradas = new Double[250][2];
        Double[][] saidas = new Double[250][1];

        for(int i = 0; i < 250; i++){
            entradas[i][0] = Math.random()*50;
            entradas[i][1] = Math.random()*50;
            saidas[i][0] = entradas[i][0] * entradas[i][1];
        }

        rede.setAlimentacao(entradas, saidas);
        rede.getPartida(1000);
        rede.treinar();


        System.out.println(new ArrayList<>(Arrays.asList(rede.getProcessamento(new Double[]{0.3,0.3}))));
        System.out.println(new ArrayList<>(Arrays.asList(rede.getProcessamento(new Double[]{0.4,0.3}))));
        System.out.println(new ArrayList<>(Arrays.asList(rede.getProcessamento(new Double[]{0.7,0.7}))));
        System.out.println(new ArrayList<>(Arrays.asList(rede.getProcessamento(new Double[]{0.2,0.3}))));
        System.out.println(new ArrayList<>(Arrays.asList(rede.getProcessamento(new Double[]{0.03,0.5}))));
        System.out.println(new ArrayList<>(Arrays.asList(rede.getProcessamento(new Double[]{2.0,3.0}))));
        System.out.println(new ArrayList<>(Arrays.asList(rede.getProcessamento(new Double[]{4.0,4.0}))));
        System.out.println(new ArrayList<>(Arrays.asList(rede.getProcessamento(new Double[]{2.0,5.0}))));
        System.out.println(new ArrayList<>(Arrays.asList(rede.getProcessamento(new Double[]{3.0,2.0}))));

    }
}
