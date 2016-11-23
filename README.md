# gdx-ssplayer

SpriteStudioのlibGDX用プレイヤーです。

HTML5版をlibGDX用に移植したものです。
使い方も制限事項も同じです。

あらかじめJSONファイルを生成しておきます。

コンバータは以下のところにあります。

https://github.com/SpriteStudio/Ss5ConverterToSSAJSON/wiki



##構成

libGDXのデフォルト構成をベースに作られています。

* desktop:サンプルのランチャー
* core:サンプル
* ssplib:ライブラリ本体

##ライブラリの説明

* SsImageList、SsAnimation、SsSpriteはHTML5版と同様のものです。
* SsImageはScene2Dで利用可能なActorです。SsSpriteなどをラップしています。基本的にこちらを利用するとよいでしょう。

##サンプル起動

サンプルの起動は DesktopLauncher.java のmainを実行してください。

コメントでSsSpriteを利用したものとSsImageを切り替えれるようにしてあります。


##ライセンス

このソフトウェアを利用したことによる損害などいかなる保証もしません。


SpriteStudioは、株式会社ウェブテクノロジの登録商標です。
