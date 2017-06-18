package application;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MediaBar extends HBox {
	Slider time = new Slider(); // 建立時間軸
	Slider vol = new Slider();// 建立音量軸
	Slider bal = new Slider();// 建立聲道平衡軸
	Slider sliderDuration; // 持續時間
	Button replayButton = new Button("重播");// 建立重播按鈕
	Button playButton = new Button("||");// 建立播放按鈕
	// Button fullButton = new Button("全螢幕");// 建立全螢幕按鈕
	Label volume = new Label("Volume:");// 建立音量標籤
	Label balance = new Label("Balance:");// 建立平衡標籤
	Stage primaryStage;
	MediaPlayer player;// 建立播放器

	public MediaBar(MediaPlayer play) {
		player = play;
		setAlignment(Pos.CENTER); // 使功能在Hbox中對齊
		setPadding(new Insets(5, 10, 5, 10));
		vol.setPrefWidth(100);// 設置首選音量軸寬度
		vol.setMinWidth(50);// 最小音量軸寬度
		vol.setMaxWidth(Region.USE_PREF_SIZE);// 最大音量軸寬度，若不符合布局，返回首選寬度
		bal.setPrefWidth(60);// 設置首選平衡軸寬度
		bal.setMinWidth(30);// 最小平衡軸寬度
		bal.setMaxWidth(Region.USE_PREF_SIZE);// 最大平衡軸寬度，若不符合布局，返回首選寬度
		vol.setValue(100);// 初始音量大小
		bal.setMin(-1.0);// 平衡聲道最小值
		bal.setMax(1.0);// 平衡聲道最大值
		bal.setValue(0.0);// 平衡聲道初始值
		playButton.setPrefWidth(50);// 播放按鈕寬度
		// fullButton.setPrefWidth(55);// 全螢幕按鈕寬度
		HBox.setHgrow(time, Priority.ALWAYS);// 優先分配時間軸的空間
		getChildren().add(replayButton);
		getChildren().add(playButton);
		// getChildren().add(fullButton);
		getChildren().add(time);
		getChildren().add(volume);
		getChildren().add(vol);
		getChildren().add(balance);
		getChildren().add(bal);
		// 加入各個按鈕及滑動軸

		playButton.setOnAction(new EventHandler<ActionEvent>() { // 處理playButton按鈕事件
			public void handle(ActionEvent e) {// 取得音訊的總時間
				Status status = player.getStatus();// 取得當前播放狀態
				if (status == Status.PLAYING) { // 若正在播放
					if (player.getCurrentTime().greaterThanOrEqualTo(player.getTotalDuration())) {

						player.seek(player.getStartTime());

						player.play();

					} else {

						player.pause();

						playButton.setText(">");
					}
				}
				if (status == Status.PAUSED || status == Status.HALTED || status == Status.STOPPED) { // 若狀態為暫停或錯誤或停止

					player.play();

					playButton.setText("||");
				}
			}
		});

		replayButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e) {

				final Duration currentduration = Duration.ZERO;// 設定目前的進度時間為零

				if (player.getStatus() == MediaPlayer.Status.STOPPED) {// 取得音訊目前的狀態

					player.pause();
				}

				player.seek(currentduration); // 移至音訊的最前端

				if (player.getStatus() != MediaPlayer.Status.PLAYING) {// 取得音訊目前的狀態

					if (sliderDuration.isValueChanging())

						return;

					final Duration total = player.getTotalDuration(); // 讀取總播放時間

					if (total == null) {

						sliderDuration.setValue(0); // 調整滑動軸的位置

					} else {

						sliderDuration.setValue(currentduration.toMillis() / total.toMillis()); // 調整滑動軸的位置

					}
				}
			}
		});

		player.currentTimeProperty().addListener(new InvalidationListener() {// 當前播放進度

			public void invalidated(Observable ov) {

				updateValues();
			}
		});
		time.valueProperty().addListener(new InvalidationListener() { // 處理滑動時間軸可調整播放進度

			public void invalidated(Observable ov) {

				if (time.isPressed()) {

					player.seek(player.getMedia().getDuration().multiply(time.getValue() / 100));
				}
			}
		});

		vol.valueProperty().addListener(new InvalidationListener() {

			public void invalidated(Observable ov) { // 處理滑動音量軸可調整音量

				if (vol.isPressed()) {

					player.setVolume(vol.getValue() / 100);
				}
			}
		});

		bal.valueProperty().addListener(new InvalidationListener() {

			public void invalidated(Observable ov) {// 設定音訊的聲道平衡

				if (bal.isValueChanging()) {
					player.setBalance(bal.getValue() / 10);

				}
			}
		});
	}

	protected void updateValues() {// 跟著進度調整時間軸的位置

		Platform.runLater(new Runnable() {

			public void run() {

				time.setValue(player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis() * 100);
			}
		});
	}
}