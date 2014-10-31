package org.andengine.extension.svg.util.constants;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 22:22:08 - 23.05.2011
 */
public class ColorUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int COLOR_MASK_32BIT_ARGB_ALPHA = 0xFF000000;
	public static final int COLOR_MASK_32BIT_ARGB_RGB = 0xFFFFFF;
	public static final int COLOR_MASK_32BIT_ARGB_R = 0xFF0000;
	public static final int COLOR_MASK_32BIT_ARGB_G = 0x00FF00;
	public static final int COLOR_MASK_32BIT_ARGB_B = 0x0000FF;

	public static final int COLOR_MASK_12BIT_RGB_R = 0xF00;
	public static final int COLOR_MASK_12BIT_RGB_G = 0x0F0;
	public static final int COLOR_MASK_12BIT_RGB_B = 0x00F;

	public static final Pattern RGB_PATTERN = Pattern.compile("rgb\\((.*[\\d]+),.*([\\d]+),.*([\\d]+).*\\)");

	private static HashMap<String, Integer> NAMED_COLORS = new HashMap<String, Integer>();
	static {
		// TODO With Java7 a switch on the string might perform better.
		NAMED_COLORS.put("aliceblue", 0xf0f8ff);
		NAMED_COLORS.put("antiquewhite", 0xfaebd7);
		NAMED_COLORS.put("aqua", 0x00ffff);
		NAMED_COLORS.put("aquamarine", 0x7fffd4);
		NAMED_COLORS.put("azure", 0xf0ffff);
		NAMED_COLORS.put("beige", 0xf5f5dc);
		NAMED_COLORS.put("bisque", 0xffe4c4);
		NAMED_COLORS.put("black", 0x000000);
		NAMED_COLORS.put("blanchedalmond", 0xffebcd);
		NAMED_COLORS.put("blue", 0x0000ff);
		NAMED_COLORS.put("blueviolet", 0x8a2be2);
		NAMED_COLORS.put("brown", 0xa52a2a);
		NAMED_COLORS.put("burlywood", 0xdeb887);
		NAMED_COLORS.put("cadetblue", 0x5f9ea0);
		NAMED_COLORS.put("chartreuse", 0x7fff00);
		NAMED_COLORS.put("chocolate", 0xd2691e);
		NAMED_COLORS.put("coral", 0xff7f50);
		NAMED_COLORS.put("cornflowerblue", 0x6495ed);
		NAMED_COLORS.put("cornsilk", 0xfff8dc);
		NAMED_COLORS.put("crimson", 0xdc143c);
		NAMED_COLORS.put("cyan", 0x00ffff);
		NAMED_COLORS.put("darkblue", 0x00008b);
		NAMED_COLORS.put("darkcyan", 0x008b8b);
		NAMED_COLORS.put("darkgoldenrod", 0xb8860b);
		NAMED_COLORS.put("darkgray", 0xa9a9a9);
		NAMED_COLORS.put("darkgreen", 0x006400);
		NAMED_COLORS.put("darkgrey", 0xa9a9a9);
		NAMED_COLORS.put("darkkhaki", 0xbdb76b);
		NAMED_COLORS.put("darkmagenta", 0x8b008b);
		NAMED_COLORS.put("darkolivegreen", 0x556b2f);
		NAMED_COLORS.put("darkorange", 0xff8c00);
		NAMED_COLORS.put("darkorchid", 0x9932cc);
		NAMED_COLORS.put("darkred", 0x8b0000);
		NAMED_COLORS.put("darksalmon", 0xe9967a);
		NAMED_COLORS.put("darkseagreen", 0x8fbc8f);
		NAMED_COLORS.put("darkslateblue", 0x483d8b);
		NAMED_COLORS.put("darkslategray", 0x2f4f4f);
		NAMED_COLORS.put("darkslategrey", 0x2f4f4f);
		NAMED_COLORS.put("darkturquoise", 0x00ced1);
		NAMED_COLORS.put("darkviolet", 0x9400d3);
		NAMED_COLORS.put("deeppink", 0xff1493);
		NAMED_COLORS.put("deepskyblue", 0x00bfff);
		NAMED_COLORS.put("dimgray", 0x696969);
		NAMED_COLORS.put("dimgrey", 0x696969);
		NAMED_COLORS.put("dodgerblue", 0x1e90ff);
		NAMED_COLORS.put("firebrick", 0xb22222);
		NAMED_COLORS.put("floralwhite", 0xfffaf0);
		NAMED_COLORS.put("forestgreen", 0x228b22);
		NAMED_COLORS.put("fuchsia", 0xff00ff);
		NAMED_COLORS.put("gainsboro", 0xdcdcdc);
		NAMED_COLORS.put("ghostwhite", 0xf8f8ff);
		NAMED_COLORS.put("gold", 0xffd700);
		NAMED_COLORS.put("goldenrod", 0xdaa520);
		NAMED_COLORS.put("gray", 0x808080);
		NAMED_COLORS.put("green", 0x008000);
		NAMED_COLORS.put("greenyellow", 0xadff2f);
		NAMED_COLORS.put("grey", 0x808080);
		NAMED_COLORS.put("honeydew", 0xf0fff0);
		NAMED_COLORS.put("hotpink", 0xff69b4);
		NAMED_COLORS.put("indianred", 0xcd5c5c);
		NAMED_COLORS.put("indigo", 0x4b0082);
		NAMED_COLORS.put("ivory", 0xfffff0);
		NAMED_COLORS.put("khaki", 0xf0e68c);
		NAMED_COLORS.put("lavender", 0xe6e6fa);
		NAMED_COLORS.put("lavenderblush", 0xfff0f5);
		NAMED_COLORS.put("lawngreen", 0x7cfc00);
		NAMED_COLORS.put("lemonchiffon", 0xfffacd);
		NAMED_COLORS.put("lightblue", 0xadd8e6);
		NAMED_COLORS.put("lightcoral", 0xf08080);
		NAMED_COLORS.put("lightcyan", 0xe0ffff);
		NAMED_COLORS.put("lightgoldenrodyellow", 0xfafad2);
		NAMED_COLORS.put("lightgray", 0xd3d3d3);
		NAMED_COLORS.put("lightgreen", 0x90ee90);
		NAMED_COLORS.put("lightgrey", 0xd3d3d3);
		NAMED_COLORS.put("lightpink", 0xffb6c1);
		NAMED_COLORS.put("lightsalmon", 0xffa07a);
		NAMED_COLORS.put("lightseagreen", 0x20b2aa);
		NAMED_COLORS.put("lightskyblue", 0x87cefa);
		NAMED_COLORS.put("lightslategray", 0x778899);
		NAMED_COLORS.put("lightslategrey", 0x778899);
		NAMED_COLORS.put("lightsteelblue", 0xb0c4de);
		NAMED_COLORS.put("lightyellow", 0xffffe0);
		NAMED_COLORS.put("lime", 0x00ff00);
		NAMED_COLORS.put("limegreen", 0x32cd32);
		NAMED_COLORS.put("linen", 0xfaf0e6);
		NAMED_COLORS.put("magenta", 0xff00ff);
		NAMED_COLORS.put("maroon", 0x800000);
		NAMED_COLORS.put("mediumaquamarine", 0x66cdaa);
		NAMED_COLORS.put("mediumblue", 0x0000cd);
		NAMED_COLORS.put("mediumorchid", 0xba55d3);
		NAMED_COLORS.put("mediumpurple", 0x9370db);
		NAMED_COLORS.put("mediumseagreen", 0x3cb371);
		NAMED_COLORS.put("mediumslateblue", 0x7b68ee);
		NAMED_COLORS.put("mediumspringgreen", 0x00fa9a);
		NAMED_COLORS.put("mediumturquoise", 0x48d1cc);
		NAMED_COLORS.put("mediumvioletred", 0xc71585);
		NAMED_COLORS.put("midnightblue", 0x191970);
		NAMED_COLORS.put("mintcream", 0xf5fffa);
		NAMED_COLORS.put("mistyrose", 0xffe4e1);
		NAMED_COLORS.put("moccasin", 0xffe4b5);
		NAMED_COLORS.put("navajowhite", 0xffdead);
		NAMED_COLORS.put("navy", 0x000080);
		NAMED_COLORS.put("oldlace", 0xfdf5e6);
		NAMED_COLORS.put("olive", 0x808000);
		NAMED_COLORS.put("olivedrab", 0x6b8e23);
		NAMED_COLORS.put("orange", 0xffa500);
		NAMED_COLORS.put("orangered", 0xff4500);
		NAMED_COLORS.put("orchid", 0xda70d6);
		NAMED_COLORS.put("palegoldenrod", 0xeee8aa);
		NAMED_COLORS.put("palegreen", 0x98fb98);
		NAMED_COLORS.put("paleturquoise", 0xafeeee);
		NAMED_COLORS.put("palevioletred", 0xdb7093);
		NAMED_COLORS.put("papayawhip", 0xffefd5);
		NAMED_COLORS.put("peachpuff", 0xffdab9);
		NAMED_COLORS.put("peru", 0xcd853f);
		NAMED_COLORS.put("pink", 0xffc0cb);
		NAMED_COLORS.put("plum", 0xdda0dd);
		NAMED_COLORS.put("powderblue", 0xb0e0e6);
		NAMED_COLORS.put("purple", 0x800080);
		NAMED_COLORS.put("red", 0xff0000);
		NAMED_COLORS.put("rosybrown", 0xbc8f8f);
		NAMED_COLORS.put("royalblue", 0x4169e1);
		NAMED_COLORS.put("saddlebrown", 0x8b4513);
		NAMED_COLORS.put("salmon", 0xfa8072);
		NAMED_COLORS.put("sandybrown", 0xf4a460);
		NAMED_COLORS.put("seagreen", 0x2e8b57);
		NAMED_COLORS.put("seashell", 0xfff5ee);
		NAMED_COLORS.put("sienna", 0xa0522d);
		NAMED_COLORS.put("silver", 0xc0c0c0);
		NAMED_COLORS.put("skyblue", 0x87ceeb);
		NAMED_COLORS.put("slateblue", 0x6a5acd);
		NAMED_COLORS.put("slategray", 0x708090);
		NAMED_COLORS.put("slategrey", 0x708090);
		NAMED_COLORS.put("snow", 0xfffafa);
		NAMED_COLORS.put("springgreen", 0x00ff7f);
		NAMED_COLORS.put("steelblue", 0x4682b4);
		NAMED_COLORS.put("tan", 0xd2b48c);
		NAMED_COLORS.put("teal", 0x008080);
		NAMED_COLORS.put("thistle", 0xd8bfd8);
		NAMED_COLORS.put("tomato", 0xff6347);
		NAMED_COLORS.put("turquoise", 0x40e0d0);
		NAMED_COLORS.put("violet", 0xee82ee);
		NAMED_COLORS.put("wheat", 0xf5deb3);
		NAMED_COLORS.put("white", 0xffffff);
		NAMED_COLORS.put("whitesmoke", 0xf5f5f5);
		NAMED_COLORS.put("yellow", 0xffff00);
		NAMED_COLORS.put("yellowgreen", 0x9acd32);
	}

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static Integer getColorByName(final String pColorName) {
		return NAMED_COLORS.get(pColorName);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
