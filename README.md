# gdx-ssplayer

SpriteStudioのlibGDX用プレイヤーです。

HTML5版をlibGDX用に移植したものです。
使い方も制限事項も同じです。

あらかじめJSONファイルを生成しておきます。

コンバータは以下のところにあります。
https://github.com/SpriteStudio/Ss5ConverterToSSAJSON/wiki



#構成

サンプルの起動は
DesktopLauncher.java

コメントでSsSpriteを利用したものとSsImageを切り替えれるようにしてあります。

SsSpriteはHTML5と同様版のものです。

SsImageはScene2Dで利用可能なActorです。基本的にこちらを利用するとよいでしょう。
