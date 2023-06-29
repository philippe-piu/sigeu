package br.edu.utfpr.dv.sigeu.enumeration;

public enum StatusReserva {
    EFETIVADA('E', "Efetivada"),
    CANCELADA('C', "Cancelada"),
    PENDENTE('P', "Pendente");

    private Character status;
    private String descricao;

    private StatusReserva(Character status, String descricao) {
        this.status = status;
        this.descricao = descricao;
    }

    public Character getStatus() {
        return status;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusReserva getFromStatus(Character status) {
        return getFromStatusByCriteria(s -> s.getStatus().equals(status));
    }

    // Método auxiliar para buscar StatusReserva baseado em um critério específico
    private static StatusReserva getFromStatusByCriteria(java.util.function.Predicate<StatusReserva> predicate) {
        for (StatusReserva s : StatusReserva.values()) {
            if (predicate.test(s)) {
                return s;
            }
        }
        return null;
    }
}