import rede.Rede;

public class teste {
    public static void main(String[] args) {

        Rede.setDados("teste", 2, 1, 10, 6, "leaky relu", "leaky relu");
        Rede.init();

        Double[][] entrada = new Double[100][2];
        Double[][] saida = new Double[100][1];

        for(int i = 0; i < 100; i++){
            entrada[i][0] = Math.random()*10;
            entrada[i][1] = Math.random()*10;
            saida[i][0] = entrada[i][0] * entrada[i][1];
        }

        Rede.setAlimentacao(entrada, saida);
        Rede.treinar(5, 20);
    }
}