package outros.bot;

import outros.sql.Sql;

import java.awt.*;
import java.util.function.Function;

public class Bots {

    public static Function<Find, Exception> algoritimoExecutavel = null;
    public static boolean paginaVisivel = false;
    public static int numeroDeExecucoes = 1;
    public static int numeroDeThreads = 1;
    public static int tempoDeEsperaDriver = 10;

    //localProject->

    public static void main(String[] args) {

        algoritimoExecutavel = find -> {
            try { find.init(paginaVisivel); } catch (AWTException e) { e.printStackTrace(); }
            /*Area de Código*/

            Sql sql = new Sql("localhost","birocronix","abc","teste");
            sql.codeSet("DROP DATABASE teste;");

            /*Area de Código*/
            return null;
        };
        new OrdenarThreads();
    }
}
