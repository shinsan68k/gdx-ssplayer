
package com.shingames.ssplayer.scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.shingames.ssplayer.SsAnimation;
import com.shingames.ssplayer.SsSprite;

/**
 * scene2Dで利用するもの
 * @author shin
 */
public class SsImage extends Group{

    protected final SsAnimation animation;
    protected long time;
    protected SsSprite sprite;
    
    /**
     * 
     * @param animation 
     */
    public SsImage(SsAnimation animation) {
        this.animation = animation;
        
        this.sprite = new SsSprite(animation);
        
        this.sprite.setPosition(animation.getMarginWidth(), animation.getMarginHeight());
        setSize(animation.getCanvasWidth(), animation.getCanvasHeight());
        setOrigin(Align.center);
    }

    
    @Override
    public void act(float delta) {
        super.act(delta);
        
        time += delta * 1000;
    }
    

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        sprite.draw(batch, time);
    }


    
}
