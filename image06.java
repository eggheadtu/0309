package Image;

import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.image.BufferedImage;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.imgproc.Imgproc;

import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class image06 {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	private JFrame frmjavaSwing;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					image06 window = new image06();
					window.frmjavaSwing.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public image06() {
		initialize();
		// �@���ؤǰt�t��k
		System.out.println("����t�ǰt=" + Imgproc.TM_SQDIFF);
		System.out.println("�зǤƥ���t�ǰt=" + Imgproc.TM_SQDIFF_NORMED);
		System.out.println("�����ǰt=" + Imgproc.TM_CCORR);
		System.out.println("�зǤƬ����ǰt=" + Imgproc.TM_CCORR_NORMED);
		System.out.println("�����Y�Ƥǰt=" + Imgproc.TM_CCOEFF);
		System.out.println("�зǤƬ����Y�Ƥǰt=" + Imgproc.TM_CCOEFF_NORMED);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Ū���H�y�Ϥ��P�����Ϥ����x�}
		final Mat source = Imgcodecs.imread("C://opencv3.1//20.bmp");
		final Mat template = Imgcodecs.imread("C://opencv3.1//21.bmp");

		// �x�}��Ϲ�
		BufferedImage image = matToBufferedImage(source);
		BufferedImage image2 = matToBufferedImage(template);

		// �إ�Frame
		frmjavaSwing = new JFrame();
		frmjavaSwing.setTitle("�v���ǰt");
		frmjavaSwing.setBounds(100, 100, 530, 621);
		frmjavaSwing.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmjavaSwing.getContentPane().setLayout(null);

		// "method"
		JLabel word1 = new JLabel("Method");
		word1.setBounds(10, 20, 46, 15);
		frmjavaSwing.getContentPane().add(word1);
		// �إ�slider
		final JSlider slider = new JSlider();
		slider.setMaximum(5);
		slider.setValue(0);
		slider.setBounds(64, 15, 150, 25);
		frmjavaSwing.getContentPane().add(slider);
		// �ǰt�t��k���s��
		final JLabel method_num = new JLabel("0");
		method_num.setBounds(224, 20, 46, 15);
		frmjavaSwing.getContentPane().add(method_num);

		// "Template"
		JLabel word2 = new JLabel("Template");
		word2.setBounds(264, 20, 86, 15);
		frmjavaSwing.getContentPane().add(word2);
		// �����Ϥ�����m
		JLabel eyes = new JLabel("");
		eyes.setBounds(339, 10, 175, 40);
		eyes.setIcon(new ImageIcon(image2));
		frmjavaSwing.getContentPane().add(eyes);

		// �H�y�Ϥ�����m
		final JLabel face = new JLabel("");
		face.setBounds(10, 77, 500, 500);
		face.setIcon(new ImageIcon(image));
		frmjavaSwing.getContentPane().add(face);

		// ���ܤǰt�t��k���s��
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				System.out.println(slider.getValue());
				method_num.setText(slider.getValue() + "");
				Mat source_fix = Imgcodecs.imread("C://opencv3.1//20.bmp");
				// �M��ǰt������
				Mat input = findTemplete(source_fix, template, slider.getValue());
				BufferedImage newImage = matToBufferedImage(input);
				face.setIcon(new ImageIcon(newImage));
			}
		});
	}

	// �M��ǰt������
	public Mat findTemplete(Mat source, Mat template, int match_method) {
		// �ƻs�x�}������
		Mat source_c = source.clone();
		Mat result = new Mat(source.cols() - template.cols() + 1, source.rows() - template.rows() + 1, CvType.CV_32FC1);
		// �ҪO�ǰt
		Imgproc.matchTemplate(source, template, result, match_method);
		// ��}�C����������зǤƪ��{�ǡA�Ԩ�P3-13
//		Core.normalize(result, result, 0, 255, Core.NORM_MINMAX, -1, new Mat());
		// �M��x�}���̤j�Ȥγ̤p�Ȫ���m
		MinMaxLocResult mml = Core.minMaxLoc(result);
		Point matchLoc;
		if (match_method == Imgproc.TM_SQDIFF || match_method == Imgproc.TM_SQDIFF_NORMED)
			matchLoc = mml.minLoc;
		else
			matchLoc = mml.maxLoc;

		// �ζ®ؼХX�ۦ���m
		Imgproc.rectangle(source, matchLoc, new Point(matchLoc.x + template.cols(), matchLoc.y + template.rows()),
				new Scalar(0, 0, 0), 2);
		// �^���ۦ���m���x�}
		Mat subsource_g = source_c.submat((int) matchLoc.y, (int) (matchLoc.y + template.rows()), (int) matchLoc.x,
				(int) (matchLoc.x + template.cols()));
		Imgcodecs.imwrite("C://opencv3.1//24.bmp", source);
		// ��ﵲ�G
		if (isTheSame(subsource_g, template)) {
			// �Y�����˹����Ȧ��ŦX�A�h�e�X����
			Imgproc.rectangle(source, matchLoc, new Point(matchLoc.x + template.cols(), matchLoc.y + template.rows()),
					new Scalar(255, 0, 255), 2);
			Imgcodecs.imwrite("C://opencv3.1//24.bmp", source);
		}
		return source;
	}

	// ���
	public boolean isTheSame(Mat img1, Mat img2) {
		// System.out.println("img1-size="+img1.size());
		// System.out.println("img2-size="+img2.size());

		boolean isTheSame = false;
		// subsource_g�ϧΪ��e�T�I
		double dataA0 = img1.get(0, 0)[0];
		double dataA1 = img1.get(0, 1)[0];
		double dataA2 = img1.get(0, 2)[0];
		double dataA3 = img1.get(0, 0)[1];
		double dataA4 = img1.get(0, 1)[1];
		double dataA5 = img1.get(0, 2)[1];
		double dataA6 = img1.get(0, 0)[2];
		double dataA7 = img1.get(0, 1)[2];
		double dataA8 = img1.get(0, 2)[2];
		// template�ϧΪ��e�T�I
		double dataB0 = img2.get(0, 0)[0];
		double dataB1 = img2.get(0, 1)[0];
		double dataB2 = img2.get(0, 2)[0];
		double dataB3 = img2.get(0, 0)[1];
		double dataB4 = img2.get(0, 1)[1];
		double dataB5 = img2.get(0, 2)[1];
		double dataB6 = img2.get(0, 0)[2];
		double dataB7 = img2.get(0, 1)[2];
		double dataB8 = img2.get(0, 2)[2];

		if (Math.abs(dataA0 - dataB0) <= 1 && Math.abs(dataA1 - dataB1) <= 1 && Math.abs(dataA2 - dataB2) <= 1) {
			if (Math.abs(dataA3 - dataB3) <= 1 && Math.abs(dataA4 - dataB4) <= 1 && Math.abs(dataA5 - dataB5) <= 1) {
				if (Math.abs(dataA6 - dataB6) <= 1 && Math.abs(dataA7 - dataB7) <= 1
						&& Math.abs(dataA8 - dataB8) <= 1) {
					isTheSame = true;
				}
			}
		}

		return isTheSame;
	}

	// �x�}��Ϲ�
	public BufferedImage matToBufferedImage(Mat matrix) {
		int cols = matrix.cols();
		int rows = matrix.rows();
		int elemSize = (int) matrix.elemSize();
		byte[] data = new byte[cols * rows * elemSize];
		int type;
		matrix.get(0, 0, data);
		switch (matrix.channels()) {
		case 1:
			type = BufferedImage.TYPE_BYTE_GRAY;
			break;
		case 3:
			type = BufferedImage.TYPE_3BYTE_BGR;
			// bgr to rgb
			byte b;
			for (int i = 0; i < data.length; i = i + 3) {
				b = data[i];
				data[i] = data[i + 2];
				data[i + 2] = b;
			}
			break;
		default:
			return null;
		}
		BufferedImage image2 = new BufferedImage(cols, rows, type);
		image2.getRaster().setDataElements(0, 0, cols, rows, data);
		return image2;
	}
}
