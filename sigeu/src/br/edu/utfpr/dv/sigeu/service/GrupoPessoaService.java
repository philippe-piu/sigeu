package br.edu.utfpr.dv.sigeu.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.GrupoPessoaDAO;
import br.edu.utfpr.dv.sigeu.dao.PessoaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.GrupoPessoa;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class GrupoPessoaService {
	//Criação de objeto
	private static Transaction trans = new Transaction();
	private static GrupoPessoaDAO dao = new GrupoPessoaDAO(trans);

	/**
	 * Cria um novo grupo
	 * 
	 * @param gp
	 */
	public static void criar(GrupoPessoa gp) {
		trans.begin();

		dao.criar(gp);

		trans.commit();
		trans.close();
	}

	public static void alterar(GrupoPessoa gp) {
		trans.begin();

		dao.alterar(gp);

		trans.commit();
		trans.close();
	}

	/**
	 * Exclui os grupos que não estão mais relacionados e inclui os grupos que
	 * faltam relacionamento
	 * 
	 * @param trans
	 *            Transação de controle. Este método foi projetado para ser
	 *            utilizado em um loop com várias atualizações de grupos, e não
	 *            somente uma. Portanto o controle transacional não deve ficar
	 *            interno a este método.
	 * @param pessoa
	 *            Objeto pessoa do banco de dados
	 * @param grupos
	 *            Lista de grupos do LDAP
	 */
	public static void atualizaGrupos(Transaction trans, Pessoa pessoa, List<GrupoPessoa> grupos) throws Exception {

		PessoaDAO pessoaDAO = new PessoaDAO(trans);
		GrupoPessoaDAO grupoPessoaDAO = new GrupoPessoaDAO(trans);

		List<GrupoPessoa> gruposCadastrados = pessoa.getGrupoPessoaList();

		boolean modificado = false;

		if (gruposCadastrados == null || gruposCadastrados.size() == 0) {
			modificado = true;
		}

		if (!modificado) {
			// Verifica se algum grupo foi removido
			for (GrupoPessoa gp : grupos) {
				boolean eliminado = true;
				boolean naoRelacionado = true;

				for (GrupoPessoa grupoCadastrado : gruposCadastrados) {
			
					if (grupoCadastrado.getIdGrupoPessoa() == gp.getIdGrupoPessoa()) {
						eliminado = false;
						break;
					}
				}

				if (eliminado) {
					modificado = true;
					break;
				}

				for (GrupoPessoa grupoCadastrado : gruposCadastrados) {
				
					if (gp.getIdGrupoPessoa() == grupoCadastrado.getIdGrupoPessoa()) {
						naoRelacionado = false;
						break;
					}
				}

				if (naoRelacionado) {
					modificado = true;
					break;
				}
			}
		}

		if (modificado) {
			// Elimina todos os grupos
			if (gruposCadastrados != null && gruposCadastrados.size() > 0) {
				for (int i = pessoa.getGrupoPessoaList().size() - 1; i >= 0; i--) {
					GrupoPessoa grupo = pessoa.getGrupoPessoaList().get(i);
					grupo.getPessoaList().remove(pessoa);
					grupoPessoaDAO.alterar(grupo);
				}
			}

			pessoa.setGrupoPessoaList(null);
			pessoaDAO.alterar(pessoa);


			// Inclui novamente os grupos buscando do banco de dados
			gruposCadastrados = new ArrayList<GrupoPessoa>();

			for (int i = 0; i < grupos.size(); i++) {
				Integer id = grupos.get(i).getIdGrupoPessoa();
				GrupoPessoa grupo = grupoPessoaDAO.encontrePorId(id);
				List<Pessoa> pessoaList = grupo.getPessoaList();

				if (pessoaList == null) {
					pessoaList = new ArrayList<Pessoa>();
				}
				pessoaList.add(pessoa);

				grupo.setPessoaList(pessoaList);

				grupoPessoaDAO.alterar(grupo);
				gruposCadastrados.add(grupo);
			}

			// Readiciona todos os grupos
			pessoa.setGrupoPessoaList(gruposCadastrados);

			pessoaDAO.alterar(pessoa);
		}

	}

	/**
	 * Retorna um grupo de pessoa por sua descricao ou null
	 * 
	 * @param descricao
	 * @return
	 * @throws SQLException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static GrupoPessoa encontrePorDescricao(Campus campus, String descricao)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
		trans.begin();

		GrupoPessoa gp = dao.encontrePorDescricao(campus, descricao);

		if (gp != null) {
			Hibernate.initialize(gp.getIdCampus());
			Hibernate.initialize(gp.getIdCampus().getIdInstituicao());
		}
		// trans.commit();
		trans.close();

		return gp;
	}

	/**
	 * Retorna um grupo de pessoa por seu ID
	 * 
	 * @return
	 */
	public static GrupoPessoa encontrePorId(Integer id) {
		trans.begin();
		
		GrupoPessoa gp = dao.encontrePorId(id);
		Hibernate.initialize(gp.getIdCampus());
		Hibernate.initialize(gp.getIdCampus().getIdInstituicao());

		// trans.commit();
		trans.close();

		return gp;
	}
}
