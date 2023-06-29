package br.edu.utfpr.dv.sigeu.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * A entidade LdapServer representa um servidor LDAP no sistema.
 */
@Entity
@Table(name = "ldap_server")
@NamedQueries({
    @NamedQuery(name = "LdapServer.findAll", query = "SELECT l FROM LdapServer l")
})
public class LdapServer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Atributos da entidade
    
    @Id
    @Basic(optional = false)
    @Column(name = "id_server")
    private Integer idServer;
    
    @Basic(optional = false)
    private String host;
    
    @Basic(optional = false)
    private int port;
    
    @Basic(optional = false)
    private boolean ssl;
    
    @Basic(optional = false)
    private String basedn;
    
    @Basic(optional = false)
    @Column(name = "sufixo_email")
    private String sufixoEmail;
    
    @Column(name = "var_ldap_nome_completo")
    private String varLdapNomeCompleto;
    
    @Column(name = "var_ldap_email")
    private String varLdapEmail;
    
    @Column(name = "var_ldap_cnpj_cpf")
    private String varLdapCnpjCpf;
    
    @Column(name = "var_ldap_matricula")
    private String varLdapMatricula;
    
    @Column(name = "var_ldap_uid")
    private String varLdapUid;
    
    @Basic(optional = false)
    @Column(name = "var_ldap_campus")
    private String varLdapCampus;
    
    @Basic(optional = false)
    private boolean ativo;
    
    @JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Campus idCampus;

    // Construtores
    
    public LdapServer() {
    }

    public LdapServer(Integer idServer) {
        this.idServer = idServer;
    }

    public LdapServer(Integer idServer, String host, int port, boolean ssl, String basedn, String sufixoEmail, String varLdapCampus, boolean ativo) {
        this.idServer = idServer;
        this.host = host;
        this.port = port;
        this.ssl = ssl;
        this.basedn = basedn;
        this.sufixoEmail = sufixoEmail;
        this.varLdapCampus = varLdapCampus;
        this.ativo = ativo;
    }

    // Getters e Setters
    
    public Integer getIdServer() {
        return idServer;
    }

    public void setIdServer(Integer idServer) {
        this.idServer = idServer;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public String getBasedn() {
        return basedn;
    }

    public void setBasedn(String basedn) {
        this.basedn = basedn;
    }

    public String getSufixoEmail() {
        return sufixoEmail;
    }

    public void setSufixoEmail(String sufixoEmail) {
        this.sufixoEmail = sufixoEmail;
    }

    public String getVarLdapNomeCompleto() {
        return varLdapNomeCompleto;
    }

    public void setVarLdapNomeCompleto(String varLdapNomeCompleto) {
        this.varLdapNomeCompleto = varLdapNomeCompleto;
    }

    public String getVarLdapEmail() {
        return varLdapEmail;
    }

    public void setVarLdapEmail(String varLdapEmail) {
        this.varLdapEmail = varLdapEmail;
    }

    public String getVarLdapCnpjCpf() {
        return varLdapCnpjCpf;
    }

    public void setVarLdapCnpjCpf(String varLdapCnpjCpf) {
        this.varLdapCnpjCpf = varLdapCnpjCpf;
    }

    public String getVarLdapMatricula() {
        return varLdapMatricula;
    }

    public void setVarLdapMatricula(String varLdapMatricula) {
        this.varLdapMatricula = varLdapMatricula;
    }

    public String getVarLdapUid() {
        return varLdapUid;
    }

    public void setVarLdapUid(String varLdapUid) {
        this.varLdapUid = varLdapUid;
    }

    public String getVarLdapCampus() {
        return varLdapCampus;
    }

    public void setVarLdapCampus(String varLdapCampus) {
        this.varLdapCampus = varLdapCampus;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Campus getIdCampus() {
        return idCampus;
    }

    public void setIdCampus(Campus idCampus) {
        this.idCampus = idCampus;
    }

    // hashCode, equals e toString
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idServer != null ? idServer.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LdapServer)) {
            return false;
        }
        LdapServer other = (LdapServer) object;
        if ((this.idServer == null && other.idServer != null) || (this.idServer != null && !this.idServer.equals(other.idServer))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.LdapServer[ idServer=" + idServer + " ]";
    }
    
}