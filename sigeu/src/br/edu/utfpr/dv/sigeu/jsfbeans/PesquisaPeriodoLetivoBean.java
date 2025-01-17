package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.PeriodoLetivo;
import br.edu.utfpr.dv.sigeu.service.PeriodoLetivoService;

@Named
@ViewScoped
public class PesquisaPeriodoLetivoBean extends JavaBean {
	
	@Inject
	private LoginBean loginBean;
	
	private static final long serialVersionUID = -7338998125000395663L;

	private String textoPesquisa;
	private List<PeriodoLetivo> lista;

	@PostConstruct
	public void init() {
		pesquisarPeriodoLetivo(null);
	}

	/**
	 * Realiza a pesquisa de períodos letivos
	 * @param query o texto de pesquisa
	 */
	public void pesquisa() {
		pesquisarPeriodoLetivo(textoPesquisa);
	}

	private void pesquisarPeriodoLetivo(String query) {
		try {
			lista = PeriodoLetivoService.pesquisar(loginBean.getCampus(), query);
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Pesquisa", "Erro na pesquisa");
		}
	}

	public List<PeriodoLetivo> getLista() {
		return lista;
	}

	public String getTextoPesquisa() {
		return textoPesquisa;
	}

	public void setTextoPesquisa(String textoPesquisa) {
		this.textoPesquisa = textoPesquisa;
	}
}