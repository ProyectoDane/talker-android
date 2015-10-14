package ar.uba.fi.talker.component;

import android.graphics.Point;
import android.os.Parcel;

public class PencilPoint extends Point {
    public boolean initial = false;
    public boolean end = false;

    public PencilPoint() {
        super();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeBooleanArray(new boolean[]{initial, end});
    }

    @Override
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        boolean[] temp = new boolean[2];
        in.readBooleanArray(temp);
        initial = temp[0];
        end = temp[1];
    }

    public static final Creator<PencilPoint> CREATOR = new Creator<PencilPoint>() {

        @Override
        public PencilPoint createFromParcel(Parcel source) {
            PencilPoint dao = new PencilPoint();
            dao.readFromParcel(source);
            return dao;
        }

        @Override
        public PencilPoint[] newArray(int size) {
            return new PencilPoint[size];
        }
    };
}