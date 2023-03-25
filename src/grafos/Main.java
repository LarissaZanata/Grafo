package grafos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class Main {
	public static void main(String[] args) {
		List<String> linhasArquivo = new ArrayList<>();
		
		/*
		 * Adiciona linhas do arquivo na lista 'linhasArquivo'
		 * */
		try{
			BufferedReader br = new BufferedReader(new FileReader("arquivo.txt"));
	        String linha;
	        while ((linha = br.readLine()) != null) {
	           linhasArquivo.add(linha);
	        }
	    } catch(Exception e) {
	    	System.out.println("Arquivo não encontrado ou com erro: " + e.getMessage());
	    }
		
		String tipoDoGrafo = "";
		int qtdLinhas = 0;
		if(!linhasArquivo.isEmpty()) {
			tipoDoGrafo = linhasArquivo.get(0);
			qtdLinhas = linhasArquivo.size() - 1;
		}
		
		String[][] matriz = new String[qtdLinhas][3];
		String[] arrayAux = new String[3];
		
		/*
		 * Preenche matriz com dados vindos do arquivo 
		 * */
		for( int linha = 0; linha < matriz.length; linha++) {
			  arrayAux = linhasArquivo.get(linha+1).split(" ");
		      for( int coluna = 0; coluna < 3 ; coluna++) {
		    	  matriz[linha][coluna] = arrayAux[coluna];
		      }
		}
		
		/*
		 * Adiciona Vertices a partir da matriz  ao Grafo
		 * */
		Grafo<String> grafo = new Grafo<String>();
		Set<String> vertices = new HashSet<String>();
		for( int linha = 0; linha < matriz.length; linha++) {
		      for( int coluna = 0; coluna < 2; coluna++) {
		    	  vertices.add(matriz[linha][coluna]);
		      }
		}
		vertices.stream().forEach(v -> {
			grafo.adicionarVertice(v);
		});
		
		/*
		 * Adiciona Arestas a partir da matriz ao Grafo
		 * */
		//int tamanho =  matriz.length;
		if(tipoDoGrafo.equals("directed")) {
			for( int linha = 0; linha < matriz.length; linha++) {
			    String origem = matriz[linha][0];
			    String destino = matriz[linha][1];
			    int peso = Integer.parseInt(matriz[linha][2]);
			    grafo.adicionarAresta(origem, destino, peso);
			}
		} else {
			for( int linha = 0; linha < matriz.length; linha++) {
			    String origem = matriz[linha][0];
			    String destino = matriz[linha][1];
			    int peso = Integer.parseInt(matriz[linha][2]);
			    grafo.adicionarAresta(origem, destino, peso);
			    grafo.adicionarAresta(destino, origem, peso);
			}
		}
		
		
		/*
		 * Menu de Escolhas
		 * */
		menuDeEscolhas(grafo, vertices);
	}
	
	public static void menuDeEscolhas(Grafo grafo, Set<String> vertices) {
		String opcao;
	    Scanner sc = new Scanner(System.in);
	    System.out.println("Escolha uma opção:");
	    System.out.println("");
	    System.out.println("Inserir aresta ao grafo: 1");
	    System.out.println("");
	    System.out.println("Remover um vértice e suas arestas: 2");
	    System.out.println("");
	    System.out.println("Mostrar grafo: 3");
	    System.out.println("");
	    System.out.println("Mostrar lista de adjacência: 4");
	    System.out.println("");
	    System.out.println("Verificar se aresta pertence ao grafo: 5");
	    System.out.println("");
	    System.out.println("Mostrar vértices adjacentes a um vértice: 6");
	    System.out.println("");
	    System.out.println("Mostrar vértices incidentes a um vértice: 7");
		/*
		 * System.out.println("");
		 * System.out.println("Mostrar grafo complemento: 8*******");
		 * System.out.println("");
		 * System.out.println("Mostrar grafo transposto: 9*********");
		 */
	    System.out.println("");
	    System.out.println("Algoritmo Kruskal: 10");
	    System.out.println("");
	    System.out.println("Busca em Profundidade: 11");
	    System.out.println("");
	    System.out.println("Busca pelo Custo Uniforme: 12");
	    opcao = sc.next();
	    
	    
	    switch (opcao) {
	    case "1":
	    	String verticeOrigem;
		    String verticeDestino;
		    int peso;
		    Scanner sc_origem = new Scanner(System.in);
	   	 	System.out.println("Informe o vétice de origem para a aresta:");
	   	 	verticeOrigem = sc_origem.next();
	   	 	Scanner sc_destino = new Scanner(System.in);
	   	 	System.out.println("Informe o vértice de destino para a aresta:");
	   	 	verticeDestino = sc_destino.next();
	   	 	
	   	 	Scanner sc_peso = new Scanner(System.in);
	   	 	System.out.println("Informe o peso da aresta:");
	   	 	peso = sc_peso.nextInt();
	   	 	
	   	 	if(!vertices.contains(verticeOrigem)) {
		   	 	grafo.adicionarVertice(verticeOrigem);
	   	 	}
	   	 
	   	 	if(!vertices.contains(verticeDestino)) {
	   	 		grafo.adicionarVertice(verticeDestino);
	   	 	}
	   	 	
	    	grafo.adicionarAresta(verticeOrigem, verticeDestino, peso);
	    	System.out.println("Aresta Inserida ao Grafo!");
	    	System.out.println(" ");
	      break;
	    case "2":
		    String verticeRemover;
		    Scanner sc_remover = new Scanner(System.in);
	   	 	System.out.println("Informe o vétice a ser removido:");
	   	 	verticeRemover = sc_remover.next();
	   	 	grafo.removeVerticeESuasArestas(verticeRemover);
	    	System.out.println(" ");
	      break;
	    case "3":
	    	System.out.println("Grafo:");
	    	grafo.mostrarGrafo();
	    	System.out.println(" ");
	      break;
	    case "4":
	    	System.out.println("Matriz de Adjacência:");
	    	grafo.imprimeMatrizAdjacencia(vertices);
	    	System.out.println(" ");
		  break;
	    case "5":
	    	String verticeEntrada;
		    String verticeSaida;
		    Scanner sc_entrada = new Scanner(System.in);
	   	 	System.out.println("Informe o vétice de origem da aresta:");
	   	 	verticeEntrada = sc_entrada.next();
	   	 	Scanner sc_saida = new Scanner(System.in);
	   	 	System.out.println("Informe o vértice de destino da aresta:");
	   	 	verticeSaida = sc_saida.next();
	    	boolean arestaExiste = grafo.verificaArestaPertenceGrafo(verticeEntrada, verticeSaida);
	    	System.out.println("Existencia da aresta no grafo: " + arestaExiste);
	    	System.out.println(" ");
	      break;
	    case "6":
	    	String vertice;
		    Scanner sc_vertice = new Scanner(System.in);
	   	 	System.out.println("Informe o vétice a ser consultados os adjacentes:");
	   	 	vertice = sc_vertice.next();
	   	 	System.out.println("Os vétices adjacentes a '" + vertice + "' são:");
	    	grafo.getVerticesAdjacentes(vertice, vertices);
	    	System.out.println(" ");
	      break;
	    case "7":
	    	String vertice1;
		    Scanner sc_vertice1 = new Scanner(System.in);
	   	 	System.out.println("Informe o vétice a ser consultados os incidentes:");
	   	 	vertice1 = sc_vertice1.next();
	   	 	System.out.println("Os vétices incidentes a '" + vertice1 + "' são:");
	    	grafo.getVerticesIncidentes(vertice1);
	    	System.out.println(" ");
	       break;
	    case "8":
	    	System.out.println("Grafo Complemento:");
	    	grafo.mostrarGrafoComplemento();
	    	System.out.println(" ");
		   break; 
	    case "9":
	    	System.out.println("Matriz de Adjacência Transposta:");
	    	grafo.imprimeMatrizAdjacenciaTransposta(vertices);
	    	System.out.println(" ");
		   break;  
	    case "10":
	    	grafo.algoritmoKruskal(vertices);
	    	break;
	    case "11":
	    	String orig;
		    String dest;
		    Scanner sc_orig = new Scanner(System.in);
	   	 	System.out.println("Informe o vétice de origem da aresta:");
	   	 	orig = sc_orig.next();
	   	 	Scanner sc_dest = new Scanner(System.in);
	   	 	System.out.println("Informe o vértice de destino da aresta:");
	   	 	dest = sc_dest.next();
	   	 	Vertice vOrigem = grafo.getVertice(orig);
	   	  	Vertice vDestino = grafo.getVertice(dest);
	    	System.out.println("Caminho percorrido pela busca em Profundidade:");
	    	grafo.buscaEmProfundidade(vOrigem, vDestino);
	    	break;
	    case "12":
	    	String orig12;
		    String dest12;
		    Scanner sc_orig12 = new Scanner(System.in);
	   	 	System.out.println("Informe o vétice de origem da aresta:");
	   	 	orig12 = sc_orig12.next();
	   	 	Scanner sc_dest12 = new Scanner(System.in);
	   	 	System.out.println("Informe o vértice de destino da aresta:");
	   	 	dest12 = sc_dest12.next();
	   	 	Vertice vOrigem12 = grafo.getVertice(orig12);
	   	  	Vertice vDestino12 = grafo.getVertice(dest12);
	    	System.out.println("Caminho percorrido pela busca pelo caminho minimo:");
	    	grafo.buscaPeloCustoUniforme(vOrigem12, vDestino12);
	    	break;
	    default:
	    	System.out.print("Opção inválida!");
	  }	
	    
	 
	 Scanner sc_continuar = new Scanner(System.in);
   	 System.out.println("Deseja fazer outra operação no mesmo grafo?");
   	 System.out.println("s -> Sim");
   	 System.out.println("n -> Não");
   	 String continuar = sc_continuar.next();
   	 System.out.println(" ");
   	 
   	 if(continuar.equals("s")) {
   		menuDeEscolhas(grafo, vertices);
   	 }
	}

}
