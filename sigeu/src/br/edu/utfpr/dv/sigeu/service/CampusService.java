package br.edu.utfpr.dv.sigeu.service;

import java.util.List;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.CampusDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Instituicao;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class CampusService {
	//Criação de objeto
	private static Transaction trans = new Transaction();
	private static CampusDAO dao = new CampusDAO(trans);
	/**
	 * Conta a qtde de campus
	 */
	public static Integer contarCampus() {
		trans = null;
		try {

			trans = new Transaction();
			trans.begin();

			int q = dao.contarCampus();
			return q;
		} catch (Exception e) {

		} finally {
			if (trans != null) {
				trans.close();
			}
		}
		return null;
	}

	/**
	 * Cria novo
	 * 
	 * @param i
	 */
	public static void criar(Campus i) {
		trans.begin();

		dao.criar(i);

		trans.commit();
		trans.close();
	}

	/**
	 * Altera existente
	 * 
	 * @param i
	 */
	public static void alterar(Campus i) {
		trans.begin();

		dao.alterar(i);

		trans.commit();
		trans.close();
	}

	/**
	 * Realiza a pesquisa no banco de dados conforme o texto
	 * 
	 * @param textoPesquisa
	 * @return
	 * @throws Exception
	 */
	public static List<Campus> pesquisar(String textoPesquisa) throws Exception {
		List<Campus> lista = null;

		try {
			trans.begin();

			lista = dao.pesquisa(textoPesquisa, 0);

			for (Campus c : lista) {
				Hibernate.initialize(c.getIdInstituicao());
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
	 * Realiza a pesquisa no banco de dados conforme o texto
	 * 
	 * @param textoPesquisa
	 * @return
	 * @throws Exception
	 */
	public static List<Campus> pesquisar(String textoPesquisa, Instituicao instituicao) throws Exception {
		List<Campus> lista = null;

		try {
			trans.begin();

			if (textoPesquisa == null || textoPesquisa.trim().length() <= 0) {
				lista = dao.pesquisa(instituicao, HibernateDAO.PESQUISA_LIMITE);
			} else {
				lista = dao.pesquisa(textoPesquisa, instituicao, 0);
			}

			for (Campus c : lista) {
				Hibernate.initialize(c.getIdInstituicao());
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
	 * Encontra por ID
	 * 
	 * @param editarId
	 * @return
	 * @throws Exception
	 */
	public static Campus encontrePorId(Integer editarId) throws Exception {

		try {
			trans.begin();

			CampusDAO dao = new CampusDAO(trans);
			Campus obj = dao.encontrePorId(editarId);
			Hibernate.initialize(obj.getIdInstituicao());

			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	/**
	 * Remove
	 * 
	 * @param i
	 * @throws MessagingException
	 * @throws Exception
	 */
	public static void remover(Campus i) throws EntidadePossuiRelacionamentoException, Exception {

		try {
			trans.begin();

			Campus campusBd = dao.encontrePorId(i.getIdCampus());

			Hibernate.initialize(campusBd.getGrupoPessoaList());
			Hibernate.initialize(campusBd.getPessoaList());

			if (campusBd.getGrupoPessoaList().size() > 0 || campusBd.getPessoaList().size() > 0) {
				throw new EntidadePossuiRelacionamentoException(campusBd.getNome());
			}

			dao.remover(campusBd);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	public static Campus encontrePorEmail(String email) throws Exception {

		try {
			trans.begin();

			Campus obj = dao.encontrePorEmail(email);

			if (obj != null) {
				Hibernate.initialize(obj.getIdInstituicao());
				Hibernate.initialize(obj.getIdCampus());
				Hibernate.initialize(obj.getLdapServerList());
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	public static Campus encontrePorSigla(String sigla) throws Exception {

		try {
			trans.begin();

			Campus obj = dao.encontrePorSigla(sigla);

			if (obj != null) {
				Hibernate.initialize(obj.getIdInstituicao());
				Hibernate.initialize(obj.getIdCampus());
				Hibernate.initialize(obj.getLdapServerList());
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

}
