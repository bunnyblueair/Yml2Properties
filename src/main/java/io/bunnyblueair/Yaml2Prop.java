package io.bunnyblueair;

import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author bunnyblue
 */
public class Yaml2Prop {
    public static void main(String[] args) throws IOException {
        Collection<File> files = FileUtils.listFiles(new File("."), new String[]{"yml"}, true);
        if (files.isEmpty()){
            System.err.println("plz run java -jar yaml2prop.jar in your project dir");
            return;
        }
        for (File ymlFile : files) {
            System.out.println(ymlFile.getAbsolutePath());
            Yaml yaml = new Yaml();
            Map<String, String> map = yaml.load(new FileInputStream(ymlFile));
            StringBuilder stringBuilder = new StringBuilder();
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                Object val = entry.getValue();

                if (val instanceof HashMap) {
                    flatMap((HashMap<String, ?>) val, key, stringBuilder);
                } else {
                    stringBuilder.append(key + "= " + map.get(key));
                    stringBuilder.append("\n");
                }


            }
            File propFile = new File(ymlFile.getAbsolutePath().replace(".yml", ".properties"));
            propFile.delete();
            FileUtils.writeStringToFile(propFile, stringBuilder.toString());
            ymlFile.renameTo(new File(ymlFile.getAbsolutePath() + ".bak"));
        }


    }

    public static void flatMap(HashMap<String, ?> map, String tag, StringBuilder stringBuilder) {
        for (String key : map.keySet()) {
            Object object = map.get(key);
            if (object instanceof HashMap) {
                flatMap((HashMap<String, ?>) object, tag + "." + key, stringBuilder);
            } else {
                stringBuilder.append(tag + "." + key + "= " + map.get(key));
                stringBuilder.append("\n");
                // .final.  System.err.println(tag + "." + key + "= " + map.get(key));
            }
        }
    }


}
