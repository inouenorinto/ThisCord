//ゲームのサイズ(実際の表示にかかわらず、ゲーム内はこのサイズで動く)
const GameW = 480;
const GameH = 800;

//プレイヤーの最初の位置
const PlayerDefX = 75;
const PlayerDefY = 60

//最初のシーンクラスをつくる(無くても動かす方法はあるが、この方があとが楽)
class Main extends Phaser.Scene {
    constructor() {
        super('Main');//これがこのシーンの名前になる
    }

    //最初に一回呼ばれる(画像をロードしたりする)
    preload() {
        this.load.spritesheet('Player', 'player.png', { frameWidth: 150, frameHeight: 116 });
        this.load.spritesheet('biribiri', 'biribiri.png', { frameWidth: 171, frameHeight: 50 });
        this.load.image('miss', 'miss.png');
        this.load.audio('birisnd', 'birisnd.mp3');
        this.load.image('checker', 'checker.png');
    }
    //最初に一回呼ばれる(必要なオブジェクトを作ったりする)
    create() {
        let Player = this.physics.add.sprite(PlayerDefX, PlayerDefY, 'Player');//座標は画像

        let PlayerAnime = {
            key: "PlayerAnime",
            frames: this.anims.generateFrameNumbers("Player", {
                start: 0,
                end: 10,
                first: 0
            }),
            frameRate: 50,
            repeat: 0//お試し用
        };
        this.anims.create(PlayerAnime);
        // Player.play("PlayerAnime");//お試し用


        //ドラッグできるようにする
        Player.setInteractive({ draggable: true })

        Player.NowDrag = false;
        //ドラッグスタート
        Player.on('dragstart', function (pointer, dragX, dragY) {
            Player.NowDrag = true;
        }, this);

        //ドラッグ中
        Player.on('drag', function (pointer, dragX, dragY) {
            if (!Player.NowDrag)
                return;
            let BeforeX = Player.x;

            Player.setPosition(dragX, dragY);
            let Move = Player.x - BeforeX;
            if (Math.abs(Move) > 2) {
                if (Move < 0)
                    Player.setFlipX(true);
                else
                    Player.setFlipX(false);

            }

            Player.play("PlayerAnime", true);

        }, this);

        //ドラッグ後
        Player.on('dragend', function (pointer, dragX, dragY, dropped) {
            Player.NowDrag = false;
            this.ResetPos(Player);
        }, this);

        //ビリビリ用のアニメーションつくる
        let BiriAnime = {
            key: "BiriAnime",
            frames: this.anims.generateFrameNumbers("biribiri", {
                start: 0,
                end: 1,
                first: 0
            }),
            frameRate: 15,
            repeat: -1
        };
        this.anims.create(BiriAnime);

        //仮設置
        // let biribiri = this.physics.add.sprite(200, 400, 'biribiri');//座標は中心
        // biribiri.play(BiriAnime);//アニメーション再生
        let biribiri = this.physics.add.group();
        //1段目
        biribiri.create(85, 180, "biribiri");
        biribiri.create(85 + 171, 180, "biribiri").setFlipX(true);
        //2段目
        biribiri.create(85 + GameW - 171, 180 + 240, "biribiri").setFlipX(true);
        biribiri.create(85 + GameW - 171 - 171, 180 + 240, "biribiri");
        //3段目
        biribiri.create(85 + 0, 180 + 240 + 240, "biribiri");
        biribiri.create(85 + 171, 180 + 240 + 240, "biribiri").setFlipX(true);
        //createはbiribiriを指定しなくても作れるが、当たり判定が変になる

        biribiri.playAnimation("BiriAnime");

        this.physics.add.collider(Player, biribiri, this.Miss, null, this);
        //グループでもうごく、最後のthisを送ると、関数内でthisがこのシーンになる

        let missObj = this.add.image(GameW, GameH / 2, 'miss');
        //当たり判定しないのでphysicsつかない

        missObj.x += missObj.width / 2;//画面外

        let MissTimeLine = this.tweens.createTimeline();
        MissTimeLine.add({
            targets: missObj,
            x: GameW / 2,
            y: GameH / 2,
            ease: Phaser.Math.Easing.Back.Out,
            duration: 200
        });
        MissTimeLine.add({
            targets: missObj,
            x: 0 - missObj.width / 2,
            y: 400,
            ease: Phaser.Math.Easing.Back.In,
            duration: 200,
            delay: 500
        });
        MissTimeLine.loop = -1;//無限ループ
        MissTimeLine.on("loop", function () {
            MissTimeLine.pause();
        });
        //プレイヤーの中にいれこんで、ほかでも使える様にする(JavaScript独特の書き方)
        Player.MissTimeLine = MissTimeLine;

        let checker = this.physics.add.image(GameW - 50, GameH - 50, 'checker');//座標は中心
        checker.setBodySize(50, 50);//当たり判定のサイズをかえる
        checker.depth = -1;//描画順を一番下に
        this.physics.add.overlap(Player, checker, this.Goal, null, this);//コツンとしない当たり判定、最後のthisを送ると、関数内でthisがこのシーンになる
        this.scene.pause();
        this.scene.launch('Start');
    }
    Miss(Jibun, Aite) {
        Jibun.x = PlayerDefX;
        Jibun.y = PlayerDefY;

        Jibun.NowDrag = false;
        this.ResetPos(Jibun);

        if (Jibun.MissTimeLine.state == 20 && Jibun.MissTimeLine.paused) {//初回かどうか
            Jibun.MissTimeLine.play();
        }
        else {//初回以外
            //再生途中は一旦終わらせる
            if (Jibun.MissTimeLine.progress != 0)
                Jibun.MissTimeLine.nextState();
            Jibun.MissTimeLine.resume();//ポーズから復帰
        }
        this.sound.play("birisnd");
        console.log("ミス");
    }

