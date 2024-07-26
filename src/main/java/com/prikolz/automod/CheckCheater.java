package com.prikolz.automod;

import com.prikolz.automod.users.UserData;
import com.prikolz.automod.users.Users;
import com.prikolz.lscommands.LSHandler;
import com.prikolz.run;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CheckCheater {
    public static String owner = "";
    public static String target = "";
    public static boolean isOpen = true;
    public static HashMap<String, String> reports = new HashMap<>();

    public static boolean addRequest(String owner, String target) {
        if(!isOpen) return false;
        if(!Users.isOpen) return false;
        if(reports.containsKey(target)) return false;
        System.out.println("Новый репорт от игрока " + owner + " на игрока " + target);
        System.out.println("Текущие репорты >reports");
        reports.put(target, owner);
        CheckCheater.isOpen = false;
        CheckCheater.owner = owner; CheckCheater.target = target;
        Users.infoRequest(target, () -> {
            String date = Users.data.registrationDate;
            if(date.equals("N/A")) {
                System.out.println("⚠⚠⚠ Решение ЗАБАНИТЬ по N/A дате " + CheckCheater.target);
                LSHandler.say(CheckCheater.owner, "✔ Бот принял решение забанить игрока " + CheckCheater.target + "! Спасибо за ваше обращение!");
                run.automod.sendCommand("kick " + CheckCheater.target + " Вы были забанены за читы. Если это ошибка, сообщите в любую соцсеть Just MC.");
                run.automod.sendCommand("ban " + CheckCheater.target + " 99999d [ᴀᴜᴛᴏᴍᴏᴅ] 3.2 Аккаунт для читов");
                CheckCheater.isOpen = true;
                return;
            }
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                Date dateClass = dateFormat.parse(date);
                long registerTime = dateClass.getTime() / 1000;
                System.out.println("");
                if( (System.currentTimeMillis() / 1000 - registerTime) < 172800) {
                    LSHandler.say(CheckCheater.owner, "✔ Бот принял решение забанить игрока " + CheckCheater.target + "! Спасибо за ваше обращение!");
                    run.automod.sendCommand("kick " + CheckCheater.target + " Вы были забанены за читы. Если это ошибка, сообщите в любую соцсеть Just MC.");
                    run.automod.sendCommand("ban " + CheckCheater.target + " 99999d [ᴀᴜᴛᴏᴍᴏᴅ] 3.2 Аккаунт для читов");
                    System.out.println("⚠⚠⚠ Решение ЗАБАНИТЬ по дате " + CheckCheater.target + " Данные: Оригинальная дата =" + date + " Преобразованная =" + registerTime + " Разница =" + (System.currentTimeMillis() / 1000 - registerTime));
                }else{
                    LSHandler.say(CheckCheater.owner, "✔ Ваш репорт на игрока " + CheckCheater.target + " успешно отправлен модераторам!");
                }
            }catch (Exception e) {
                e.printStackTrace();
                LSHandler.say(CheckCheater.owner, "❌ При обработке запроса возникала ошибка. Обратитесь к @2m3v в дискорде.");
            }

            CheckCheater.isOpen = true;
        });
        return true;
    }

}
