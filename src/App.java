import org.jpl7.*;
//import java.util.*;



public class App {
    
    //Metodo para abrir a base de dados do Prolog
    static boolean checkDB(){

        //Esta pesquisa vai verificar se o ficheiro do prolog abre com sucesso
        Query Conexao = 
            new Query( 
                "consult", 
                new Term[] {new Atom("src/DB.pl")} 
            );

        return Conexao.hasSolution();
    }
    public static void main(String[] args) throws Exception {    

    if(!checkDB()){//Se falhar a abrir, retornar mensagem de erro e fecha o programa
        System.out.println("Erro. Falha ao consultar a base de dados...\n"
        + "A fechar programa...\n");
        System.exit(0);
    }

        SistemaBancario BasedeDados = new SistemaBancario();
        BasedeDados.Menu();
    }
}
