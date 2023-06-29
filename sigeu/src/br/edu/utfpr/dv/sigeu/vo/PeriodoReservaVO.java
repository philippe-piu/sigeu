package br.edu.utfpr.dv.sigeu.vo;

import java.io.Serializable;
import java.util.Date;

public class PeriodoReservaVO implements Serializable {
	private static final long serialVersionUID = -6545997839475340725L;

	private Integer idReserva;
	private Date data;
	private String diaDaSemana;
	private String nomeItemReserva;
	private String[] rotulo;
	private String[] cor;
	private String[] horario;
	private String[] motivo;

	public PeriodoReservaVO() {
		rotulo = new String[17];
		cor = new String[17];
		horario = new String[17];
		motivo = new String[17];
	}

	// Métodos Set para rotulo, cor, horario e motivo
	public void setRotulo(int n, String rotulo) {
		validateIndex(n);
		this.rotulo[n - 1] = rotulo;
	}

	public void setCor(int n, String cor) {
		validateIndex(n);
		this.cor[n - 1] = cor;
	}

	public void setHorario(int n, String horario) {
		validateIndex(n);
		this.horario[n - 1] = horario;
	}

	public void setMotivo(int n, String motivo) {
		validateIndex(n);
		this.motivo[n - 1] = motivo;
	}

	// Métodos Get para rotulo, cor, horario e motivo
	public String getRotulo(int n) {
		validateIndex(n);
		return this.rotulo[n - 1];
	}

	public String getCor(int n) {
		 validateIndex(n);
		 return this.cor[n - 1];
	}

	public String getHorario(int n) {
		validateIndex(n);
		return this.horario[n - 1];
	}

	public String getMotivo(int n) {
		validateIndex(n);
		return this.motivo[n - 1];
	}

	// Métodos Get para os demais atributos
	public Integer getIdReserva() {
		return idReserva;
	}

	public Date getData() {
		return data;
	}

	public String getDiaDaSemana() {
		return diaDaSemana;
	}

	public String getNomeItemReserva() {
		return nomeItemReserva;
	}

	// Métodos Set para os demais atributos
	public void setIdReserva(Integer idReserva) {
		this.idReserva = idReserva;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setDiaDaSemana(String diaDaSemana) {
		this.diaDaSemana = diaDaSemana;
	}

	public void setNomeItemReserva(String nomeItemReserva) {
		this.nomeItemReserva = nomeItemReserva;
	}

	// Método auxiliar para validar o índice de 1 a 17
	private void validateIndex(int n) {
		if (n < 1 || n > 17) {
			throw new IllegalArgumentException("O índice deve estar entre 1 e 17.");
		}
	}
}