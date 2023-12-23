package isep.lapr3.g094.domain.irrigation;

public class IrrigationSector {

    private int sector;
    private int duracao;
    private char periodicidade;
    private int mix;
    private int recorrencia;

    public IrrigationSector(int sector, int duracao, char periodicidade, int mix, int recorrencia) {
        this.sector = sector;
        this.duracao = duracao;
        this.periodicidade = periodicidade;
        this.mix = mix;
        this.recorrencia = recorrencia;
    }

    public IrrigationSector(int sector, int duracao, char periodicidade) {
        this.sector = sector;
        this.duracao = duracao;
        this.periodicidade = periodicidade;
        this.mix = -1;
        this.recorrencia = -1;
    }

    public int getSector() {
        return sector;
    }

    public int getDuracao() {
        return duracao;
    }

    public char getPeriodicidade() {
        return periodicidade;
    }

    public int getMix() {
        return mix;
    }

    public int getRecorrencia() {
        return recorrencia;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + sector;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IrrigationSector other = (IrrigationSector) obj;
        if (sector != other.sector)
            return false;
        return true;
    }

    @Override
    public String toString() {
        String result = "Setor=" + sector + ", Duração=" + duracao + "min , Periodicidade=" + periodicidade;
        
        if (mix != -1) {
            result += ", Mix=" + mix;
        }
        
        if (recorrencia != -1) {
            result += ", Recorrência=" + recorrencia;
        }
        
        return result;
    }
}
