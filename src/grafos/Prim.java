package grafos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Prim<TIPO> {
	private Set<Aresta<TIPO>>  arestasVisitadas = new HashSet<Aresta<TIPO>>();
	private Set<Aresta<TIPO>>  arestasSelecionadas = new HashSet<Aresta<TIPO>>();
	
	public Set<Aresta<TIPO>> prim(Grafo grafo, String verticeOrigem) {
		
		Vertice<TIPO> origem = grafo.getVerticeString(verticeOrigem);
		//Vertice origem = grafo.selecionarAleatorio();
		Aresta aresta = selecionarArestaPrimeiraVez(grafo, origem);

		while (aresta != null) {
			arestasSelecionadas.add(aresta);
			origem = aresta.getFim();
			aresta = selecionarAresta(grafo, origem);
		}
		return arestasSelecionadas;
	}
	
	private Aresta selecionarArestaPrimeiraVez(Grafo grafo, Vertice origem) {
		List<Aresta<TIPO>> arestasVertice = new ArrayList<>();
		arestasVertice.addAll(grafo.getAdjacentes(origem));
		Aresta aresta = buscaMenorCustoPrimeiraVez(arestasVertice);
		return aresta;
	}
	

	private Aresta selecionarAresta(Grafo grafo, Vertice origem) {
		List<Aresta<TIPO>> arestasVertice = new ArrayList<>();
		arestasVertice.addAll(grafo.getAdjacentes(origem));
		Aresta aresta = buscaMenorCusto(arestasVertice);
		return aresta;
	}
	
	private Aresta buscaMenorCustoPrimeiraVez(List<Aresta<TIPO>> arestas) {
		int peso = Integer.MAX_VALUE;
		Aresta selecionada = null;
		for (Aresta aresta : arestas) {
			Integer pesoArestaAtual = aresta.getPeso();
			if(pesoArestaAtual < peso) {
				peso = pesoArestaAtual;
				selecionada = aresta;
			}
			arestasVisitadasAdd(aresta);
		}
		return selecionada;
	}

	private Aresta buscaMenorCusto(List<Aresta<TIPO>> arestas) {
		int peso = Integer.MAX_VALUE;
		Aresta selecionada = null;
		for (Aresta aresta : arestas) {
			Integer pesoArestaAtual = aresta.getPeso();
			if(pesoArestaAtual < peso && !isVisitado(aresta)) {
				peso = pesoArestaAtual;
				selecionada = aresta;
			}
			arestasVisitadasAdd(aresta);
		}
		return selecionada;
	}

	private boolean isVisitado(Aresta<TIPO> aresta) {
		boolean visitada = false;
		
		for (Aresta<TIPO> a : arestasVisitadas) {
			if(a.getInicio().getDado() == aresta.getFim().getDado()) {
				visitada = true;
				break;
			}
		}
		
		return visitada;
	}
	
	private void arestasVisitadasAdd(Aresta<TIPO> aresta) {
		arestasVisitadas.add(aresta);
	}
	
}
