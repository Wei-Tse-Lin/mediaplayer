package application;

import java.io.File;
import java.net.MalformedURLException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

//安裝 JavaFx http://download.eclipse.org/efxclipse/updates-released/0.9.0/site
public class Main extends Application {
	Player player;
	FileChooser fileChooser;

	public void start(Stage primaryStage) throws MalformedURLException {
		primaryStage.setTitle("簡潔快速的影片播放器");
		MenuBar menu = new MenuBar(); // 建立選單列
		Menu file = new Menu("選單"); // 建立選單
		MenuItem open = new MenuItem("開啟"); // 建立子選單的標題
		menu.getMenus().add(file);
		file.getItems().add(open);
		// 加入file及open
		fileChooser = new FileChooser();
		File filestart = fileChooser.showOpenDialog(primaryStage);
		player = new Player(filestart.toURI().toURL().toExternalForm()); // 選取影片路徑
		player.setTop(menu);// 將選單設置在左上方
		Scene scene = new Scene(player, 1080, 720, Color.BLACK);
		primaryStage.setScene(scene);
		// primaryStage.setFullScreen(true); //設定全螢幕
		primaryStage.show();

		open.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				player.player.pause();// 播放暫停
				File fileopen = fileChooser.showOpenDialog(primaryStage); // 顯示一個新的開啟檔案視窗
				if (file != null) {
					try {
						player = new Player(fileopen.toURI().toURL().toExternalForm());
						player.setTop(menu);// 將新的視窗設置選單在左上方
						Scene scene = new Scene(player, 1080, 720, Color.BLACK);
						primaryStage.setScene(scene);
					} catch (MalformedURLException e1) { // 拋出格式不正確的影片
						e1.printStackTrace(); // 在console顯示異常信息在程序中出錯的位置及原因
					}
				}
			}
		});
	}

	public static void main(String[] args) {
		Application.launch(args); // 啟動獨立的應用程序
	}
}