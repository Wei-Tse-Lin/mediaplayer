package application;

import javafx.beans.binding.Bindings;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class Player extends BorderPane {
	Media media;
	MediaPlayer player;
	MediaView view;
	Pane mpane;
	MediaBar bar;

	public Player(String file) {
		mpane = new Pane(); // 建立視窗
		media = new Media(file);
		player = new MediaPlayer(media);
		view = new MediaView(player);
		mpane.getChildren().add(view);
		setCenter(mpane);
		view.fitWidthProperty().bind(Bindings.selectDouble(view.sceneProperty(), "width"));
		view.fitHeightProperty().bind(Bindings.selectDouble(view.sceneProperty(), "height"));
		// 自動設定解析度
		view.setPreserveRatio(true); // 視窗縮放的時候，影片維持比例
		bar = new MediaBar(player);
		setBottom(bar); // 將功能設置在底部
		setStyle("-fx-background-color:#ffe4c4"); // 影片背景顏色
		player.play();// 播放影像
	}
}