package Image;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class image03 {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		try {

			Mat source = Imgcodecs.imread("C://opencv3.1//10.jpg", 0);
			System.out.println("source=" + source.dump());
			Mat diff = new Mat(source.rows(), source.cols() - 1, CvType.CV_32FC1);
			for (int j = 0; j < source.rows(); j++) {
				for (int i = 0; i < source.cols() - 1; i++) {
					double[] temp1 = source.get(j, i);
					double[] temp2 = source.get(j, i + 1);
					diff.put(j, i, temp2[0] - temp1[0]);
				}
			}
			System.out.println("diff=" + diff.dump());
			
			Mat qan = new Mat(diff.rows(), diff.cols(), diff.type());
			Mat adj = new Mat(diff.rows(), diff.cols(), diff.type());
			for (int j = 0; j < diff.rows(); j++) {
				for (int i = 0; i < diff.cols(); i++) {
					double[] temp3 = diff.get(j, i);
					int temp4 = (int) (temp3[0] / 5);
					int temp5 = (int) (temp3[0] % 5);
					
					if(temp3[0] < 0){
						if (temp5 < -2) {
							temp4 = (temp4 - 1) * 5;
						}
						else {
							temp4 = temp4 * 5;
						}
					}
					else {
						if (temp5 > 2) {
							temp4 = (temp4 + 1) * 5;
						}
						else {
							temp4 = temp4 * 5;
						}
					}
					temp5 = (int) (temp4 - temp3[0]);
					qan.put(j, i, temp4);
					adj.put(j, i, temp5);
				}
			}
			System.out.println("qan=" + qan.dump());
			System.out.println("adj=" + adj.dump());
			
//			System.out.println(source.dump().hashCode());
//			System.out.println(diff.dump().hashCode());
			int HASH1 = qan.dump().hashCode();
			System.out.println("qan_HASH=" + HASH1);
//			System.out.println(adj.dump().hashCode());

			
			Mat input = Imgcodecs.imread("C://opencv3.1//13.jpg", 0);
			System.out.println("input=" + input.dump());
			Mat diff_p = new Mat(input.rows(), input.cols() - 1, CvType.CV_32FC1);
			for (int j = 0; j < input.rows(); j++) {
				for (int i = 0; i < input.cols() - 1; i++) {
					double[] temp1 = input.get(j, i);
					double[] temp2 = input.get(j, i + 1);
					diff_p.put(j, i, temp2[0] - temp1[0]);
				}
			}
			System.out.println("diff_p=" + diff_p.dump());
			
			Mat p_p = new Mat(diff_p.rows(), diff_p.cols(), CvType.CV_32FC1);
			Core.add(diff_p, adj, p_p);
			System.out.println("p_p=" + p_p.dump());
			
			Mat qan_p = new Mat(p_p.rows(), p_p.cols(), CvType.CV_32FC1);
			for (int j = 0; j < p_p.rows(); j++) {
				for (int i = 0; i < p_p.cols(); i++) {
					double[] temp3 = p_p.get(j, i);
					int temp4 = (int) (temp3[0] / 5);
					int temp5 = (int) (temp3[0] % 5);
					
					if(temp3[0] < 0){
						if (temp5 < -2) {
							temp4 = (temp4 - 1) * 5;
						}
						else {
							temp4 = temp4 * 5;
						}
					}
					else {
						if (temp5 > 2) {
							temp4 = (temp4 + 1) * 5;
						}
						else {
							temp4 = temp4 * 5;
						}
					}
					temp5 = (int) (temp4 - temp3[0]);
					qan_p.put(j, i, temp4);
				}
			}
			System.out.println("qan_p=" + qan_p.dump());
			
			int HASH2 = qan_p.dump().hashCode();
			System.out.println("qan_HASH=" + HASH2);
			
			if (HASH1 == HASH2) {
				System.out.println("Authentication succeeds.");
			}
			else {
				System.out.println("Authentication fails.");
			}
			
		} catch (Exception e) {
			System.out.println("error: " + e.getMessage());
		}
	}
}



















