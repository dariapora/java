import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramFacturi {

    private static List<Factura> facturi = new ArrayList<>();

    static String[] denumiriClienti = new String[]{
            "ALCOR CONSTRUCT SRL",
            "SC DOMINO COSTI SRL",
            "SC TRANSCRIPT SRL",
            "SIBLANY SRL",
            "INTERFLOOR SYSTEM SRL",
            "MERCURY  IMPEX  2000  SRL",
            "ALEXANDER SRL",
            "METAL INOX IMPORT EXPOSRT SRL",
            "EURIAL BROKER DE ASIGURARE SRL"
    };

    static String[] denumiriProduse = new String[]{
            "Stafide 200g",
            "Seminte de pin 300g",
            "Bulion Topoloveana 190g",
            "Paine neagra Frontera",
            "Ceai verde Lipton"

    };

    static double[] preturiProduse = new double[]{
            5.20,
            12.99,
            6.29,
            4.08,
            8.99
    };

    public static List<Factura> generareListaFacturi(LocalDate dataMin, int numarFacturi)
    {
        List<Factura> facturi = new ArrayList<>();
        for(int i=0; i<numarFacturi; i++)
        {
            int numarLinii = (int)(Math.random()*10) + 1;
            Factura noua = new Factura(denumiriClienti[(int)(Math.random()*denumiriClienti.length)], dataMin.plusDays((int)(Math.random()*31)));
            for(int j=0; j<numarLinii; j++)
            {
                int index = (int)(Math.random()*denumiriProduse.length);
                noua.addLinie(new Factura.Linie(denumiriProduse[index], preturiProduse[index], (int)(Math.random()*50)));
            }
            facturi.add(noua);
        }
        return facturi;
    }

    public static void salvareFacturiBinar() {
        try {
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("./test.dat")));
            for (var factura : facturi) {
                dos.writeUTF(factura.getDenumireClient());
                dos.writeInt(factura.getDataEmitere().getYear());
                dos.writeInt(factura.getDataEmitere().getMonthValue());
                dos.writeInt(factura.getDataEmitere().getDayOfMonth());
                dos.writeInt(factura.getLinii().size());
                for (var linie : factura.getLinii()) {
                    dos.writeUTF(linie.produs);
                    dos.writeDouble(linie.pret);
                    dos.writeInt(linie.cantitate);
                }
            }
            dos.close();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static List<Factura> incarcareFacturiBinar() throws IOException
    {
        List<Factura> facturi = new ArrayList<>();
        try {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream("./test.dat")));
            while(true) {
                String denumireClient = dis.readUTF();
                int anEmitere = dis.readInt();
                int lunaEmitere = dis.readInt();
                int ziEmitere = dis.readInt();
                LocalDate ld = LocalDate.of(anEmitere, lunaEmitere, ziEmitere);
                int numarLinii = dis.readInt();
                Factura factura = new Factura(denumireClient, ld);
                for (int i = 0; i < numarLinii; i++) {
                    factura.addLinie(new Factura.Linie(dis.readUTF(), dis.readDouble(), dis.readInt()));
                }
                facturi.add(factura);
            }
        } catch (EOFException e) {

        }
        return facturi;
    }

    public static void afisareFacturi()
    {
        for(var factura : facturi)
        {
            System.out.println(factura);
        }
    }

    public static void salvareText() throws IOException
    {
        try (var writer = new PrintWriter(new BufferedWriter(new FileWriter("./test.txt")))){
            for(var factura : facturi) {
                writer.println(factura.getDenumireClient());
                writer.println(factura.getDataEmitere());
                writer.println(factura.getLinii().size());
                for(var linie : factura.getLinii())
                {
                    writer.println(linie.produs);
                    writer.println(linie.pret);
                    writer.println(linie.cantitate);
                }
            }
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
        }
    }

    public static List<Factura> incarcareText()
    {
        List<Factura> facturi = new ArrayList<>();
        try(var reader = new BufferedReader(new FileReader("./test.txt")))
        {
           String client;
           while((client = reader.readLine()) != null)
           {
               String[] data = reader.readLine().split("-");
               LocalDate ld = LocalDate.of(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]));
               Factura factura = new Factura(client, ld);
               int numarLinii = Integer.parseInt(reader.readLine());
               for(int i=0; i<numarLinii; i++)
               {
                   Factura.Linie linie = new Factura.Linie(reader.readLine(), Double.parseDouble(reader.readLine()), Integer.parseInt(reader.readLine()));
                   factura.addLinie(linie);
               }
               facturi.add(factura);
           }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return facturi;
    }

    public static void generareRaport(List<Factura> listaFacturi, String denumireFisier)
    {
        Map<String, Double> dictionar = new HashMap<>();
        Map<String, Integer> counts = new HashMap<>();
        for(var factura : listaFacturi) {
            double valoareTotala = 0.0;
            for (var linie : factura.getLinii()) {
              valoareTotala += linie.pret* linie.cantitate;
            }
            dictionar.merge(factura.getDenumireClient(), valoareTotala, Double::sum);
            counts.merge(factura.getDenumireClient(), 1, Integer::sum);
        }
        List<Map.Entry<String, Double>> listaDesc = new ArrayList<>(dictionar.entrySet());
        listaDesc.sort((l1, l2) -> l2.getValue().compareTo(l1.getValue()));
        try (var writer = new PrintWriter(new BufferedWriter(new FileWriter(denumireFisier+".txt")))){
            for(Map.Entry<String, Double> map : listaDesc) {
                writer.printf("%-40s %20d facturi, TOTAL: %.2f\n", map.getKey(), counts.get(map.getKey()), map.getValue());
            }
        }
        catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        }
    public static void main(String[] args) throws IOException {
        facturi = generareListaFacturi(LocalDate.now().minusDays(100), 10);
        salvareText();
        facturi=incarcareText();
        afisareFacturi();
        generareRaport(facturi, "raport");
    }

}
