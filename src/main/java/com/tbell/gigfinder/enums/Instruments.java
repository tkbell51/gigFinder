package com.tbell.gigfinder.enums;

public enum Instruments {
    SINGER("Singer"), ACCORDION("Accordion"), ACOUSTIC_GUITAR("Acoustic Guitar"), BAGPIPES("Bagpipes"), BANJO("Banjo"),
    BASS_GUITAR("bass guitar"), BONGO("Bongo"), CELLO("Cello"), CLARINET("Clarinet"), DIDGERIDOO("Didgeridoo"),
    DRUMS("Drums"), ELECTRIC_GUITAR("Electric Guitar"), FLUTE("Flute"), FRENCH_HORN("French Horn"), HARMONICA("Harmonica"),
    HARP("Harp"), KEYBOARD("Keyboard"), MANDOLIN("Mandolin"), MARIMBA("Marimba"), OBOE("Oboe"), ORGAN("Organ"),
    PIANO("Piano"), SAXOPHONE("Saxophone"), TROMBONE("Trombone"), TRUMPET("Trumpet"), TUBA("Tuba"), UKULELE("Ukulele"),
    VIOLA("Viola"), VIOLIN("Violin");

    private String name;

    private Instruments(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
