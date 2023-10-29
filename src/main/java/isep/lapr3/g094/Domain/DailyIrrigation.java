package isep.lapr3.g094.Domain;

public class DailyIrrigation {
    private char parcela;
    private int duracao;
    private char tipoRega;

    public DailyIrrigation(char parcela, int duracao, char tipoRega) {
        this.parcela = parcela;
        this.duracao = duracao;
        this.tipoRega = tipoRega;
    }
    public char getParcela(){
        return parcela;
    }
    public int getDuracao(){
        return duracao;
    }
    public char getTipoRega(){
        return tipoRega;
    }
}
