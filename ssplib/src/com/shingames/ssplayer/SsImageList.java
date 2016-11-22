
package com.shingames.ssplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

/**
 * 画像を管理する
 * @author shin
 */
public class SsImageList implements Disposable{
    private Texture[] textures;
    private final AssetManager assetManager;
    private final String[] imageFiles;
    private final String fileRoot;

    /**
     * コンストラクタ。画像のロードが終わるまでブロックする
     * @param assetManager ロードに利用するアセットマネージャ
     * @param imageFiles
     * @param fileRoot アセットのパス
     */
    public SsImageList(AssetManager assetManager, String[] imageFiles, String fileRoot) {
        this.assetManager = assetManager;
        this.imageFiles = imageFiles;
        this.fileRoot = fileRoot;
        textures = new Texture[imageFiles.length];
        
        for(int i=0;i<textures.length;i++){
            FileHandle file = Gdx.files.internal(fileRoot).child(imageFiles[i]);
            Gdx.app.debug("SSP", "load texture:"+file.path());
            assetManager.load(file.path(), Texture.class);
            assetManager.finishLoading();
            
            textures[i] = assetManager.get(file.path());
            textures[i].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
    }

    /**
     * 利用が終わったらメモリを開放する
     */
    @Override
    public void dispose() {
        int i=0;
        for(String name : imageFiles ){
            FileHandle file = Gdx.files.internal(fileRoot).child(imageFiles[i]);
            assetManager.unload(file.path());
            Gdx.app.debug("SSP", "unload texture:" + file.path());
            i++;
        }
    }

    /**
     * 指定したインデックスのImageを返す
     * Get image at specified index.
     * 
     * @param index
     * @return 
     */
    public Texture getImage(int index) {
	if (index < 0 || index >= this.textures.length) return null;
	return this.textures[index];
    }
    
    
}
