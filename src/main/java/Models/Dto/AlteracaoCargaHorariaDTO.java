package Models.Dto;

public class AlteracaoCargaHorariaDTO {
    private Long usuarioId;
    private Integer novaCargaHorariaMinima;
    private Long solicitanteId;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getNovaCargaHorariaMinima() {
        return novaCargaHorariaMinima;
    }

    public void setNovaCargaHorariaMinima(Integer novaCargaHorariaMinima) {
        this.novaCargaHorariaMinima = novaCargaHorariaMinima;
    }

    public Long getSolicitanteId() {
        return solicitanteId;
    }

    public void setSolicitanteId(Long solicitanteId) {
        this.solicitanteId = solicitanteId;
    }
}