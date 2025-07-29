package forest;

/**
 * ノードの座標情報や名前を保持する型
 */
public class Node extends Object {

	/**
	 * ノードの左上のX座標を保持する変数
	 */
	private Integer x;

	/**
	 * ノードの左上のY座標を保持する変数
	 */
	private Integer y;

	/**
	 * ノードの名前を保持する変数
	 */
	private String name;

	/**
	 * 自身の子孫の中で一番Y座標が大きいノードのY座標の値を保持する変数
	 */
	private Integer maxY;

	/**
	 * 自身の子孫の中で一番Y座標が小さいノードのY座標の値を保持する変数
	 */
	private Integer minY;

	/**
	 * ノード情報を保持するためのインスタンスを生成するためのコンストラクタ
	 */
	public Node(Integer x, Integer y, String name) {

	}

	/**
	 * 自身のX座標を返すメソッド
	 */
	public Integer getX() {
		return null;
	}

	/**
	 * 自身のX座標を引数の値に変更するメソッド
	 */
	public void setX(Integer setterX) {

	}

	/**
	 * 自身のY座標を返すメソッド
	 */
	public Integer getY() {
		return null;
	}

	/**
	 * 自身のY座標を引数の値に変更するメソッド
	 */
	public void setY(Integer setterY) {

	}

	/**
	 * 自身のMaxY座標を返すメソッド
	 */
	public Integer getMaxY() {
		return null;
	}

	/**
	 * 自身のMaxY座標を引数の値に変更するメソッド
	 */
	public void setMaxY(Integer setterMaxY) {

	}

	/**
	 * 自身のMinY座標を返すメソッド
	 */
	public Integer getMinY() {
		return null;
	}

	/**
	 * 自身のMinY座標を引数の値に変更するメソッド
	 */
	public void setMixY(Integer setterMinY) {

	}

	/**
	 * 自身のnameを標準出力に表示するメソッド
	 */
	public void printNodeName() {

	}

}
