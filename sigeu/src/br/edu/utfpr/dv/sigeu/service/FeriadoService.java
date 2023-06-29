package br.edu.utfpr.dv.sigeu.service;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.FeriadoDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Feriado;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class FeriadoService {
	public static Transaction trans = new Transaction();
	public static FeriadoDAO dao = new FeriadoDAO(trans);
	/**
	 * Cria nova
	 * 
	 * @param cat
	 */
	public static void criar(Feriado cat) {
		trans.begin();

		dao.criar(cat);

		trans.commit();
		trans.close();
	}

	/**
	 * Altera uma existente
	 * 
	 * @param cat
	 */
	public static void alterar(Feriado cat) {
		trans.begin();

		dao.alterar(cat);

		trans.commit();
		trans.close();
	}

	/**
	 * Verifica se já existe objeto. Se não existe, cria. Se existe, atualiza.
	 * 
	 * @param item
	 * @throws Exception
	 */
	public static void persistir(Feriado item) throws Exception {

		try {
			trans.begin();

			if (item.getIdFeriado() != null) {
				dao.alterar(item);
			} else {
				dao.criar(item);
			}

			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	/**
	 * Realiza a pesquisa no banco de dados conforme o texto
	 * 
	 * @param textoPesquisa
	 * @return
	 * @throws Exception
	 */
	public static List<Feriado> pesquisar(Campus campus, String textoPesquisa) throws Exception {
		List<Feriado> lista = null;

		try {
			trans.begin();

			if (textoPesquisa == null || textoPesquisa.trim().length() <= 0) {
				lista = dao.pesquisa(campus, 0);
			} else {
				lista = dao.pesquisa(campus, textoPesquisa, 0);
			}

			if (lista != null) {
				for (Feriado c : lista) {
					Hibernate.initialize(c.getIdCampus());
					Hibernate.initialize(c.getIdCampus().getIdInstituicao());
				}
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
	 * 
	 * @param editarId
	 * @return
	 * @throws Exception
	 */
	public static Feriado encontrePorId(Integer editarId) throws Exception {

		try {
			trans.begin();
			Feriado obj = dao.encontrePorId(editarId);
			if (obj != null) {
				Hibernate.initialize(obj.getIdCampus());
				Hibernate.initialize(obj.getIdCampus().getIdInstituicao());
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	/**
	 * Remove uma
	 * 
	 * @param
	 * @throws Exception
	 */
	public static void remover(Feriado item) throws Exception {

		try {
			trans.begin();

			Feriado existente = dao.encontrePorId(item.getIdFeriado());

			dao.remover(existente);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	public static List<Feriado> pesquisarPorData(Campus campus, Date dataInicial, Date dataFinal) throws Exception {
		List<Feriado> lista = null;

		try {
			trans.begin();

			lista = dao.pesquisa(campus, dataInicial, dataFinal);

			if (lista != null) {
				for (Feriado c : lista) {
					Hibernate.initialize(c.getIdCampus());
					Hibernate.initialize(c.getIdCampus().getIdInstituicao());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			trans.close();
		}

		return lista;
	}

	/**
	 * Confere se a data passada como parâmetro é um feriado.
	 * 
	 * @param data
	 *            Data a ser conferida
	 * @return Retorna true se ao menos um feriado for encontrado na data.
	 */
	public static boolean verificaFeriado(Campus campus, Date data) {
		List<Feriado> lista = null;

		try {
			trans.begin();

			lista = dao.pesquisa(campus, data);

			return (lista != null && lista.size() > 0);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			trans.close();
		}
	}

}
