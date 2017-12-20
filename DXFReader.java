import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DXFReader {
    private String fileName;
    private BufferedReader br;

    DXFReader(String fileName) {
        this.fileName = fileName;
    }

    public Entities getEntities() {
        Entities entities = new Entities();
        try {
            FileInputStream fstream = new FileInputStream(fileName);
            br = new BufferedReader(new InputStreamReader(fstream));

            String sCode = "";
            String sValue = "";

            while (true) {
                if (goToNextSection()) {
                    if (goToNextEntitySection()) {
                        while (!sValue.equals("ENDSEC")) {
                            sCode = getNextLine();
                            sValue = getNextLine();
                            if (sCode == null || sValue == null) {
                                break;
                            }

                            if (sCode.equals(DXFConstants.CODE_ENTITY)) {
                                Entity entity = getEntity(sValue);
                                if (entity != null) {
                                    entities.add(entity);
                                }
                            }
                        }
                    }
                }
                else {
                    break;
                }
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return entities;
    }

    private String getNextLine() {
        try {
            String line = br.readLine();
            if (line == null) {
                return null;
            }
            return line = line.trim();
        }
        catch (IOException e) {
            return null;
        }
    }

    private boolean goToNextSection() {
        String sCode = "";
        String sValue = "";

        while (!sValue.equals("ENDSEC")) {
            sCode = getNextLine();
            sValue = getNextLine();
            if (sCode == null || sValue == null) {
                break;
            }

            if (sCode.equals("0") && sValue.equals("SECTION")) {
                return true;
            }
        }
        return false;
    }

    private boolean goToNextEntitySection() {
        String sCode = "";
        String sValue = "";

        while (!sValue.equals("ENDSEC")) {
            sCode = getNextLine();
            sValue = getNextLine();
            if (sCode == null || sValue == null) {
                break;
            }

            if (sCode.equals("2") && sValue.equals("ENTITIES")) {
                return true;
            }
        }
        return false;
    }

    private Entity getEntity(String entityName) {
        if (entityName.equals(DXFConstants.VALUE_CIRCLE)) {
            return getCircleOrArc();
        }
        if (entityName.equals(DXFConstants.VALUE_LWPOLYLINE)) {
            return getLWPolyline();
        }
        if (entityName.equals(DXFConstants.VALUE_LINE)) {
            return getLine();
        }
        return null;
    }

    private double dxfValueToDouble(String value) {
        return Double.parseDouble(value);
    }

    private Entity getCircleOrArc() {
        boolean isArc = false;
        String sCode = "";
        String sValue = "";
        double x0 = 0;
        double y0 = 0;
        double radius = 0;
        double angleStart = 0;
        double angleEnd = 0;
        do {
            sCode = getNextLine();
            sValue = getNextLine();

            if (sCode.equals(DXFConstants.CODE_X_START)) {
                x0 = dxfValueToDouble(sValue);
            }
            if (sCode.equals(DXFConstants.CODE_Y_START)) {
                y0 = dxfValueToDouble(sValue);
            }
            if (sCode.equals(DXFConstants.CODE_RADIUS)) {
                radius = dxfValueToDouble(sValue);
            }
            if (sCode.equals(DXFConstants.CODE_ANGLE_START)) {
                angleStart = dxfValueToDouble(sValue);
                isArc = true;
            }
            if (sCode.equals(DXFConstants.CODE_ANGLE_END)) {
                angleEnd = dxfValueToDouble(sValue);
                isArc = true;
            }
        } while (!(sCode.equals("0") || (sCode == null || sValue == null)));
        if (isArc) {
            return new Arc(x0, y0, radius, angleStart, angleEnd);
        }
        else {
            return new Circle(x0, y0, radius);
        }
    }

    private Entity getLWPolyline() {
        String sCode = "";
        String sValue = "";
        int verticiesCount = 0;
        boolean isPolylineClosed = false;
        ArrayList<Double> xCoords = new ArrayList<Double>();
        ArrayList<Double> yCoords = new ArrayList<Double>();

        do {
            sCode = getNextLine();
            sValue = getNextLine();
            if (sCode == null || sValue == null) {
                break;
            }
            if (sCode.equals(DXFConstants.CODE_NUMBER_OF_VERTICES)) {
                verticiesCount = Integer.parseInt(sValue);
            }
            if (sCode.equals(DXFConstants.CODE_POLYLINE_FLAG)) {
                if (sValue.equals(DXFConstants.VALUE_POLYLINE_CLOSED)) {
                    isPolylineClosed = true;
                }
                else {
                    isPolylineClosed = false;
                }
            }

            if (sCode.equals(DXFConstants.CODE_X_START)) {
                xCoords.add(Double.parseDouble(sValue));
            }
            if (sCode.equals(DXFConstants.CODE_Y_START)) {
                yCoords.add(Double.parseDouble(sValue));
            }
        } while (!sCode.equals("0"));

        assert xCoords.size() == yCoords.size() && xCoords.size() == verticiesCount;

        Entities entities = new Entities();
        for (int i = 0; i < verticiesCount - 1; i++) {
            entities.add(new Line(xCoords.get(i), yCoords.get(i),xCoords.get(i+1), yCoords.get(i+1)));
        }

        if (isPolylineClosed) {
            entities.add(new Line(xCoords.get(xCoords.size()-1), yCoords.get(yCoords.size()-1),xCoords.get(0), yCoords.get(0)));
        }

        return entities;
    }

    private Entity getLine() {
        String sCode = "";
        String sValue = "";
        double x0 = 0;
        double y0 = 0;
        double x1 = 0;
        double y1 = 0;

        do {
            sCode = getNextLine();
            sValue = getNextLine();
            if (sCode == null || sValue == null) {
                break;
            }

            if (sCode.equals(DXFConstants.CODE_X_START)) {
                x0 = Double.parseDouble(sValue);
            }
            if (sCode.equals(DXFConstants.CODE_Y_START)) {
                y0 = Double.parseDouble(sValue);
            }
            if (sCode.equals(DXFConstants.CODE_X_END)) {
                x1 = Double.parseDouble(sValue);
            }
            if (sCode.equals(DXFConstants.CODE_Y_END)) {
                y1 = Double.parseDouble(sValue);
            }
        } while (!sCode.equals("0"));

        return new Line(x0, y0, x1, y1);
    }
}

class DXFConstants {
    //Group codes
    public static final String CODE_ENTITY = "100";
    public static final String CODE_LAYER = "8";
    public static final String CODE_X_START = "10";
    public static final String CODE_Y_START = "20";
    public static final String CODE_X_END = "11";
    public static final String CODE_Y_END = "21";
    public static final String CODE_RADIUS = "40";
    public static final String CODE_BULGE = "42";
    public static final String CODE_ANGLE_START = "50";
    public static final String CODE_ANGLE_END = "51";

    public static final String CODE_NUMBER_OF_VERTICES = "90";
    public static final String CODE_POLYLINE_FLAG = "70";

    public static final String CODE_SPLINE_FLAG = "70";
    public static final String CODE_SPLINE_CURVE_DEGREE = "71";
    public static final String CODE_KNOTS_COUNT = "72";
    public static final String CODE_CONTROL_POINTS_COUNT = "73";
    public static final String CODE_FIT_POINTS_COUNT = "74";
    public static final String CODE_KNOT_TOLERANCE = "42";
    public static final String CODE_CONTROL_POINT_TOLERANCE = "43";
    public static final String CODE_FIT_TOLERANCE = "44";
    public static final String CODE_KNOT_VALUE = "40";

    //Values
    public static final String VALUE_ENTITY = "AcDbEntity";
    public static final String VALUE_CIRCLE = "AcDbCircle";
    public static final String VALUE_LINE = "AcDbLine";
    public static final String VALUE_LWPOLYLINE = "AcDbPolyline";
    public static final String VALUE_SPLINE = "AcDbSpline";
    public static final String VALUE_POLYLINE_CLOSED = "1";
    public static final String VALUE_POLYLINE_PLINEGEN = "128";
}