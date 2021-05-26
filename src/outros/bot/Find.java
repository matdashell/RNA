package outros.bot;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static java.lang.Thread.sleep;

@SuppressWarnings("ALL")
public class Find {

    public WebDriver driver = null;
    Robot r = null;
    int meuNumero;
    int numeroThread;
    static int contadorBot = 0;
    static WebElement html = null;


    //informações sobre quantiade de execuções
    public Find() {
        meuNumero = Find.contadorBot;
        Find.contadorBot++;
    }


    //configuração para iniciar o driver
    public void init(boolean headless) throws AWTException {
        System.setProperty("webdriver.chrome.driver", ".idea/drivers/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        if(!headless) { options.addArguments("--headless"); }
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Bots.tempoDeEsperaDriver, TimeUnit.SECONDS);
    }


    //retorna o primeiro webElement com base na busca
    public WebElement one(String busca){
      return auxReturnWebDriver(driver, busca).get(0);
    }


    //retorna o primeiro webElement com base na busca dentro do webElement
    public static WebElement one(WebElement webElement ,String busca){
        return auxReturnWebElement(webElement, busca).get(0);

    }


    //retorna o primeiro webElement com base na busca dentro da lista de webElements
    public static WebElement one(List<WebElement> listaWE ,String busca){
        for(WebElement dados : listaWE){
            return auxReturnWebElement(dados, busca).get(0);
        }
        return null;
    }


    //retornar uma lista de webElement com base na busca
    public List<WebElement> more(String busca){
        return auxReturnWebDriver(driver, busca);
    }


    //retorna uma lista de webElement com base na busca dentro do webElement
    public static List<WebElement> more(WebElement webElement ,String busca){
        return auxReturnWebElement(webElement, busca);
    }


    //retorna todos os webElements dentro de uma lista de webElements
    public static List<WebElement> more(List<WebElement> listaWE ,String busca){
        List<WebElement> retorno = new ArrayList<>();
        List<WebElement> temp = new ArrayList<>();

        for(WebElement dados : listaWE){
            try {
                temp = (auxReturnWebElement(dados, busca));
            }catch (Exception ignore){
            }
            for(WebElement elementos : temp) {
                retorno.add(elementos);
            }
        }
        return retorno;
    }


    //tem função de aguardar enquanto o elemento procurado está ativo na tela
    public void waitWhileEnable(String busca){
        driver.manage().timeouts().implicitlyWait(200,TimeUnit.MILLISECONDS);
        boolean esperar = true;
        try{
            while(esperar) {
                esperar = false;
                try{
                    one(busca);
                    esperar = true;
                    time(500);
                }catch (Exception e) {  }
            }
        }catch (Exception ignored){ }
        driver.manage().timeouts().implicitlyWait(Bots.tempoDeEsperaDriver,TimeUnit.SECONDS);
    }


    ////tem função de aguardar enquanto o elemento procurado está ativo na tela
    public void waitWhileEnable(WebElement webElement ,String busca){
        driver.manage().timeouts().implicitlyWait(200,TimeUnit.MILLISECONDS);
        boolean esperar = true;
        try{
            while(esperar) {
                esperar = false;
                try{
                    one(webElement ,busca);
                    esperar = true;
                    time(500);
                }catch (Exception e) {  }
            }
        }catch (Exception ignored){ }
        driver.manage().timeouts().implicitlyWait(Bots.tempoDeEsperaDriver,TimeUnit.SECONDS);
    }


    //tem função de aguardar enquanto o elemento procurado não está ativo na tela
    public void waitWhileDisable(String busca){
        driver.manage().timeouts().implicitlyWait(200,TimeUnit.MILLISECONDS);
        boolean esperar = true;
        try{
            while(esperar) {
                esperar = false;
                try{
                    one(busca);
                }catch (Exception e) {
                    esperar = true;
                    time(500);
                }
            }
        }catch (Exception ignored){ }
        driver.manage().timeouts().implicitlyWait(Bots.tempoDeEsperaDriver,TimeUnit.SECONDS);
    }


    //tem função de aguardar enquanto o elemento procurado não está ativo na tela
    public void waitWhileDisable(WebElement webElement ,String busca){
        driver.manage().timeouts().implicitlyWait(200,TimeUnit.MILLISECONDS);
        boolean esperar = true;
        try{
            while(esperar) {
                esperar = false;
                try{
                    one(webElement ,busca);
                }catch (Exception e) {
                    esperar = true;
                    time(500);
                }
            }
        }catch (Exception ignored){ }
        driver.manage().timeouts().implicitlyWait(Bots.tempoDeEsperaDriver,TimeUnit.SECONDS);
    }


