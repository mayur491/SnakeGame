import javax.swing.JFrame;

public class GameFrame extends JFrame {

	private static final long serialVersionUID = -8519965885188987594L;

	/**
	 * @author mayur.somani
	 * @instagram mayur491
	 * @date 31-10-2020
	 */
	public GameFrame() {
		this.add(new GamePanel());
		this.setTitle("Snake - MacmaK");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
