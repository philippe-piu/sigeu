package br.edu.utfpr.dv.sigeu.service;

import java.util.List;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.CategoriaItemReservaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.CategoriaItemReserva;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class CategoriaItemReservaService {

	//Criação de objeto
	private static Transaction trans = new Transaction();
	private static CategoriaItemReservaDAO dao = new CategoriaItemReservaDAO(trans);
	/**
	 * Cria nova categoria
	 * 
	 * @param cat
	 */
	public static void criar(CategoriaItemReserva cat) {
		trans.begin();

		dao.criar(cat);

		trans.commit();
		trans.close();
	}

	/**
	 * Altera uma categoria existente
	 * 
	 * @param cat
	 */
	public static void alterar(CategoriaItemReserva cat) {
		trans.begin();

		dao.alterar(cat);

		trans.commit();
		trans.close();
	}

	/**
	 * Verifica se já existe objeto. Se não existe, cria. Se existe, atualiza.
	 * 
	 * @param cat
	 * @throws Exception
	 */
	public static void persistir(CategoriaItemReserva cat) throws Exception {

		try {
			trans.begin();

			if (cat.getIdCategoria() != null) {
				dao.alterar(cat);
			} else {
				dao.criar(cat);
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
	 * @param ativo
	 *            Informar null para trazer todos os objetos ativos/inativos
	 * @return
	 * @throws Exception
	 */
	public static List<CategoriaItemReserva> pesquisar(Campus campus, String textoPesquisa, Boolean ativo) throws Exception {
		List<CategoriaItemReserva> lista = null;

		try {
			trans.begin();

			lista = dao.pesquisa(campus, textoPesquisa, ativo, 0);

			if (lista != null) {
				for (CategoriaItemReserva c : lista) {
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
	public static CategoriaItemReserva encontrePorId(Integer editarId) throws Exception {

		try {
			trans.begin();
			CategoriaItemReserva obj = dao.encontrePorId(editarId);
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
	 * Remove uma categoria
	 * 
	 * @param categoria
	 * @throws Exception
	 */
	public static void remover(CategoriaItemReserva categoria) throws Exception {

		try {
			trans.begin();
			
			CategoriaItemReserva cat = dao.encontrePorId(categoria.getIdCategoria());

			if (cat != null) {
				Hibernate.initialize(cat.getItemReservaList());
			}

			if (cat.getItemReservaList().size() > 0) {
				throw new EntidadePossuiRelacionamentoException(cat.getNome());
			}

			dao.remover(cat);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

}
