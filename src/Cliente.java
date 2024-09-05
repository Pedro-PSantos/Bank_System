import java.util.*;
import org.jpl7.*;
import org.jpl7.Integer;



public class Cliente {
    int NumeroDeCliente;
    String Nome;
    String Agencia;
    String Cidade;
    String DataAbertura;
    boolean EligibilidadeCredito;

    //Construtor da Classe Cliente
    public Cliente(){
        NumeroDeCliente = 0;
        Nome = null;
        Agencia = null;
        Cidade = null;
        DataAbertura = null;
        EligibilidadeCredito = false;
    }

    //Implementacao do Menu de Cliente
    public void MenuCliente(){

        
        Scanner sc = new Scanner(System.in);

        while(true){
            System.out.println("\nEscolha uma opcao do Menu Cliente, clicando no numero da frente:"
            + "\nVer saldo real do cliente - 1"
            + "\nVer saldo de credito do cliente - 2"
            + "\nVer movimentos da conta - 3"
            + "\nDepositar dinheiro - 4"
            + "\nLevantar dinheiro - 5"
            + "\nVerificar Eligibilidade a credito - 6"
            + "\nConceder Credito - 7"
            + "\nVoltar para tras - 0");

        int opcao = sc.nextInt();

        switch (opcao) {//Escolhe a opcao do menu consoante o input
            case 0:
                return;
            case 1:
                SaldoReal();
                break;
            case 2:
                SaldoCredito();
                break;
            case 3:
                Movimentos();
                break;
            case 4:
                Deposito();
                break;
            case 5:
                Levantar();
                break;
            case 6:
                VerificarElegivelCredito();
                break;
            case 7:
                ConcederCredito();
                break;
            default:
                System.out.println("Opcao invalida.\n");//Caso a opcao nao seja valida, imprime essa informacao
                break;
        }
        }  
    }

    //Metodo de obtencao do Saldo Real do cliente
    public void SaldoReal(){

        //Variaveis/Valores para a pesquisa
        Integer ID = new Integer(NumeroDeCliente);
        Variable Saldo = new Variable("Saldo");

        System.out.println("A fazer procura do Saldo Real do cliente " + NumeroDeCliente + "...\n");

        //Procura do saldo real do cliente
        Query q1 = 
            new Query( 
                "getSaldoReal",
                new Term[] {ID,Saldo} 
            );

        java.util.Map<String,Term> solution = q1.oneSolution();

        System.out.print("Saldo Real do cliente " + NumeroDeCliente + ": "
        + solution.get("Saldo") + " unidades monetarias.\n");
    }
    
    //Metodo para obter Saldo de Credito do cliente
    public void SaldoCredito(){

        //Variaveis/Valores para a pesquisa
        Integer ID = new Integer(NumeroDeCliente);
        Variable Saldo = new Variable("Saldo");

        System.out.println("A fazer procura do Saldo de Credito do cliente " + NumeroDeCliente + "...\n");

        //Procura do saldo de credito do cliente
        Query q1 = 
            new Query( 
                "getSaldoCredito", 
                new Term[] {ID,Saldo} 
            );

        java.util.Map<String,Term> solution = q1.oneSolution();

        System.out.print("Saldo de Credito do cliente " + NumeroDeCliente + ": "
        + solution.get("Saldo") + " unidades monetarias.\n");
    }

    //Metodo para obter os movimentos do cliente
    public void Movimentos(){

        //Variaveis/Valores para a pesquisa
        Integer ID = new Integer(NumeroDeCliente);
        Variable Valor = new Variable("Valor");
        Variable Data = new Variable("Data");

        System.out.println("A fazer procura dos Movimentos do cliente " + NumeroDeCliente + "...\n");

        //procura dos movimentos de conta do cliente
        Query q2 = 
            new Query( 
                "getMovimentos", 
                new Term[] {ID,Valor,Data} 
            );
            

        java.util.Map<String,Term>[] solutions2 = q2.allSolutions();


        //Caso a pesquisa tenha solucao ou nao, devolve a informacao se ha ou nao movimentos
        if(!q2.hasSolution())//caso nao haja retorna informacao de que nao ha movimentos
        {
            System.out.println("O cliente " + NumeroDeCliente + " não tem movimentos de conta.");
            return;
        }
        else//caso contrario, imprime os movimentos do cliente
            System.out.println("A mostrar movimentos do cliente " + NumeroDeCliente + ":");

        System.out.println("Temos "+ solutions2.length+" movimentos:\n");
        for(int i = 0; i < solutions2.length;i++)
        {
            System.out.println("Movimento de " + solutions2[i].get("Valor") 
            + " no dia " + solutions2[i].get("Data").name());
        }
        System.out.println("");

    }

