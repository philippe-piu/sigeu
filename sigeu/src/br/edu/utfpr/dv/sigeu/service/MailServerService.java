package br.edu.utfpr.dv.sigeu.service;

import br.edu.utfpr.dv.sigeu.dao.MailServerDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.MailServer;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class MailServerService {
	public static Transaction trans = new Transaction();
	public static MailServerDAO dao = new MailServerDAO(trans);
	/**
	 * Cria nova
	 * 
	 * @param cat
	 */
	public static void criar(MailServer cat) {
		 trans = null;
		try {
			trans.begin();
			dao.criar(cat);
			trans.commit();
		} catch (Exception e) {

		} finally {
			trans.close();
		}
	}

	/**
	 * Altera uma existente
	 * 
	 * @param cat
	 */
	public static void alterar(MailServer cat) {
	
		try {
			trans = new Transaction();
			trans.begin();
			dao.alterar(cat);
			trans.commit();
		} catch (Exception e) {

		} finally {
			trans.close();
		}
	}

	public static void remover(MailServer item) throws Exception {

		try {
			trans.begin();
			dao.remover(item);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	public static MailServer encontrePorCampus(Campus campus) throws Exception {
		try {
			trans.begin();
			MailServer ms = dao.encontrePorCampus(campus);

			return ms;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}
}
