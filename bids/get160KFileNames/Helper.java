import java.io.*;
import java.util.*;
import java.net.*;
/** Use the given ids to generate a file of file names, fileNames.txt */

public class Helper {
    public static void main(String[] args) throws Exception {
        String idsPath = "runid.txt";
        getids(idsPath);
        String output = "fileNames.txt";
        File f = new File(output);
        f.createNewFile();
        storeFileNames(5000000);
    }

    private static void getids(String path) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(new File(path)));
        String line = br.readLine();
        while (line != null) {
            ids.add(Integer.parseInt(line));
            line = br.readLine();
        }
        br.close();
    }

    public static void storeFileNames(int limit) throws IOException {
        StringBuffer buffer = new StringBuffer("http://das.sdss.org/imaging/");
        int count = 0;
        while (!ids.isEmpty() && count < limit) {
            int id = ids.remove();
            StringBuffer idBuffer = new StringBuffer(buffer);
            idBuffer.append(id);
            idBuffer.append("/");
            count = getRerunIDFromID(new String(idBuffer), limit, count);
            System.out.println(count);
        }
    }

    public static int getRerunIDFromID(String path, int limit, int curr) throws IOException {
        URL rerunIDPage = new URL(path);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(rerunIDPage.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            String[] rerunIDs = inputLine.split("\"");
            try {
                String candidate = rerunIDs[7];
                Integer.parseInt(candidate.substring(0, candidate.length() - 1));
                curr += outputToFile(path + candidate, limit, curr);
            } catch (ArrayIndexOutOfBoundsException e){
            } catch (NumberFormatException e) {
            }
        }
        in.close();
        return curr;
    }

    public static int outputToFile(String path, int limit, int curr) throws IOException {
        for (int i = 1; i <= 6; i++) {
            StringBuffer br = new StringBuffer(path);
            br.append("corr/");
            br.append(i);
            br.append("/");
            URL dataPage = new URL(new String(br));
            BufferedReader in = new BufferedReader(
                new InputStreamReader(dataPage.openStream()));
            String inputLine;
            Writer output = new BufferedWriter(new FileWriter("fileNames.txt", true));
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
                        output.append(link);
                        output.append("\n");
                        curr += 1;
                    }
                } catch (ArrayIndexOutOfBoundsException e){
                } catch (NumberFormatException e) {
                }
            }
            in.close();
            output.close();
        }
        return curr;
    }

    private static LinkedList<Integer> ids = new LinkedList<Integer>();
}