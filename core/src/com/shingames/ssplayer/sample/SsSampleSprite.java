package com.shingames.ssplayer.sample;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shingames.ssplayer.SsAnimation;
import com.shingames.ssplayer.SsImageList;
import com.shingames.ssplayer.SsJsonData;
import com.shingames.ssplayer.SsSprite;
import java.io.IOException;

/**
 * SsSpriteを直接利用するサンプル
 * @author shin
 */
public class SsSampleSprite extends ApplicationAdapter {

    AssetManager assetManager;
    SpriteBatch batch;
    SsImageList ssImageList;
    SsAnimation ssAnimation;
    SsSprite ssSprite;
    long time;
    

    @Override
    public void resize(int width, int height) {
        System.out.println("size="+width+","+height);
        ssSprite.setPosition(width/2, height/2);//画面中央に
    }
    
    @Override
    public void create() {
        System.out.println("create");
        Gdx.app.setLogLevel(Logger.DEBUG);
        
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        
        String jsonName = "datas/animetest.json";
        String root = "datas";
        
//        String jsonName = "newsplash/newsplash256.json";
//        String root = "./newsplash/";

//        String jsonName = "charatemplate1/character_template1.json";
//        String root = "charatemplate1/";
        
        try {
            String json = Gdx.files.internal(jsonName).readString("UTF-8");
    
            ObjectMapper om = new ObjectMapper();
            SsJsonData jsonData = om.readValue(json, SsJsonData.class);
            int animeIndex = 0;
            
            ssImageList = new SsImageList(assetManager, jsonData.get(animeIndex).images, root);
            ssAnimation = new SsAnimation(jsonData.get(animeIndex).animation, ssImageList);
            
            ssSprite = new SsSprite(ssAnimation);
//ssSprite.step = 0.5f;
//ssSprite.rootScaleX = 0.125f;
//ssSprite.rootScaleY = 0.125f;
        } catch (IOException ex) {
            Gdx.app.error("SSP", "load error", ex);
        }
        
    }

    @Override
    public void render() {
        //時間経過
        time += Gdx.graphics.getDeltaTime()*1000;
        
        Gdx.gl.glClearColor(0, 0, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        
        ssSprite.draw(batch, time);//アニメ描画

        batch.end();
        
    }

    @Override
    public void dispose() {
        batch.dispose();
        
        if(ssImageList != null){
            ssImageList.dispose();
        }
        assetManager.dispose();
        
    }
}
