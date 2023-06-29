package br.edu.utfpr.dv.sigeu.util;

import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.dv.sigeu.config.Config;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.MailServer;
import br.edu.utfpr.dv.sigeu.exception.DestinatarioInexistenteException;
import br.edu.utfpr.dv.sigeu.service.MailServerService;

import com.adamiworks.utils.crypto.CryptoMode;
import com.adamiworks.utils.crypto.CryptoUtils;
import com.adamiworks.utils.mailsender.MailSender;
import com.adamiworks.utils.mailsender.MailSenderMessage;

public class MensagemEmail {

	private static final byte[] MAIL_KEY_PASSWORD = { -112, 78, -12, 45, -13, 51, -84, 8 };

	private List<MailSenderMessage> mensagens;
	private Campus campus;

	public MensagemEmail(Campus campus) {
		this.campus = campus;
	}

	/**
	 * Cria uma mensagem de e-mail.
	 * 
	 * @param to               Array de destinatários principais
	 * @param cc               Array de destinatários em cópia
	 * @param bcc              Array de destinatários em cópia oculta
	 * @param assunto          Assunto da mensagem
	 * @param conteudo         Conteúdo da mensagem (texto ou HTML)
	 * @param conteudoHtml     Indica se o conteúdo é HTML ou texto simples
	 * @param pathArquivosAnexos Lista de caminhos dos arquivos anexos
	 * @throws DestinatarioInexistenteException Se nenhum destinatário for especificado
	 */
	public void criaMensagem(String[] to, String[] cc, String[] bcc, String assunto, String conteudo,
			boolean conteudoHtml, List<String> pathArquivosAnexos) throws DestinatarioInexistenteException {
		if (to == null && cc == null && bcc == null) {
			throw new DestinatarioInexistenteException();
		}

		if (mensagens == null) {
			mensagens = new ArrayList<MailSenderMessage>();
		}

		MailSenderMessage mensagem = new MailSenderMessage("SIGEU UTFPR DV", campus.getMailServer().getFromEmail());

		if (to != null) {
			for (String s : to) {
				mensagem.addTo(s);
			}
		}

		if (cc != null) {
			for (String s : cc) {
				mensagem.addCc(s);
			}
		}

		if (bcc != null) {
			for (String s : bcc) {
				mensagem.addBcc(s);
			}
		}

		mensagem.setSubject(assunto);

		if (conteudoHtml) {
			mensagem.setHtmlBody(conteudo);
		} else {
			mensagem.setBody(conteudo);
		}

		if (pathArquivosAnexos != null) {
			for (String file : pathArquivosAnexos) {
				mensagem.addAttachFile(file);
			}
		}

		mensagens.add(mensagem);
	}

	/**
	 * Envia todas as mensagens armazenadas no objeto.
	 * Este método deve ser chamado pela thread de envio de e-mails.
	 * @throws Exception
	 */
	void enviarMensagensThread() {
		System.out.println("--> ThreadEnviaMensagemEmail: INICIO");
		CryptoUtils cu;
		try {
			cu = new CryptoUtils(CryptoMode.DES, MAIL_KEY_PASSWORD);

			MailServer server = MailServerService.encontrePorCampus(campus);
			String password = cu.decrypt(server.getPassword());

			MailSender sender = new MailSender(server.getAuthenticationRequired(), server.getHost(), server.getPort(),
					server.getSsl(), server.getStarttls(), server.getPlainTextOverTls(), server.getFromEmail(),
					server.getUserName(), password);

			for (MailSenderMessage message : mensagens) {
				sender.addMessage(message);
			}

			sender.sendMessages();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("--> ThreadEnviaMensagemEmail: FIM");
		}
	}

	/**
	 * Envia as mensagens de e-mail.
	 * Se o modo de depuração estiver ativado, envia as mensagens diretamente.
	 * Caso contrário, inicia uma thread para enviar as mensagens.
	 */
	public void enviaMensagens() {
		if (!Config.getInstance().isDebugMode()) {
			ThreadEnviaMensagemEmail thread = new ThreadEnviaMensagemEmail(this);
			thread.start();
		} else {
			this.enviarMensagensThread();
		}
	}
}