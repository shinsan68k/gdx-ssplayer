
package com.shingames.ssplayer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shingames.ssplayer.SsJsonData.AnimationData;
import java.util.ArrayList;

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
