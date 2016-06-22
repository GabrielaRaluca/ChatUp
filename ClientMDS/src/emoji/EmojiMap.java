package emoji;

import java.util.ArrayList;

public class EmojiMap 
{
	private static final String smileyEmoji = "/resources/1.png";
	private static final String sadEmoji = "/resources/2.png";
    private static final String grinEmoji = "/resources/3.png";
    private static final String lolEmoji = "/resources/4.png";
    private static final String tongueEmoji = "/resources/5.png";
    private static final String loveEmoji = "/resources/6.png";
    private static final String winkEmoji = "/resources/7.png";
    private static final String kissEmoji = "/resources/8.png";
    private static final String wowEmoji = "/resources/9.png";
    private static final String coolEmoji = "/resources/10.png";
    private static final String eyesEmoji = "/resources/11.png";
    private static final String madEmoji = "/resources/12.png";
    private static final String nervousEmoji = "/resources/13.png";
    private static final String nerdEmoji = "/resources/14.png";
    private static final String sickEmoji = "/resources/15.png";
    private static final String scaredEmoji = "/resources/16.png";
    private static final String facepalmEmoji = "/resources/17.png";
	
    private static final String smileyCode = ":)";
	private static final String sadCode = ":(";
    private static final String grinCode = ":D";
    private static final String lolCode = "xD";
    private static final String tongueCode = ":P";
    private static final String loveCode = ":X";
    private static final String winkCode = ";)";
    private static final String kissCode = ":*";
    private static final String wowCode = ":O";
    private static final String coolCode = "B-)";
    private static final String eyesCode = "O-(";
    private static final String madCode = "X(";
    private static final String nervousCode = ":-S";
    private static final String nerdCode = ":-B";
    private static final String sickCode = ":-&";
    private static final String scaredCode = "X_X";
    private static final String facepalmCode = ":-F";
	
    public static ArrayList<Emoji> emojis = new ArrayList<Emoji>();
	
	public static void initialize()
	{
		emojis.add(new Emoji(smileyCode, smileyEmoji));
		emojis.add(new Emoji(sadCode, sadEmoji));
        emojis.add(new Emoji(grinCode, grinEmoji));
        emojis.add(new Emoji(lolCode, lolEmoji));
        emojis.add(new Emoji(tongueCode, tongueEmoji));
        emojis.add(new Emoji(loveCode, loveEmoji));
        emojis.add(new Emoji(winkCode, winkEmoji));
        emojis.add(new Emoji(kissCode, kissEmoji));
        emojis.add(new Emoji(wowCode, wowEmoji));
        emojis.add(new Emoji(coolCode, coolEmoji));
        emojis.add(new Emoji(eyesCode, eyesEmoji));
        emojis.add(new Emoji(madCode, madEmoji));
        emojis.add(new Emoji(nervousCode, nervousEmoji));
        emojis.add(new Emoji(nerdCode, nerdEmoji));
        emojis.add(new Emoji(sickCode, sickEmoji));
        emojis.add(new Emoji(scaredCode, scaredEmoji));
        emojis.add(new Emoji(facepalmCode, facepalmEmoji));
	}
	
} 
