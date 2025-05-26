package app.services;

public class SVG {
    private static final String SVG_TEMPLATE = "<svg xmlns=\"http://www.w3.org/2000/svg\"\n" +
            "         x=\"%d\"\n" +
            "         y=\"%d\"\n" +
            "         width=\"%s\"\n" +
            "         height=\"%s\"\n" +
            "         viewBox=\"%s\"\n" +
            "         preserveAspectRatio=\"xMinYMin\">";


    private static final String SVG_ARROW_DEFS = "  <defs>\n" +
            "            <marker\n" +
            "                    id=\"beginArrow\"\n" +
            "                    markerWidth=\"12\"\n" +
            "                    markerHeight=\"12\"\n" +
            "                    refX=\"0\"\n" +
            "                    refY=\"6\"\n" +
            "                    orient=\"auto\">\n" +
            "                <path d=\"M0,6 L12,0 L12,12 L0,6\" style=\"fill: #000000;\" />\n" +
            "            </marker>\n" +
            "            <marker\n" +
            "                    id=\"endArrow\"\n" +
            "                    markerWidth=\"12\"\n" +
            "                    markerHeight=\"12\"\n" +
            "                    refX=\"12\"\n" +
            "                    refY=\"6\"\n" +
            "                    orient=\"auto\">\n" +
            "                <path d=\"M0,0 L12,6 L0,12 L0,0 \" style=\"fill: #000000;\" />\n" +
            "            </marker>\n" +
            "        </defs>";

    private static final String SVG_RECT_TEMPLATE = "<rect x=\"%d\" y=\"%d\" height=\"%d\" width=\"%d\" style=\"%s\"/>";

    private static final String SVG_LINE_TEMPLATE = "<line x1=\"%d\"  y1=\"%d\" x2=\"%d\"   y2=\"%d\"\n" +
            "              style=\"%s\n" +
            " marker-start: url(#beginArrow);\n" +
            " marker-end: url(#endArrow);\" />";

    private static final String SVG_ARROW_TEMPLATE = "<marker\n" +
            "                    id=\"%s\"\n" +
            "                    markerWidth=\"%d\"\n" +
            "                    markerHeight=\"%d\"\n" +
            "                    refX=\"%d\"\n" +
            "                    refY=\"%d\"\n" +
            "                    orient=\"auto\">\n" +
            "                <path d=\"%s\" style=\"fill: #000000;\" />\n" +
            "            </marker>";

    private static final String SVG_TEXT_TEMPLATE = "<text style=\"text-anchor: middle\" transform=\"translate(%d,%d) rotate(%d)\">%s</text>";

    private StringBuilder svg = new StringBuilder();

    public SVG(int x, int y, String viewBox, String width, String height){
        svg.append(String.format(SVG_TEMPLATE, x, y, width, height, viewBox));
        svg.append(SVG_ARROW_DEFS);
        String lineStyle = "stroke:black; marker-start: url(#beginArrow);";


    }
    public void addRectangle(int x, int y, int height, int width, String style){
        svg.append(String.format(SVG_RECT_TEMPLATE, x, y, height, width, style));
    }
    public void addLine(int x1, int y1, int x2, int y2, String style){
        svg.append(String.format(SVG_LINE_TEMPLATE, x1, y1, x2, y2, style));
    }
    public void addArrow(int x1, int y1, int x2, int y2, String style){
        addLine(x1, y1, x2, y2, style);

    }
    public void addText(int x, int y, int rotation, String text){
        svg.append(String.format(SVG_TEXT_TEMPLATE, x, y, rotation, text));
    }
    public void addSvg(SVG innerSvg){
        svg.append(innerSvg.toString());
    }

    @Override
    public String toString(){
        return svg.append("</svg>").toString();
    }


}

