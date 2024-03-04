package com.prikolz.automod;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerData {
    public HashMap<String, AD> ADs;
    public List<FludeCharacter> fludSameCharacters;
    public boolean isViolation;
    public String violationMsg;
    public int violationInstantMinutes;
    public static HashMap<String, Badword> badWords;

    public PlayerData() {
        isViolation = false;
        violationMsg = "";
        ADs = new HashMap<>();
        badWords = new HashMap<>();
        fludSameCharacters = new ArrayList<>();
        setBadWords();
    }

    private void setBadWords() {
        badWords.put("卐", new Badword("2.1 Неадекватное поведение", 10080));
        badWords.put(" пидор ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" пидарас ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" пидорас ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" гандон ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" пошел нахуй ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" пошол нахуй ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" пашел нахуй ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" пашёл нахуй ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" пошёл нахуй ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" иди нахуй ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" долбаеб ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" долбаёб ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" долбоеб ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" даун ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" аутист ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" сын шлюхи ", new Badword("2.1 Неадекватное поведение", 240));
        badWords.put("секс", new Badword("2.7", 40));
        badWords.put("порно", new Badword("2.7", 40));
        badWords.put("порнушка", new Badword("2.7", 40));
        badWords.put("сперма", new Badword("2.7", 40));
        badWords.put("трах", new Badword("2.7", 40));
        badWords.put(" еблан", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" динаху", new Badword("2.1 Неадекватное поведение", 30));
        badWords.put(" гомосек ", new Badword("2.1 Неадекватное поведение", 30));
        badWords.put(" хуегрыз ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" хуеглот ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" хуйло ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" хуесос ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" хуерык ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" уёбище ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" уебище ", new Badword("2.1 Неадекватное поведение", 40));
    }

    public void analyseMessage(String newMsg) {

        checkBadWords(" " + newMsg + " ");
        if(isViolation) return;
        if(newMsg.contains("/join ")) {
            checkSpam(newMsg);
            return;
        }
        writeToCharacterFludDetector(newMsg);
    }

    private void writeToCharacterFludDetector(String msg){
        FludeCharacter current = FludeCheck.getMaxFludeCharacter( FludeCheck.analysSymvolCounter(msg) );
        current.time = System.currentTimeMillis();
        current.power = (int)( ((double) current.power / msg.length()) * 100 );
        fludSameCharacters.add(current);
        if(fludSameCharacters.size() > 2) {
            checkCharacterFlood();
            fludSameCharacters.removeFirst();
        }
    }

    public void checkSpam(String newMsg) {
        long time = System.currentTimeMillis();
        String cut; int space; String adID;
        cut = newMsg;
        int index = cut.indexOf("/join ");
        List<String> buffer = new ArrayList<>();

        Instant instant = Instant.ofEpochSecond(time / 1000);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String format = "[" + zonedDateTime.format(formatter) + "]";

        while(index != -1) {
            cut = cut.substring(index + 6);
            space = cut.indexOf(" ");
            if(space == -1) space = cut.length();
            adID = cut.substring(0, space);
            if(buffer.contains(adID)) {
                index = cut.indexOf("/join ");
                continue;
            }
            buffer.add(adID);
            if(ADs.containsKey(adID)) {
                AD ad = ADs.get(adID);
                int counter = 0;
                for(long t : ad.time.stream().toList()) {
                    if(time - t < 1800000) {
                        counter++;
                    }else {
                        ad.time.remove(t);
                    }
                    if(counter > 2) {
                        isViolation = true;
                        violationMsg = "2.3 Спам";
                        violationInstantMinutes = 15;
                        ad.time.clear();
                    }
                }
                if(!isViolation) ad.time.add(time);
                ADs.put(adID, ad);
                System.out.println(format + " \uD83D\uDC41 AD| " + adID + " | " + (counter + 1) + "/3");

            }else {
                AD ad = new AD(time);
                ADs.put(adID, ad);
                System.out.println(format + " \uD83D\uDC41 AD| + " + adID + " | 1/3");
            }
            index = cut.indexOf("/join ");
        }
    }

    public void checkBadWords(String newMsg) {
        String lower = newMsg.toLowerCase();
        for(String bad : badWords.keySet()) {
            if(lower.contains(bad)) {
                Badword badword = badWords.get(bad);
                isViolation = true;
                violationMsg = badword.comment;
                violationInstantMinutes = badword.initialMinutes;
            }
        }
    }

    public void checkCharacterFlood() {

        long i = 0;
        for(FludeCharacter f : fludSameCharacters) {
            if(i == 0) {
                i = f.time;
                continue;
            }
            if(f.time - i > 420000) return;
        }

        FludeCharacter first = fludSameCharacters.getFirst();
        FludeCharacter second = fludSameCharacters.get(1);
        FludeCharacter current = fludSameCharacters.get(2);

        if(first.character != second.character || second.character != current.character) return;
        int general = first.power + second.power + current.power;

        if(general > 150) {
            isViolation = true;
            violationMsg = "2.3 Флуд";
            violationInstantMinutes = 10;
        }
    }
}
