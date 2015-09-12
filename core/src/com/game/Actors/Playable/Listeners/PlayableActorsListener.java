package com.game.Actors.Playable.Listeners;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.game.Actors.Playable.Products.PlayableActor;
import com.game.Actors.Field;
import com.game.Controllers.ActorsController;
import com.game.Controllers.WorldController;

public class PlayableActorsListener extends DragListener {

    private Field field;
    private ActorsController actorsController;
    private WorldController worldController;
    private PlayableActor draggedActor;
    private float rightFieldEdge,leftFieldEdge,topFieldEdge,bottomFieldEdge;
    private float originalActorsWidth, originalActorsHeight, actorsNewWidth, actorsNewHeight;
    private int actorCellIndexBeforeMovementX,actorCellIndexBeforeMovementY;

    public PlayableActorsListener(PlayableActor draggedActor, WorldController worldController){
        this.draggedActor=draggedActor;
        this.field=worldController.getActorsController().getField();
        this.worldController=worldController;
        this.actorsController = worldController.getActorsController();
        rightFieldEdge =field.getCoordinates().getRightFieldEdge();
        leftFieldEdge=field.getCoordinates().getLeftFieldEdge();
        topFieldEdge=field.getCoordinates().getTopFieldEdge();
        bottomFieldEdge = field.getCoordinates().getBottomFieldEdge();
        originalActorsWidth=draggedActor.getWidth();
        originalActorsHeight=draggedActor.getHeight();
        float actorsSizeIncrease=0.3f;
        actorsNewWidth = originalActorsWidth*(1+actorsSizeIncrease); // actor's size after increase
        actorsNewHeight = originalActorsHeight*(1+actorsSizeIncrease);
    }

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        if (worldController.getTurn().isPlayerTurn()) { // if this is a turn of player not AI
            super.touchDown(event, x, y, pointer, button);
            draggedActor.toFront();
            float actorPossitionX = event.getListenerActor().getX();
            float actorPossitionY = event.getListenerActor().getY();
            float actorCenterPossitionX = actorPossitionX + event.getListenerActor().getWidth() / 2;
            float actorCenterPossitionY = actorPossitionY + event.getListenerActor().getWidth() / 2;
            int IndexX = findXCellIndex(actorCenterPossitionX);
            int IndexY = findYCellIndex(actorCenterPossitionY);
            actorCellIndexBeforeMovementX = IndexX;
            actorCellIndexBeforeMovementY = IndexY;
            draggedActor.getPosition().cellIndexX = IndexX;
            draggedActor.getPosition().cellIndexY = IndexY;
            draggedActor.setSize(actorsNewWidth, actorsNewHeight);
            x = fitActorToTouchCenterX(x);
            y = fitActorToTouchCenterY(y);
            displayAvailableForMovementCells();
            draggedActor.moveBy(x, y);
            return true;
        } else return false;
    }

    @Override
    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);
        actorsController.clearAvailableForMovementCellsArray();
        draggedActor.setSize(originalActorsWidth, originalActorsHeight);
        float actorPositionX=event.getListenerActor().getX();
        float actorPositionY=event.getListenerActor().getY();
        float actorCenterPositionX=actorPositionX+event.getListenerActor().getWidth()/2;
        float actorCenterPositionY=actorPositionY+event.getListenerActor().getWidth()/2;
        int IndexX=findXCellIndex(actorCenterPositionX);
        int IndexY=findYCellIndex(actorCenterPositionY);
        int newCellIndex=field.getCoordinates().getCellIndexByXYIndexes(IndexX, IndexY);
        int oldCellIndex=field.getCoordinates().getCellIndexByXYIndexes(actorCellIndexBeforeMovementX,actorCellIndexBeforeMovementY);
        boolean flag=false;
        if (!movementFacilitiesCheck(IndexX,IndexY)){
            newCellIndex=oldCellIndex;
        } else {
            worldController.getTurn().endPlayerTurn(); //  this is possible to move and we end up the turn of player
            field.getCellByIndex(oldCellIndex).setActorRef(null); // as well we set reference to actor's old cell as null
            field.getCellByIndex(newCellIndex).setActorRef(draggedActor); // and we set reference to new actor's cell
            flag=true;
        }
        x = fitActorToCellCenterX(newCellIndex);
        y = fitActorToCellCenterY(newCellIndex);
        draggedActor.getPosition().cellIndexX =IndexX;
        draggedActor.getPosition().cellIndexY =IndexY;
        draggedActor.setPosition(x, y);
        if (flag) worldController.getTurn().startAITurn(); //after player complete his turn we allow AI to move
    }

    @Override
    public void dragStart (InputEvent event, float x, float y, int pointer) {
    }

    @Override
    public void dragStop (InputEvent event, float x, float y, int pointer) {
    }

    // x, y - coordinates delta
    @Override
    public void drag (InputEvent event, float x, float y, int pointer) {
        x=fieldEdgeCheckX(x, event.getListenerActor().getX(), event.getListenerActor().getWidth());
        y=fieldEdgeCheckY(y, event.getListenerActor().getY(), event.getListenerActor().getHeight());
        x= fitActorToTouchCenterX(x);
        y= fitActorToTouchCenterY(y);
        draggedActor.moveBy(x, y);
    }

    // t - actor's coordinates before drag operation
    private float fieldEdgeCheckX(float x, float t, float width){
        if ((x+t)>rightFieldEdge-width/2) x=rightFieldEdge-t-width/2;
        if ((x+t)<leftFieldEdge+width/2) x=leftFieldEdge-t+width/2;
        return x;
    }

    private float fieldEdgeCheckY(float y, float t, float height){
        if ((y+t)>topFieldEdge-height/2) y=topFieldEdge-t-height/2;
        if ((y+t)<bottomFieldEdge+height/2) y=bottomFieldEdge-t+height/2;
        return y;
    }

    private float fitActorToTouchCenterX(float x){
        x=x - draggedActor.getWidth() / 2;
        return x;
    }

    private float fitActorToTouchCenterY(float y){
        y=y - draggedActor.getWidth() / 2;
        return y;
    }

    private float fitActorToCellCenterX(int cellIndex){
        float x=field.getCellByIndex(cellIndex).getcX();
        x=fitActorToTouchCenterX(x);
        return x;
    }

    private float fitActorToCellCenterY(int cellIndex){
        float y=field.getCellByIndex(cellIndex).getcY();
        y =fitActorToTouchCenterY(y);
        return y;
    }

    private int findXCellIndex(float actorPositionX){
        int i;
        for (i=1;i<=field.getFeildSizeX() ;i++ ){
            float cell0blX=field.getCellByIndex(0).getbLX();
            float length = field.getCellWidth()*(i-1);
            if (actorPositionX>cell0blX+length) ;
            else break;
        }
        return i-1;
    }

    private int findYCellIndex(float actorPositionY){
        int j;
        for (j = 1;j<=field.getFeildSizeY() ;j++ ){
            float cell0blY=field.getCellByIndex(0).getbLY();
            float length = field.getCellWidth()*(j-1);
            if (actorPositionY > cell0blY+length);
            else break;
        }
        return j-1;
    }

    private boolean movementFacilitiesCheck(int IndexX,int IndexY){
        int oldPositionX=draggedActor.getPosition().cellIndexX;
        int oldPositionY=draggedActor.getPosition().cellIndexY;
        int newPositionX=IndexX;
        int newPositionY=IndexY;
        int R=draggedActor.getMovingFacilities().R;
        int T=draggedActor.getMovingFacilities().T;
        int B=draggedActor.getMovingFacilities().B;
        int L=draggedActor.getMovingFacilities().L;
        int TR=draggedActor.getMovingFacilities().TR;
        int TL=draggedActor.getMovingFacilities().TL;
        int BR=draggedActor.getMovingFacilities().BR;
        int BL=draggedActor.getMovingFacilities().BL;

        boolean X=false;
        boolean Y=false;
        boolean XY=false;
        boolean YX=false;
        boolean flag=false;
        if (oldPositionX==newPositionX && oldPositionY==newPositionY) return false;
        if(((IndexX-oldPositionX>0)&&(IndexX-oldPositionX<=R))||((oldPositionX-IndexX>0)&&(oldPositionX-IndexX<=L)))X=true;
        if(((IndexY-oldPositionY>0)&&(IndexY-oldPositionY<=T))||((oldPositionY-IndexY>0)&&(oldPositionY-IndexY<=B)))Y=true;
        if ((((oldPositionX-IndexX>0)&&(oldPositionX-IndexX<=BL))&&((oldPositionY-IndexY>0)&&(oldPositionY-IndexY<=BL)))||(((IndexX-oldPositionX>0)&&(IndexX-oldPositionX<=TR))&&((IndexY-oldPositionY>0)&&(IndexY-oldPositionY<=TR))))XY=true;
        if ((((oldPositionX-IndexX>0)&&(oldPositionX-IndexX<=TL))&&((IndexY-oldPositionY>0)&&(IndexY-oldPositionY<=TL)))||(((IndexX-oldPositionX>0)&&(IndexX-oldPositionX<=BR))&&((oldPositionY-IndexY>0)&&(oldPositionY-IndexY<=BR))))YX=true;
        if ((field.getCellByIndex(field.getCoordinates().getCellIndexByXYIndexes(IndexX,IndexY)).getActorRef()==null)) {
            if (((X) && (IndexY - oldPositionY == 0)) || ((Y) && (IndexX - oldPositionX == 0)) || ((XY) && (Math.abs(newPositionX - oldPositionX) == Math.abs(newPositionY - oldPositionY))) || ((YX) && (Math.abs(newPositionX - oldPositionX) == Math.abs(newPositionY - oldPositionY))))
                flag = true;
        }
        return flag;
    }

    private void displayAvailableForMovementCells(){
        Field.Cell cell;
        for (int i=0; i<field.getCoordinates().getFieldSize(); i++){
            cell=field.getCellByIndex(i);
            if(movementFacilitiesCheck(cell.getIndexX(), cell.getIndexY())){
                actorsController.drawAvailableForMovementCells(cell.getcX(), cell.getcY());
            }
        }
    }

}
