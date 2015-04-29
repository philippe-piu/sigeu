package br.edu.utfpr.dv.sigeu.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.CategoriaItemReserva;
import br.edu.utfpr.dv.sigeu.entities.ItemReserva;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.entities.Reserva;
import br.edu.utfpr.dv.sigeu.entities.TipoReserva;
import br.edu.utfpr.dv.sigeu.entities.Transacao;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class ReservaDAO extends HibernateDAO<Reserva> {

	public ReservaDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public Reserva encontrePorId(Integer id) {
		String hql = "from Reserva o where o.idReserva = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);
		Reserva r = (Reserva) q.uniqueResult();

		if (r != null) {
			Hibernate.initialize(r.getIdItemReserva());
			Hibernate.initialize(r.getIdPessoa());
			Hibernate.initialize(r.getIdTransacao());
			Hibernate.initialize(r.getIdTipoReserva());
			Hibernate.initialize(r.getIdCampus());
			Hibernate.initialize(r.getIdUsuario());
		}
		return r;
	}

	@Override
	public String getNomeSequencia() {
		return "reserva";
	}

	@Override
	public void defineId(Reserva o) {
		Integer id = this.gerarNovoId().intValue();
		o.setIdReserva(id);
	}

	/**
	 * Lista todas as reservas do dia
	 * 
	 * @param campus
	 * @param data
	 * @param categoria
	 * @param item
	 * @return
	 */
	public List<Reserva> pesquisaReserva(Campus campus, Date data, CategoriaItemReserva categoria, ItemReserva item) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT o ");
		hql.append("FROM Reserva o JOIN o.idCampus c JOIN o.idItemReserva i JOIN i.idCategoria c ");
		hql.append("WHERE c.idCampus = :idCampus AND ");

		if (categoria != null) {
			hql.append("c.idCategoria = :idCategoria AND ");
		}

		if (item != null) {
			hql.append("i.idItemReserva = :idItemReserva AND ");
		}
		hql.append("o.data = :data ");
		hql.append("ORDER BY i.nome, o.data ASC, o.horaInicio ASC, o.horaFim ASC ");

		Query q = session.createQuery(hql.toString());
		q.setInteger("idCampus", campus.getIdCampus());
		if (categoria != null) {
			q.setInteger("idCategoria", categoria.getIdCategoria());
		}
		if (item != null) {
			q.setInteger("idItemReserva", item.getIdItemReserva());
		}
		q.setDate("data", data);

		return this.pesquisaObjetos(q, 0);
	}

	public List<Reserva> pesquisaReserva(Campus campus, Pessoa pessoa, Date data, CategoriaItemReserva categoria, ItemReserva item) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT o ");
		hql.append("FROM Reserva o JOIN o.idCampus c JOIN o.idItemReserva i JOIN i.idCategoria c ");
		hql.append("WHERE c.idCampus = :idCampus AND ");
		hql.append("c.idCategoria = :idCategoria AND ");
		hql.append("o.idPessoa.idPessoa = :idPessoa AND ");

		if (item != null) {
			hql.append("i.idItemReserva = :idItemReserva AND ");
		}

		hql.append("o.data = :data ");
		hql.append("ORDER BY i.nome, o.data ASC, o.horaInicio ASC, o.horaFim ASC ");

		Query q = session.createQuery(hql.toString());
		q.setInteger("idCampus", campus.getIdCampus());
		q.setInteger("idCategoria", categoria.getIdCategoria());
		q.setInteger("idPessoa", pessoa.getIdPessoa());

		if (item != null) {
			q.setInteger("idItemReserva", item.getIdItemReserva());
		}

		q.setDate("data", data);

		return this.pesquisaObjetos(q, 0);
	}

	public List<Reserva> pesquisaReserva(Campus campus, Date data, Date horaInicio, Date horaFim, CategoriaItemReserva categoria, ItemReserva item) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT o ");
		hql.append("FROM Reserva o JOIN o.idCampus c JOIN o.idItemReserva i JOIN i.idCategoria c ");
		hql.append("WHERE c.idCampus = :idCampus AND ");
		hql.append("c.idCategoria = :idCategoria AND ");
		hql.append("o.horaInicio >= :horaInicio AND ");
		hql.append("o.horaFim <= :horaFim AND ");

		if (item != null) {
			hql.append("i.idItemReserva = :idItemReserva AND ");
		}

		hql.append("o.data = :data ");
		hql.append("ORDER BY i.nome, o.data ASC, o.horaInicio ASC, o.horaFim ASC ");

		Query q = session.createQuery(hql.toString());
		q.setInteger("idCampus", campus.getIdCampus());
		q.setInteger("idCategoria", categoria.getIdCategoria());
		q.setDate("data", data);
		q.setTime("horaInicio", horaInicio);
		q.setTime("horaFim", horaFim);

		if (item != null) {
			q.setInteger("idItemReserva", item.getIdItemReserva());
		}

		return this.pesquisaObjetos(q, 0);
	}

	/**
	 * Elimina todas as reservas de uma determinada transação
	 * 
	 * @param transacao
	 */
	public void removeByTransacao(Campus campus, Transacao transacao) {
		this.removeByTransacao(campus, transacao.getIdTransacao());
	}

	/**
	 * Elimina todas as reservas de uma determinada transação
	 * 
	 * @param transacao
	 */
	public void removeByTransacao(Campus campus, int idTransacao) {
		Query q = session.createQuery("DELETE FROM Reserva o WHERE o.idCampus.idCampus = :idCampus AND o.idTransacao.idTransacao = :idTransacao");
		q.setInteger("idCampus", campus.getIdCampus());
		q.setInteger("idTransacao", idTransacao);
		q.executeUpdate();
	}

	public List<Reserva> pesquisaReserva(Campus campus, Date data, TipoReserva tipoReserva) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT o ");
		hql.append("FROM Reserva o JOIN o.idCampus c JOIN o.idItemReserva i JOIN i.idTipoReserva tr ");
		hql.append("WHERE c.idCampus = :idCampus AND ");
		hql.append("tr.idTipoReserva = :idTipoReserva AND ");
		hql.append("o.data = :data ");
		hql.append("ORDER BY i.nome, o.data ASC, o.horaInicio ASC, o.horaFim ASC ");

		Query q = session.createQuery(hql.toString());
		q.setInteger("idCampus", campus.getIdCampus());
		q.setInteger("idTipoReserva", tipoReserva.getIdTipoReserva());
		q.setDate("data", data);

		return this.pesquisaObjetos(q, 0);
	}

	/**
	 * Lista todas as reservas de uma transação
	 * 
	 * @param campus
	 * @param idTransacao
	 * @return
	 */
	public List<Reserva> listaReservaPorTransacao(Campus campus, Integer idTransacao) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT o ");
		hql.append("FROM Reserva o JOIN o.idCampus c JOIN o.idTransacao t ");
		hql.append("WHERE c.idCampus = :idCampus AND ");
		hql.append("t.idTransacao = :idTransacao ");
		hql.append("ORDER BY o.data ASC, o.horaInicio ASC, o.horaFim ASC ");

		Query q = session.createQuery(hql.toString());
		q.setInteger("idCampus", campus.getIdCampus());
		q.setInteger("idTransacao", idTransacao);

		return this.pesquisaObjetos(q, 0);
	}

}