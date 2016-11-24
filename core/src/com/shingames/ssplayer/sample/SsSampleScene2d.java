package com.shingames.ssplayer.sample;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.shingames.ssplayer.SsAnimation;
import com.shingames.ssplayer.SsImageList;
import com.shingames.ssplayer.SsJsonData;
import com.shingames.ssplayer.scene2d.SsImage;
import java.io.IOException;

/**
 * Scene2Dを利用したサンプル
 * @author shin
 */
public class SsSampleScene2d extends ApplicationAdapter {

    AssetManager assetManager;
    SsImageList ssImageList;
    SsAnimation ssAnimation;
    SsImage image;
    Stage stage;
    
    

    @Override
    public void resize(int width, int height) {
        System.out.println("size="+width+","+height);
        image.setPosition(width/2, height/2, Align.center);
    }
    
    @Override
    public void create() {
        System.out.println("create");
        Gdx.app.setLogLevel(Logger.DEBUG);
        
        assetManager = new AssetManager();
        
        stage = new Stage();
        
        
        String jsonName = "datas/animetest.json.gz";
        String root = "datas";
        
//        String jsonName = "newsplash/newsplash256.json";
//        String root = "./newsplash/";

//        String jsonName = "charatemplate1/character_template1.json";
//        String root = "charatemplate1/";
        
        try {
            FileHandle file = Gdx.files.internal(jsonName);
            SsJsonData jsonData = SsJsonData.create(file);
            
            for(SsJsonData.AnimationData data : jsonData){
                Gdx.app.debug("SSP", data.name);
            }

            int animeIndex = 0;
            SsJsonData.AnimationData data = jsonData.get(animeIndex);
            
            ssImageList = new SsImageList(assetManager, data.images, root);
            ssAnimation = new SsAnimation(data.animation, ssImageList);
            
            image = new SsImage(ssAnimation);
            
            stage.addActor(image);
//            image.setScale(0.5f);
//            image.setRotation(-45);
            image.debug();
        } catch (IOException ex) {
            Gdx.app.error("SSP", "load error", ex);
            Gdx.app.exit();
        }
        
    }

    @Override
    public void render() {
        
        Gdx.gl.glClearColor(0, 0, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.act();
        stage.draw();
        
    }

    @Override
    public void dispose() {
        
        if(ssImageList != null){
            ssImageList.dispose();
        }
        assetManager.dispose();
        
    }
}