    ResetPos(Jibun) {
        Jibun.x = PlayerDefX;
        Jibun.y = PlayerDefY;
        Jibun.setFlipX(false);
        Jibun.NowDrag = false;
    }

    Goal(Jibun, Aite) {
        console.log("ゴール");
        this.scene.pause();
        this.scene.launch('Goal');//別のシーンをうえに乗っける
    }

}

class Goal extends Phaser.Scene {
    constructor() {
        super('Goal');//これがこのシーンの名前になる
    }

    //最初に一回呼ばれる(画像をロードしたりする)
    preload() {
        this.load.image('goal', 'goal.png');
    }
    //最初に一回呼ばれる(必要なオブジェクトを作ったりする)
    create() {
        let goalimg = this.physics.add.sprite(GameW / 2, GameH / 2, 'goal');//座標は中心

        this.input.on("pointerdown", function (event) {
            console.log("restart");
            this.scene.start('Main');
        }, this);
    }
}

class Start extends Phaser.Scene {
    constructor() {
        super('Start');//これがこのシーンの名前になる
    }

    //最初に一回呼ばれる(画像をロードしたりする)
    preload() {
        this.load.image('start', 'start.png');
    }
    //最初に一回呼ばれる(必要なオブジェクトを作ったりする)
    create() {
        let shikaku = this.add.graphics();
        shikaku.fillStyle(0x000000, 0.5);
        shikaku.fillRect(0, 0, GameW, GameH);

        let startimg = this.physics.add.sprite(GameW / 2, GameH / 2, 'start');//座標は中心

        this.input.on("pointerdown", function (event) {
            console.log("start");
            this.scene.stop('Start');
            this.scene.run('Main');
        }, this);//thisを送るとコールバック内でthisがこのシーンになる

    }
}

const config = {
    type: Phaser.AUTO,
    backgroundColor: '#000000',
    parent: "",
    width: GameW,
    height: GameH,
    physics: {
        default: 'arcade',
        arcade: {
            gravity: { y: 0 } // 重力
        }
    },
    scale: {
        mode: Phaser.Scale.FIT,
        autoCenter: Phaser.Scale.CENTER_BOTH
    },
    scene: [Main, Goal, Start]
};
const game = new Phaser.Game(config);
