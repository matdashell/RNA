package rede;

public class Thread extends java.lang.Thread {

    Data data = new Data();
    int ciclos;

    public void conf(int ciclos){
        this.ciclos = ciclos;
        this.start();
    }

    public Data obt() {
        try{this.join();}catch (Exception ignore) {}
        return this.data;
    }

    public Data getData(){
        return this.data;
    }

    @Override
    public void run(){

        data = Tools.setSize(data);
        data = Tools.loadSaveGen(data);

        double temp00;
        double temp01;

        for(int i = 0; i < this.ciclos; i++){
            temp00 = this.data.margemDeErro;
            this.data = Tools.reduzirMargem(this.data);
            temp01 = this.data.margemDeErro;

            if(temp00 == temp01){
                break;
            }
        }
    }
}
