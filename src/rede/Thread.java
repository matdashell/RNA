package rede;

public class Thread extends java.lang.Thread {

    Data data = null;
    int ciclos;

    public void set(Data data, int ciclos){
        this.ciclos = ciclos;
        this.data = data;
        this.start();
    }

    public Data get() {
        try{this.join();}catch (Exception ignore) {}
        return this.data;
    }

    @Override
    public void run(){
        for(int i = 0; i < this.ciclos; i++){

        }
    }
}
