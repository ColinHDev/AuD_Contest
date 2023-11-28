package com.gatdsen.animation;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Class that represents the camera of our {@link Animator}
 * Has functions used for handling/accepting movement commands and jumping between targets.
 */
public class AnimatorCamera extends OrthographicCamera {

    //Todo Comment and refactor/clean up this class
    private final float defaultZoomValue = 1f;
    private final float zoomIncrement = 0.01f;
    private final Vector3 defaultPosition;
    private Vector3 cameraDirection;
    private float cameraSpeed;
    private Vector3 targetPosition = new Vector3(0,0,0);

    private Interpolation cameraInterpolation = Interpolation.linear;
    private int lifetime = 2;
    private float elapsed = lifetime;

    //1 = zoom out, 0 = no zoom, -1 = zoom in
    private float cameraZoomPressed;

    private boolean canMoveToVector;
    public AnimatorCamera(float viewportWidth, float viewportHeight) {
        super(viewportWidth,viewportHeight);
        this.defaultPosition = new Vector3(258,258,0);
        this.cameraDirection = new Vector3(0,0,0);
        this.cameraSpeed = 200;
        this.canMoveToVector = true;

    }

     /**
     * Moves the camera with the Speed defined in {@link AnimatorCamera#cameraSpeed} and the {@link AnimatorCamera#cameraDirection}.
     * Also calculates the lerp-movement towards a defined target.
      * This function is supposed to be called repeatedly. (e.g. inside a render() call)
     * @param delta Delta-time of the Application.
     */
    private void processMoveInput(float delta) {

        this.translate(new Vector3(cameraDirection).scl(cameraSpeed * delta));



       //linear interpolation to make a smooth transition towards a target position
        //the target position ist often reached with  0.7 so it will stop there
        //this way the camera will not be unable to move from other sources
       if(elapsed<=0.7) {
           elapsed += delta;
           float progress = Math.min(1f, elapsed / lifetime);
           float alpha = cameraInterpolation.apply(progress);

           this.position.lerp(targetPosition, alpha);
       }
    }
    private void processZoomInput(float delta){
        if(this.zoom > 0.1||cameraZoomPressed>0) {
            this.zoom += zoomIncrement * cameraZoomPressed;
        }

    }

    /**
     * Adjusts zoom by adding the value (in terms of percent).
     * E.g. float of 10 would add 10 percent of zoom.
     * @param zoomChange
     */
    public void addZoomPercent(float zoomChange){
        if(this.zoom+zoomChange<0.1){
            this.zoom = 0.1f;
        }
       else {
            this.zoom += zoomChange;
        }
    }

    /**
     * Resets the cameras current zoom and position to the defined defaults.
     */
    public void resetCamera(){
        this.zoom = defaultZoomValue;
        this.position.set(defaultPosition);
    }

    /**
     * Continuously adjusts the position of the camera with the values of the input Array.
     * [{x-Movement},{y-Movement}
     * @param ingameCameraDirection
     */
    public void setDirections(float[] ingameCameraDirection) {

             //left*-cameraSpeed+right*cameraSpeed,up*cameraSpeed+down*-cameraSpeed,0
            //for later camera.position.lerp() for smooth movement
        if(ingameCameraDirection.length ==3) {
            this.cameraDirection = new Vector3(ingameCameraDirection);
        }
        else{
           System.err.println("Length of the camera direction Array needs to be 3!");
        }
    }

    public void updateMovement(float delta) {
       processMoveInput(delta);
       processZoomInput(delta);

    }

    public void setZoomPressed(float zoomPressed) {
        this.cameraZoomPressed = zoomPressed;
    }

    /**
     * Determines, whether the camer can be moved by {@link AnimatorCamera#moveToVector}
     * @param move
     */
    public void setCanMoveToVector(boolean move){
        canMoveToVector = move;
    }

    public Vector2 getScreenCenter() {
        return new Vector2(position.x,position.y);
    }

    public void moveToVector(Vector2 position){

        if(canMoveToVector){

            targetPosition = new Vector3(position,0);

            elapsed = 0;

        }
    }

    /**
     * Moves the camera by a certain offset-Vector once.
     * @param offset
     */
    public void moveByOffset(Vector2 offset){
         translate(-offset.x,-offset.y,0);
    }

    public boolean getCanMoveToVector() {
        return canMoveToVector;
    }

    public void toggleCanMoveToVector(){
        canMoveToVector  = !canMoveToVector;
    }
}
