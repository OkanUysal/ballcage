package uysal.okan.ballcage.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetDescriptors {

    public static final AssetDescriptor<BitmapFont> EFFECT_FONT =
            new AssetDescriptor<BitmapFont>(AssetPaths.EFFECT_FONT, BitmapFont.class);

    public static final AssetDescriptor<BitmapFont> EFFECT_FONT2 =
            new AssetDescriptor<BitmapFont>(AssetPaths.EFFECT_FONT2, BitmapFont.class);

    public static final AssetDescriptor<TextureAtlas> BG =
            new AssetDescriptor<TextureAtlas>(AssetPaths.BG, TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> BLOCKS =
            new AssetDescriptor<TextureAtlas>(AssetPaths.BLOCKS, TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> BALLS =
            new AssetDescriptor<TextureAtlas>(AssetPaths.BALLS, TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> GRAVELBALLS =
            new AssetDescriptor<TextureAtlas>(AssetPaths.GRAVELBALLS, TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas>[] BALLSVAR = new AssetDescriptor[]{BALLS, GRAVELBALLS};

    public static final AssetDescriptor<TextureAtlas> FLOORS =
            new AssetDescriptor<TextureAtlas>(AssetPaths.FLOORS, TextureAtlas.class);

    public static final AssetDescriptor<Skin> UISKIN =
            new AssetDescriptor<Skin>(AssetPaths.UISKIN, Skin.class);

    public static final AssetDescriptor<TextureAtlas> BUTTONS =
            new AssetDescriptor<TextureAtlas>(AssetPaths.BUTTONS, TextureAtlas.class);


}
