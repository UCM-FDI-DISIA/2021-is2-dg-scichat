package utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import logic.Cell;
import utils.GeoComp.Line;
import utils.GeoComp.Point;

public class GeoCompTest {
    private List<GeoComp.Point> points;

    @BeforeEach
    void init() {
	points = new ArrayList<>();
        points.add(new GeoComp.Point(0,6));
        points.add(new GeoComp.Point(4,0));
        points.add(new GeoComp.Point(4,12));
        points.add(new GeoComp.Point(12,0));
        points.add(new GeoComp.Point(12,12));
        points.add(new GeoComp.Point(16,6));
    }

    @Test
    void PoligonoTest() {
	Assertions.assertDoesNotThrow(() -> {
	    new GeoComp.Poligono(points);
	});
    }

    @Test
    void AreaTest() {
	double area = (new GeoComp.Poligono(points)).convexHull().area();
	System.out.println("Max sparse: "+ area);
	Assertions.assertEquals(144.0, area);
    }
    
    @Test
    void DistanceTest() {
	double dist = (new Cell(0,6,null)).getDistanceBetween(new Cell(16,6,null));
	System.out.println("Max distance: "+dist);
    }
    
    @Test
    void DiameterSymmetry() {
	GeoComp.Point a01 = new GeoComp.Point(4,0);
	GeoComp.Point a02 = new GeoComp.Point(4,12);
	GeoComp.Point a03 = new GeoComp.Point(5,1);
	GeoComp.Point a04 = new GeoComp.Point(5,12);
	GeoComp.Point a05 = new GeoComp.Point(6,1);
	GeoComp.Point a06 = new GeoComp.Point(10,1);
	GeoComp.Point a07 = new GeoComp.Point(11,1);
	GeoComp.Point a08 = new GeoComp.Point(11,12);
	GeoComp.Point a09 = new GeoComp.Point(12,0);
	GeoComp.Point a10 = new GeoComp.Point(12,12);
	GeoComp.Line line = new GeoComp.Line(
		new GeoComp.Point(0,6),
		new GeoComp.Point(16,6)
	);
	double symmetry = 0;
	symmetry += line.dist(a01);
	symmetry += line.dist(a02);
	symmetry += line.dist(a03);
	symmetry += line.dist(a04);
	symmetry += line.dist(a05);
	symmetry += line.dist(a06);
	symmetry += line.dist(a07);
	symmetry += line.dist(a08);
	symmetry += line.dist(a09);
	symmetry += line.dist(a10);
	System.out.println("Max symmetry: "+symmetry);
    }
    
    @Test
    void CounterClockWiseTest() {
	GeoComp.Point a = new GeoComp.Point(1,1);
	GeoComp.Point b = new GeoComp.Point(10,1);
	GeoComp.Point c = new GeoComp.Point(1,10);
	
	Assertions.assertTrue((new GeoComp.Segment(a, b)).counterClockWise(c));
	Assertions.assertFalse((new GeoComp.Segment(a, c)).counterClockWise(b));
	Assertions.assertFalse((new GeoComp.Segment(a, c)).counterClockWise(c));
    }
}