    //retorna um boolean com base da atividade do elemento procurado
    public boolean visible(String busca){
        driver.manage().timeouts().implicitlyWait(200,TimeUnit.MILLISECONDS);
        boolean retorno;

        try{
            one(busca);
            retorno = true;
        }catch (Exception e){
            retorno = false;
        }

        driver.manage().timeouts().implicitlyWait(Bots.tempoDeEsperaDriver,TimeUnit.SECONDS);
        return retorno;


    }

    //retorna um boolean com base da atividade do elemento procurado
    public boolean visible(String busca, int tempoMLS){
        driver.manage().timeouts().implicitlyWait(tempoMLS,TimeUnit.MILLISECONDS);
        boolean retorno;

        try{
            one(busca);
            retorno = true;
        }catch (Exception e){
            retorno = false;
        }

        driver.manage().timeouts().implicitlyWait(Bots.tempoDeEsperaDriver,TimeUnit.SECONDS);
        return retorno;

    }

    //retorna um boolean com base da atividade do elemento procurado
    public boolean visible(WebElement webElement ,String busca){
        driver.manage().timeouts().implicitlyWait(200,TimeUnit.MILLISECONDS);
        boolean retorno;

        try{
            one(webElement ,busca);
            retorno = true;
        }catch (Exception e){
            retorno = false;
        }

        driver.manage().timeouts().implicitlyWait(Bots.tempoDeEsperaDriver,TimeUnit.SECONDS);
        return retorno;

    }

    //retorna um boolean com base da atividade do elemento procurado
    public boolean visible(WebElement webElement ,String busca, int tempoMLS){
        driver.manage().timeouts().implicitlyWait(tempoMLS,TimeUnit.MILLISECONDS);
        boolean retorno;

        try{
            one(webElement ,busca);
            retorno = true;
        }catch (Exception e){
            retorno = false;
        }

        driver.manage().timeouts().implicitlyWait(Bots.tempoDeEsperaDriver,TimeUnit.SECONDS);
        return retorno;

    }

    //navega até uma página https
    public void page(String url){
        driver.get(url);
    }


    //retorna o driver do navegador
    public WebDriver driver(){
        return driver;
    }


    //encerrar a execução
    public synchronized void exit(){
        driver.quit();
    }


    //tempo
    public void time(long milissegundos){
        try{sleep(milissegundos);}catch (Exception ignored){}
    }


    //recarregar a pagina
    public void reload(){
        page(driver().getCurrentUrl());
    }


    //obter o numero Find (em caso de uso)
    public int getNumero(){
        return meuNumero;
    }

    //filtro de pesquisa de WebElements
    public static List<WebElement> auxReturnWebElement(WebElement webElement, String busca){

        if (busca.startsWith("x")) {
            return webElement.findElements(By.xpath(busca.substring(2)));
        } else if (busca.startsWith("i")) {
            return webElement.findElements(By.id(busca.substring(2)));
        } else if (busca.startsWith("n")) {
            return webElement.findElements(By.name(busca.substring(2)));
        } else if (busca.startsWith("css")) {
            return webElement.findElements(By.cssSelector(busca.substring(4)));
        } else if (busca.startsWith("c")) {
            return webElement.findElements(By.className(busca.substring(2)));
        } else if (busca.startsWith("t")) {
            return webElement.findElements(By.linkText(busca.substring(2)));
        } else if (busca.startsWith("p")) {
            return webElement.findElements(By.partialLinkText(busca.substring(2)));
        } else if (busca.startsWith("tn")) {
            return webElement.findElements(By.tagName(busca.substring(3)));
        } else{
            System.out.println("erro ao filtrar elemento ["+busca+"]");
            System.exit(0);
        }

        return null;
    }


    //filtro de pesquisa de WebElements usando o WebDriver
    public List<WebElement> auxReturnWebDriver(WebDriver webDriver, String busca) {

        if (busca.startsWith("x")) {
            return webDriver.findElements(By.xpath(busca.substring(2)));
        } else if (busca.startsWith("i")) {
            return webDriver.findElements(By.id(busca.substring(2)));
        } else if (busca.startsWith("n")) {
            return webDriver.findElements(By.name(busca.substring(2)));
        } else if (busca.startsWith("css")) {
            return webDriver.findElements(By.cssSelector(busca.substring(4)));
        } else if (busca.startsWith("c")) {
            return webDriver.findElements(By.className(busca.substring(2)));
        } else if (busca.startsWith("t")) {
            return webDriver.findElements(By.linkText(busca.substring(2)));
        } else if (busca.startsWith("p")) {
            return webDriver.findElements(By.partialLinkText(busca.substring(2)));
        } else if (busca.startsWith("tn")) {
            return webDriver.findElements(By.tagName(busca.substring(3)));
        } else {
            System.out.println("erro ao filtrar elemento [" + busca + "]");
            System.exit(0);
        }

        return null;
    }
}
