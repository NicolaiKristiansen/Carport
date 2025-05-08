package app.services;

public class CarportSVG {
    private SVG svgDrawing;
    private int width;
    private int length;

    public CarportSVG(int width, int length){
        this.svgDrawing = new SVG(0, 0, "0 0 855 690", "100%", "auto");
        this.width = width;
        this.length = length;
        addBeams();
        addRafters();
    }

    @Override
    public String toString() {
        return svgDrawing.toString();
    }

    private void addBeams(){
        svgDrawing.addRectangle(0,35,405,780, "stroke:black; fill:white");
        svgDrawing.addRectangle(0,565,405,780, "stroke:black; fill:white");
    }
    private void addRafters(){
        for(int i = 0; i < 780; i = i + 55){
            svgDrawing.addRectangle(i, 0, 600, 405, "stroke:black; fill:white");
        }
    }
    private void addPost(){}
}
