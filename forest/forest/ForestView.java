package forest;

import java.awt.Point;
import javax.swing.JPanel;

/**
 * 木構造を表示するためのモデルクラス
 */
public class ForestView extends JPanel {

	private ForestModel aModel;


	/**
	 * 樹状整列のビューのインスタンスを生成し、引数として受け取ったモデルのインスタンスをmodelに束縛するコンストラクタ
	 */
	public ForestView(ForestModel aModel) {
		System.out.println("Hello from ForestView!");
		
		this.aModel = aModel;
	}

	/**
	 * 描画を更新するメソッド
	 */
	public void update() {

	}

}
