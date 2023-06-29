package br.edu.utfpr.dv.sigeu.config;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.faces.context.FacesContext;

import com.adamiworks.utils.FileUtils;

public class Config {

    public static final String APPLICATION_NAME = "Sistema Integrado de Gestão Universitária";
    public static final String APPLICATION_CODE = "SIGEU";
    public static final String APPLICATION_VERSION = "1.4.8";
    public static final String NOME_GRUPO_EXTERNO = "EXTERNO";

    // Constantes relacionadas ao arquivo de configuração
    private static class ConfigProperties {
        public static final String ADMIN = "admin";
        public static final String DEBUG = "debug";
        public static final String PATH_UPLOAD = "path.upload";
        public static final String FILE = "config.properties";
        public static final String SEND_MAIL = "sendmail";
    }

    private static Config self;

    private Properties config;
    private boolean debugMode;
    private boolean adminMode;
    private int threadMax = 2;
    private String url;
    private boolean sendMail = false;

    static {
        self = new Config();
    }

    private Config() {
        // Lê arquivo de configurações
        try {
            config = FileUtils.getPropertiesFromClasspath(ConfigProperties.FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.adminMode = false;
        try {
            System.out.print("Validando modo Admin: [");
            String admin = getProperty(ConfigProperties.ADMIN); 
            System.out.println(admin + "]");
            if (admin != null && admin.equals("true")) {
                System.out.println("*** SISTEMA ESTÁ EM MANUTENÇÃO ***");
                this.adminMode = true;
            }
        } catch (Exception e) {
            // ignora
        }

        this.debugMode = false;

        try {
            String debug = getProperty(ConfigProperties.DEBUG); 
            if (debug != null && debug.equals("true")) {
                this.debugMode = true;
            }
        } catch (Exception e) {
            // ignora
        }

        try {
            String thread = getProperty("thread.max"); 
            if (thread != null) {
                this.threadMax = Integer.valueOf(thread);
            }
        } catch (Exception e) {
            this.threadMax = 2;
        }

        try {
            url = getProperty("url"); 
        } catch (Exception e) {
            // ignora
        }

        this.sendMail = true;

        try {
            String sendMailstr = getProperty(ConfigProperties.SEND_MAIL); 
            if (sendMailstr != null && sendMailstr.equals("false")) {
                this.sendMail = false;
            }
        } catch (Exception e) {
            // ignora
        }
    }

    public static Config getInstance() {
        return self;
    }

    /**
     * Retorna o valor de uma propriedade do arquivo config.properties.
     *
     * @param name Nome da propriedade.
     * @return Valor da propriedade.
     */
    public String getConfig(String name) {
        return config.getProperty(name);
    }

    /**
     * Insere uma variável de sessão e define seu valor.
     *
     * @param key   Chave da variável de sessão.
     * @param value Valor da variável de sessão.
     */
    public void setSessionVariable(String key, Object value) {
        Map<String, Object> map = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        map.put(key, value);
    }

    /**
     * Recupera o valor de uma variável de sessão.
     *
     * @param key Chave da variável de sessão.
     * @return Valor da variável de sessão.
     */
    public Object getSessionVariable(String key) {
        Map<String, Object> map = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        return map.get(key);
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public int getThreadMax() {
        return threadMax;
    }

    public String getUrl() {
        return url;
    }

    public boolean isAdminMode() {
        return adminMode;
    }

    public boolean isSendMail() {
        return sendMail;
    }

    private String getProperty(String propertyName) {
        String value = config.getProperty(propertyName);
        return value != null ? value.trim().toLowerCase() : null;
    }
}