package grafos;

import java.util.ArrayList;

public class Vertice<TIPO> implements Comparable<Vertice<TIPO>>{
	private TIPO dado;
	private ArrayList<Aresta<TIPO>> arestasEntrada;
	private ArrayList<Aresta<TIPO>> arestasSaida;
	private int menorCusto;
	private int distancia;
	private boolean visitado = false;
	private Vertice<TIPO> pai;
	
	public Vertice(TIPO dado) {
		this.dado = dado;
		this.arestasEntrada = new ArrayList<Aresta<TIPO>>();
		this.arestasSaida = new ArrayList<Aresta<TIPO>>();
	}
	
	public Vertice() {
		
	}
	
	public int getMenorCusto() {
		return menorCusto;
	}

	public void setMenorCusto(int menorCusto) {
		this.menorCusto = menorCusto;
	}
	

	public int getDistancia() {
		return distancia;
	}

	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}
	

	public boolean isVisitado() {
		return visitado;
	}

	public void setVisitado(boolean visitado) {
		this.visitado = visitado;
	}
	

	public Vertice<TIPO> getPai() {
		return pai;
	}

	public void setPai(Vertice<TIPO> pai) {
		this.pai = pai;
	}

	public TIPO getDado() {
		return dado;
	}
	
	public String getDadosString(){
		TIPO dadoTipo = dado;
		return dadoTipo.toString();
	}

	public void setDado(TIPO dado) {
		this.dado = dado;
	}
	
	public void adicionarArestaEntrada(Aresta<TIPO> aresta) {
		this.arestasEntrada.add(aresta);
	}
	
	public void adicionarArestaSaida(Aresta<TIPO> aresta) {
		this.arestasSaida.add(aresta);
	}

	public ArrayList<Aresta<TIPO>> getArestasEntrada() {
		return arestasEntrada;
	}

	public void setArestasEntrada(ArrayList<Aresta<TIPO>> arestasEntrada) {
		this.arestasEntrada = arestasEntrada;
	}

	public ArrayList<Aresta<TIPO>> getArestasSaida() {
		return arestasSaida;
	}

	public void setArestasSaida(ArrayList<Aresta<TIPO>> arestasSaida) {
		this.arestasSaida = arestasSaida;
	}

	@Override
	public int compareTo(Vertice<TIPO> v) {
		if (this.distancia > v.getDistancia()) { 
			return -1; 
		} if (this.distancia < v.getDistancia()) { 
			return 1; 
		} 
			return 0; 
	}
}
