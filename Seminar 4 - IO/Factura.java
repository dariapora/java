import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Factura {
    private String denumireClient;
    private LocalDate dataEmitere;
    private List<Linie> linii;

    public static final class Linie {
        final String produs;
        final double pret;
        final int cantitate;

        @Override
        public String toString() {
            return "Linie{" +
                    "produs='" + produs + '\'' +
                    ", pret=" + pret +
                    ", cantitate=" + cantitate +
                    '}';
        }

        public Linie(String produs, double pret, int cantitate) {
            this.produs = produs;
            this.pret = pret;
            this.cantitate = cantitate;
        }
    }

    public Factura(String denumireClient, LocalDate dataEmitere) {
        this.denumireClient = denumireClient;
        this.dataEmitere = dataEmitere;
        this.linii = new ArrayList<>();
    }

    public String getDenumireClient() {
        return denumireClient;
    }

    public void setDenumireClient(String denumireClient) {
        this.denumireClient = denumireClient;
    }

    public LocalDate getDataEmitere() {
        return dataEmitere;
    }

    public void setDataEmitere(LocalDate dataEmitere) {
        this.dataEmitere = dataEmitere;
    }

    public List<Linie> getLinii() {
        return linii;
    }

    public void setLinii(List<Linie> linii) {
        this.linii = linii;
    }

    public void addLinie(Linie linie)
    {
        linii.add(linie);
    }

    @Override
    public String toString() {
        return "Factura{" +
                "denumireClient='" + denumireClient + '\'' +
                ", dataEmitere=" + dataEmitere +
                ", linii=" + linii +
                '}';
    }
}
