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
    public HashMap<String, SameMessages> sameMessages;

    public PlayerData() {
        isViolation = false;
        violationMsg = "";
        ADs = new HashMap<>();
        badWords = new HashMap<>();
        fludSameCharacters = new ArrayList<>();
        sameMessages = new HashMap<>();
        setBadWords();
    }

    @Override
    public String toString() {
        return "ADs = " + ADs + "\nCharacter flude = " + fludSameCharacters + "\nSame messages = " + sameMessages;
    }

    private void setBadWords() {
        badWords.put("卐", new Badword("2.1 Неадекватное поведение", 10080));
        badWords.put("卍", new Badword("2.1 Неадекватное поведение", 120));
        badWords.put(" пидор ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" пидарас ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" пидорас ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" гандон ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" пошел нахуй", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" пошол нахуй", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" пашел нахуй", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" пашёл нахуй", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" пошёл нахуй", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" пошёл на*", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" иди на*", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" иди нах", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" долбаеб ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" долбаёб ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" долбоеб ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" далбаеб ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" далбоеб ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" даун ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" ты лох ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" аутист ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" сын шлюх", new Badword("2.1 Неадекватное поведение", 240));
        badWords.put(" мать ебал", new Badword("2.1 Неадекватное поведение", 240));
        badWords.put(" сиськ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" секс", new Badword("2.7", 40));
        badWords.put(" порно ", new Badword("2.7", 40));
        badWords.put(" порн ", new Badword("2.7", 40));
        badWords.put(" порна ", new Badword("2.7", 40));
        badWords.put(" порнушка", new Badword("2.7", 40));
        badWords.put(" сперм", new Badword("2.7", 40));
        badWords.put(" трах", new Badword("2.7", 40));
        badWords.put(" сum ", new Badword("2.7", 40));
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
        badWords.put(" хуила ", new Badword("2.1 Неадекватное поведение", 40));
        badWords.put(" дал в рот мат", new Badword("2.1 Неадекватное поведение", 240));
        badWords.put("mс.minеlаnd", new Badword("2.2 Реклама сторонних проектов", 120));
        badWords.put("minеlаnd.nеt", new Badword("2.2 Реклама сторонних проектов", 120));
    }

    public void analyseMessage(String newMsg) {
        checkBadWords(" " + newMsg + " ");
        if(isViolation) return;
        if(newMsg.contains("/join ")) {
            checkSpam(newMsg);
            return;
        }
        checkSameMessages(makeSimple(newMsg));
        writeToCharacterFludDetector(newMsg);
    }

    public static String makeSimple(String text) {
        String result = text.toLowerCase();
        result = result.replaceAll("a", "а");
        result = result.replaceAll("o", "о");
        result = result.replaceAll("y", "у");
        result = result.replaceAll("p", "р");
        result = result.replaceAll("k", "к");
        result = result.replaceAll("c", "с");
        result = result.replaceAll("x", "х");
        result = result.replaceAll("e", "е");
        return result;
    }

    private void checkSameMessages(String msg) {
        if( !this.sameMessages.containsKey(msg) ) this.sameMessages.put(msg, new SameMessages());
        SameMessages sm = this.sameMessages.get(msg);
        boolean check = sm.addTimes(System.currentTimeMillis(), 3, 420000);
        if(sm.times.size() > 1) System.out.println("⚠ Одинаковые сообщения!");
        if(check) {
            this.isViolation = true;
            this.violationMsg = "2.3 Флуд";
            this.violationInstantMinutes = 20;
        }
        if(this.sameMessages.size() > 50) {
            this.sameMessages.clear();
            this.sameMessages.put(msg, sm);
        }
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
                    if(time - t < 900000) {
                        counter++;
                    }else {
                        ad.time.remove(t);
                    }
                    if(counter > 1) {
                        isViolation = true;
                        violationMsg = "2.3 Спам";
                        violationInstantMinutes = 15;
                        ad.time.clear();
                    }
                }
                if(!isViolation) ad.time.add(time);
                ADs.put(adID, ad);
                System.out.println(format + " \uD83D\uDC41 AD| " + adID + " | " + (counter + 1) + "/2");

            }else {
                AD ad = new AD(time);
                ADs.put(adID, ad);
                System.out.println(format + " \uD83D\uDC41 AD| + " + adID + " | 1/2");
            }
            index = cut.indexOf("/join ");
        }
    }

    public void checkBadWords(String newMsg) {
        String lower = makeSimple(newMsg);
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
