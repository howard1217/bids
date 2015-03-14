import java.io.*;
import java.util.*;
import java.net.*;

/** Given a stripe number n, output all .fit files in n.txt 
 *  http://das.sdss.org/www/html/imaging/dr-99.html */
public class Helper {
    public static void main(String[] args) throws IOException {
        int stripeID = -1;
        try {
            stripeID = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("You need a stripe number as an input.");
            System.exit(1);
        }
        parseStripeToRun();
        output = String.valueOf(stripeID) + ".txt";
        File f = new File(output);
        f.createNewFile();
        storeFileNames(stripeID);
    }

    private static void parseStripeToRun() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File("stripeToRun.txt")));
        String line;
        int counter = 1;
        int currStripe = -1;
        while ((line = br.readLine()) != null) {
            if (counter % 5 == 1) {
                int num = Integer.parseInt(line);
                if (num != currStripe) {
                    HashSet<Integer> runIDs = new HashSet<Integer>();
                    stripeMap.put(num, runIDs);
                    currStripe = num;
                }
            } else if (counter % 5 == 2) {
                int num = Integer.parseInt(line);
                stripeMap.get(currStripe).add(num);
            }
            counter++;
        }
        br.close();
    }

    /** Given ID, write the corresponding .fits file to output. */
    public static void storeFileNames(int id) throws IOException {
        StringBuffer buffer = new StringBuffer("http://das.sdss.org/imaging/");
        Set<Integer> ids = null;
        try {
            ids = stripeMap.get(id);
            for (Integer runID : ids) {
                StringBuffer idBuffer = new StringBuffer(buffer);
                idBuffer.append(runID);
                idBuffer.append("/");
                getRerunIDFromID(new String(idBuffer));
            }
        } catch (NullPointerException e) {
            System.out.println("No runs for the given stripID.");
            System.exit(1);
        }    
    }

    public static void getRerunIDFromID(String path) throws IOException {
        URL rerunIDPage = new URL(path);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(rerunIDPage.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            String[] rerunIDs = inputLine.split("\"");
            try {
                String candidate = rerunIDs[7];
                Integer.parseInt(candidate.substring(0, candidate.length() - 1));
                outputToFile(path + candidate);
            } catch (ArrayIndexOutOfBoundsException e){
            } catch (NumberFormatException e) {
            }
        }
        in.close();
    }

    private static void outputToFile(String path) throws IOException {
        for (int i = 1; i <= 6; i++) {
            StringBuffer br = new StringBuffer(path);
            br.append("corr/");
            br.append(i);
            br.append("/");
            URL dataPage = new URL(new String(br));
            BufferedReader in = new BufferedReader(
                new InputStreamReader(dataPage.openStream()));
            String inputLine;
            Writer out = new BufferedWriter(new FileWriter(output, true));
            while ((inputLine = in.readLine()) != null) {
                String[] parts = inputLine.split("\"");
                String s1 = "fp";
                String s2 = ".fit.gz";
                try {
                    String data = parts[7];
                    if (data.substring(0, 2).equals(s1) 
                        && data.substring(data.length() - 7).equals(s2)) {
                        StringBuffer link = new StringBuffer(br);
                        link.append(data);
                        out.append(link);
                        out.append("\n");
                    }
                } catch (ArrayIndexOutOfBoundsException e){
                } catch (NumberFormatException e) {
                }
            }
            in.close();
            out.close();
        }
    }

    /* The map from strip to a list of run numbers. */
    private static Map<Integer, Set<Integer>> stripeMap = new HashMap<Integer, Set<Integer>>();
    /* The output file name. */
    private static String output;
}