//Mac,Windows共通ロジック
//返す色の値はOSによって変更する

#define MACOS
//#define WINDOWS
using System;
using System.Collections.Generic;
#if MACOS
using CoreGraphics;
#elif WINDOWS
using System.Windows.Media;
#endif

namespace MameComment
{
    public static class ColorList
    {

        private static Dictionary<String, int[]> Names = new Dictionary<String, int[]>()
        {
            {"Transparent",new int[] {0,0,0,0}},
            {"AliceBlue",new int[] {240,248,255,255}},
            {"AntiqueWhite",new int[] {250,235,215,255}},
            {"Aqua",new int[] {0,255,255,255}},
            {"Aquamarine",new int[] {127,255,212,255}},
            {"Azure",new int[] {240,255,255,255}},
            {"Beige",new int[] {245,245,220,255}},
            {"Bisque",new int[] {255,228,196,255}},
            {"Black",new int[] {0,0,0,255}},
            {"BlanchedAlmond",new int[] {255,235,205,255}},
            {"Blue",new int[] {0,0,255,255}},
            {"BlueViolet",new int[] {138,43,226,255}},
            {"Brown",new int[] {165,42,42,255}},
            {"BurlyWood",new int[] {222,184,135,255}},
            {"CadetBlue",new int[] {95,158,160,255}},
            {"Chartreuse",new int[] {127,255,0,255}},
            {"Chocolate",new int[] {210,105,30,255}},
            {"Coral",new int[] {255,127,80,255}},
            {"CornflowerBlue",new int[] {100,149,237,255}},
            {"Cornsilk",new int[] {255,248,220,255}},
            {"Crimson",new int[] {220,20,60,255}},
            {"Cyan",new int[] {0,255,255,255}},
            {"DarkBlue",new int[] {0,0,139,255}},
            {"DarkCyan",new int[] {0,139,139,255}},
            {"DarkGoldenrod",new int[] {184,134,11,255}},
            {"DarkGray",new int[] {169,169,169,255}},
            {"DarkGreen",new int[] {0,100,0,255}},
            {"DarkKhaki",new int[] {189,183,107,255}},
            {"DarkMagenta",new int[] {139,0,139,255}},
            {"DarkOliveGreen",new int[] {85,107,47,255}},
            {"DarkOrange",new int[] {255,140,0,255}},
            {"DarkOrchid",new int[] {153,50,204,255}},
            {"DarkRed",new int[] {139,0,0,255}},
            {"DarkSalmon",new int[] {233,150,122,255}},
            {"DarkSeaGreen",new int[] {143,188,143,255}},
            {"DarkSlateBlue",new int[] {72,61,139,255}},
            {"DarkSlateGray",new int[] {47,79,79,255}},
            {"DarkTurquoise",new int[] {0,206,209,255}},
            {"DarkViolet",new int[] {148,0,211,255}},
            {"DeepPink",new int[] {255,20,147,255}},
            {"DeepSkyBlue",new int[] {0,191,255,255}},
            {"DimGray",new int[] {105,105,105,255}},
            {"DodgerBlue",new int[] {30,144,255,255}},
            {"FireBrick",new int[] {178,34,34,255}},
            {"FloralWhite",new int[] {255,250,240,255}},
            {"ForestGreen",new int[] {34,139,34,255}},
            {"Fuchsia",new int[] {255,0,255,255}},
            {"Gainsboro",new int[] {220,220,220,255}},
            {"GhostWhite",new int[] {248,248,255,255}},
            {"Gold",new int[] {255,215,0,255}},
            {"Goldenrod",new int[] {218,165,32,255}},
            {"Gray",new int[] {128,128,128,255}},
            {"Green",new int[] {0,128,0,255}},
            {"GreenYellow",new int[] {173,255,47,255}},
            {"Honeydew",new int[] {240,255,240,255}},
            {"HotPink",new int[] {255,105,180,255}},
            {"IndianRed",new int[] {205,92,92,255}},
            {"Indigo",new int[] {75,0,130,255}},
            {"Ivory",new int[] {255,255,240,255}},
            {"Khaki",new int[] {240,230,140,255}},
            {"Lavender",new int[] {230,230,250,255}},
            {"LavenderBlush",new int[] {255,240,245,255}},
            {"LawnGreen",new int[] {124,252,0,255}},
            {"LemonChiffon",new int[] {255,250,205,255}},
            {"LightBlue",new int[] {173,216,230,255}},
            {"LightCoral",new int[] {240,128,128,255}},
            {"LightCyan",new int[] {224,255,255,255}},
            {"LightGoldenrodYellow",new int[] {250,250,210,255}},
            {"LightGreen",new int[] {144,238,144,255}},
            {"LightGrey",new int[] {211,211,211,255}},
            {"LightPink",new int[] {255,182,193,255}},
            {"LightSalmon",new int[] {255,160,122,255}},
            {"LightSeaGreen",new int[] {32,178,170,255}},
            {"LightSkyBlue",new int[] {135,206,250,255}},
            {"LightSlateGray",new int[] {119,136,153,255}},
            {"LightSteelBlue",new int[] {176,196,222,255}},
            {"LightYellow",new int[] {255,255,224,255}},
            {"Lime",new int[] {0,255,0,255}},
            {"LimeGreen",new int[] {50,205,50,255}},
            {"Linen",new int[] {250,240,230,255}},
            {"Magenta",new int[] {255,0,255,255}},
            {"Maroon",new int[] {128,0,0,255}},
            {"MediumAquamarine",new int[] {102,205,170,255}},
            {"MediumBlue",new int[] {0,0,205,255}},
            {"MediumOrchid",new int[] {186,85,211,255}},
            {"MediumPurple",new int[] {147,112,219,255}},
            {"MediumSeaGreen",new int[] {60,179,113,255}},
            {"MediumSlateBlue",new int[] {123,104,238,255}},
            {"MediumSpringGreen",new int[] {0,250,154,255}},
            {"MediumTurquoise",new int[] {72,209,204,255}},
            {"MediumVioletRed",new int[] {199,21,133,255}},
            {"MidnightBlue",new int[] {25,25,112,255}},
            {"MintCream",new int[] {245,255,250,255}},
            {"MistyRose",new int[] {255,228,225,255}},
            {"Moccasin",new int[] {255,228,181,255}},
            {"NavajoWhite",new int[] {255,222,173,255}},
            {"Navy",new int[] {0,0,128,255}},
            {"OldLace",new int[] {253,245,230,255}},
            {"Olive",new int[] {128,128,0,255}},
            {"OliveDrab",new int[] {107,142,35,255}},
            {"Orange",new int[] {255,165,0,255}},
            {"OrangeRed",new int[] {255,69,0,255}},
            {"Orchid",new int[] {218,112,214,255}},
            {"PaleGoldenrod",new int[] {238,232,170,255}},
            {"PaleGreen",new int[] {152,251,152,255}},
            {"PaleTurquoise",new int[] {175,238,238,255}},
            {"PaleVioletRed",new int[] {219,112,147,255}},
            {"PapayaWhip",new int[] {255,239,213,255}},
            {"PeachPuff",new int[] {255,218,185,255}},
            {"Peru",new int[] {205,133,63,255}},
            {"Pink",new int[] {255,192,203,255}},
            {"Plum",new int[] {221,160,221,255}},
            {"PowderBlue",new int[] {176,224,230,255}},
            {"Purple",new int[] {128,0,128,255}},
            {"Red",new int[] {255,0,0,255}},
            {"RosyBrown",new int[] {188,143,143,255}},
            {"RoyalBlue",new int[] {65,105,225,255}},
            {"SaddleBrown",new int[] {139,69,19,255}},
            {"Salmon",new int[] {250,128,114,255}},
            {"SandyBrown",new int[] {244,164,96,255}},
            {"SeaGreen",new int[] {46,139,87,255}},
            {"Seashell",new int[] {255,245,238,255}},
            {"Sienna",new int[] {160,82,45,255}},
            {"Silver",new int[] {192,192,192,255}},
            {"SkyBlue",new int[] {135,206,235,255}},
            {"SlateBlue",new int[] {106,90,205,255}},
            {"SlateGray",new int[] {112,128,144,255}},
            {"Snow",new int[] {255,250,250,255}},
            {"SpringGreen",new int[] {0,255,127,255}},
            {"SteelBlue",new int[] {70,130,180,255}},
            {"Tan",new int[] {210,180,140,255}},
            {"Teal",new int[] {0,128,128,255}},
            {"Thistle",new int[] {216,191,216,255}},
            {"Tomato",new int[] {255,99,71,255}},
            {"Turquoise",new int[] {64,224,208,255}},
            {"Violet",new int[] {238,130,238,255}},
            {"Wheat",new int[] {245,222,179,255}},
            {"White",new int[] {255,255,255,255}},
            {"WhiteSmoke",new int[] {245,245,245,255}},
            {"Yellow",new int[] {255,255,0,255}},
            {"YellowGreen",new int[] {154,205,50,255}}
        };

#if MACOS
        public static CGColor GetColor(String colorName)
#elif WINDOWS
        public static Color GetColor(String colorName)
#endif
        {
            if (Names.ContainsKey(colorName))
            {
                int[] colorValue = Names[colorName];
#if MACOS
                return new CGColor((nfloat)colorValue[0] / (nfloat)255, (nfloat)colorValue[1] / (nfloat)255, (nfloat)colorValue[2] / (nfloat)255, (nfloat)colorValue[3] / (nfloat)255);
#elif WINDOWS
                return Color.FromArgb((byte)colorValue[3], (byte)colorValue[0], (byte)colorValue[1], (byte)colorValue[2]);
#endif
            }
            else
            {
#if MACOS
                return new CGColor(0, 0, 0, 0);
#elif WINDOWS
                return Color.FromArgb(0, 0, 0, 0);
#endif
            }
        }

        public static String[] GetKey()
        {
            String[] keys = new string[Names.Count];
            Names.Keys.CopyTo(keys, 0);
            return keys;
        }
    }
}
