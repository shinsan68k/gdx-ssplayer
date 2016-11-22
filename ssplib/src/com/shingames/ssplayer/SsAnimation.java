package com.shingames.ssplayer;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.NumberUtils;
import com.badlogic.gdx.utils.ObjectIntMap;

/**
 * アニメーション情報を保持する
 * @author shin
 */
public class SsAnimation {

    private final SsJsonData.SsaData ssaData;

    private final SsImageList imageList;
//    private Batch animationBatch;
    private ObjectIntMap<String> partsMap = new ObjectIntMap<>();
    private String[] parts;

    private Sprite sp ;
    
    static final int iPartNo = 0;
    static final int iImageNo = 1;
    static final int iSouX = 2;
    static final int iSouY = 3;
    static final int iSouW = 4;
    static final int iSouH = 5;
    static final int iDstX = 6;
    static final int iDstY = 7;
    static final int iDstAngle = 8;
    static final int iDstScaleX = 9;
    static final int iDstScaleY = 10;
    static final int iOrgX = 11;
    static final int iOrgY = 12;
    static final int iFlipH = 13;
    static final int iFlipV = 14;
    static final int iAlpha = 15;
    static final int iBlend = 16;

//    static final int iColor = 27;//左上頂点カラー
    
    /**
     * コンストラクタ
     * @param ssaData
     * @param ssImageList 
     */
    public SsAnimation(SsJsonData.SsaData ssaData, SsImageList ssImageList) {
        this.ssaData = ssaData;
        this.imageList = ssImageList;

        this.parts = ssaData.parts;
        for (int i = 0; i < this.parts.length; i++) {
            this.partsMap.put(this.parts[i], i);
        }
        
//        animationBatch = new SpriteBatch();
        sp = new Sprite();
    }

    /**
     * 描画メソッド
     *
     * @param batch
     * @param frameNo
     * @param x
     * @param y
     * @param flipH 未実装
     * @param flipV 未実装
     * @param partStates
     * @param rootScaleX
     * @param rootScaleY
     */
    public void drawFunc(Batch batch, int frameNo, float x, float y, boolean flipH, boolean flipV, Array<SsPartState> partStates, float rootScaleX, float rootScaleY) {
        batch.end();
        
        Matrix4 oldBatch = batch.getTransformMatrix().cpy();
        batch.begin();

        float dx = 0, dy = 0;
        int partNo = 0;

        float[][] frameData = this.ssaData.ssa[frameNo];

        
        for (float[] partData : frameData) {
            int partDataLen = partData.length;

            partNo = (int) partData[iPartNo];
            Texture img = this.imageList.getImage((int) partData[iImageNo]);
            int sx = (int) partData[iSouX];
            int sy = (int) partData[iSouY];
            int sw = (int) partData[iSouW];
            int sh = (int) partData[iSouH];
            dx = partData[iDstX] * rootScaleX;
            dy = partData[iDstY] * rootScaleY;

            float vdw = sw;
            float vdh = sh;

            dx += x;
            dy += y;

            if (sw > 0 && sh > 0) {

                float dang = partData[iDstAngle];
                float scaleX = partData[iDstScaleX];
                float scaleY = partData[iDstScaleY];

                float ox = (partDataLen > iOrgX) ? partData[iOrgX] : 0;
                float oy = (partDataLen > iOrgY) ? partData[iOrgY] : 0;
                float fh = (partDataLen > iFlipH) ? (partData[iFlipH] != 0 ? -1 : 1) : (1);
                float fv = (partDataLen > iFlipV) ? (partData[iFlipV] != 0 ? -1 : 1) : (1);
                float alpha = (partDataLen > iAlpha) ? partData[iAlpha] : 1.0f;
                int blend = (int) ((partDataLen > iBlend) ? partData[iBlend] : 0);
//                float color = NumberUtils.intBitsToFloat((partDataLen > iColor) ? (int)partData[iColor] : 0xffffffff);
                

                switch(blend){
                    default://ミックス
                        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                        break;
//                        case 1://乗算
//                            break;
                    case 2://加算
                        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
                        break;
//                        case 3://減算
//                            break;
                }
                

                Matrix4 m4 = new Matrix4(oldBatch);
                m4.translate(dx, -dy, 0);
                m4.rotateRad(0, 0, 1, dang);
                m4.scale(scaleX*rootScaleX, scaleY*rootScaleY, 1);
                m4.translate(-ox + vdw / 2, -(-oy + vdh / 2), 0);
                batch.setTransformMatrix(m4);

                
                sp.setTexture(img);
                sp.setOrigin(sw/2, sh/2);
                sp.setRegion(sx, sy, sw, sh);
                sp.setSize(Math.abs(sw), Math.abs(sh));
//                sp.setColor(color);
                sp.setAlpha(alpha);
                sp.setPosition(-vdw / 2, -vdh / 2);
                sp.setFlip(fh < 0, fv < 0);
                sp.draw(batch);
            }
        }

        SsPartState state = partStates.get(partNo);
        state.x = dx;
        state.y = dy;

        batch.end();
        
        batch.setTransformMatrix(oldBatch);
        batch.begin();
    }

    /**
     * このアニメーションのFPS
     * This animation FPS.
     * 
     * @return 
     */
    public int getFPS() {
        return ssaData.fps;
    }

    /**
     * トータルフレーム数を返す
     *
     * @return
     */
    public int getFrameCount() {
        return this.ssaData.ssa.length;
    }

    /**
     * パーツリストを返す
     * Get parts list.
     * 
     * @return 
     */
    public String[] getParts() {
        return this.ssaData.parts;
    }

    /**
     * パーツ名からNoを取得するマップを返す
     * Return the map, to get the parts from number.
     * 
     * @return 
     */
    public ObjectIntMap<String> getPartsMap() {
        return partsMap;
    }

    /**
     * キャンバスの幅を取得
     * @return 
     */
    public int getCanvasWidth(){
        return this.ssaData.CanvasWidth;
    }
    
    /**
     * キャンバスの高さを取得
     * @return 
     */
    public int getCanvasHeight(){
        return this.ssaData.CanvasHeight;
    }
    
    /**
     * マージンの幅を取得
     * @return 
     */
    public int getMarginWidth(){
        return this.ssaData.MarginWidth;
    }
    
    /**
     * マージンの高さを取得
     * @return 
     */
    public int getMarginHeight(){
        return this.ssaData.MarginHeight;
    }
    
}
