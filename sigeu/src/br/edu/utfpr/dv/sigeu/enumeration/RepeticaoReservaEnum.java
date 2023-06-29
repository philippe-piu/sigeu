package br.edu.utfpr.dv.sigeu.enumeration;

public enum RepeticaoReservaEnum {
    SEM_REPETICAO("N", "Sem repetição", 0),
    SEMANAL("S", "Semanal", 7),
    DIARIO("D", "Diário", 1);

    private String id;
    private String descricao;
    private int dias;

    private RepeticaoReservaEnum(String id, String descricao, int dias) {
        this.id = id;
        this.descricao = descricao;
        this.dias = dias;
    }

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getDias() {
        return dias;
    }

    public static RepeticaoReservaEnum getEnum(String id) {
        return getEnumByCriteria(e -> e.getId().equals(id));
    }

    private static RepeticaoReservaEnum getEnumByCriteria(java.util.function.Predicate<RepeticaoReservaEnum> predicate) {
        for (RepeticaoReservaEnum e : RepeticaoReservaEnum.values()) {
            if (predicate.test(e)) {
                return e;
            }
        }
        return null;
    }
}