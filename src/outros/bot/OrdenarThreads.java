package outros.bot;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

@SuppressWarnings("ALL")
public class OrdenarThreads{

    static List<MyThread> myThreadList = new ArrayList<>();
    static int controleDeExecucoes = 0;

    public OrdenarThreads(){
        for (int i = 0; i < Bots.numeroDeThreads; i++){
            myThreadList.add(new MyThread());
        }
        executarThreads();
        encerrarThreads();
    }

    public static void executarThreads(){
        while(MyThread.quantidadeDeExecucoesNucleo < Bots.numeroDeExecucoes){
            try{sleep(1000);}catch (Exception ignored){}
            myThreadList
                    .forEach(myThread -> {
                    if (!myThread.processar && controleDeExecucoes < Bots.numeroDeExecucoes) {
                        myThread.ligarNucleo();
                        controleDeExecucoes++;
                    }
                }
            );
        }
    }

    public static void encerrarThreads(){
        myThreadList.forEach(MyThread::encerrarNucleo);
        myThreadList = null;
    }
}