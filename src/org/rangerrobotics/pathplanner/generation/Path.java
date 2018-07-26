package org.rangerrobotics.pathplanner.generation;

import org.rangerrobotics.pathplanner.gui.PathEditor;

import java.util.ArrayList;

public class Path {
    public double length;
    private ArrayList<Double> x = new ArrayList<>();
    private ArrayList<Double> y = new ArrayList<>();
    private ArrayList<Double> l = new ArrayList<>();
    private SegmentGroup inGroup;
    public SegmentGroup group = new SegmentGroup();
    private PathEditor editor;

    public Path(SegmentGroup s, PathEditor editor){
        inGroup = s;
        this.editor = editor;
        makePath();
    }

    private void makePath(){
        long start = System.currentTimeMillis();
        System.out.println("Generating Path...");
        makeScaledLists();
        calculateLength();
        createSegments();
        System.out.println("DONE IN: " + (System.currentTimeMillis() - start) + " ms");
    }

    private void makeScaledLists(){
        for(int i = 0; i < inGroup.segments.size(); i++){
            x.add((inGroup.segments.get(i).x-editor.plannedPath.get(0).getX() - PlannedPath.xPixelOffset)/PlannedPath.pixelsPerFoot);
            y.add((inGroup.segments.get(i).y-editor.plannedPath.get(0).getY() - PlannedPath.yPixelOffset)/PlannedPath.pixelsPerFoot);
        }
    }

    private double calculateLength(){
        System.out.print("    Calculating Length... ");
        long start = System.currentTimeMillis();
        for(int i = 1; i < x.size(); i++){
            double dx = x.get(i) - x.get(i - 1);
            double dy = y.get(i) - y.get(i - 1);
            double c2 = (dx*dx) + (dy*dy);
            double c = Math.sqrt(c2);
            length += c;

            double prevLength = 0;
            if(i != 1){
                prevLength = l.get(l.size() - 1);
            }
            l.add(c + prevLength);
        }
        System.out.println("Length: " + length + " ft, Time: " + (System.currentTimeMillis() - start) + " ms");
        return length;
    }

    private void createSegments(){
        System.out.print("    Calculating Segments... ");
        long start = System.currentTimeMillis();
        for(int i = 0; i < x.size() - 1; i++){
            int s = i;
            int s2 = i+1;
            Segment seg = new Segment();
            seg.x = x.get(s);
            seg.y = y.get(s);
            seg.pos = l.get(s);
            seg.dydx = derivative(s, s2);
            if(i != 0){
                seg.dx = seg.pos - group.segments.get(group.segments.size() - 1).pos;
            }
            group.segments.add(seg);
        }
        System.out.println("Created " + group.segments.size() + " Segments. Time: " + (System.currentTimeMillis() - start) + " ms");
    }

    private double derivative(int t1, int t2){
        return Util.slope(new Vector2(x.get(t1), y.get(t1)), new Vector2(x.get(t2), y.get(t2)));
    }
}
