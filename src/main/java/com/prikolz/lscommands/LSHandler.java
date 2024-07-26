package com.prikolz.lscommands;

import com.prikolz.DisplayNick;
import com.prikolz.automod.CheckCheater;
import com.prikolz.automod.PlayerData;
import com.prikolz.run;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class LSHandler {

    public static long prikolcd = 0;

    public static void analys(String msg) {
        DisplayNick displayNick = new DisplayNick();
        displayNick.fromPrivateMessage(msg);
        String command = displayNick.message;
        String lowerCommand = displayNick.message.toLowerCase();

        if(lowerCommand.startsWith("чит"))  {
            cheaterReport(displayNick);
            return;
        }
        command = PlayerData.makeSimple(displayNick.message);
        if(command.contains("чит"))  {
            say(displayNick.name, "ℹ Если вы хотите зарепортить читера, отправьте мне сообщение в подобном формате: чит НИК, Читер НИК, читак НИК и т.д.");
            return;
        }

        if(command.equals("анекдот"))  {
            sayPrikol(displayNick);
            return;
        }
        if(command.equals("команды"))  {
            say(displayNick.name, "Список команд: анекдот, команды");
            return;
        }
        if(command.equals("f"))  {
            say(displayNick.name, "kто то умер?");
            return;
        }
        if(command.startsWith("репис"))  {
            say(displayNick.name, "пенис");
            return;
        }
        if(command.startsWith("лол"))  {
            say(displayNick.name, "кек");
            return;
        }
        if(command.startsWith("кек"))  {
            say(displayNick.name, "лол");
            return;
        }
        if(command.contains("шутк"))  {
            String[] phrases = new String[]{"неа", "я вам не шут", "ты кто"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("скольк") || command.contains("скока") || command.contains("скоко"))  {
            String[] phrases = new String[]{"нисколько", "не знаю", "хз"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("можно"))  {
            String[] phrases = new String[]{"нельзя", "можно"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("скажи"))  {
            String[] phrases = new String[]{"не скажу", "не", "не хочу говорить"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.startsWith("реписка"))  {
            say(displayNick.name, "пиписка");
            return;
        }
        if(command.startsWith("реписька"))  {
            say(displayNick.name, "пиписька");
            return;
        }
        if(command.startsWith("пиписка"))  {
            say(displayNick.name, "реписка");
            return;
        }
        if(command.startsWith("пиписька"))  {
            say(displayNick.name, "реписька");
            return;
        }
        if(command.equals("пенис"))  {
            say(displayNick.name, "репис");
            return;
        }
        if(command.startsWith("что такое"))  {
            String[] phrases = new String[]{"это я не понмню", "не знаю", "спросите у другого", "да не знаю"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("зайди"))  {
            String[] phrases = new String[]{"не зайду", "зачем", "не хочу"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("как дела"))  {
            String[] phrases = new String[]{"с делами хорошо", "хорошо", "отлично", "нормально", "а у тебя как дела?"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("крутой"))  {
            String[] phrases = new String[]{"крутой", "крут", "не крутой", "крутышка", "не крутец"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("это"))  {
            String[] phrases = new String[]{"хорошо понятно", "понятно", "буду знать"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("секс") || command.contains("порно"))  {
            String[] phrases = new String[]{"что такое секс", "это что", "что это"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.equals("нет") || command.equals("не"))  {
            String[] phrases = new String[]{"что нет", "да"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.equals("да") || command.equals("угу"))  {
            String[] phrases = new String[]{"что да", "нет"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("кто ты") || command.contains("ты кто"))  {
            String[] phrases = new String[]{"я автомод", "я автобот", "я стас борецкий"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("помоги") || command.contains("помаги") || command.contains("памаги"))  {
            String[] phrases = new String[]{"я не могу помочь", "я не умею помогать", "что случилось"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("как"))  {
            String[] phrases = new String[]{"никак", "както", "как"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("когда"))  {
            String[] phrases = new String[]{"никогда", "когда-то", "когда раки свиснут"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("ты ") || command.startsWith("ти "))  {
            String[] phrases = new String[]{"нет", "я нет"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("гей"))  {
            String[] phrases = new String[]{"кто гей", "ты гей?", "я не гей"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("2м3в") || command.startsWith("2м3v") || command.startsWith("двамтрив"))  {
            String[] phrases = new String[]{"что надо", "чего надо", "что хотел", "это не я"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.startsWith("я "))  {
            say(displayNick.name, "да");
            return;
        }
        if(command.contains("бот") || command.contains("bot"))  {
            String[] phrases = new String[]{"я не бот", "кто бот", "я бот"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("размут"))  {
            String[] phrases = new String[]{"не хочу", "не буду", "неа"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("хах"))  {
            String[] phrases = new String[]{"смешной", "смешно", "ха-ха", "смешно", "обхохочешься"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("туда"))  {
            String[] phrases = new String[]{"куда", "куда туда", "туда", "сюда"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.contains("зовут"))  {
            String[] phrases = new String[]{"кого зовут", "куда зовут", "никак не зовут", "тебя зовут"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.startsWith("прив") || command.startsWith("ку") || command.startsWith("q") || command.startsWith("здра") || command.startsWith("йй"))  {
            String[] phrases = new String[]{"ку", "здравствуйте", "привет", "приветствую"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }
        if(command.startsWith("пок") || command.startsWith("досви") || command.startsWith("bb") || command.startsWith("бб"))  {
            String[] phrases = new String[]{"пока", "до встречи", "бывай", "ну пока"};
            say(displayNick.name, phrases[new Random().nextInt(phrases.length)]);
            return;
        }

        List<String> what = Arrays.asList("ты что тупой", "понял", "ладно", "я бот", "окей", "нет", "да", "круто", "глупый " + displayNick.name + " говорит " + command);

        say( displayNick.name, what.get( new Random().nextInt(what.size()) ));

    }

    private static void sayPrikol(DisplayNick displayNick) {
        if(System.currentTimeMillis() < prikolcd) {
            return;
        }
        try {
            prikolcd = System.currentTimeMillis() + 1000;
            URL url = new URL("https://randstuff.ru/joke/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder content = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();
            connection.disconnect();

            line = content.substring(content.indexOf("id=\"joke\"><table class=\"text\"><tr><td>"));
            line = line.substring( line.indexOf("<td>") + 4, line.indexOf("</td>") );

            say(displayNick.name, "☻ " + line + " ☻");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void cheaterReport(DisplayNick displayNick) {
        String[] args = displayNick.message.split(" ");
        if(args.length < 2) {
            say(displayNick.name, "ℹ Если вы хотите зарепортить читера, отправьте мне сообщение в подобном формате: чит НИК, Читер НИК, читак НИК и т.д.");
            return;
        }
        boolean s = CheckCheater.addRequest(displayNick.name, args[1]);
        if(!s) {
            say(displayNick.name, "❌ Запрос не выполнен. Возможно, на этого игрока уже есть репорт. Повторите попытку через минуту.");
            return;
        }
        say(displayNick.name, "⌚ Выполняю ваш запрос: репорт читера с ником " + args[1] + " ...");
    }

    public static void say(String getter, String content) {
        run.automod.sendCommand("msg " + getter + " " + content);
    }

}
