package grafos;

import java.util.ArrayList;

public class Vertice<TIPO> {
	private TIPO dado;
	private ArrayList<Aresta<TIPO>> arestasEntrada;
	private ArrayList<Aresta<TIPO>> arestasSaida;
	private int menorCusto;
	
	
	public int getMenorCusto() {
		return menorCusto;
	}

	public void setMenorCusto(int menorCusto) {
		this.menorCusto = menorCusto;
	}

	public Vertice(TIPO dado) {
		this.dado = dado;
		this.arestasEntrada = new ArrayList<Aresta<TIPO>>();
		this.arestasSaida = new ArrayList<Aresta<TIPO>>();
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
}
