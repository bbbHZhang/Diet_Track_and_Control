/**
 * @author Han Zhang  hanzhan2
 */
package hw2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVFiler extends DataFiler {

    @Override
    public void writeFile(String filename) {
        // TODO Auto-generated method stub
        // leave empty
    }

    @Override
    public boolean readFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            String row = null;
            while((row = br.readLine()) != null) {
                String[] elements = row.split(",");
                //use StringBuilder and the while-loop to concatenate all nutrients to watch
                StringBuilder sb = new StringBuilder();
                int i = 5;
                while (i < elements.length) {
                    sb.append(elements[i]);
                    i++;
                }
                
                //based on input information, create person when gender is not null
                if(elements[0] != null && elements[0].trim().equalsIgnoreCase("female")) {
                    NutriByte.person = new Female(Float.parseFloat(elements[1].trim()), Float.parseFloat(elements[2].trim()),
                            Float.parseFloat(elements[3].trim()), Float.parseFloat(elements[4].trim()), sb.toString());
                } else if(elements[0] != null && elements[0].trim().equalsIgnoreCase("male")) {
                    NutriByte.person = new Male(Float.parseFloat(elements[1].trim()), Float.parseFloat(elements[2].trim()),
                            Float.parseFloat(elements[3].trim()), Float.parseFloat(elements[4].trim()), sb.toString());   
                } else { return false;}

            }
            return true;
        }
        catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
