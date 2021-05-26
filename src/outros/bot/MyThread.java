package outros.bot;

import static java.lang.Thread.sleep;

@SuppressWarnings("ALL")
public class MyThread {

    static int quantidadeDeNucleos = 0;
    static int quantidadeDeExecucoesNucleo = 0;
    int meuNumero;
    Find find = null;
    Nucleo nucleo = new Nucleo();
    boolean iniciado = false;
    boolean processar = false;

    public MyThread(){
        this.meuNumero = quantidadeDeNucleos;
        quantidadeDeNucleos++;
    }

    public class Nucleo extends Thread{
        public void run(){
            iniciado = true;
            while(true){
                try{sleep(1000);}catch (Exception ignored){}
                if(processar){
                    /*funções do núcleo*/

                    Bots.algoritimoExecutavel.apply(find);
                    alterarVariaveis();
                    processar = false;

                    /*funções do núcleo*/
                }
            }
        }
    }
    public synchronized void setNucleo(){
        this.find = null;
        this.find = new Find();
        this.find.numeroThread = meuNumero;
    }
    public synchronized void ligarNucleo(){
        setNucleo();
        if(this.iniciado){
            this.processar = true;
        }else{
            this.processar = true;
            this.nucleo.start();
        }
    }
    public synchronized void encerrarNucleo(){
        this.nucleo.stop();
    }
    public synchronized static void alterarVariaveis(){
        try{sleep(1);}catch (Exception e){}
        quantidadeDeExecucoesNucleo++;
    }
}