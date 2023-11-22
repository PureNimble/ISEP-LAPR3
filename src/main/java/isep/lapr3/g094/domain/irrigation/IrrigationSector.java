package isep.lapr3.g094.domain.irrigation;

public class IrrigationSector {

    private int sector;
    private int duracao;
    private char periodicidade;

    public IrrigationSector(int sector, int duracao, char periodicidade) {
        this.sector = sector;
        this.duracao = duracao;
        this.periodicidade = periodicidade;
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
        return "sector=" + sector + ", duracao=" + duracao + ", periodicidade=" + periodicidade;
    }

}
