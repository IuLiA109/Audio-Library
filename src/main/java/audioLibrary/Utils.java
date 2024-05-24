package audioLibrary;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String[] trimInput(String input, String command) {
        String[] parts = input.split("\" ");
        List<String> result = new ArrayList<>();
        for (String part : parts) {

            part = part.trim();
            if(!part.isEmpty())
                result.add(part);
        }
        result.set(0, result.getFirst().substring(command.length()));
        return result.toArray(new String[0]);
    }
}
