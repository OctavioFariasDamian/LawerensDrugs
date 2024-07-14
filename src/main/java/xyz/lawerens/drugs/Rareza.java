package xyz.lawerens.drugs;

public enum Rareza {
    COMUN,
    POCO_COMUN,
    EPICA,
    MITICA,
    LEGENDARIA,
    EXOTICA;

    @Override
    public String toString() {
        String ret = "&C&l"+name();
        switch (this){
            case EPICA: {
                ret = "&5Epico";
                break;
            }
            case MITICA: {
                ret = "&x&C&C&0&C&1&3Mítica";
                break;
            }
            case LEGENDARIA: {
                ret = "&x&F&F&D&9&4&2LEGENDARIA";
                break;
            }
            case EXOTICA: {
                ret = "&x&F&F&2&2&0&2&ka&x&F&F&4&3&0&4&ka &x&F&F&6&5&0&6E&x&F&F&8&7&0&8x&x&F&F&A&8&0&Ao&x&F&F&C&A&0&Ct&x&F&F&A&8&0&Ai&x&F&F&8&7&0&8c&x&F&F&6&5&0&6o &x&F&F&4&3&0&4&ka&x&F&F&2&2&0&2&ka";
                break;
            }
        }
        return ret;
    }
}