    //Metodo para efetuar um levantamento na conta do cliente
    public void Levantar(){

        System.out.println("Valor a levantar:");
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();

        //Variaveis/Valores para a pesquisa
        Integer ID = new Integer(NumeroDeCliente);
        Integer Valor = new Integer(input);

        //Pesquisa feita para aplicar um predicado para efetuar o levantamento
        Query Levantar =
            new Query(
                "levantamento",
                new Term[] {ID,Valor}
            );


        //Consoante o resultado da pesquisa, imprime o sucesso ou insucesso da operacao
        if(Levantar.hasSolution()){
            System.out.println("Levantamento efetuado com sucesso!\n");
        }
        else
            System.out.println("Não existem fundos suficientes para fazer o levantamento!\n");
    }

    //Metodo para efetuar um deposito na conta do cliente
    public void Deposito(){

        System.out.println("Valor a depositar:");
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();

        //Variaveis/Valores para a pesquisa
        Integer ID = new Integer(NumeroDeCliente);
        Integer Valor = new Integer(input);

        //Pesquisa feita para aplicar um predicado para efetuar o levantamento
        Query Deposito =
            new Query(
                "deposito",
                new Term[] {ID,Valor}
            );

        //Consoante o resultado da pesquisa, imprime o sucesso ou insucesso da operacao
        if(Deposito.hasSolution()){
            System.out.println("Deposito efetuado com sucesso!");
        }
        else
            System.out.println("Erro no Deposito. Tente novamente!");
    }

    //Metodo para verificar elegibilidade a credito do cliente
    public void VerificarElegivelCredito(){

        //Variaveis para pesquisa
        Integer ID = new Integer(NumeroDeCliente);

        System.out.println("A verificar se o cliente e elegivel para credito...\n");

        //Pesquisa feita para aplicar um predicado para verificar a elegibilidade de um cliente
        Query ClienteElegivel = 
            new Query( 
                "getElegivel", 
                new Term[] {ID} 
            );

        //Consoante o resultado da pesquisa, imprime o sucesso ou insucesso da operacao
        if(ClienteElegivel.hasSolution()){
            System.out.println("O cliente e elegivel para credito!\n");
            EligibilidadeCredito = true;
        }
        else{
            System.out.println("O cliente nao e elegivel para credito!\n");
            EligibilidadeCredito = false;
        }
        
    }

    public void ConcederCredito(){

        //Utiliza a elegibilidade a credito para saber se concede ou nao credito,
        //uma vez que ja foi utilizado o Prolog para a obter
        if(!EligibilidadeCredito){//Se o cliente nao for elegivel para credito
            System.out.println("Erro. O Cliente nao e elegivel a credito!\n");//Devolve se mensagem de erro
            return;//e retorna da função
        }        

        System.out.println("Valor a creditar:");
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();

        //Variaveis para pesquisa
        Integer ID = new Integer(NumeroDeCliente);
        Integer Valor = new Integer(input);

        //Pesquisa feita para aplicar um predicado para efetuar um credito no cliente
        Query Creditar = 
            new Query( 
                "concedercredito", 
                new Term[] {ID,Valor} 
            );

        //Consoante o resultado da pesquisa, imprime o sucesso ou insucesso da operacao
        if(Creditar.hasSolution()){
            System.out.println("Credito efetuado com sucesso!\n");
        }
        else{    
            System.out.println("Erro ao efetuar credito. Tente mais tarde!\n");
        }
    }

}
