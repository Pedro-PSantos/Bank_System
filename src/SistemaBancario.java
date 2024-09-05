//import java.util.Iterator;
import java.util.*;
import org.jpl7.*;
import java.lang.Integer;


public class SistemaBancario {
    Vector<Cliente> ListaClientes;
    Iterator<Cliente> it;

    //Construtor da Classe SistemaBancario, obtendo a lista de todos os clientes
    public SistemaBancario(){

        ListaClientes = new Vector<Cliente>();

        //Cria variaveis para usar nas pesquisas
        Variable ID = new Variable("ID");
        Variable Nome = new Variable("Nome");
        Variable Posto = new Variable("Posto");
        Variable Cidade = new Variable("Cidade");
        Variable Data = new Variable("Data");

        Query InfoCliente = //pesquisa de informacao dos clientes
        new Query( 
            "getInformacoes", 
            new Term[] {ID,Nome,Posto,Cidade,Data} 
        );

        java.util.Map<String,Term>[] solutions;

        if(!InfoCliente.hasSolution()){ // se nao existe solucao, nao consegui ler com sucesso e fecha o programa
            System.out.println("Erro a ler dados. Tente novamente mais tarde!");
            System.exit(0);
        }    
            solutions=InfoCliente.allSolutions();
        
        for ( int i=0; i < solutions.length ; i++ ) { // enquanto houver soluccao (i < sol.lenght) adiciona as informacoes
                                                      // a lista de clientes
            
            Cliente aux = new Cliente();//Variavel de Cliente auxiliar para adicionar a lista de clientes

            aux.NumeroDeCliente = Integer.parseInt(solutions[i].get("ID").toString());
            aux.Nome = solutions[i].get("Nome").name();
            aux.Cidade = solutions[i].get("Cidade").name();
            aux.Agencia = solutions[i].get("Posto").name();
            aux.DataAbertura = solutions[i].get("Data").name();

            ListaClientes.add(aux);//Adiciona cliente a lista de clientes
        }
    }

    //Implementacao do Menu principal
    public void Menu(){

    Scanner sc = new Scanner(System.in);//Abre um scanner para receber inputs    

        
        while(true){//loop para Menu. Só sai quando o programa acaba

            System.out.println("Escolha uma opcao, pressionando o numero em frente:"
            + "\nMostrar Lista de Clientes - 1"
            + "\nMostrar Clientes de uma Cidade - 2"
            + "\nMostrar Clientes elegiveis para credito - 3"
            + "\nMostrar Cliente por Num. de Cliente - 4"
            + "\nSair - 0");

            int opcao = sc.nextInt();

            switch (opcao) {//Usa a funcao segundo a escolha do utilizador
                case 0:
                    sc.close();
                    System.exit(0);
                case 1:
                    Imprimir();
                    break;
                case 2:
                    getClienteCidade();
                    break;
                case 3:
                    getClienteElegivelCredito();
                    break;
                case 4:
                    AcederCliente();
                    break;
                default:
                    System.out.println("Opcao invalida.\n");
                    break;
            }
        }
    }

    //Imprimir lista de clientes
    public void Imprimir(){

        System.out.println("A mostrar Lista de Clientes:\n");

        for(int i = 0; i < ListaClientes.size(); i++)//Ciclo para imprimir todos os clientes
        {
            System.out.println("-----------------");
            System.out.println("Numero de Cliente:" + ListaClientes.get(i).NumeroDeCliente
            + "\nNome:"+ ListaClientes.get(i).Nome 
            + "\nCidade:"+ListaClientes.get(i).Cidade 
            + "\nAgencia:"+ListaClientes.get(i).Agencia
            + "\nData:" + ListaClientes.get(i).DataAbertura);
            System.out.println("-----------------\n");         
        }
    }

    //Metodo para obter clientes de uma cidade
    public void getClienteCidade(){

        //Variaveis para pesquisa
        Variable ID = new Variable("ID");
        Variable Nome = new Variable("Nome");

        System.out.println("Inserir nome da cidade:");
        Scanner sc = new Scanner(System.in);//Abre um scanner para receber inputs 
        String input = sc.nextLine().toLowerCase();//recebe a cidade em letras minusculas para igualar a base de dados

        //usa-se um tokenizer para igualar o nome da cidade ao formato da base de dados
        String Cidade = new String();
        StringTokenizer st = new StringTokenizer(input," ");
        
        while(st.hasMoreTokens())//ciclo para formatar nome da cidade
        {
            Cidade+=st.nextToken();
            Cidade+="_";
        }

        Cidade=Cidade.substring(0,Cidade.length()-1);//retira-se o ultimo "_" adicionado, uma vez que esta a mais

        System.out.println("\nOs clientes de " + input + " sao:\n");

        //Pesquisa dos clientes da cidade com nome guardado na var. Cidade
        Query ClientesCidade = 
        new Query( 
            "getClientesCidade", 
            new Term[] {ID,Nome,new Atom(Cidade)} 
        );

        if(!ClientesCidade.hasSolution()){
            System.out.println("Nao existem clientes em " + input + "!\n");
            return;
        }

        java.util.Map<String,Term>[] solutions = ClientesCidade.allSolutions();//solucoes
        
        for(int i = 0; i < solutions.length; i++){//Enquanto houver solucoes, imprime os dados do cliente da Cidade
            System.out.println("-----------------");
            System.out.println("Numero de Cliente:"+ solutions[i].get("ID")
            + "\nNome:" + solutions[i].get("Nome").name());
            System.out.println("-----------------\n"); 
        }
    }
    
    public void getClienteElegivelCredito(){//Metodo para pesquisa de Clientes elegiveis a credito

        //Variaveis para pesquisa
        Variable ID = new Variable("ID");
        Variable Nome = new Variable("Nome");
        Variable Posto = new Variable("Posto");
        Variable Cidade = new Variable("Cidade");
        Variable Data = new Variable("Data");

        //Pesquisa dos clientes elegiveis a credito
        Query ClientesElegiveis = 
            new Query( 
                "getElegiveis", 
                new Term[] {ID,Nome,Posto,Cidade,Data} 
            );

        if(!ClientesElegiveis.hasSolution()){//Informa caso o cliente nao seja elegivel para credito
            System.out.println("Nao existem clientes elegiveis para credito!\n");
            return;
        }

        java.util.Map<String,Term>[] solutions = ClientesElegiveis.allSolutions();

        System.out.println("A mostrar os clientes elegiveis para credito:\n");

        for ( int i = 0; i < solutions.length ; i++ ) {
            
            System.out.println("-----------------"); 
            System.out.println("Numero de Cliente:" + solutions[i].get("ID")
            + "\nNome:" + solutions[i].get("Nome").name()
            + "\nPosto:" + solutions[i].get("Posto").name()
            + "\nCidade:" + solutions[i].get("Cidade").name()
            + "\nData:" + solutions[i].get("Data").name());
            System.out.println("-----------------\n"); 
            
        }
    }

    public void AcederCliente(){//metodo para aceder a um determinado cliente, segundo o seu ID

        int ID;
        Cliente aux = new Cliente();

        System.out.println("Insira o Numero de Cliente:");
        Scanner sc = new Scanner(System.in);
        ID = sc.nextInt();

        for(int i = 0; i < ListaClientes.size(); i++)
        {
            aux = ListaClientes.get(i);

            if(ID == aux.NumeroDeCliente)//Após confirmar que o cliente existe
                break;
        }

        aux.MenuCliente();//Abrir o menu de cliente para o cliente inserido
    }

    
}
