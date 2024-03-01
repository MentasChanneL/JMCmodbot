package com.prikolz.automod;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FludeCheck {

    public static List<FludeCharacter> analysSymvolCounter(String message){
        List<FludeCharacter> result = new ArrayList<>();
        HashMap<Character, Integer> counter = new HashMap<>();
        int count;
        for(char s : message.toCharArray()) {
            count = 0;
            if(counter.containsKey(s)) {
                count = counter.get(s);
            }
            count++;
            counter.put(s, count);
        }

        for(char i : counter.keySet()) {
            result.add( new FludeCharacter(i, counter.get(i)) );
        }

        return result;
    }

    public static FludeCharacter getMaxFludeCharacter(List<FludeCharacter> counter) {
        FludeCharacter max = new FludeCharacter('?',0);
        int maxC = 0;
        int currentC;
        for(FludeCharacter i : counter) {
            currentC = i.power;
            if(currentC > maxC) {
                max = i;
                maxC = currentC;
            }
        }
        return max;
    }
    public static void main(String[] args) {
        try {
            FileReader fr = new FileReader("C:/Users/Сыр/AppData/Roaming/.minecraft/game/logs/text.txt");
            BufferedReader br = new BufferedReader(fr);

            int indexGlobal;
            for(String line : br.lines().toList()) {
                indexGlobal = line.indexOf("[!]");
                if(indexGlobal == -1) continue;
                line = line.substring(indexGlobal);
                line = line.substring(line.indexOf(":") + 2);

                List<FludeCharacter> list = analysSymvolCounter(line);
                FludeCharacter max = getMaxFludeCharacter(list);

                System.out.println(line + " ☀ Частый: " + max.character + " Размер: " + max.power + " к " + line.length() + " = " + (int)(((double)max.power / line.length()) * 100) + "%");
            }

            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}