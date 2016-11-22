package com.shingames.ssplayer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;

/**
 * 座標関係を保持
 * @author shin
 */
public class SsSprite {

    private SsAnimation animation;

    // 描画Xポジション
    // X position at drawing.
    private float x = 0;

    // 描画Yポジション
    // Y position at drawing
    private float y = 0;

    // ※未実装
    // *Not implemented.
    private boolean flipH = false;
    private boolean flipV = false;

    // scale
    private float rootScaleX = 1.0f;
    private float rootScaleY = 1.0f;

    //inner
    private float playingFrame = 0;
    private long prevDrawnTime = 0;
    private float step = 1f;
    private int loop = 0;
    private int loopCount = 0;
    private Runnable endCallBack = null;
    private final Array<SsPartState> partStates = new Array<>();

    /**
     * コンストラクタ
     * @param ssAnimation 
     */
    public SsSprite(SsAnimation ssAnimation) {
        this.animation = ssAnimation;

        this.initPartStates();
        this.playingFrame = 0;
        this.prevDrawnTime = 0;
        this.clearLoopCount();
    }

    //初期化
    private void initPartStates() {
        this.partStates.clear();

        if (this.animation != null) {
            String[] parts = this.animation.getParts();
            for (String part : parts) {
                partStates.add(new SsPartState(part));
            }
        }
    }

    /**
     * アニメーションの設定
     * Set animation.
     * @param animation 
     */
    public void setAnimation(SsAnimation animation) {
        this.animation = animation;
        this.initPartStates();
        this.playingFrame = 0;
        this.prevDrawnTime = 0;
        this.clearLoopCount();
    }

    /**
     * アニメーションの取得
     * Get animation.
     * @return 
     */
    public SsAnimation getAnimation() {
        return this.animation;
    }

    /**
     * 再生フレームNoを設定
     * Set frame no of playing.
     * @param frameNo
     */
    public void setFrameNo(int frameNo) {
        this.playingFrame = frameNo;
        this.prevDrawnTime = 0;
    }

    /**
     * 再生フレームNoを取得
     * Get frame no of playing.
     * @return 
     */
    public int getFrameNo() {
        return (int) this.playingFrame;
    }

    /**
     * ループ回数の設定 (0:無限)
     * Set a playback loop count.  (0:infinite)
     * 
     * @param loop 
     */
    public void setLoop(int loop) {
        if (loop < 0) {
            return;
        }
        this.loop = loop;
    }

    /**
     * ループ回数の設定を取得
     * Get a playback loop count of specified. (0:infinite)
     * @return 
     */
    public int getLoop() {
        return this.loop;
    }

    /**
     * 現在の再生回数を取得
     * Get repeat count a playback.
     * 
     * @return 
     */
    public int getLoopCount() {
        return this.loopCount;
    }

    /**
     * 現在の再生回数をクリア
     * Clear repeat count a playback.
     * 
     */
    public void clearLoopCount() {
        this.loopCount = 0;
    }

    /**
     * パーツの状態（現在のX,Y座標など）を取得
     * Gets the state of the parts. (Current x and y positions)
     * 
     * @param name
     * @return 
     */
    public SsPartState getPartState(String name) {
        if (this.partStates == null) {
            return null;
        }

        ObjectIntMap<String> partsMap = this.animation.getPartsMap();
        int partNo = partsMap.get(name, -1);
        if (partNo < 0) {
            return null;
        }
        return this.partStates.get(partNo);
    }

    /**
     * コールバック
     * @param callback 
     */
    public void setEndCallback(Runnable callback){
        this.endCallBack = callback;
    }
    
    /**
     * 描画実行
     * Drawing method.
     * 
     * @param batch
     * @param currentTime 
     */
    public void draw(Batch batch, long currentTime) {

        if (this.animation == null) {
            return;
        }

        if (this.loop == 0 || this.loop > this.loopCount) {
            // フレームを進める
            // To next frame.
            if (this.prevDrawnTime > 0) {

                float s = (currentTime - this.prevDrawnTime) / (1000f / this.animation.getFPS());
                this.playingFrame += s * this.step;

                int c = (int) (this.playingFrame / this.animation.getFrameCount());

                if (this.step >= 0) {
                    if (this.playingFrame >= this.animation.getFrameCount()) {
                        // ループ回数更新
                        // Update repeat count.
                        this.loopCount += c;
                        if (this.loop == 0 || this.loopCount < this.loop) {
                            // フレーム番号更新、再生を続ける
                            // Update frame no, and playing.
                            this.playingFrame %= this.animation.getFrameCount();
                        } else {
                            // 再生停止、最終フレームへ
                            // Stop animation, to last frame.
                            this.playingFrame = this.animation.getFrameCount() - 1;
                            // 停止時コールバック呼び出し
                            // Call finished callback.
                            if (this.endCallBack != null) {
                                this.endCallBack.run();
                            }
                        }
                    }
                } else {
                    if (this.playingFrame < 0) {
                        // ループ回数更新
                        // Update repeat count.
                        this.loopCount += 1 + -c;
                        if (this.loop == 0 || this.loopCount < this.loop) {
                            // フレーム番号更新、再生を続ける
                            // Update frame no, and playing.
                            this.playingFrame %= this.animation.getFrameCount();
                            if (this.playingFrame < 0) {
                                this.playingFrame += this.animation.getFrameCount();
                            }
                        } else {
                            // 再生停止、先頭フレームへ
                            // Stop animation, to first frame.
                            this.playingFrame = 0;
                            // 停止時コールバック呼び出し
                            // Call finished callback.
                            if (this.endCallBack != null) {
                                this.endCallBack.run();
                            }
                        }
                    }
                }

            }
        }
        this.prevDrawnTime = currentTime;

        
        //描画のコアはアニメ側が持っている
        this.animation.drawFunc(batch, this.getFrameNo(), this.x, -this.y, this.flipH, this.flipV, this.partStates, this.rootScaleX, this.rootScaleY);

    }

    /**
     * アニメの速度を設定する
     * @param speed 1.0f=標準速度
     */
    public void setSpeed(float speed){
        this.step = speed;
    }
    
    /**
     * アニメのサイズ
     * @param scaleX 1.0f=標準サイズ
     * @param scaleY 1.0f=標準サイズ
     */
    public void setScale(float scaleX, float scaleY){
        this.rootScaleX = scaleX;
        this.rootScaleY = scaleY;
    }
    
    /**
     * 表示座標を設定する
     * @param x
     * @param y 
     */
    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }
}
