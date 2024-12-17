# marvspawn
marvsetworldspawn
MarvTeleport
MarvTeleport は、Minecraft サーバー向けの便利なテレポート管理プラグインです。
指定した高度以下に下がったプレイヤーや、サーバーに参加したプレイヤーを安全な座標にテレポートします。

機能
指定高度以下での自動テレポート

プレイヤーが設定した高度以下に下がると、自動的に指定座標にテレポートします。
プレイヤー参加時の自動テレポート

プレイヤーがサーバーに参加すると、設定した座標に自動的にテレポートします。
カスタムスポーン地点設定

コマンドを使って任意の場所をテレポート先として設定できます。
設定の確認とリロード

設定ファイルとメッセージファイルをリロードして、サーバー再起動なしで反映できます。
コマンド
コマンド	説明	権限
/marvteleport setspawn	現在位置をスポーン地点として設定する	marvteleport.use
/marvteleport getspawn	設定されたスポーン地点を表示する	marvteleport.use
/marvteleport reload	設定ファイルとメッセージをリロードする	marvteleport.use
設定ファイル
config.yml
yaml
コードをコピーする
teleport-location:
  world: "world"    # テレポート先のワールド名
  x: 0              # X座標
  y: 64             # Y座標
  z: 0              # Z座標
  yaw: 0            # 向き (ヨー)
  pitch: 0          # 向き (ピッチ)

teleport-threshold: 10  # 高度の閾値。この高度以下でテレポート
message.yml
yaml
コードをコピーする
teleport-message: "§a指定高度を下回ったため、安全な地点へテレポートしました！"
join-teleport-message: "§a参加時に指定された地点にテレポートしました！"
reload-message: "§a設定とメッセージがリロードされました！"
使い方
スポーン地点の設定
テレポート先にしたい場所に移動し、以下を実行します：

bash
コードをコピーする
/marvteleport setspawn
スポーン地点の確認
設定されたスポーン地点を確認するには：

bash
コードをコピーする
/marvteleport getspawn
設定リロード
設定ファイルやメッセージを編集した後、以下を実行して反映：

bash
コードをコピーする
/marvteleport reload
権限
権限名	説明
marvteleport.use	コマンドの実行に必要な権限
インストール方法
MarvTeleport.jar をサーバーの plugins フォルダに配置します。
サーバーを再起動します。
config.yml と message.yml が生成されるので、必要に応じて編集します。
