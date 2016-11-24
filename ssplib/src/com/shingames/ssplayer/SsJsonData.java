
package com.shingames.ssplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shingames.ssplayer.SsJsonData.AnimationData;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

/**
 * JSONマッピング用
 * @author shin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SsJsonData extends ArrayList<AnimationData>{
    
    /**
     * アニメーション名で検索
     * @param name
     * @return null:見つからなかった
     */
    @JsonIgnore
    public AnimationData findByName(String name){
        if(name == null){
            return null;
        }
        if(name.isEmpty()){
            return null;
        }
        
        for(AnimationData row : this){
            if(name.equals(row.name)){
                return row;
            }
        }
        throw new RuntimeException("not found:" + name);//見つからなかった
    }
    
    /**
     * JSONを読み込む。ファイル名が.gzipまたは.gzで終わっていたならばgzip圧縮とみなし展開する。
     * @param file
     * @return 
     * @throws java.io.IOException 
     */
    public static SsJsonData create(FileHandle file) throws IOException{
        Gdx.app.debug("SSP", "load file :" + file.path());
        
        String json;
        
        if(file.name().endsWith(".gz") || file.name().endsWith(".gzip")){
            ByteArrayOutputStream out = new ByteArrayOutputStream(128*1024);
            GZIPInputStream in = new GZIPInputStream(file.read(64*1024));
            byte[] buf = new byte[32*1024];
            while(true){
                int len = in.read(buf);
                if(len < 0){
                    break;
                }
                out.write(buf, 0, len);
            }
            json = out.toString("UTF-8");
            
        }else{
        
            json = file.readString("UTF-8");
        }

        ObjectMapper om = new ObjectMapper();
        SsJsonData jsonData = om.readValue(json, SsJsonData.class);
        
        return jsonData;
    }
    
    /**
     * アニメーション情報と画像情報
     */
    public static class AnimationData {
        public String[] images;
        public String name;
        public SsaData animation;
    }
    
    /**
     * アニメデータ本体
     */
    public static class SsaData {
        public int fps;
        public int CanvasWidth;
        public int CanvasHeight;
        public int MarginWidth;
        public int MarginHeight;
        public String[] parts;
        public float[][][] ssa;
    }

    
}
