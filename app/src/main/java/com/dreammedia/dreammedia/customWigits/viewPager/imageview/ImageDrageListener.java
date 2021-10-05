package com.dreammedia.dreammedia.customWigits.viewPager.imageview;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public  class ImageDrageListener implements View.OnTouchListener {

        private float rotationAngle;
        private Matrix matrix = new Matrix();
        private Matrix savedMatrix = new Matrix();
        // We can be in one of these 3 states
        static final int NONE = 0;
        static final int DRAG = 1;
        static final int ZOOM = 2;
        int mode = NONE;


        // Remember some things for zooming
        PointF start = new PointF();
        PointF mid = new PointF();
        float oldDist = 1f;

    float startX, startY, currentX, currentY;
        ImageView iv;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            iv = (ImageView) v;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());

                    startX = event.getX();
                    startY = event.getY();

                    mode = DRAG;

                    break;
                case MotionEvent.ACTION_POINTER_DOWN:

                    rotationAngle = rotation(event);
                    oldDist = spacing(event);
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;

                    break;
                case MotionEvent.ACTION_UP:

                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;

                    break;

                case MotionEvent.ACTION_MOVE:

                    currentX = event.getX();
                    currentY = event.getY();

                    if (Math.abs(Math.abs(startX) - Math.abs(currentX)) > 10
                            || Math.abs(Math.abs(startY) - Math.abs(currentY)) > 10) {

                    }

                    if (mode == DRAG) {
                        matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                    } else if (mode == ZOOM) {

                        float newDist = spacing(event);

                        matrix.set(savedMatrix);
                        if (newDist > 10f) {
                            float scale = newDist / oldDist;
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }

                        // code for rotation

                        float newAngle = rotation(event);
                        float r = newAngle - rotationAngle;
                        matrix.postRotate(r, iv.getMeasuredWidth() / 2, iv.getMeasuredHeight() / 2);

                        // end of rotation

                    }
                    break;
            }

            ((ImageView) v).setImageMatrix(matrix);
            return true; // indicate event was handled
        }

        /**
         * Determine the space between the first two fingers
         */
        private float spacing(MotionEvent event) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);

        }

        /**
         * Calculate the mid point of the first two fingers
         */
        private void midPoint(PointF point, MotionEvent event) {
            float x = event.getX(0) + event.getX(1);
            float y = event.getY(0) + event.getY(1);
            point.set(x / 2, y / 2);
        }

        private float rotation(MotionEvent event) {
            double delta_x = (event.getX(0) - event.getX(1));
            double delta_y = (event.getY(0) - event.getY(1));
            double radians = Math.atan2(delta_y, delta_x);
            return (float) Math.toDegrees(radians);

        }
    }
