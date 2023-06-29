package br.edu.utfpr.dv.sigeu.converter;

import java.util.Date;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.PeriodoLetivo;
import br.edu.utfpr.dv.sigeu.service.PeriodoLetivoService;
import br.edu.utfpr.dv.sigeu.util.LoginFilter;

@FacesConverter(value = "periodoLetivoConverter", forClass = Date.class)
public class PeriodoLetivoConverter implements Converter {

	@Override
	public PeriodoLetivo getAsObject(FacesContext context,
			UIComponent component, String value) {
		try {
			PeriodoLetivoService periodoLetivoService = getPeriodoLetivoService();
			return periodoLetivoService.encontrePorNome(value);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private PeriodoLetivoService getPeriodoLetivoService() {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Campus campus = (Campus) sessionMap.get(LoginFilter.SESSION_CAMPUS);
		return new PeriodoLetivoService(campus);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null || !(value instanceof PeriodoLetivo)) {
			return "";
		}

		String periodoLetivo = ((PeriodoLetivo) value).getNome();

		return periodoLetivo;
	}

}
