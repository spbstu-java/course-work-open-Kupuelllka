package com.example.kursach.Classes.Hero;


public class HeroTraining {
    private String heroName;
    private int strength;
    private int agility;
    private int intelligence;
    
    public HeroTraining(String name) {
        this.heroName = name;
        this.strength = 10;
        this.agility = 10;
        this.intelligence = 10;
    }
    @Repeat(3)
    public String doPushUps(int count) {
        int gained = count / 5;
        strength += gained;
        return heroName + " делает " + count + " отжиманий! +" + gained + " к силе";
    }

    public String rest() {
        return heroName + " отдыхает... Zzz";
    }

    @Repeat(2)
    protected String practiceArchery(int arrows) {
        int gained = arrows / 3;
        agility += gained;
        return heroName + " выпускает " + arrows + " стрел! +" + gained + " к ловкости";
    }

    @Repeat(4)
    protected String studySpell(String spellName, int complexity) {
        int gained = complexity * 2;
        intelligence += gained;
        return heroName + " изучает заклинание '" + spellName + "'! +" + gained + " к интеллекту";
    }

    @Repeat(1)
    private String meditate(boolean deepMeditation) {
        int gained = deepMeditation ? 5 : 2;
        intelligence += gained;
        return heroName + (deepMeditation ? " глубоко медитирует" : " медитирует") +
                "! +" + gained + " к интеллекту";
    }

    @Repeat(5)
    private String runMarathon(int distance, int speed) {
        int staminaGain = distance * speed / 10;
        agility += staminaGain;
        return heroName + " пробегает марафон " + distance + " км со скоростью " + speed +
                " км/ч! +" + staminaGain + " к ловкости";
    }

    @Repeat(2)
    private String liftWeights(double weight, String weaponType) {
        int strengthGain = (int)(weight / 20);
        strength += strengthGain;
        return heroName + " поднимает " + weight + " кг " + weaponType +
                "! +" + strengthGain + " к силе";
    }

    public String showStats() {
        return "\n=== СТАТИСТИКА ГЕРОЯ ===\n" +
                "Имя: " + heroName + "\n" +
                "Сила: " + strength + "\n" +
                "Ловкость: " + agility + "\n" +
                "Интеллект: " + intelligence + "\n" +
                "Общий уровень: " + (strength + agility + intelligence) + "\n" +
                "=======================\n";
    }
    
    public boolean isReadyForBattle() {
        return (strength + agility + intelligence) > 50;
    }
    
    // Геттеры
    public String getHeroName() { return heroName; }
    public int getStrength() { return strength; }
    public int getAgility() { return agility; }
    public int getIntelligence() { return intelligence; }
}
