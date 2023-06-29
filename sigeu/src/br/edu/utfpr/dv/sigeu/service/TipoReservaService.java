package br.edu.utfpr.dv.sigeu.service;

import java.util.List;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.TipoReservaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.TipoReserva;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class TipoReservaService {
	public static Transaction trans = new Transaction();
	public static TipoReservaDAO dao = new TipoReservaDAO(trans);
	/**
	 * Cria novo tipo
	 * 
	 * @param tipo
	 */
	public static void criar(TipoReserva tipo) throws Exception {
		trans = null;
		try {
			trans.begin();
			dao.criar(tipo);
			trans.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			trans.close();
		}
	}

	/**
	 * Altera um tipo existente
	 * 
	 * @param obj
	 */
	public static void alterar(TipoReserva obj) throws Exception {
		
		try {
			trans.begin();
			dao.alterar(obj);
			trans.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			trans.close();
		}
	}

	/**
	 * Verifica se já existe objeto. Se não existe, cria. Se existe, atualiza.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public static void persistir(TipoReserva obj) throws Exception {
		if (obj.getIdTipoReserva() != null) {
			TipoReservaService.alterar(obj);
		} else {
			TipoReservaService.criar(obj);
		}
	}

	/**
	 * Realiza a pesquisa no banco de dados conforme o texto
	 * 
	 * @param textoPesquisa
	 * @param ativo
	 *            Informar null para trazer todos os objetos ativos/inativos
	 * @return
	 * @throws Exception
	 */
	public static List<TipoReserva> pesquisar(Campus campus, String textoPesquisa, Boolean ativo) throws Exception {
		List<TipoReserva> lista = null;

		try {
			trans.begin();

			lista = dao.pesquisa(campus, textoPesquisa, ativo, 0);

			if (lista != null) {
				for (TipoReserva c : lista) {
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
	public static TipoReserva encontrePorId(Integer editarId) throws Exception {
		try {
			trans.begin();
			TipoReserva obj = dao.encontrePorId(editarId);

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
	 * Remove um tipo
	 * 
	 * @param categoria
	 * @throws Exception
	 */
	public static void remover(TipoReserva categoria) throws Exception {
		try {
			trans.begin();
			TipoReserva tipo = dao.encontrePorId(categoria.getIdTipoReserva());

			if (tipo != null) {
				Hibernate.initialize(tipo.getReservaList());
			}

			if (tipo.getReservaList().size() > 0) {
				throw new EntidadePossuiRelacionamentoException(tipo.getDescricao());
			}

			dao.remover(tipo);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

}
