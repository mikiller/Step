package com.westepper.step.responses;

import android.app.Activity;
import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;

import java.util.List;

/**
 * Created by Mikiller on 2017/9/30.
 */

public class Graphics {
    private final int NORMAL_FILL = Color.parseColor("#6600A8FF"), NORMAL_STROKE = Color.parseColor("#D2D2D2");
    private final int ACHEIVE_FILL_REACHED = Color.parseColor("#8800cca3"), ACHEIVE_FILL = Color.parseColor("#66a0bed2");
    public static final int MAP = 1, ACHEIVE = 2;
    private int graphicsType;
    private Polygon polygon;
    private Circle circle;

    public Circle getCircle() {
        return circle;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public Graphics(AMap aMap, boolean reached, List<LatLng>borderList){
        polygon = aMap.addPolygon(new PolygonOptions().addAll(borderList));
        setGraphicsReached(reached);
    }

    public Graphics(AMap aMap, boolean reached, LatLng latLng, int radius){
        circle = aMap.addCircle(new CircleOptions().center(latLng).radius(radius));
        setGraphicsReached(reached);
    }

    public void setGraphicsReached(boolean reached){
        if(polygon != null)
            setPolygonReached(reached);
        else if(circle != null)
            setCircleReached(reached);
    }

    private void setPolygonReached(boolean reached){
        if(reached){
            polygon.setStrokeWidth(0f);
            polygon.setFillColor(NORMAL_FILL);
        }else{
            polygon.setStrokeWidth(4);
            polygon.setStrokeColor(NORMAL_STROKE);
            polygon.setFillColor(Color.TRANSPARENT);
        }
    }

    private void setCircleReached(boolean reached){
        if(reached){
            circle.setStrokeWidth(0f);
            circle.setFillColor(NORMAL_FILL);
        }else{
            circle.setStrokeWidth(4);
            circle.setStrokeColor(NORMAL_STROKE);
            circle.setFillColor(Color.TRANSPARENT);
        }
    }

    public void setGraphicsType(int type){
        graphicsType = type;

    }

    public void setReached(boolean reached){
        if(graphicsType == MAP){
            setGraphicsReached(reached);
        }else if(graphicsType == ACHEIVE){
            setGraphicsAcheiveReached(reached);
        }
    }

    public void setGraphicsAcheiveReached(boolean reached){
        if(polygon != null)
            setPolygonAcheiveReached(reached);
        else if(circle != null)
            setCircleAcheiveReached(reached);
    }

    private void setPolygonAcheiveReached(boolean reached){
        if(reached){
            polygon.setStrokeWidth(0);
            polygon.setFillColor(ACHEIVE_FILL_REACHED);
        }else{
            polygon.setStrokeWidth(0);
            polygon.setFillColor(ACHEIVE_FILL);
        }
    }

    private void setCircleAcheiveReached(boolean reached){
        if(reached){
            circle.setStrokeWidth(0);
            circle.setFillColor(ACHEIVE_FILL_REACHED);
        }else{
            circle.setStrokeWidth(0);
            circle.setFillColor(ACHEIVE_FILL);
        }
    }

    public void hide(boolean reached){
        if(polygon != null)
            hidePolygon(reached);
        else if(circle != null)
            hideCircle(reached);
    }

    public void show(boolean reached){
        if(polygon != null)
            showPolygon(reached);
        else if(circle != null)
            showCircle(reached);
    }

    private void hidePolygon(boolean reached){
        if(reached){
            polygon.setFillColor(Color.TRANSPARENT);
        }else{
            polygon.setStrokeWidth(0);
            if(graphicsType == ACHEIVE)
                polygon.setFillColor(Color.TRANSPARENT);
        }
    }

    private void showPolygon(boolean reached){
        if(reached){
            if(graphicsType == MAP)
                polygon.setFillColor(NORMAL_FILL);
            else if(graphicsType == ACHEIVE){
                polygon.setFillColor(ACHEIVE_FILL_REACHED);
            }
        }else{
            if(graphicsType == MAP) {
                polygon.setStrokeWidth(4);
            }else if(graphicsType == ACHEIVE){
                polygon.setFillColor(ACHEIVE_FILL);
            }
        }
    }

    private void hideCircle(boolean reached){
        if(reached){
            circle.setFillColor(Color.TRANSPARENT);
        }else{
            circle.setStrokeWidth(0);
            if(graphicsType == ACHEIVE)
                circle.setFillColor(Color.TRANSPARENT);
        }
    }

    private void showCircle(boolean reached){
        if(reached){
            if(graphicsType == MAP)
                circle.setFillColor(NORMAL_FILL);
            else if(graphicsType == ACHEIVE)
                circle.setFillColor(ACHEIVE_FILL_REACHED);
        }else{
            if(graphicsType == MAP) {
                circle.setStrokeWidth(4);
            }else if(graphicsType == ACHEIVE){
                circle.setFillColor(ACHEIVE_FILL);
            }
        }
    }
}
