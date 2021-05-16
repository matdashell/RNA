import rede.Rede;

public class teste {
    public static void main(String[] args) {
        Rede.setDados("nomeRede" ,2,1,3,5,"leaky relu","leaky relu");
        Rede.init();

        int amostras = 50;
        boolean refinar = false;

        while(true) {
            Double[][] entradas = new Double[amostras][2];
            Double[][] saidas = new Double[amostras][1];
            for (int i = 0; i < amostras; i++) {
                entradas[i][0] = Math.random() * 10;
                entradas[i][1] = Math.random() * 10;
                saidas[i][0] = entradas[i][0] * entradas[i][1];
            }
            Rede.setAlimentacao(entradas, saidas);
            Rede.getPartida(100, refinar);
            Rede.treinar();

            for(int i = 0; i < 100; i++){
                double primeiro = Math.round(Math.random() * 10);
                double segundo = Math.round(Math.random() * 10);
                System.out.printf("\n %f | %f = %f ",primeiro, segundo, Rede.getProcessamento(new Double[]{primeiro,segundo})[0]);
            }
            refinar = true;
        }
    }
}
