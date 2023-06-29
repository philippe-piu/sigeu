package br.edu.utfpr.dv.sigeu.service;

import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.dv.sigeu.dao.PessoaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.GrupoPessoa;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class PessoaService {
	public static Transaction trans = new Transaction();
	public static PessoaDAO dao = new PessoaDAO(trans);

	/**
	 * Cria nova entrada para uma pessoa
	 * 
	 * @param p
	 */
	public static void criar(Pessoa p) throws Exception {
		trans.begin();
		dao.criar(p);
		trans.commit();
		trans.close();
	}

	/**
	 * Altera uma pessoa existente
	 * 
	 * @param p
	 */
	public static void alterar(Pessoa p) throws Exception {
		trans.begin();
		dao.alterar(p);
		trans.commit();
		trans.close();
	}

	/**
	 * Busca uma pessoa do banco de dados através do seu ID
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static Pessoa encontrePorId(Integer id) throws Exception {
		return PessoaService.encontrePorId(id, false);
	}

	/**
	 * Busca uma pessoa do banco de dados através do seu ID
	 * 
	 * @return
	 */
	public static Pessoa encontrePorId(Integer id, boolean carregaGrupos) throws Exception {
		Pessoa p = null;
		try {
			trans.begin();
			p = dao.encontrePorId(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			trans.close();
		}

		return p;
	}

	/**
	 * Busca uma pessoa do banco de dados através do seu Email e Campus
	 * 
	 * @param email
	 * @param campus
	 * @return
	 * @throws Exception
	 */
	public static Pessoa encontrePorEmail(String email, Campus campus) throws Exception {
		trans.begin();
		Pessoa p;
		try {
			p = dao.encontrePorEmail(email, campus);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			trans.close();
		}

		return p;
	}

	public static Pessoa encontrePorCnpjCpf(Campus campus, String cnpjCpf) throws Exception {
		trans.begin();
		Pessoa p;
		try {
			p = dao.encontrePorCnpjCpf(campus, cnpjCpf);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			trans.close();
		}

		return p;
	}

	/**
	 * Pesquisa todas as pessoas
	 * 
	 * @param textoPesquisa
	 * @return
	 * @throws Exception
	 */
	public static List<Pessoa> pesquisar(Campus campus, String textoPesquisa) throws Exception {
		List<Pessoa> lista = null;

		try {
			trans.begin();

			if (textoPesquisa == null || textoPesquisa.trim().length() <= 0) {
				lista = dao.pesquisa(campus, HibernateDAO.PESQUISA_LIMITE);
			} else {
				lista = dao.pesquisa(campus, textoPesquisa, 0);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}

		return lista;
	}

	/**
	 * Pesquisa as pessoas passando o parametro ativo ou não
	 * 
	 * @param query
	 * @param ativo
	 * @return
	 * @throws Exception
	 */
	public static List<Pessoa> pesquisar(Campus campus, String query, boolean ativo, int limit) throws Exception {
		List<Pessoa> lista = null;

		try {
			trans.begin();

			if (query == null || query.trim().length() <= 0) {
				if (limit == 0) {
					limit = HibernateDAO.PESQUISA_LIMITE;
				}
				lista = dao.pesquisa(campus, ativo, limit);
			} else {
				lista = dao.pesquisa(campus, query, ativo, limit);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}

		return lista;
	}

	/**
	 * Pesquisa as pessoas passando o parametro ativo ou não
	 * 
	 * @param query
	 * @param ativo
	 * @param grupo
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public static List<Pessoa> pesquisar(Campus campus, String query, boolean ativo, String grupo, int limit)
			throws Exception {
		List<Pessoa> lista = null;
		List<Pessoa> listaRetorno = null;

		try {
			trans.begin();

			if (query == null || query.trim().length() <= 0) {
				if (limit == 0) {
					limit = HibernateDAO.PESQUISA_LIMITE;
				}
				lista = dao.pesquisa(campus, ativo, limit);
			} else {
				lista = dao.pesquisa(campus, query, ativo, limit);
			}

			listaRetorno = lista;

			if (grupo != null && grupo.trim().length() > 0 && lista != null && lista.size() > 0) {
				listaRetorno = new ArrayList<Pessoa>();

				grupo = grupo.trim().toUpperCase();

				for (Pessoa p : lista) {

					for (GrupoPessoa gp : p.getGrupoPessoaList()) {
						if (gp.getNome().trim().toUpperCase().equals(grupo)) {
							listaRetorno.add(p);
							break;
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}

		return listaRetorno;
	}
}
